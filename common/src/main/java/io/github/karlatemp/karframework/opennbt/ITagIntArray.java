/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/04 18:28:55
 *
 * kar-framework/kar-framework.common.main/ITagIntArray.java
 */

package io.github.karlatemp.karframework.opennbt;

import org.jetbrains.annotations.NotNull;

public interface ITagIntArray {
    int[] getValues();

    int size();

    int get(int index);

    int set(int index, int value);

    void add(int index, int value);

    boolean set(int index, ITag value);

    boolean add(int index, ITag value);

    int remove(int index);

    void clear();

    @NotNull ITagIntArray clone();
}
