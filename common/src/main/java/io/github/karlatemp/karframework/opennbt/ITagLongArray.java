/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/04 18:38:58
 *
 * kar-framework/kar-framework.common.main/ITagLongArray.java
 */

package io.github.karlatemp.karframework.opennbt;

import org.jetbrains.annotations.NotNull;

public interface ITagLongArray extends ITag {
    @NotNull ITagLongArray clone();

    @NotNull long[] getValues();

    int size();

    long set(int index, long value);

    long get(int index);

    void add(int index, long value);

    boolean set(int index, ITag value);

    boolean add(int index, ITag value);

    long remove(int index);

    void clear();
}
