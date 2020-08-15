/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/04 19:29:26
 *
 * kar-framework/kar-framework.spigot-nms-1_16_1.main/NBTDouble.java
 */

package io.github.karlatemp.karframework.nms.v1_16_R2;

import io.github.karlatemp.karframework.opennbt.ITagFloat;
import net.minecraft.server.v1_16_R2.NBTTagFloat;
import org.jetbrains.annotations.NotNull;

public class NBTFloat extends NBTNumberBase<NBTTagFloat> implements ITagFloat {
    public NBTFloat(NBTTagFloat base) {
        super(base);
    }

    @Override
    public float getValue() {
        return base.asFloat();
    }

    @Override
    public @NotNull ITagFloat clone() {
        return new NBTFloat(base.clone());
    }
}
