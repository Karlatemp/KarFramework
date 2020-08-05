/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/03 21:58:18
 *
 * kar-framework/kar-framework.bukkit-nms-1_8_3.main/NMSProviderImpl.java
 */

package io.github.karlatemp.karframework.nms.v1_8_R2;

import io.github.karlatemp.karframework.bukkit.NMSProvider;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import net.minecraft.server.v1_8_R2.*;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.craftbukkit.v1_8_R2.CraftServer;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R2.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

@SuppressWarnings("unused")
public class NMSProviderImpl implements NMSProvider {
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
        return LocaleI18n::get;
    }

    @Override
    public @NotNull BaseComponent[] getItemName(@NotNull org.bukkit.inventory.ItemStack itemStack) {
        ItemStack stack = CraftItemStack.asNMSCopy(itemStack);
        return TextComponent.fromLegacyText(stack.getName());
    }

    @Override
    public @NotNull BaseComponent[] toComponents(@NotNull org.bukkit.inventory.ItemStack itemStack) {
        ItemStack stack = CraftItemStack.asNMSCopy(itemStack);
        return ComponentSerializer.parse(IChatBaseComponent.ChatSerializer.a(stack.C()));
    }
}
