/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/04 19:11:34
 *
 * kar-framework/kar-framework.spigot-nms-1_16_1.main/NBTByteArray.java
 */

package io.github.karlatemp.karframework.nms.v1_13_R2;

import io.github.karlatemp.karframework.opennbt.ITag;
import io.github.karlatemp.karframework.opennbt.ITagIntArray;
import io.github.karlatemp.karframework.opennbt.ITagNumber;
import net.minecraft.server.v1_13_R2.NBTTagInt;
import net.minecraft.server.v1_13_R2.NBTTagIntArray;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Objects;

public class NBTIntArray
        extends NBTBaseList<NBTTagIntArray>
        implements ITagIntArray {
    public NBTIntArray(NBTTagIntArray base) {
        super(base);
    }

    private static final Field VALUE;

    static {
        try {
            Field f = null;
            for (Field fw : NBTTagIntArray.class.getDeclaredFields()) {
                if (!Modifier.isStatic(fw.getModifiers())) {
                    if (fw.getType() == int[].class) {
                        f = fw;
                        fw.setAccessible(true);
                        break;
                    }
                }
            }
            VALUE = Objects.requireNonNull(f, "Field int[] NBTTagIntArray.value not found");
        } catch (Throwable any) {
            throw new ExceptionInInitializerError(any);
        }
    }

    private void setValue(int[] value) {
        try {
            VALUE.set(base, value);
        } catch (IllegalAccessException e) {
            throw new Error(e);
        }
    }

    @Override
    public @NotNull int[] getValues() {
        return base.d();
    }

    @Override
    public int get(int index) {
        return base.get(index).e();
    }

    @Override
    public void clear() {
        base.clear();
    }

    @Override
    public int set(int index, int value) {
        return base.set(index, new NBTTagInt(value)).e();
    }

    @Override
    public void add(int index, int value) {
        setValue(ArrayUtils.add(getValues(), index, value));
    }

    @Override
    public int size() {
        return base.size();
    }

    @Override
    public @NotNull NBTIntArray clone() {
        return new NBTIntArray(new NBTTagIntArray(
                Arrays.copyOf(getValues(), size())
        ));
    }

    @Override
    public boolean add(int index, ITag value) {
        if (value instanceof ITagNumber) {
            add(index, ((ITagNumber) value).asInt());
            return true;
        }
        return false;
    }

    @Override
    public int remove(int index) {
        int[] values = getValues();
        int old = values[index];
        setValue(ArrayUtils.remove(values, index));
        return old;
    }
}
