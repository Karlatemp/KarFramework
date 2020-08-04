/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/04 18:26:53
 *
 * kar-framework/kar-framework.common.main/ITagByteArray.java
 */

package io.github.karlatemp.karframework.opennbt;

import org.jetbrains.annotations.NotNull;

public interface ITagByteArray extends ITag {
    @NotNull ITagByteArray clone();

    @NotNull byte[] getValues();

    void clear();

    byte set(int index, byte value);

    void add(int index, byte value);

    int size();

    boolean set(int index, ITag value);

    boolean add(int index, ITag value);

    byte get(int index);

    byte remove(int index);
}
