/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/04 19:29:26
 *
 * kar-framework/kar-framework.spigot-nms-1_16_1.main/NBTDouble.java
 */

package io.github.karlatemp.karframework.nms.v1_8_R1;

import io.github.karlatemp.karframework.opennbt.ITagInt;
import net.minecraft.server.v1_8_R1.NBTTagInt;
import org.jetbrains.annotations.NotNull;

public class NBTInt extends NBTBaseWrapper<NBTTagInt> implements ITagInt {
    public NBTInt(NBTTagInt base) {
        super(base);
    }

    @Override
    public int getValue() {
        return asInt();
    }

    @Override
    public @NotNull ITagInt clone() {
        return new NBTInt(new NBTTagInt(asInt()));
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
