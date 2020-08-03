package io.github.karlatemp.karframework;

import io.github.karlatemp.karframework.internal.ConfigurationFactory;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.InputStream;
import java.util.logging.Logger;

public interface IPluginProvider {
    @NotNull String getName();

    @Nullable InputStream getResource(@NotNull String path);

    @NotNull File getPluginDataFolder();

    default @Nullable ConfigurationLoader<? extends ConfigurationNode> loadConfiguration(
            @NotNull String path
    ) {
        return ConfigurationFactory.loadConfiguration(this, path);
    }

    default @NotNull ConfigurationNode loadConfiguration(@NotNull ConfigurationLoader<? extends ConfigurationNode> loader) {
        return ConfigurationFactory.loadConfiguration(this, loader);
    }

    default void restoreConfiguration(@NotNull ConfigurationLoader<?> loader, @NotNull ConfigurationNode node) {
        ConfigurationFactory.restoreConfiguration(this,loader,node);
    }

    @NotNull Logger getLogger();
}
