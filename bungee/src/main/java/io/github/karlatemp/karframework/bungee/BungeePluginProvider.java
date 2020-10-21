/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/03 13:08:30
 *
 * kar-framework/kar-framework.bungee.main/BungeePluginProvider.java
 */

package io.github.karlatemp.karframework.bungee;

import io.github.karlatemp.karframework.IPluginProvider;
import io.github.karlatemp.karframework.command.ICommandNode;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Locale;
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

    @Override
    public <T> void provideCommand(@NotNull String name, @NotNull ICommandNode<T> node) {
        @SuppressWarnings("unchecked") ICommandNode<CommandSender> node0 = (ICommandNode<CommandSender>) node;
        class CC extends Command implements TabExecutor {
            public CC() {
                super(name.toLowerCase(Locale.ENGLISH),
                        node.getPermission(),
                        (plugin.getDescription().getName() + ":" + node.getName()).toLowerCase(Locale.ENGLISH)
                );
            }

            @Override
            public void execute(CommandSender sender, String[] args) {
                node0.execute(sender, Arrays.asList(args));
            }

            @Override
            public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
                return node0.tabCompile(sender, Arrays.asList(args));
            }
        }
        ProxyServer.getInstance().getPluginManager().registerCommand(plugin, new CC());
    }

    @Override
    public @Nullable ClassLoader getClassLoader() {
        return plugin.getClass().getClassLoader();
    }
}
