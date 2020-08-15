/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/04 19:11:34
 *
 * kar-framework/kar-framework.spigot-nms-1_16_1.main/NBTByteArray.java
 */

package io.github.karlatemp.karframework.nms.v1_16_R2;

import io.github.karlatemp.karframework.opennbt.ITagIntArray;
import net.minecraft.server.v1_16_R2.NBTTagInt;
import net.minecraft.server.v1_16_R2.NBTTagIntArray;
import org.jetbrains.annotations.NotNull;

public class NBTIntArray
        extends NBTBaseList<NBTTagIntArray>
        implements ITagIntArray {
    public NBTIntArray(NBTTagIntArray base) {
        super(base);
    }

    @Override
    public @NotNull int[] getValues() {
        return base.getInts();
    }

    @Override
    public int get(int index) {
        return base.get(index).asInt();
    }

    @Override
    public void clear() {
        base.clear();
    }

    @Override
    public int set(int index, int value) {
        return base.set(index, NBTTagInt.a(value)).asInt();
    }

    @Override
    public void add(int index, int value) {
        base.add(index, NBTTagInt.a(value));
    }

    @Override
    public int size() {
        return base.size();
    }

    @Override
    public @NotNull NBTIntArray clone() {
        return new NBTIntArray(base.clone());
    }

    @Override
    public int remove(int index) {
        return base.remove(index).asInt();
    }
}
