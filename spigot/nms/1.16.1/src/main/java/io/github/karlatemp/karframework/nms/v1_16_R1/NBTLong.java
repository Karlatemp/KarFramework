/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/04 19:29:26
 *
 * kar-framework/kar-framework.spigot-nms-1_16_1.main/NBTDouble.java
 */

package io.github.karlatemp.karframework.nms.v1_16_R1;

import io.github.karlatemp.karframework.opennbt.ITagFloat;
import io.github.karlatemp.karframework.opennbt.ITagLong;
import net.minecraft.server.v1_16_R1.NBTTagFloat;
import net.minecraft.server.v1_16_R1.NBTTagLong;
import org.jetbrains.annotations.NotNull;

public class NBTLong extends NBTNumberBase<NBTTagLong> implements ITagLong {
    public NBTLong(NBTTagLong base) {
        super(base);
    }

    @Override
    public long getValue() {
        return base.asLong();
    }

    @Override
    public @NotNull ITagLong clone() {
        return new NBTLong(base.clone());
    }
}
