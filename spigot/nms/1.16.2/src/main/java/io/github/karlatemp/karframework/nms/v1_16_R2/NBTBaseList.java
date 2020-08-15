/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/04 19:16:18
 *
 * kar-framework/kar-framework.spigot-nms-1_16_1.main/NBTBaseList.java
 */

package io.github.karlatemp.karframework.nms.v1_16_R2;

import io.github.karlatemp.karframework.opennbt.ITag;
import net.minecraft.server.v1_16_R2.NBTList;

public abstract class NBTBaseList<
        T extends NBTList<?>
        > extends NBTBaseWrapper<T> {
    public NBTBaseList(T base) {
        super(base);
    }

    public boolean set(int index, ITag value) {
        return base.a(index, ((NBTBaseWrapper<?>) value).base);
    }

    public boolean add(int index, ITag value) {
        return base.b(index, ((NBTBaseWrapper<?>) value).base);
    }
}
