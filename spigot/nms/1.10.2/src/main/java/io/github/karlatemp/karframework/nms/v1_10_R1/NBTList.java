/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/04 19:11:34
 *
 * kar-framework/kar-framework.spigot-nms-1_16_1.main/NBTByteArray.java
 */

package io.github.karlatemp.karframework.nms.v1_10_R1;

import io.github.karlatemp.karframework.opennbt.ITag;
import io.github.karlatemp.karframework.opennbt.ITagCompound;
import io.github.karlatemp.karframework.opennbt.ITagList;
import net.minecraft.server.v1_10_R1.NBTBase;
import net.minecraft.server.v1_10_R1.NBTTagList;
import net.minecraft.server.v1_10_R1.NBTTagShort;
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
        // TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public ITag set(int index, ITag value) {
        // TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(int index, ITag value) {
        // TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean setSafely(int index, ITag value) {
        // TODO: Finish set safely
        return false;
    }

    @Override
    public boolean addSafely(int index, ITag value) {
        // TODO: Finish add safely
        return false;
    }

    @Override
    public int size() {
        return base.size();
    }

    @Override
    public @NotNull NBTList clone() {
        return new NBTList(base.d());
    }

    @Override
    public ITag remove(int index) {
        // TODO: Finish remove
        return of(base.remove(index));
    }

    @Override
    public @NotNull ITagCompound getCompound(int index) {
        return new NBTComponent(base.get(index));
    }

    @Override
    public @NotNull ITagList getList(int index) {
        final NBTBase base = this.base.h (index);
        if (base instanceof NBTTagList) {
            return new NBTList((NBTTagList) base);
        }
        return new NBTList(new NBTTagList());
    }

    @Override
    public short getShort(int index) {
        final NBTBase base = this.base.h(index);
        if (base instanceof NBTTagShort) return ((NBTTagShort) base).f();
        return 0;
    }

    @Override
    public int getInt(int index) {
        return base.c(index);
    }

    @Override
    public @NotNull int[] getIntArray(int index) {
        return base.d(index);
    }

    @Override
    public double getDouble(int index) {
        return base.e(index);
    }

    @Override
    public float getFloat(int index) {
        return base.f(index);
    }

    @Override
    public @NotNull String getString(int index) {
        return base.getString(index);
    }

    @Override
    public void add(ITag value) {
        base.add(((NBTBaseWrapper<?>) value).base);
    }
}
