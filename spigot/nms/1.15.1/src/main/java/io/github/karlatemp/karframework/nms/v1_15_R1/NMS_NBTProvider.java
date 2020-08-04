/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/04 19:51:37
 *
 * kar-framework/kar-framework.spigot-nms-1_16_1.main/NMS_NBTProvider.java
 */

package io.github.karlatemp.karframework.nms.v1_15_R1;

import io.github.karlatemp.karframework.bukkit.IBukkitNbtProvider;
import io.github.karlatemp.karframework.opennbt.*;
import net.minecraft.server.v1_15_R1.*;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NMS_NBTProvider implements IBukkitNbtProvider {
    @Override
    public @NotNull ITagByte newByte(byte value) {
        return new NBTByte(NBTTagByte.a(value));
    }

    @Override
    public @NotNull ITagByteArray newByteArray(@NotNull byte[] value) {
        return new NBTByteArray(new NBTTagByteArray(value));
    }

    @Override
    public @NotNull ITagCompound newCompound() {
        return new NBTComponent(new NBTTagCompound());
    }

    @Override
    public @NotNull ITagDouble newDouble(double value) {
        return new NBTDouble(NBTTagDouble.a(value));
    }

    @Override
    public @NotNull ITagFloat newFloat(float value) {
        return new NBTFloat(NBTTagFloat.a(value));
    }

    @Override
    public @NotNull ITagInt newInt(int value) {
        return new NBTInt(NBTTagInt.a(value));
    }

    @Override
    public @NotNull ITagIntArray newIntArray(@NotNull int[] value) {
        return new NBTIntArray(new NBTTagIntArray(value));
    }

    @Override
    public @NotNull ITagList newList() {
        return new NBTList(new NBTTagList());
    }

    @Override
    public @NotNull ITagLong newLong(long value) {
        return new NBTLong(NBTTagLong.a(value));
    }

    @Override
    public @NotNull ITagLongArray newLongArray(@NotNull long[] value) {
        return new NBTLongArray(new NBTTagLongArray(value));
    }

    @Override
    public @NotNull ITagShort newShort(short value) {
        return new NBTShort(NBTTagShort.a(value));
    }

    @Override
    public @NotNull ITagString newString(@NotNull String value) {
        return new NBTString(NBTTagString.a(value));
    }


    @Override
    public @NotNull ITagCompound readCompound(Entity entity) {
        return new NBTComponent(
                ((CraftEntity) entity).getHandle().save(new NBTTagCompound())
        );
    }


    @Override
    public @NotNull ITagCompound readCompound(ItemStack itemStack) {
        return new NBTComponent(
                CraftItemStack.asNMSCopy(itemStack).save(new NBTTagCompound())
        );
    }

    @Override
    public @NotNull ItemStack fromCompound(@NotNull ITagCompound compound) {
        return CraftItemStack.asCraftMirror(
                net.minecraft.server.v1_15_R1.ItemStack.a(
                        ((NBTComponent) compound).base
                )
        );
    }

    @Override
    public @NotNull ITagCompound readFromIO(@NotNull DataInput input) throws IOException {
        return new NBTComponent(NBTCompressedStreamTools.a(input, NBTReadLimiter.a));
    }

    @Override
    public void writeToIO(@NotNull ITagCompound nbt, @NotNull DataOutput output) throws IOException {
        NBTCompressedStreamTools.a(((NBTComponent) nbt).base, output);
    }
}
