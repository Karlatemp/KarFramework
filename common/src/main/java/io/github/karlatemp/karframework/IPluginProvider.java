/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/03 13:08:30
 *
 * kar-framework/kar-framework.common.main/IPluginProvider.java
 */

package io.github.karlatemp.karframework;

import com.google.common.io.ByteStreams;
import com.google.common.io.Files;
import io.github.karlatemp.karframework.command.ICommandNode;
import io.github.karlatemp.karframework.internal.ConfigurationFactory;
import io.github.karlatemp.karframework.io.RandomAccessFileOutputStream;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public interface IPluginProvider {
    @Contract(pure = true)
    @NotNull String getName();

    @Nullable InputStream getResource(@NotNull String path);

    @Contract(pure = true)
    @NotNull File getPluginDataFolder();

    default @Nullable ConfigurationLoader<? extends ConfigurationNode> loadConfiguration(
            @NotNull String path
    ) {
        return ConfigurationFactory.loadConfiguration(this, path);
    }

    default @Nullable ConfigurationLoader<? extends ConfigurationNode> loadConfiguration(
            @NotNull String resourcePath, @NotNull String filePath
    ) {
        return ConfigurationFactory.loadConfiguration(this, resourcePath, filePath);
    }

    default @NotNull ConfigurationNode loadConfiguration(@NotNull ConfigurationLoader<? extends ConfigurationNode> loader) {
        return ConfigurationFactory.loadConfiguration(this, loader);
    }

    default void restoreConfiguration(@NotNull ConfigurationLoader<?> loader, @NotNull ConfigurationNode node) {
        ConfigurationFactory.restoreConfiguration(this, loader, node);
    }

    @Contract(pure = true)
    @NotNull Logger getLogger();

    <T> void provideCommand(@NotNull String name, @NotNull ICommandNode<T> node);

    @SuppressWarnings("UnstableApiUsage")
    default void storeResource(String path, File file, boolean force) {
        if (!force && file.isFile()) {
            return;
        }
        InputStream resource = getResource(path);
        if (resource == null) {
            getLogger().warning("Resource `" + path + "` not found.");
            return;
        }
        try {
            Files.createParentDirs(file);
            try (InputStream incoming = resource;
                 OutputStream outgoing = new BufferedOutputStream(new RandomAccessFileOutputStream(file))) {
                ByteStreams.copy(incoming, outgoing);
            }
        } catch (IOException ioe) {
            getLogger().log(Level.SEVERE, "Exception in saving " + path, ioe);
        }
    }

    default @Nullable File getPluginFile() {
        return null;
    }

    default @Nullable ClassLoader getClassLoader() {
        return null;
    }
}
