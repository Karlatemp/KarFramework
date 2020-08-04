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
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class NBTByteArray
        extends NBTBaseList<NBTTagByteArray>
        implements ITagByteArray {
    public NBTByteArray(NBTTagByteArray base) {
        super(base);
    }

    @Override
    public byte get(int index) {
        return base.get(index).g();
    }

    @Override
    public byte remove(int index) {
        return base.remove(index).g();
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
        base.add(index, new NBTTagByte(value));
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
                Arrays.copyOf(base.c(),base.size())
        ));
    }
}
