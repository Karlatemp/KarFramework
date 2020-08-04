/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/04 18:31:49
 *
 * kar-framework/kar-framework.common.main/ITagList.java
 */

package io.github.karlatemp.karframework.opennbt;

import org.jetbrains.annotations.NotNull;

public interface ITagList extends ITag {
    ITag remove(int index);
    @NotNull ITagCompound getCompound(int index);
    @NotNull ITagList getList(int index);
    short getShort(int index);
    int getInt(int index);
    @NotNull int[] getIntArray(int index);
    double getDouble(int index);
    float getFloat(int index);
    @NotNull String getString(int index);
    int size();
    ITag get(int index);
    ITag set(int index,ITag value);
    void add(int index,ITag value);
    boolean setSafely(int index,ITag value);
    boolean addSafely(int index,ITag value);
    @NotNull ITagList clone();
    void clear();
}
