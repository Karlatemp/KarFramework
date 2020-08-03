/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/03 13:08:30
 *
 * kar-framework/kar-framework.bukkit.main/BukkitPluginProvider.java
 */

package io.github.karlatemp.karframework.bukkit;

import io.github.karlatemp.karframework.IPluginProvider;
import io.github.karlatemp.karframework.internal.ConfigurationFactory;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.InputStream;
import java.util.logging.Logger;

public class BukkitPluginProvider implements IPluginProvider {
    private final Plugin plugin;

    public BukkitPluginProvider(@NotNull Plugin plugin){
        this.plugin=plugin;
    }
    @Override
    public @NotNull String getName() {
        return plugin.getName();
    }

    @Override
    public @Nullable InputStream getResource(@NotNull String path) {
        return plugin.getResource(path);
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
