/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/04 19:11:34
 *
 * kar-framework/kar-framework.spigot-nms-1_16_1.main/NBTByteArray.java
 */

package io.github.karlatemp.karframework.nms.v1_12_R1;

import io.github.karlatemp.karframework.opennbt.ITag;
import io.github.karlatemp.karframework.opennbt.ITagLongArray;
import io.github.karlatemp.karframework.opennbt.ITagNumber;
import net.minecraft.server.v1_12_R1.NBTTagLongArray;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Objects;

public class NBTLongArray
        extends NBTBaseWrapper<NBTTagLongArray>
        implements ITagLongArray {
    public NBTLongArray(NBTTagLongArray base) {
        super(base);
    }

    private static final Field VALUE;

    static {
        try {
            Field f = null;
            for (Field fw : NBTTagLongArray.class.getDeclaredFields()) {
                if (!Modifier.isStatic(fw.getModifiers())) {
                    if (fw.getType() == long[].class) {
                        f = fw;
                        fw.setAccessible(true);
                        break;
                    }
                }
            }
            VALUE = Objects.requireNonNull(f, "Field long[] NBTTagLongArray.value not found");
        } catch (Throwable any) {
            throw new ExceptionInInitializerError(any);
        }
    }

    private void setValue(long[] value) {
        try {
            VALUE.set(base, value);
        } catch (IllegalAccessException e) {
            throw new Error(e);
        }
    }

    @Override
    public boolean add(int index, ITag value) {
        if (value instanceof ITagNumber) {
            add(index, ((ITagNumber) value).asLong());
            return true;
        }
        return false;
    }

    public static  @NotNull long[] getValues(NBTTagLongArray base) {
        try {
            return (long[]) VALUE.get(base);
        } catch (IllegalAccessException e) {
            throw new Error(e);
        }
    }
    @Override
    public @NotNull long[] getValues() {
        return getValues(base);
    }

    @Override
    public long get(int index) {
        return getValues()[index];
    }

    @Override
    public void clear() {
        setValue(new long[0]);
    }

    @Override
    public long set(int index, long value) {
        long[] values = getValues();
        long old = values[index];
        values[index] = value;
        return old;
    }

    @Override
    public void add(int index, long value) {
        setValue(ArrayUtils.add(getValues(), index, value));
    }

    @Override
    public boolean set(int index, ITag value) {
        if (value instanceof ITagNumber) {
            set(index, ((ITagNumber) value).asLong());
            return true;
        }
        return false;
    }

    @Override
    public int size() {
        return getValues().length;
    }

    @Override
    public @NotNull NBTLongArray clone() {
        return new NBTLongArray(base.c());
    }

    @Override
    public long remove(int index) {
        long[] values = getValues();
        long old = values[index];
        setValue(ArrayUtils.remove(values, index));
        return old;
    }
}
