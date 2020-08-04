/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/04 19:29:26
 *
 * kar-framework/kar-framework.spigot-nms-1_16_1.main/NBTDouble.java
 */

package io.github.karlatemp.karframework.nms.v1_8_R3;

import io.github.karlatemp.karframework.opennbt.ITagShort;
import net.minecraft.server.v1_8_R3.NBTTagShort;
import org.jetbrains.annotations.NotNull;

public class NBTShort extends NBTBaseWrapper<NBTTagShort> implements ITagShort {
    public NBTShort(NBTTagShort base) {
        super(base);
    }

    @Override
    public short getValue() {
        return asShort();
    }

    @Override
    public @NotNull ITagShort clone() {
        return new NBTShort(new NBTTagShort(asShort()));
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
