/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/04 19:29:26
 *
 * kar-framework/kar-framework.spigot-nms-1_16_1.main/NBTDouble.java
 */

package io.github.karlatemp.karframework.nms.v1_13_R2;

import io.github.karlatemp.karframework.opennbt.ITagDouble;
import net.minecraft.server.v1_13_R2.NBTTagDouble;
import org.jetbrains.annotations.NotNull;

public class NBTDouble extends NBTNumberBase<NBTTagDouble> implements ITagDouble {
    public NBTDouble(NBTTagDouble base) {
        super(base);
    }

    @Override
    public double getValue() {
        return base.asDouble();
    }

    @Override
    public @NotNull ITagDouble clone() {
        return new NBTDouble(base.c());
    }
}
