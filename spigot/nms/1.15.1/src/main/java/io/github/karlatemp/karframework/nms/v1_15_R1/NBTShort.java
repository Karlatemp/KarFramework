/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/04 19:29:26
 *
 * kar-framework/kar-framework.spigot-nms-1_16_1.main/NBTDouble.java
 */

package io.github.karlatemp.karframework.nms.v1_15_R1;

import io.github.karlatemp.karframework.opennbt.ITagShort;
import net.minecraft.server.v1_15_R1.NBTTagShort;
import org.jetbrains.annotations.NotNull;

public class NBTShort extends NBTNumberBase<NBTTagShort> implements ITagShort {
    public NBTShort(NBTTagShort base) {
        super(base);
    }

    @Override
    public short getValue() {
        return base.asShort();
    }

    @Override
    public @NotNull ITagShort clone() {
        return new NBTShort(base.clone());
    }
}
