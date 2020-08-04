/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/04 20:17:29
 *
 * kar-framework/kar-framework.spigot.main/IBukkitNbtProvider.java
 */

package io.github.karlatemp.karframework.bukkit;

import io.github.karlatemp.karframework.opennbt.INbtProvider;
import io.github.karlatemp.karframework.opennbt.ITagCompound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.Base64;

public interface IBukkitNbtProvider extends INbtProvider {
    @NotNull ITagCompound readCompound(Entity entity);

    @NotNull ITagCompound readCompound(ItemStack itemStack);

    @NotNull ItemStack fromCompound(@NotNull ITagCompound compound);

    @NotNull ITagCompound readFromIO(@NotNull DataInput input) throws IOException;

    void writeToIO(@NotNull ITagCompound nbt, @NotNull DataOutput output) throws IOException;

    default @NotNull byte[] toBytes(@NotNull ITagCompound nbt) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(16 * nbt.size());
        try {
            writeToIO(nbt, new DataOutputStream(baos));
        } catch (IOException ioException) {
            throw new RuntimeException(ioException);
        }
        return baos.toByteArray();
    }

    default @NotNull String toBase64(@NotNull ITagCompound nbt) {
        return Base64.getEncoder().encodeToString(toBytes(nbt));
    }

    default @NotNull ITagCompound fromBase64(@NotNull String base64) throws IOException {
        return fromBytes(Base64.getDecoder().decode(base64));
    }

    default @NotNull ITagCompound fromBytes(@NotNull byte[] bytes) throws IOException {
        return readFromIO(new DataInputStream(new ByteArrayInputStream(bytes)));
    }
}
