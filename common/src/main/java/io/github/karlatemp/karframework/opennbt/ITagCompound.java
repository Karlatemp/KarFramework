/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/04 18:05:04
 *
 * kar-framework/kar-framework.common.main/ITagCompound.java
 */

package io.github.karlatemp.karframework.opennbt;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface ITagCompound extends ITag {
    @Nullable ITag get(@NotNull String key);

    void put(@NotNull String key, @Nullable ITag tag);

    @NotNull Set<@NotNull String> keySet();

    int size();

    @Nullable ITag set(String key, ITag value);

    void setByte(String key, byte value);

    void setShort(String key, short value);

    void setInt(String key, int value);

    void setLong(String key, long value);

    void setUUID(String key, UUID value);

    UUID getUUID(String key);

    void setFloat(String key, float value);

    void setDouble(String key, double value);

    void setString(String key, String value);

    void setByteArray(String key, byte[] value);

    void setIntArray(String key, int[] value);

    void setIntArray(String key, List<Integer> value);

    void setLongArray(String key, long[] value);

    void setLongArray(String key, List<Long> value);

    void setBoolean(String key, boolean value);

    boolean hasKey(String key);

    byte getByte(String key);

    short getShort(String key);

    int getInt(String key);

    long getLong(String key);

    float getFloat(String key);

    double getDouble(String key);

    @NotNull String getString(String key);

    @NotNull byte[] getByteArray(String key);

    @NotNull int[] getIntArray(String key);

    @NotNull long[] getLongArray(String key);

    @NotNull ITagCompound getCompound(String key);

    @NotNull ITagList getList(String key,int type);

    boolean getBoolean(String key);

    void remove(String key);

    @NotNull ITagCompound clone();

    @NotNull ITagCompound merge(@NotNull ITagCompound compound);
}
