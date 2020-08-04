/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/04 19:54:33
 *
 * kar-framework/kar-framework.spigot.main/UnsupportedNBTProvider.java
 */

package io.github.karlatemp.karframework.bukkit;

import io.github.karlatemp.karframework.opennbt.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

class UnsupportedNBTProvider implements IBukkitNbtProvider {
    @Override
    public @NotNull ITagByte newByte(byte value) {
        throw new UnsupportedOperationException("Unsupported operation.");
    }

    @Override
    public @NotNull ITagByteArray newByteArray(@NotNull byte[] value) {
        throw new UnsupportedOperationException("Unsupported operation.");
    }

    @Override
    public @NotNull ITagCompound newCompound() {
        throw new UnsupportedOperationException("Unsupported operation.");
    }

    @Override
    public @NotNull ITagDouble newDouble(double value) {
        throw new UnsupportedOperationException("Unsupported operation.");
    }

    @Override
    public @NotNull ITagFloat newFloat(float value) {
        throw new UnsupportedOperationException("Unsupported operation.");
    }

    @Override
    public @NotNull ITagInt newInt(int value) {
        throw new UnsupportedOperationException("Unsupported operation.");
    }

    @Override
    public @NotNull ITagIntArray newIntArray(@NotNull int[] value) {
        throw new UnsupportedOperationException("Unsupported operation.");
    }

    @Override
    public @NotNull ITagList newList() {
        throw new UnsupportedOperationException("Unsupported operation.");
    }

    @Override
    public @NotNull ITagLong newLong(long value) {
        throw new UnsupportedOperationException("Unsupported operation.");
    }

    @Override
    public @NotNull ITagLongArray newLongArray(@NotNull long[] value) {
        throw new UnsupportedOperationException("Unsupported operation.");
    }

    @Override
    public @NotNull ITagShort newShort(short value) {
        throw new UnsupportedOperationException("Unsupported operation.");
    }

    @Override
    public @NotNull ITagString newString(@NotNull String value) {
        throw new UnsupportedOperationException("Unsupported operation.");
    }

    @Override
    public @NotNull ITagCompound readCompound(Entity entity) {
        throw new UnsupportedOperationException("Unsupported operation.");
    }

    @Override
    public @NotNull ITagCompound readCompound(ItemStack itemStack) {
        throw new UnsupportedOperationException("Unsupported operation.");
    }

    @Override
    public @NotNull ItemStack fromCompound(@NotNull ITagCompound compound) {
        throw new UnsupportedOperationException("Unsupported operation.");
    }

    @Override
    public @NotNull ITagCompound readFromIO(@NotNull DataInput input) throws IOException {
        throw new UnsupportedOperationException("Unsupported operation.");
    }

    @Override
    public void writeToIO(@NotNull ITagCompound nbt, @NotNull DataOutput output) throws IOException {
        throw new UnsupportedOperationException("Unsupported operation.");
    }
}
