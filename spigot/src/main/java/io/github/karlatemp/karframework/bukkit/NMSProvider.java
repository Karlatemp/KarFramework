/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/03 21:32:24
 *
 * kar-framework/kar-framework.bukkit.main/NMSProvider.java
 */

package io.github.karlatemp.karframework.bukkit;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.command.CommandMap;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public interface NMSProvider {
    void sendPacket(@NotNull Player player, @NotNull Object packet);

    void sendTitle(
            @NotNull Player player,
            @Nullable BaseComponent title,
            @Nullable BaseComponent subtitle,
            int fadeIn, int keep, int fadeOut
    );

    void sendAction(@NotNull Player player, @NotNull BaseComponent component);

    @NotNull Object getHandle(@NotNull Entity entity);

    @NotNull CommandMap getCommandMap();

    @NotNull Function<@NotNull String, @Nullable String> getSystemLocale();

    @NotNull BaseComponent[] toComponents(@NotNull ItemStack itemStack);

    @NotNull BaseComponent[] getItemName(@NotNull ItemStack itemStack);

    @NotNull String getVersion();

}
