/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/04 19:11:34
 *
 * kar-framework/kar-framework.spigot-nms-1_16_1.main/NBTByteArray.java
 */

package io.github.karlatemp.karframework.nms.v1_16_R2;

import io.github.karlatemp.karframework.opennbt.ITagByteArray;
import net.minecraft.server.v1_16_R2.NBTTagByte;
import net.minecraft.server.v1_16_R2.NBTTagByteArray;
import org.jetbrains.annotations.NotNull;

public class NBTByteArray
        extends NBTBaseList<NBTTagByteArray>
        implements ITagByteArray {
    public NBTByteArray(NBTTagByteArray base) {
        super(base);
    }

    @Override
    public byte get(int index) {
        return base.get(index).asByte();
    }

    @Override
    public byte remove(int index) {
        return base.remove(index).asByte();
    }

    @Override
    public @NotNull byte[] getValues() {
        return base.getBytes();
    }

    @Override
    public void clear() {
        base.clear();
    }

    @Override
    public byte set(int index, byte value) {
        return base.set(index, NBTTagByte.a(value)).asByte();
    }

    @Override
    public void add(int index, byte value) {
        base.add(index, NBTTagByte.a(value));
    }

    @Override
    public int size() {
        return base.size();
    }

    @Override
    public @NotNull ITagByteArray clone() {
        return new NBTByteArray((NBTTagByteArray) base.clone());
    }
}
