/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/04 19:09:43
 *
 * kar-framework/kar-framework.spigot-nms-1_16_1.main/NBTByte.java
 */

package io.github.karlatemp.karframework.nms.v1_16_R1;

import io.github.karlatemp.karframework.opennbt.ITag;
import io.github.karlatemp.karframework.opennbt.ITagByte;
import net.minecraft.server.v1_16_R1.NBTTagByte;
import org.jetbrains.annotations.NotNull;

public class NBTByte extends NBTNumberBase<NBTTagByte>
implements ITagByte {
    public NBTByte(NBTTagByte value){
        super(value);
    }

    @Override
    public byte getValue() {
        return base.asByte();
    }

    @Override
    public @NotNull ITagByte clone() {
        return new NBTByte(base.clone());
    }
}
