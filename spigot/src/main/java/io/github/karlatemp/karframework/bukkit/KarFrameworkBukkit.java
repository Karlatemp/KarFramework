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
import io.github.karlatemp.karframework.format.FormatAction;
import io.github.karlatemp.karframework.format.Translator;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class KarFrameworkBukkit extends AbstractCommandFramework<CommandSender> implements IKarFramework {
    static KarFrameworkBukkit INSTANCE;
    static final String nmsVersion;
    static final NMSProvider nmsProvider;

    static {
        Server server = Bukkit.getServer();
        Class<? extends Server> craftServerClass = server.getClass();
        String ov = craftServerClass.getName().substring(
                "org.bukkit.craftserver.".length()
        );
        int split = ov.indexOf('.');
        nmsVersion = ov.substring(0, split);
        nmsProvider = buildNMSImplement(NMSProvider.class, FormatAction.parse(
                "io.github.karlatemp.karframework.nms.{0}.NMSProviderImpl"
        ));
    }

    public static @NotNull NMSProvider getNmsProvider() {
        return nmsProvider;
    }

    @NotNull
    public static <T> T buildNMSImplement(
            @NotNull Class<T> topClass,
            @NotNull FormatAction classFormat
    ) {
        ClassLoader loader = topClass.getClassLoader();
        try {
            return Class.forName(
                    classFormat.apply(new String[]{nmsVersion}),
                    true,
                    loader
            ).asSubclass(topClass).newInstance();
        } catch (Throwable any) {
            throw new RuntimeException(any);
        }
    }

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
