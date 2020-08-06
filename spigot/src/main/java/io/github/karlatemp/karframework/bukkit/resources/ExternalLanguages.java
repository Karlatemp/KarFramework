/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/06 13:27:10
 *
 * kar-framework/kar-framework.spigot.main/ExternalLanguages.java
 */

package io.github.karlatemp.karframework.bukkit.resources;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.karlatemp.karframework.IPluginProvider;
import io.github.karlatemp.karframework.annotation.Warning;
import io.github.karlatemp.karframework.bukkit.internal.Internal;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
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
            String hashX = DownloadProviders.hashMapping.get("minecraft/lang/" + language + ".json");
            if (hashX != null) {
                try {
                    final JsonObject object = DownloadProviders.parseFile(new File(DownloadProviders.objects, hashX)).getAsJsonObject();
                    for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
                        result.put(entry.getKey(), entry.getValue().getAsString());
                    }
                } catch (Exception ioException) {
                    pprovider.getLogger().log(Level.SEVERE, "Exception in parsing " + hashX
                                    + " (" + "minecraft/lang/" + language + ".json" + ")",
                            ioException);
                }
            }
        }
        { // Select from resource packs
            for (ZipFile zip : ResourcePackLoader.resourceZipFiles) {
                final ZipEntry entry = zip.getEntry("assets/minecraft/lang/" + language + ".json");
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
        future.complete(result);
        return result;
    }

    /* internal */ static void dropAll() {
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
