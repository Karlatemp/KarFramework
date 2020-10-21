/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/10/21 05:34:19
 *
 * kar-framework/kar-framework.common.main/OpenMCLang.java
 */

package io.github.karlatemp.karframework.internal.resources;

import io.github.karlatemp.karframework.KarFramework;
import io.github.karlatemp.karframework.annotation.InternalAPI;
import io.github.karlatemp.karframework.format.FormatAction;
import io.github.karlatemp.karframework.internal.Internal;
import io.github.karlatemp.karframework.utils.EnumerationIterator;
import io.github.karlatemp.unsafeaccessor.Unsafe;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.logging.Level;
import java.util.spi.ResourceBundleControlProvider;

@InternalAPI
@SuppressWarnings({"unchecked", "rawtypes"})
public /*internal*/ abstract class OpenMCLang {
    private static Map<String, String> USING;
    private static final List<OpenMCLang> INSTANCES = new ArrayList<>(2);
    private static final Object LLC_SOURCE;

    abstract void inject();

    abstract void uninject();

    static Class<?> loadClass(String path) {
        try {
            return Class.forName(path);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    static {
        Class<?> TranslationRegistry = loadClass("net.md_5.bungee.chat.TranslationRegistry");

        // Open Language
        try {
            IMinecraftFramework framework = MinecraftFramework.minecraftFramework;
            if (framework.isBukkit()) {
                final Class<?> LLC = Class.forName(FormatAction.parse(
                        "net.minecraft.server.{0}.LocaleLanguage"
                ).apply(new String[]{framework.getNmsVersion()}));
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
                Object LLC_INSTANCE = instance;
                Field field = null;
                for (Field f : instance.getClass().getDeclaredFields()) {
                    if (f.getType().isAssignableFrom(Map.class)) {
                        field = f;
                        f.setAccessible(true);
                        break;
                    }
                }
                assert field != null;
                Field LLC_MAP_OPENACCESS = field;
                LLC_SOURCE = field.get(instance);
                USING = (Map<String, String>) LLC_SOURCE;
                INSTANCES.add(new OpenMCLang() {
                    @Override
                    void inject() {
                        try {
                            LLC_MAP_OPENACCESS.set(LLC_INSTANCE, USING);
                        } catch (Exception e) {
                            framework.getLogger().log(Level.SEVERE, "Exception in set language inject.", e);
                        }
                    }

                    @Override
                    void uninject() {
                        try {
                            LLC_MAP_OPENACCESS.set(LLC_INSTANCE, LLC_SOURCE);
                        } catch (Exception e) {
                            framework.getLogger().log(Level.SEVERE, "Exception in unset language inject.", e);
                        }
                    }
                });
            } else {
                Properties properties = new Properties();
                if (TranslationRegistry != null) {
                    try (InputStream stream = TranslationRegistry.getResourceAsStream("/mojang-translations/en_US.properties")) {
                        if (stream != null) {
                            try (InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8)) {
                                properties.load(reader);
                            }
                        }
                    }
                }
                // try(InputStream inputStream =)
                LLC_SOURCE = properties;
            }

            // Open BCCAPI
            List<Runnable> shutdowns = new LinkedList<>();
            if (TranslationRegistry != null) {
                try {
                    final Field providersField = TranslationRegistry.getDeclaredField("providers");
                    providersField.setAccessible(true);
                    List providers = (List) providersField.get(TranslationRegistry.getField("INSTANCE").get(null));
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
                    Class<?> TranslatableComponent = Class.forName("net.md_5.bungee.api.chat.TranslatableComponent");
                    //noinspection ResultOfMethodCallIgnored
                    TranslatableComponent.getDeclaredField("locales"); // java.util.ResourceBundle
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
            for (OpenMCLang om : INSTANCES) om.uninject();
        });
    }

    static void initialize() {
        Object map = ExternalLanguages.getLanguage(DownloadProviders.language);
        if (map == null) {
            System.out.println("SW IVV, SV DEF");
            map = LLC_SOURCE;
        }
        USING = (Map<String, String>) map;

        for (OpenMCLang om : INSTANCES) om.inject();

    }
}