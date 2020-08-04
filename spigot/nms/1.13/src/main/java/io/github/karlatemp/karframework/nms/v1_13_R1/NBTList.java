/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/04 19:11:34
 *
 * kar-framework/kar-framework.spigot-nms-1_16_1.main/NBTByteArray.java
 */

package io.github.karlatemp.karframework.nms.v1_13_R1;

import io.github.karlatemp.karframework.opennbt.ITag;
import io.github.karlatemp.karframework.opennbt.ITagCompound;
import io.github.karlatemp.karframework.opennbt.ITagList;
import net.minecraft.server.v1_13_R1.NBTTagList;
import org.jetbrains.annotations.NotNull;

public class NBTList
        extends NBTBaseWrapper<NBTTagList>
        implements ITagList {
    public NBTList(NBTTagList base) {
        super(base);
    }

    @Override
    public ITag get(int index) {
        return of(base.get(index));
    }

    @Override
    public void clear() {
        base.clear();
    }

    @Override
    public ITag set(int index, ITag value) {
        return of(base.set(index, ((NBTBaseWrapper<?>) value).base));
    }

    @Override
    public void add(int index, ITag value) {
        base.add(index, ((NBTBaseWrapper<?>) value).base);
    }

    @Override
    public void add(ITag value) {
        base.add(((NBTBaseWrapper<?>) value).base);
    }

    @Override
    public boolean setSafely(int index, ITag value) {
        return false;
    }

    @Override
    public boolean addSafely(int index, ITag value) {
        return false;
    }

    @Override
    public int size() {
        return base.size();
    }

    @Override
    public @NotNull NBTList clone() {
        return new NBTList(base.c());
    }

    @Override
    public ITag remove(int index) {
        return of(base.remove(index));
    }

    @Override
    public @NotNull ITagCompound getCompound(int index) {
        return new NBTComponent(base.getCompound(index));
    }

    @Override
    public @NotNull ITagList getList(int index) {
        return new NBTList(base.f(index));
    }

    @Override
    public short getShort(int index) {
        return base.g(index);
    }

    @Override
    public int getInt(int index) {
        return base.h(index);
    }

    @Override
    public @NotNull int[] getIntArray(int index) {
        return base.i(index);
    }

    @Override
    public double getDouble(int index) {
        return base.h(index);
    }

    @Override
    public float getFloat(int index) {
        return base.l(index);
    }

    @Override
    public @NotNull String getString(int index) {
        return base.getString(index);
    }
}
