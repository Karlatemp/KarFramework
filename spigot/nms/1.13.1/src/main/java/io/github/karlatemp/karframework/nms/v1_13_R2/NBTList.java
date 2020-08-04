/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/04 19:11:34
 *
 * kar-framework/kar-framework.spigot-nms-1_16_1.main/NBTByteArray.java
 */

package io.github.karlatemp.karframework.nms.v1_13_R2;

import io.github.karlatemp.karframework.opennbt.ITag;
import io.github.karlatemp.karframework.opennbt.ITagCompound;
import io.github.karlatemp.karframework.opennbt.ITagList;
import net.minecraft.server.v1_13_R2.NBTBase;
import net.minecraft.server.v1_13_R2.NBTTagList;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.List;

public class NBTList
        extends NBTBaseWrapper<NBTTagList>
        implements ITagList {
    public NBTList(NBTTagList base) {
        super(base);
    }

    private static final Field
            typeFIELD, listFIELD;

    @SuppressWarnings("unchecked")
    private List<NBTBase> getList() {
        try {
            return (List<NBTBase>) listFIELD.get(base);
        } catch (IllegalAccessException e) {
            throw new Error(e);
        }
    }

    private byte getType() {
        try {
            return typeFIELD.getByte(base);
        } catch (IllegalAccessException e) {
            throw new Error(e);
        }
    }

    private void setType(byte type) {
        try {
            typeFIELD.setByte(base, type);
        } catch (IllegalAccessException e) {
            throw new Error(e);
        }
    }

    static {
        try {
            (typeFIELD = NBTTagList.class.getDeclaredField("type")).setAccessible(true);
            (listFIELD = NBTTagList.class.getDeclaredField("list")).setAccessible(true);
        } catch (Throwable any) {
            throw new ExceptionInInitializerError(any);
        }
    }


    @Override
    public ITag get(int index) {
        return of(base.get(index));
    }

    @Override
    public void clear() {
        getList().clear();
        setType((byte) 0);
    }

    @Override
    public ITag set(int index, ITag value) {
        return of(base.set(index, ((NBTBaseWrapper<?>) value).base));
    }

    @Override
    public void add(int index, ITag value) {
        NBTBase tag = ((NBTBaseWrapper<?>) value).base;
        if (getType() == 0) {
            setType(tag.getTypeId());
        } else if (getType() != tag.getTypeId()) {
            throw new UnsupportedOperationException(String.format("Trying to add tag of type %d to list of %d", tag.getTypeId(), getType()));
        }
        getList().add(index, tag);
    }

    @Override
    public void add(ITag value) {
        base.add(((NBTBaseWrapper<?>) value).base);
    }

    @Override
    public boolean setSafely(int index, ITag value) {
        NBTBase tag = testSafely(value);
        if (tag != null) {
            getList().set(index, tag);
            return true;
        }
        return false;
    }

    private NBTBase testSafely(ITag value) {
        byte type = getType();
        NBTBase base = ((NBTBaseWrapper<?>) value).base;
        if (base.getTypeId() == 0) // ?????
            return null;
        if (type == base.getTypeId())
            return base;
        if (base.getTypeId() == 0) {
            setType(base.getTypeId());
            return base;
        }
        return null;
    }

    @Override
    public boolean addSafely(int index, ITag value) {
        NBTBase tag = testSafely(value);
        if (tag != null) {
            getList().add(index, tag);
            return true;
        }
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
        return reset(base.remove(index));
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
        return base.k(index);
    }

    @Override
    public float getFloat(int index) {
        return base.l(index);
    }

    @Override
    public @NotNull String getString(int index) {
        return base.getString(index);
    }

    private ITag reset(NBTBase a) {
        if (size() == 0) setType((byte) 0);
        return NBTBaseWrapper.of(a);
    }
}
