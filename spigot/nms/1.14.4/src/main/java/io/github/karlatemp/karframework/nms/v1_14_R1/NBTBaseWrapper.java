/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/04 19:02:36
 *
 * kar-framework/kar-framework.spigot-nms-1_16_1.main/NBTBaseWrapper.java
 */

package io.github.karlatemp.karframework.nms.v1_14_R1;

import io.github.karlatemp.karframework.opennbt.ITag;
import net.minecraft.server.v1_14_R1.*;
import org.jetbrains.annotations.NotNull;

public abstract class NBTBaseWrapper<T extends NBTBase> implements ITag {
    protected T base;

    public NBTBaseWrapper(T base) {
        this.base = base;
    }

    public int hashCode() {
        return base.hashCode();
    }

    public boolean equals(Object other) {
        if (other instanceof NBTBaseWrapper) {
            return base.equals(((NBTBaseWrapper<?>) other).base);
        }
        return false;
    }

    @Override
    public String asString() {
        return base.asString();
    }

    public abstract @NotNull ITag clone();

    static ITag of(NBTBase nbtBase) {
        if (nbtBase == null)
            return null;
        if (nbtBase instanceof NBTTagList) {
            return new NBTList((NBTTagList) nbtBase);
        } else if (nbtBase instanceof NBTTagByte) {
            return new NBTByte((NBTTagByte) nbtBase);
        } else if (nbtBase instanceof NBTTagByteArray) {
            return new NBTByteArray((NBTTagByteArray) nbtBase);
        } else if (nbtBase instanceof NBTTagCompound) {
            return new NBTComponent((NBTTagCompound) nbtBase);
        } else if (nbtBase instanceof NBTTagDouble) {
            return new NBTDouble((NBTTagDouble) nbtBase);
        } else if (nbtBase instanceof NBTTagFloat) {
            return new NBTFloat((NBTTagFloat) nbtBase);
        } else if (nbtBase instanceof NBTTagInt) {
            return new NBTInt((NBTTagInt) nbtBase);
        } else if (nbtBase instanceof NBTTagIntArray) {
            return new NBTIntArray((NBTTagIntArray) nbtBase);
        } else if (nbtBase instanceof NBTTagLong) {
            return new NBTLong((NBTTagLong) nbtBase);
        } else if (nbtBase instanceof NBTTagLongArray) {
            return new NBTLongArray((NBTTagLongArray) nbtBase);
        } else if (nbtBase instanceof NBTTagString) {
            return new NBTString((NBTTagString) nbtBase);
        }
        throw new AssertionError();
    }

    public String toString() {
        return base.toString();
    }
}
