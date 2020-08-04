/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/04 19:19:39
 *
 * kar-framework/kar-framework.spigot-nms-1_16_1.main/NBTComponend.java
 */

package io.github.karlatemp.karframework.nms.v1_14_R1;

import io.github.karlatemp.karframework.opennbt.ITag;
import io.github.karlatemp.karframework.opennbt.ITagCompound;
import io.github.karlatemp.karframework.opennbt.ITagList;
import net.minecraft.server.v1_14_R1.NBTBase;
import net.minecraft.server.v1_14_R1.NBTTagCompound;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class NBTComponent
        extends NBTBaseWrapper<NBTTagCompound>
        implements ITagCompound {
    public NBTComponent(NBTTagCompound compound) {
        super(compound);
    }

    @Override
    public @Nullable ITag get(@NotNull String key) {
        return of(base.get(key));
    }

    @Override
    public void put(@NotNull String key, @Nullable ITag tag) {
        set(key, tag);
    }

    @Override
    public @NotNull Set<@NotNull String> keySet() {
        return base.getKeys();
    }

    @Override
    public int size() {
        return base.d();
    }

    @Override
    public @Nullable ITag set(String key, ITag value) {
        NBTBase base = value == null ? null : ((NBTBaseWrapper<?>) value).base;
        return of(super.base.set(key, base));
    }

    @Override
    public void setByte(String key, byte value) {
        base.setByte(key, value);
    }

    @Override
    public void setShort(String key, short value) {
        base.setShort(key, value);
    }

    @Override
    public void setInt(String key, int value) {
        base.setInt(key, value);
    }

    @Override
    public void setLong(String key, long value) {
        base.setLong(key, value);
    }

    @Override
    public void setUUID(String key, UUID value) {
        base.a(key, value);
    }

    @Override
    public UUID getUUID(String key) {
        return base.a(key);
    }

    @Override
    public void setFloat(String key, float value) {
        base.setFloat(key, value);
    }

    @Override
    public void setDouble(String key, double value) {
        base.setDouble(key, value);
    }

    @Override
    public void setString(String key, String value) {
        base.setString(key, value);
    }

    @Override
    public void setByteArray(String key, byte[] value) {
        base.setByteArray(key, value);
    }

    @Override
    public void setIntArray(String key, int[] value) {
        base.setIntArray(key, value);
    }

    @Override
    public void setIntArray(String key, List<Integer> value) {
        base.b(key, value);
    }

    @Override
    public void setLongArray(String key, long[] value) {
        base.a(key, value);
    }

    @Override
    public void setLongArray(String key, List<Long> value) {
        base.c(key, value);
    }

    @Override
    public void setBoolean(String key, boolean value) {
        base.setBoolean(key, value);
    }

    @Override
    public boolean hasKey(String key) {
        return base.hasKey(key);
    }

    @Override
    public byte getByte(String key) {
        return base.getByte(key);
    }

    @Override
    public short getShort(String key) {
        return base.getShort(key);
    }

    @Override
    public int getInt(String key) {
        return base.getInt(key);
    }

    @Override
    public long getLong(String key) {
        return base.getLong(key);
    }

    @Override
    public float getFloat(String key) {
        return base.getFloat(key);
    }

    @Override
    public double getDouble(String key) {
        return base.getDouble(key);
    }

    @Override
    public @NotNull String getString(String key) {
        return base.getString(key);
    }

    @Override
    public @NotNull byte[] getByteArray(String key) {
        return base.getByteArray(key);
    }

    @Override
    public @NotNull int[] getIntArray(String key) {
        return base.getIntArray(key);
    }

    @Override
    public @NotNull long[] getLongArray(String key) {
        return base.getLongArray(key);
    }

    @Override
    public @NotNull ITagCompound getCompound(String key) {
        return new NBTComponent(base.getCompound(key));
    }

    @Override
    public @NotNull ITagList getList(String key, int type) {
        return (ITagList) of(base.getList(key, type));
    }

    @Override
    public boolean getBoolean(String key) {
        return base.getBoolean(key);
    }

    @Override
    public void remove(String key) {
        base.remove(key);
    }

    @Override
    public @NotNull ITagCompound clone() {
        return new NBTComponent(base.clone());
    }

    @Override
    public @NotNull ITagCompound merge(@NotNull ITagCompound compound) {
        base.a(((NBTComponent) compound).base);
        return this;
    }
}
