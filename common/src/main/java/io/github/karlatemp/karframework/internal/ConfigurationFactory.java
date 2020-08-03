/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/03 13:08:30
 *
 * kar-framework/kar-framework.common.main/ConfigurationFactory.java
 */

package io.github.karlatemp.karframework.internal;

import com.google.common.io.Files;
import io.github.karlatemp.karframework.IPluginProvider;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.gson.GsonConfigurationLoader;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.yaml.YAMLConfigurationLoader;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class ConfigurationFactory {


    @SuppressWarnings("UnstableApiUsage")
    @Contract()
    public static ConfigurationLoader<? extends ConfigurationNode> loadConfiguration(
            @NotNull IPluginProvider pluginProvider,
            @NotNull String path
    ) {
        File file = new File(path);
        FileExecutions.store(pluginProvider, file, path, false);
        String type = Files.getFileExtension(path);
        ConfigurationLoader<? extends ConfigurationNode> loader;
        switch (type) {
            case "conf": {
                // HOCON
                loader = HoconConfigurationLoader.builder()
                        .setFile(file)
                        .build();
                break;
            }
            case "yaml":
            case "yml": {
                // YAML
                loader = YAMLConfigurationLoader.builder()
                        .setFile(file)
                        .setIndent(2)
                        .build();
                break;
            }
            case "json5":
            case "json": {
                loader = GsonConfigurationLoader.builder()
                        .setFile(file)
                        .setIndent(2)
                        .setLenient(false)
                        .build();
                break;
            }
            default:
                throw new AssertionError("Unknown configuration type: " + type);
        }
        try {
            loader.load();
        } catch (Throwable ignored) {
            try {
                FileExecutions.backup(file);
            } catch (IOException ioException) {
                pluginProvider.getLogger().log(Level.WARNING, "Exception in backup files...", ioException);
            }
            FileExecutions.store(pluginProvider, file, path, true);
        }
        return loader;
    }

    @Contract
    public static ConfigurationNode loadConfiguration(
            @NotNull IPluginProvider pluginProvider,
            @NotNull ConfigurationLoader<?> configurationLoader
    ) {
        try {
            return configurationLoader.load();
        } catch (Throwable throwable) {
            pluginProvider.getLogger().log(Level.SEVERE,
                    "Exception in loading configuration.",
                    throwable);
            return configurationLoader.createEmptyNode();
        }
    }

    @Contract
    public static void restoreConfiguration(
            @NotNull IPluginProvider pluginProvider,
            @NotNull ConfigurationLoader<?> loader,
            @NotNull ConfigurationNode node) {
        try {
            if (!loadConfiguration(pluginProvider, loader).equals(node)) {
                loader.save(node);
            }
        } catch (Throwable throwable) {
            pluginProvider.getLogger().log(Level.SEVERE, "Exception in restoring configuration.", throwable);
        }
    }
}
