/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/06 15:14:33
 *
 * kar-framework/kar-framework.spigot.main/OpenMCLang.java
 */

package io.github.karlatemp.karframework.bukkit.resources;

import cn.mcres.karlatemp.unsafe.Unsafe;
import io.github.karlatemp.karframework.bukkit.KarFrameworkBukkit;
import io.github.karlatemp.karframework.bukkit.internal.Internal;
import io.github.karlatemp.karframework.format.FormatAction;
import io.github.karlatemp.karframework.utils.EnumerationIterator;
import net.md_5.bungee.api.chat.TranslatableComponent;
import net.md_5.bungee.chat.TranslationRegistry;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.logging.Level;
import java.util.spi.ResourceBundleControlProvider;

@SuppressWarnings({"unchecked", "rawtypes"})
public /*internal*/ class OpenMCLang {
    private static final Object LLC_INSTANCE;
    private static final Field LLC_MAP_OPENACCESS;
    private static final Object LLC_SOURCE;
    private static Map<String, String> USING;

    static {
        // Open Language
        try {
            final Class<?> LLC = Class.forName(FormatAction.parse(
                    "net.minecraft.server.{0}.LocaleLanguage"
            ).apply(new String[]{KarFrameworkBukkit.getNmsVersion()}));
            Object instance = null;
            for (Field f : LLC.getDeclaredFields()) {
                if (Modifier.isStatic(f.getModifiers())) {
                    f.setAccessible(true);
                    Object w = f.get(null);
                    if (LLC.isInstance(w)) {
                        instance = w;
                        break;
                    }
                }
            }
            assert instance != null;
            LLC_INSTANCE = instance;
            Field field = null;
            for (Field f : instance.getClass().getDeclaredFields()) {
                if (f.getType().isAssignableFrom(Map.class)) {
                    field = f;
                    f.setAccessible(true);
                    break;
                }
            }
            assert field != null;
            LLC_MAP_OPENACCESS = field;
            LLC_SOURCE = field.get(instance);
            USING = (Map<String, String>) LLC_SOURCE;
            // Open BCCAPI
            List<Runnable> shutdowns = new LinkedList<>();
            try {
                final Field providersField = TranslationRegistry.class.getDeclaredField("providers");
                providersField.setAccessible(true);
                List providers = (List) providersField.get(TranslationRegistry.INSTANCE);
                final Class<?> TranslationProvider = Class.forName("net.md_5.bungee.chat.TranslationRegistry$TranslationProvider");
                Object provider = Proxy.newProxyInstance(TranslationProvider.getClassLoader(), new Class[]{TranslationProvider}, (proxy, method, args) -> {
                            switch (method.getName()) {
                                case "hashCode":
                                    return 0;
                                case "equals":
                                    return false;
                                case "toString":
                                    return "KarFramework Translator";
                                default: {
                                    if (args.length == 1) {
                                        // translate
                                        Map<String, String> using = USING;
                                        if (using != null)
                                            return using.get(String.valueOf(args[0]));
                                    }
                                    return null;
                                }
                            }
                        }
                );
                providers.add(0, provider);
                shutdowns.add(() -> providers.removeIf(it -> it == provider));
                // shutdownHook
            } catch (Throwable ignored) {
                //noinspection JavaReflectionMemberAccess
                TranslatableComponent.class.getDeclaredField("locales"); // java.util.ResourceBundle
                List<ResourceBundleControlProvider> providers0, providers;
                try {
                    Field declaredField = ResourceBundle.class.getDeclaredField("providers");
                    declaredField.setAccessible(true);
                    providers0 = (List<ResourceBundleControlProvider>) declaredField.get(null);
                } catch (Throwable ignore) {
                    // ResourceBundleControlProviderHolder
                    Class<?> ResourceBundleControlProviderHolder = Class.forName("java.util.ResourceBundle$ResourceBundleControlProviderHolder");
                    Field control_providers = ResourceBundleControlProviderHolder.getDeclaredField("CONTROL_PROVIDERS");
                    Unsafe unsafe = Unsafe.getUnsafe();

                    Object base = unsafe.staticFieldBase(control_providers);
                    long offset = unsafe.staticFieldOffset(control_providers);
                    Object old = unsafe.getReference(base, offset);
                    providers0 = new ArrayList<>((List<ResourceBundleControlProvider>) unsafe.getReference(
                            unsafe.staticFieldBase(control_providers),
                            unsafe.staticFieldOffset(control_providers)
                    ));
                    unsafe.putReference(base, offset, providers0);
                    shutdowns.add(() -> {
                        unsafe.putReference(base, offset, old);
                    });
                }
                providers = providers0;

                ResourceBundle.Control cc = new ResourceBundle.Control() {
                    @Override
                    public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload) throws IllegalAccessException, InstantiationException, IOException {

                        if (locale == Locale.getDefault()) {
                            return new ResourceBundle() {
                                @Override
                                protected Object handleGetObject(@NotNull String key) {
                                    Map<String, String> sw = USING;
                                    if (sw == null) return key;
                                    String w = sw.get(key);
                                    if (w == null) return key;
                                    return w;
                                }

                                @NotNull
                                @Override
                                public Enumeration<String> getKeys() {
                                    Map<String, String> sw = USING;
                                    if (sw != null) {
                                        return new EnumerationIterator<>(
                                                new ArrayList<>(
                                                        sw.keySet()
                                                ).iterator()
                                        );
                                    }
                                    return Collections.emptyEnumeration();
                                }
                            };
                        }
                        return null;
                    }

                    @Override
                    public List<String> getFormats(String baseName) {
                        return super.getFormats(baseName);
                    }
                };
                ResourceBundleControlProvider provider = baseName -> {
                    if (baseName.startsWith("mojang-translations/")) {
                        return cc;
                    }
                    return null;
                };
                providers.add(0, provider);
                shutdowns.add(() -> providers.removeIf(it -> it == provider));
            }
            shutdownHook = () -> {
                shutdowns.forEach(Runnable::run);
            };
        } catch (Throwable throwable) {
            throw new ExceptionInInitializerError(throwable);
        }
    }

    private static final Runnable shutdownHook;

    public /*internal*/ static void preInit() {
        Internal.SHUTDOWN_HOOK.get().add(shutdownHook);
        Internal.SHUTDOWN_HOOK.get().add(() -> {
            try {
                LLC_MAP_OPENACCESS.set(LLC_INSTANCE, LLC_SOURCE);
            } catch (IllegalAccessException e) {
                JavaPlugin.getProvidingPlugin(OpenMCLang.class).getLogger()
                        .log(Level.SEVERE, "Exception in reset mc lang.", e);
            }
        });
    }

    static void initialize() {
        Object map = ExternalLanguages.getLanguage(DownloadProviders.language);
        if (map == null) {
            System.out.println("SW IVV, SV DEF");
            map = LLC_SOURCE;
        }
        USING = (Map<String, String>) map;
        try {
            LLC_MAP_OPENACCESS.set(LLC_INSTANCE, map);
        } catch (IllegalAccessException e) {
            JavaPlugin.getProvidingPlugin(OpenMCLang.class).getLogger()
                    .log(Level.SEVERE, "Exception in overriding mc lang.", e);
        }

    }
}