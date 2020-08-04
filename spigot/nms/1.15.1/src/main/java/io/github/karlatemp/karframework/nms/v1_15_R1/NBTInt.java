/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/04 19:29:26
 *
 * kar-framework/kar-framework.spigot-nms-1_16_1.main/NBTDouble.java
 */

package io.github.karlatemp.karframework.nms.v1_15_R1;

import io.github.karlatemp.karframework.opennbt.ITagInt;
import net.minecraft.server.v1_15_R1.NBTTagInt;
import org.jetbrains.annotations.NotNull;

public class NBTInt extends NBTNumberBase<NBTTagInt> implements ITagInt {
    public NBTInt(NBTTagInt base) {
        super(base);
    }

    @Override
    public int getValue() {
        return base.asInt();
    }

    @Override
    public @NotNull ITagInt clone() {
        return new NBTInt(base.clone());
    }
}
