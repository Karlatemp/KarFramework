/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/03 13:08:30
 *
 * kar-framework/kar-framework.common.main/ListUtils.java
 */

package io.github.karlatemp.karframework.internal;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ListUtils {
    public static <T> @Nullable T getOrNull(List<T> list, int index) {
        if (index < 0) return null;
        if (index < list.size()) return list.get(index);
        return null;
    }
}
