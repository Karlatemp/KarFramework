/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/04 19:09:43
 *
 * kar-framework/kar-framework.spigot-nms-1_16_1.main/NBTByte.java
 */

package io.github.karlatemp.karframework.nms.v1_9_R1;

import io.github.karlatemp.karframework.opennbt.ITagByte;
import net.minecraft.server.v1_9_R1.NBTTagByte;
import org.jetbrains.annotations.NotNull;

public class NBTByte extends NBTBaseWrapper<NBTTagByte>
        implements ITagByte {
    public NBTByte(NBTTagByte value) {
        super(value);
    }

    @Override
    public byte getValue() {
        return asByte();
    }

    @Override
    public @NotNull ITagByte clone() {
        return new NBTByte(new NBTTagByte(asByte()));
    }

    @Override
    public int asInt() {
        return base.d();
    }

    @Override
    public byte asByte() {
        return base.f();
    }

    @Override
    public short asShort() {
        return base.e();
    }

    @Override
    public long asLong() {
        return base.c();
    }

    @Override
    public double asDouble() {
        return base.g();
    }

    @Override
    public float asFloat() {
        return base.h();
    }
}
