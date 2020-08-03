/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/03 13:08:30
 *
 * kar-framework/kar-framework.bungee.main/KarFrameworkBungee.java
 */

package io.github.karlatemp.karframework.bungee;

import io.github.karlatemp.karframework.IKarFramework;
import io.github.karlatemp.karframework.IPluginProvider;
import io.github.karlatemp.karframework.command.AbstractCommandFramework;
import io.github.karlatemp.karframework.format.Translator;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class KarFrameworkBungee
        extends AbstractCommandFramework<CommandSender>
        implements IKarFramework {
    static KarFrameworkBungee INSTANCE;

    @SuppressWarnings("unused")
    public static KarFrameworkBungee getInstance() {
        return INSTANCE;
    }

    KarFrameworkBungee(Translator translator) {
        super(translator);
    }

    @Override
    public @Nullable IPluginProvider provide(@NotNull Object plugin) {
        if (plugin instanceof Plugin)
            return new BungeePluginProvider((Plugin) plugin);
        return null;
    }

    @Override
    public void sendMessage(@NotNull CommandSender target, @NotNull String message) {
        target.sendMessage(TextComponent.fromLegacyText(message));
    }

    @Override
    public boolean hasPermission(@NotNull CommandSender target, @Nullable String permission) {
        return target.hasPermission(permission);
    }

    @Override
    public @NotNull String getName(@NotNull CommandSender target) {
        return target.getName();
    }
}
