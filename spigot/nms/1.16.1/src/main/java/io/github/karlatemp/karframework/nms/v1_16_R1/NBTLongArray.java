/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/04 19:11:34
 *
 * kar-framework/kar-framework.spigot-nms-1_16_1.main/NBTByteArray.java
 */

package io.github.karlatemp.karframework.nms.v1_16_R1;

import io.github.karlatemp.karframework.opennbt.ITagLongArray;
import net.minecraft.server.v1_16_R1.NBTTagLong;
import net.minecraft.server.v1_16_R1.NBTTagLongArray;
import org.jetbrains.annotations.NotNull;

public class NBTLongArray
        extends NBTBaseList<NBTTagLongArray>
        implements ITagLongArray {
    public NBTLongArray(NBTTagLongArray base) {
        super(base);
    }

    @Override
    public @NotNull long[] getValues() {
        return base.getLongs();
    }

    @Override
    public long get(int index) {
        return base.get(index).asLong();
    }

    @Override
    public void clear() {
        base.clear();
    }

    @Override
    public long set(int index, long value) {
        return base.set(index, NBTTagLong.a(value)).asLong();
    }

    @Override
    public void add(int index, long value) {
        base.add(index, NBTTagLong.a(value));
    }

    @Override
    public int size() {
        return base.size();
    }

    @Override
    public @NotNull NBTLongArray clone() {
        return new NBTLongArray(base.clone());
    }

    @Override
    public long remove(int index) {
        return base.remove(index).asLong();
    }
}
