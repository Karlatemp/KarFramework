package io.github.karlatemp.karframework.bungee;

import io.github.karlatemp.karframework.IPluginProvider;
import net.md_5.bungee.api.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.InputStream;
import java.util.logging.Logger;

public class BungeePluginProvider implements IPluginProvider {
    private final Plugin plugin;

    public BungeePluginProvider(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getName() {
        return plugin.getDescription().getName();
    }

    @Override
    public @Nullable InputStream getResource(@NotNull String path) {
        return plugin.getResourceAsStream(path);
    }

    @Override
    public @NotNull File getPluginDataFolder() {
        return plugin.getDataFolder();
    }

    @Override
    public @NotNull Logger getLogger() {
        return plugin.getLogger();
    }
}
