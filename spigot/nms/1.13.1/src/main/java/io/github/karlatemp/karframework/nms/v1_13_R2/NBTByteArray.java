/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/04 19:11:34
 *
 * kar-framework/kar-framework.spigot-nms-1_16_1.main/NBTByteArray.java
 */

package io.github.karlatemp.karframework.nms.v1_13_R2;

import io.github.karlatemp.karframework.opennbt.ITag;
import io.github.karlatemp.karframework.opennbt.ITagByteArray;
import io.github.karlatemp.karframework.opennbt.ITagNumber;
import net.minecraft.server.v1_13_R2.NBTTagByte;
import net.minecraft.server.v1_13_R2.NBTTagByteArray;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Objects;

public class NBTByteArray
        extends NBTBaseList<NBTTagByteArray>
        implements ITagByteArray {
    public NBTByteArray(NBTTagByteArray base) {
        super(base);
    }

    private static final Field VALUE;

    static {
        try {
            Field f = null;
            for (Field fw : NBTTagByteArray.class.getDeclaredFields()) {
                if (!Modifier.isStatic(fw.getModifiers())) {
                    if (fw.getType() == byte[].class) {
                        f = fw;
                        fw.setAccessible(true);
                        break;
                    }
                }
            }
            VALUE = Objects.requireNonNull(f, "Field byte[] NBTTagByteArray.value not found");
        } catch (Throwable any) {
            throw new ExceptionInInitializerError(any);
        }
    }

    private void setValue(byte[] value) {
        try {
            VALUE.set(base, value);
        } catch (IllegalAccessException e) {
            throw new Error(e);
        }
    }

    @Override
    public byte get(int index) {
        return base.get(index).g();
    }

    @Override
    public byte remove(int index) {
        byte[] values = getValues();
        byte old = values[index];
        setValue(ArrayUtils.remove(values, index));
        return old;
    }

    @Override
    public @NotNull byte[] getValues() {
        return base.c();
    }

    @Override
    public void clear() {
        base.clear();
    }

    @Override
    public byte set(int index, byte value) {
        return base.set(index, new NBTTagByte(value)).g();
    }

    @Override
    public void add(int index, byte value) {
        setValue(ArrayUtils.add(getValues(), index, value));
    }

    @Override
    public boolean add(int index, ITag value) {
        if (value instanceof ITagNumber) {
            add(index, ((ITagNumber) value).asByte());
            return true;
        }
        return false;
    }

    @Override
    public int size() {
        return base.size();
    }

    @Override
    public @NotNull ITagByteArray clone() {
        return new NBTByteArray(new NBTTagByteArray(
                Arrays.copyOf(base.c(), base.size())
        ));
    }
}
