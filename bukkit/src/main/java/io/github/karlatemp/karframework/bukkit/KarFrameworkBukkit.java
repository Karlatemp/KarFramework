/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/03 13:08:30
 *
 * kar-framework/kar-framework.bukkit.main/KarFrameworkBukkit.java
 */

package io.github.karlatemp.karframework.bukkit;

import io.github.karlatemp.karframework.IKarFramework;
import io.github.karlatemp.karframework.IPluginProvider;
import io.github.karlatemp.karframework.command.AbstractCommandFramework;
import io.github.karlatemp.karframework.format.Translator;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class KarFrameworkBukkit extends AbstractCommandFramework<CommandSender> implements IKarFramework {
    static KarFrameworkBukkit INSTANCE;

    KarFrameworkBukkit(@NotNull Translator translator) {
        super(translator);
    }

    @SuppressWarnings("unused")
    public static KarFrameworkBukkit getInstance() {
        return INSTANCE;
    }

    @Override
    public @Nullable IPluginProvider provide(@NotNull Object plugin) {
        if (plugin instanceof Plugin) {
            return new BukkitPluginProvider((Plugin) plugin);
        }
        return null;
    }

    @Override
    public void sendMessage(@NotNull CommandSender target, @NotNull String message) {
        target.sendMessage(message);
    }

    @Override
    public boolean hasPermission(@NotNull CommandSender target, @Nullable String permission) {
        if (permission == null) return true;
        return target.hasPermission(permission);
    }

    @Override
    public @NotNull String getName(@NotNull CommandSender target) {
        return target.getName();
    }
}
