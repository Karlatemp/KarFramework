/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/19 24:19:57
 *
 * kar-framework/kar-framework.common.main/ServiceHelper.java
 */

package io.github.karlatemp.karframework.services;

import groovyjarjarasm.asm.AnnotationVisitor;
import groovyjarjarasm.asm.ClassReader;
import groovyjarjarasm.asm.ClassVisitor;
import groovyjarjarasm.asm.Opcodes;
import groovyjarjarasm.asm.tree.ClassNode;
import io.github.karlatemp.karframework.IPluginProvider;
import io.github.karlatemp.karframework.annotation.Service;
import io.github.karlatemp.unsafeaccessor.Root;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import org.jetbrains.annotations.UnmodifiableView;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.*;
import java.util.logging.Level;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ServiceHelper {
    public static void register(
            @NotNull IPluginProvider pluginProvider,
            @NotNull IServicesTable servicesTable
    ) {
        ClassLoader classLoader = pluginProvider.getClassLoader();
        if (classLoader == null) {
            throw new IllegalStateException("No classloader from " + pluginProvider.getName());
        }
        File file = pluginProvider.getPluginFile();
        if (file == null) {
            throw new IllegalStateException("Cannot open plugin.jar: PluginProvider no giving plugin file.");
        }
        class RequireTree {
            String name;
            Class<?> klass;
            boolean requested = false;
            Service.KService service;

            @Override
            public String toString() {
                return "RequireTree{" + klass + '}';
            }
        }
        ArrayDeque<RequireTree> requires = new ArrayDeque<>();
        try (ZipFile zip = new ZipFile(file)) {
            Enumeration<? extends ZipEntry> entries = zip.entries();
            // Scan all @KService

            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                if (entry.getName().endsWith(".class")) {
                    ClassReader reader = new ClassReader(zip.getInputStream(entry));
                    ClassNode node = new ClassNode();
                    reader.accept(node, 0);
                    node.accept(new ClassVisitor(Opcodes.ASM8) {
                        boolean visited;

                        @Override
                        public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
                            if (visited) return null;
                            if ("Lio/github/karlatemp/karframework/annotation/Service$KService;".equals(descriptor)) {
                                visited = true;
                                RequireTree tree = new RequireTree();
                                requires.add(tree);
                                tree.name = node.name;
                                return null;
                            }
                            // System.out.println(descriptor);
                            return null;
                        }
                    });
                }
            }
            for (RequireTree tree : requires) {
                tree.klass = Class.forName(
                        tree.name.replace('/', '.'),
                        false, classLoader
                );
                tree.service = tree.klass.getDeclaredAnnotation(Service.KService.class);
            }
            Map<Class<?>, List<RequireTree>> requireTree = new HashMap<>();
            for (RequireTree tree : requires) {
                Class<?>[] classes = tree.service.value();
                for (Class<?> c : classes) {
                    requireTree.computeIfAbsent(c, $ -> new ArrayList<>()).add(tree);
                }
                requireTree.computeIfAbsent(tree.klass, $ -> new ArrayList<>()).add(tree);
            }
            /*
            for (Map.Entry<Class<?>, List<RequireTree>> tree : requireTree.entrySet()) {
                System.out.println(tree.getKey());
                for (RequireTree rt : tree.getValue()) {
                    System.out.append("    ").println(rt);
                }
            }
            */
            new Object() {
                <T> T allocate(Constructor<T> constructor) throws Exception {
                    // Load constructors
                    Object[] args = new Object[constructor.getParameterCount()];
                    Class<?>[] parameterTypes = constructor.getParameterTypes();
                    Annotation[][] parameterAnnotations = constructor.getParameterAnnotations();
                    int paramCounts = constructor.getParameterCount();
                    for (int i = 0; i < paramCounts; i++) {
                        Class<?> parameterType = parameterTypes[i];
                        Annotation[] parameterAnnotation = parameterAnnotations[i];
                        for (Annotation a : parameterAnnotation) {
                            if (a instanceof Service.KInject) {
                                String name = ((Service.KInject) a).value();
                                if (name.isEmpty()) name = null;
                                List<RequireTree> trees = requireTree.get(parameterType);
                                if (trees != null) {
                                    for (RequireTree t : trees) load(t);
                                }
                                args[i] = servicesTable.findService(
                                        parameterType, name
                                ).orElse(null);
                            }
                        }
                    }
                    return constructor.newInstance(args);
                }

                final List<Object> autoRegister = new ArrayList<>();

                @SuppressWarnings({"unchecked", "rawtypes"})
                void load(RequireTree tree) throws Exception {
                    if (tree.requested) return;
                    tree.requested = true;
                    Constructor<?> constructor = null;
                    {
                        Constructor<?>[] constructors = tree.klass.getDeclaredConstructors();
                        Constructor<?> dyn = null;
                        for (Constructor<?> cc : constructors) {
                            if (cc.getParameterCount() == 0) dyn = cc;
                            if (cc.getAnnotation(Service.KService.class) != null) {
                                if (constructor != null) {
                                    throw new IllegalStateException("Duplicate constructor: " + tree.klass);
                                }
                                constructor = cc;
                            }
                        }
                        if (constructor == null) {
                            if (dyn == null) {
                                if (constructors.length == 1) {
                                    constructor = constructors[0];
                                } else {
                                    throw new IllegalStateException("No matching constructor for: " + tree.klass);
                                }
                            } else {
                                constructor = dyn;
                            }
                        }
                        Root.setAccessible(constructor, true);
                    }
                    Object service = allocate(constructor);
                    {
                        Class<?> scanning = service.getClass();
                        do {
                            for (Field field : scanning.getDeclaredFields()) {
                                Service.KInject inject = field.getDeclaredAnnotation(Service.KInject.class);
                                if (inject != null) {
                                    String name = inject.value();
                                    if (name.isEmpty()) name = null;
                                    Class<?> type = field.getType();
                                    List<RequireTree> trees = requireTree.get(type);
                                    if (trees != null) {
                                        for (RequireTree t : trees) load(t);
                                    }
                                    Root.setAccessible(field, true);
                                    field.set(service, servicesTable.findService(
                                            type, name
                                    ).orElse(null));
                                }
                            }
                        } while ((scanning = scanning.getSuperclass()) != null);
                    }
                    String registeredName = tree.service.name();
                    if (registeredName.isEmpty()) registeredName = null;
                    servicesTable.registerService(
                            (Class) tree.klass, service, registeredName
                    );
                    for (Class c : tree.service.value()) {
                        servicesTable.registerService(
                                c, service, registeredName
                        );
                    }
                    if (tree.klass.getDeclaredAnnotation(Service.KAuto.class) != null) {
                        autoRegister.add(service);
                    }
                }

                void load() throws Exception {
                    for (RequireTree tree : requires) load(tree);
                    // Open Auto Register
                    if (!autoRegister.isEmpty()) {
                        @Unmodifiable @UnmodifiableView Iterable<IServiceRegister> registers = servicesTable.findServicesImpl(IServiceRegister.class);
                        for (Object service : autoRegister) {
                            IServiceRegister.Result register = IServiceRegister.register(service, registers);
                            if (register.getType() == IServiceRegister.ResultType.FAILED) {
                                pluginProvider.getLogger().log(Level.SEVERE, "Exception in registering service " + service, register.getCause());
                            }
                        }
                    }
                }
            }.load();
        } catch (Exception ioException) {
            pluginProvider.getLogger().log(Level.WARNING, "Exception in scanning plugin.", ioException);
        }
    }
}
