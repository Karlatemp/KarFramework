/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/03 22:08:23
 *
 * kar-framework/kar-framework.bukkit-nms-1_10_2.main/NMSProviderImpl.java
 */

package io.github.karlatemp.karframework.nms.v1_10_R1;

import io.github.karlatemp.karframework.bukkit.NMSProvider;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import net.minecraft.server.v1_10_R1.*;
import org.apache.commons.lang.reflect.FieldUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.craftbukkit.v1_10_R1.CraftServer;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

@SuppressWarnings("unused")
public class NMSProviderImpl implements NMSProvider {
    private final LocaleLanguage manager;

    {
        try {
            manager = (LocaleLanguage) FieldUtils.readDeclaredStaticField(LocaleLanguage.class, "c", true);
        } catch (Throwable any) {
            throw new ExceptionInInitializerError(any);
        }
    }

    @Override
    public void sendPacket(@NotNull Player player, @NotNull Object packet) {
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket((Packet<?>) packet);
    }

    @Override
    public void sendTitle(@NotNull Player player, @Nullable BaseComponent title, @Nullable BaseComponent subtitle, int fadeIn, int keep, int fadeOut) {
        final PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
        if (title != null) {
            connection.sendPacket(new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, IChatBaseComponent.ChatSerializer.a(
                    ComponentSerializer.toString(title)
            )));
        }
        if (subtitle != null) {
            connection.sendPacket(new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, IChatBaseComponent.ChatSerializer.a(
                    ComponentSerializer.toString(subtitle)
            )));
        }
        if (title == null && subtitle == null) {
            connection.sendPacket(new PacketPlayOutTitle(
                    PacketPlayOutTitle.EnumTitleAction.CLEAR, null
            ));
            return;
        }
        connection.sendPacket(new PacketPlayOutTitle(
                fadeIn, keep, fadeOut
        ));
    }

    @Override
    public void sendAction(@NotNull Player player, @NotNull BaseComponent component) {
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(
                new PacketPlayOutChat(
                        IChatBaseComponent.ChatSerializer.a(ComponentSerializer.toString(component)),
                        (byte) 2
                )
        );
    }

    @Override
    public @NotNull Object getHandle(@NotNull Entity entity) {
        return ((CraftEntity) entity).getHandle();
    }

    @Override
    public @NotNull CommandMap getCommandMap() {
        return ((CraftServer) Bukkit.getServer()).getCommandMap();
    }

    @Override
    public @NotNull Function<@NotNull String, @Nullable String> getSystemLocale() {
        return manager::a;
    }
}
