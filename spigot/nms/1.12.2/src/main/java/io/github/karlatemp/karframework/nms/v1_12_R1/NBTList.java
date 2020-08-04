/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/04 19:11:34
 *
 * kar-framework/kar-framework.spigot-nms-1_16_1.main/NBTByteArray.java
 */

package io.github.karlatemp.karframework.nms.v1_12_R1;

import io.github.karlatemp.karframework.opennbt.ITag;
import io.github.karlatemp.karframework.opennbt.ITagCompound;
import io.github.karlatemp.karframework.opennbt.ITagList;
import net.minecraft.server.v1_12_R1.NBTBase;
import net.minecraft.server.v1_12_R1.NBTTagList;
import net.minecraft.server.v1_12_R1.NBTTagShort;
import org.jetbrains.annotations.Contract;
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

    @Contract("_, true -> !null")
    private NBTBase check(ITag tag, boolean throwsE) {
        NBTBase nbt = ((NBTBaseWrapper<?>) tag).base;
        top:
        {
            byte type = getType(), ntype = nbt.getTypeId();
            if (ntype == 0) {
                break top;
            } else if (type == 0) {
                setType(ntype);
            } else if (type != ntype)
                break top;
            return nbt;
        }
        if (throwsE) {
            throw new UnsupportedOperationException(
                    String.format("Trying to add tag of type %d to list of %d", nbt.getTypeId(), getType())
            );
        }
        return null;
    }

    @Override
    public ITag set(int index, ITag value) {
        final NBTBase base = check(value, true);
        final List<NBTBase> list = getList();
        return of(list.set(index, base));
    }

    @Override
    public void add(int index, ITag value) {
        getList().add(index,check(value,true));
    }

    @Override
    public boolean setSafely(int index, ITag value) {
        final NBTBase base = check(value, false);
        if(base == null)return false;
        final List<NBTBase> list = getList();
        list.set(index, base);
        return true;
    }

    @Override
    public boolean addSafely(int index, ITag value) {
        final NBTBase base = check(value, false);
        if(base == null)return false;
        final List<NBTBase> list = getList();
        list.add(index, base);
        return true;
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
        return reset(getList().remove(index));
    }

    private ITag reset(NBTBase a) {
        if (size() == 0) setType((byte) 0);
        return NBTBaseWrapper.of(a);
    }

    @Override
    public @NotNull ITagCompound getCompound(int index) {
        return new NBTComponent(base.get(index));
    }

    @Override
    public @NotNull ITagList getList(int index) {
        final NBTBase base = this.base.i(index);
        if (base instanceof NBTTagList) {
            return new NBTList((NBTTagList) base);
        }
        return new NBTList(new NBTTagList());
    }

    @Override
    public short getShort(int index) {
        final NBTBase base = this.base.i(index);
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
        return base.f(index);
    }

    @Override
    public float getFloat(int index) {
        return base.g(index);
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
