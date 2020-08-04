/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/04 19:05:02
 *
 * kar-framework/kar-framework.spigot-nms-1_16_1.main/NBTString.java
 */

package io.github.karlatemp.karframework.nms.v1_8_R3;

import io.github.karlatemp.karframework.opennbt.ITagString;
import net.minecraft.server.v1_8_R3.NBTTagString;
import org.jetbrains.annotations.NotNull;

public class NBTString extends NBTBaseWrapper<NBTTagString> implements ITagString {
    public NBTString(NBTTagString tag) {
        super(tag);
    }

    @Override
    public @NotNull String getValue() {
        return base.a_();
    }

    @Override
    public @NotNull ITagString clone() {
        return new NBTString((NBTTagString)base.clone());
    }
}
