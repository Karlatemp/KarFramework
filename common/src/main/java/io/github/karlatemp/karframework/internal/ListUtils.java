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
