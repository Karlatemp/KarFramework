/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/21 13:47:18
 *
 * kar-framework/kar-framework.common.main/MupitleIterable.java
 */

package io.github.karlatemp.karframework.utils;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.function.Consumer;

public class MultipleIterable<T> implements Iterable<T> {
    private final Iterable<Iterable<T>> iterables;

    public MultipleIterable(Iterable<Iterable<T>> iterables) {
        this.iterables = iterables;
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        for (Iterable<T> i : iterables) i.forEach(action);
    }

    private class Iter implements Iterator<Iterator<T>> {
        private final Iterator<Iterable<T>> iv = iterables.iterator();

        @Override
        public boolean hasNext() {
            return iv.hasNext();
        }

        @Override
        public Iterator<T> next() {
            return iv.next().iterator();
        }
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return new MultipleIterator<>(new Iter());
    }
}
