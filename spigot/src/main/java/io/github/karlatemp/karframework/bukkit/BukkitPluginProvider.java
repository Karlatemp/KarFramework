/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/03 13:08:30
 *
 * kar-framework/kar-framework.bukkit.main/BukkitPluginProvider.java
 */

package io.github.karlatemp.karframework.bukkit;

import io.github.karlatemp.karframework.IPluginProvider;
import io.github.karlatemp.karframework.command.ICommandNode;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BukkitPluginProvider implements IPluginProvider {
    private final Plugin plugin;
    private final File pluginFile;

    public Plugin getPlugin() {
        return plugin;
    }

    public BukkitPluginProvider(@NotNull Plugin plugin) {
        this.plugin = plugin;
        File pluginFile = null;
        try {
            Class<?> scanning = plugin.getClass();
            do {
                Method method;
                try {
                    method = scanning.getDeclaredMethod("getFile");
                    if (method.getReturnType() != File.class)
                        continue;
                    if (Modifier.isStatic(method.getModifiers()))
                        continue;
                    method.setAccessible(true);
                } catch (Throwable ignored) {
                    continue;
                }
                pluginFile = (File) method.invoke(plugin);
            } while ((scanning = scanning.getSuperclass()) != null);
        } catch (Throwable exception) {
            plugin.getLogger().log(Level.WARNING, "Exception in getting file of " + plugin.getDescription().getName(), exception);
        }
        this.pluginFile = pluginFile;
    }

    @Nullable
    @Override
    public File getPluginFile() {
        return pluginFile;
    }

    @Override
    public @Nullable ClassLoader getClassLoader() {
        return plugin.getClass().getClassLoader();
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

    @Override
    public <T> void provideCommand(@NotNull String name, @NotNull ICommandNode<T> node) {
        PluginCommand command;
        name = name.toLowerCase(Locale.ENGLISH);
        if (plugin instanceof JavaPlugin) {
            command = ((JavaPlugin) plugin).getCommand(name);
        } else {
            command = Bukkit.getPluginCommand(name);

            if (command == null || command.getPlugin() != plugin) {
                command = Bukkit.getPluginCommand(plugin.getDescription().getName().toLowerCase(Locale.ENGLISH) + ":" + name);
            }
        }
        if (command == null) {
            throw new NullPointerException("Command `" + name + "` not found.");
        }
        @SuppressWarnings("unchecked")
        CommandProvide provide = new CommandProvide((ICommandNode<CommandSender>) node);
        command.setTabCompleter(provide);
        command.setExecutor(provide);
    }
}
