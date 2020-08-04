/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/04 19:16:18
 *
 * kar-framework/kar-framework.spigot-nms-1_16_1.main/NBTBaseList.java
 */

package io.github.karlatemp.karframework.nms.v1_13_R1;

import io.github.karlatemp.karframework.opennbt.ITag;
import io.github.karlatemp.karframework.opennbt.ITagNumber;
import net.minecraft.server.v1_13_R1.NBTList;

public abstract class NBTBaseList<
        T extends NBTList<?>
        > extends NBTBaseWrapper<T> {
    public NBTBaseList(T base) {
        super(base);
    }

    public boolean set(int index, ITag value) {
        if (value instanceof ITagNumber) {
            base.a(index, ((NBTNumberBase<?>) value).base);
            return true;
        } else {
            return false;
        }
    }

}
