/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/06 13:27:10
 *
 * kar-framework/kar-framework.spigot.main/ExternalLanguages.java
 */

package io.github.karlatemp.karframework.bukkit.resources;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.karlatemp.karframework.IPluginProvider;
import io.github.karlatemp.karframework.bukkit.internal.Internal;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ExternalLanguages {
    private static final Map<String, CompletableFuture<Map<String, String>>> resources = new ConcurrentHashMap<>();
    static IPluginProvider pprovider;

    public /*internal */ static void initialize() {
        // Must call in framework initialize
        String ignored = (pprovider = Internal.PLUGIN_PROVIDER.get()).getName();
        resources.clear();
        OpenMCLang.initialize();
    }

    public static @Nullable Map<String, String> getLanguage(@NotNull String language) {
        CompletableFuture<Map<String, String>> future = resources.get(language);
        if (future != null) {
            try {
                return future.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new Error(e);
            }
        }
        resources.put(language, future = new CompletableFuture<>());
        Map<String, String> result = new HashMap<>();

        { // Select official lang first
            Map<String, String> map0 = DownloadProviders.languages.get("minecraft/lang/" + language + ".json");
            Map<String, String> map1 = DownloadProviders.languages.get("minecraft/lang/" + language + ".lang");
            if (map0 != null) result.putAll(map0);
            if (map1 != null) result.putAll(map1);
        }
        { // Select from resource packs
            for (ZipFile zip : ResourcePackLoader.resourceZipFiles) {
                final ZipEntry entry = zip.getEntry("assets/minecraft/lang/" + language + ".json");
                final ZipEntry entryLang = zip.getEntry("assets/minecraft/lang/" + language + ".lang");
                if (entryLang != null) {
                    try (Reader reader = new InputStreamReader(
                            new BufferedInputStream(
                                    zip.getInputStream(entryLang)
                            ),
                            StandardCharsets.UTF_8
                    )) {
                        Properties properties = new Properties();
                        properties.load(reader);
                        for (Map.Entry<Object, Object> entryZ : properties.entrySet()) {
                            result.put(String.valueOf(entryZ.getKey()), String.valueOf(entryZ.getValue()));
                        }
                    } catch (Exception exception) {
                        pprovider.getLogger().log(Level.SEVERE,
                                "Exception in loading (" + "assets/minecraft/lang/" + language + ".lang" + ") "
                                        + "from " + zip.getName(),
                                exception);
                    }
                }
                if (entry != null) {
                    try (Reader reader = new InputStreamReader(
                            new BufferedInputStream(
                                    zip.getInputStream(entry)
                            ),
                            StandardCharsets.UTF_8
                    )) {
                        final JsonObject object = DownloadProviders.JSON_PARSER.parse(reader).getAsJsonObject();
                        for (Map.Entry<String, JsonElement> entryZ : object.entrySet()) {
                            result.put(entryZ.getKey(), entryZ.getValue().getAsString());
                        }
                    } catch (Exception exception) {
                        pprovider.getLogger().log(Level.SEVERE,
                                "Exception in loading (" + "assets/minecraft/lang/" + language + ".json" + ") "
                                        + "from " + zip.getName(),
                                exception);
                    }
                }
            }
        }
        if (result.isEmpty()) result = null;
        else result = ImmutableMap.copyOf(result);
        future.complete(result);
        return result;
    }

    /* internal */
    static void dropAll() {
        ResourcePackLoader.resourcePacks.clear();
        ResourcePackLoader.resourceZipFiles.removeIf(it -> {
            String name = it.getName();
            try {
                it.close();
            } catch (IOException exception) {
                pprovider.getLogger().log(Level.WARNING, "Exception in closing " + name, exception);
            }
            return true;
        });
        resources.clear();
    }

}
