/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/06 16:20:28
 *
 * kar-framework/kar-framework.common.main/EnumerationIterator.java
 */

package io.github.karlatemp.karframework.utils;

import org.jetbrains.annotations.NotNull;

import java.util.Enumeration;
import java.util.Iterator;

public class EnumerationIterator<T> implements Iterator<T>, Enumeration<T> {

    private final Iterator<T> iterator;

    public EnumerationIterator(@NotNull Iterator<T> iterator) {
        this.iterator = iterator;
    }

    @Override
    public boolean hasMoreElements() {
        return iterator.hasNext();
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public T next() {
        return iterator.next();
    }

    @Override
    public T nextElement() {
        return iterator.next();
    }
}
