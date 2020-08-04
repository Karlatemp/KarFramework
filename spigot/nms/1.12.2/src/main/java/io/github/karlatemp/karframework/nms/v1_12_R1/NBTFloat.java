/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/04 19:29:26
 *
 * kar-framework/kar-framework.spigot-nms-1_16_1.main/NBTDouble.java
 */

package io.github.karlatemp.karframework.nms.v1_12_R1;

import io.github.karlatemp.karframework.opennbt.ITagFloat;
import net.minecraft.server.v1_12_R1.NBTTagFloat;
import org.jetbrains.annotations.NotNull;

public class NBTFloat extends NBTBaseWrapper<NBTTagFloat> implements ITagFloat {
    public NBTFloat(NBTTagFloat base) {
        super(base);
    }

    @Override
    public float getValue() {
        return asFloat();
    }

    @Override
    public @NotNull ITagFloat clone() {
        return new NBTFloat(base.c());
    }

    @Override
    public int asInt() {
        return base.e();
    }

    @Override
    public byte asByte() {
        return base.g();
    }

    @Override
    public short asShort() {
        return base.f();
    }

    @Override
    public long asLong() {
        return base.d();
    }

    @Override
    public double asDouble() {
        return base.asDouble();
    }

    @Override
    public float asFloat() {
        return base.i();
    }}
