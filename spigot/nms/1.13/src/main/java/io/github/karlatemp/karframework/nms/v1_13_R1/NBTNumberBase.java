/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/04 20:50:19
 *
 * kar-framework/kar-framework.spigot-nms-1_13_1.main/NBTNumberBase.java
 */

package io.github.karlatemp.karframework.nms.v1_13_R1;

import io.github.karlatemp.karframework.opennbt.ITagNumber;
import net.minecraft.server.v1_13_R1.NBTNumber;
import org.jetbrains.annotations.NotNull;

public abstract class NBTNumberBase<T extends NBTNumber>
        extends NBTBaseWrapper<T>
        implements ITagNumber {
    public NBTNumberBase(T base) {
        super(base);
    }

    @Override
    public byte asByte() {
        return base.g();
    }

    @Override
    public double asDouble() {
        return base.asDouble();
    }

    @Override
    public float asFloat() {
        return base.i();
    }

    @Override
    public int asInt() {
        return base.e();
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
    public abstract @NotNull ITagNumber clone();
}
