/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/21 13:49:03
 *
 * kar-framework/kar-framework.common.main/MultipleIterator.java
 */

package io.github.karlatemp.karframework.utils;

import org.jetbrains.annotations.Contract;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class MultipleIterator<T> implements Iterator<T> {
    private final Iterator<Iterator<T>> iterators;
    private Iterator<T> using;
    private boolean checked;
    private boolean lastStatus;

    public MultipleIterator(
            Iterator<Iterator<T>> iterators
    ) {
        this.iterators = iterators;
    }

    @Override
    @Contract(pure = false)
    public boolean hasNext() {
        while (true) {
            if (checked) return lastStatus;
            if (using == null) {
                if (iterators.hasNext()) {
                    using = iterators.next();
                } else {
                    checked = true;
                    lastStatus = false;
                    return false;
                }
            } else {
                if (using.hasNext()) {
                    checked = true;
                    lastStatus = true;
                    return true;
                }
                using = null;
            }
        }
    }

    @Override
    public T next() {
        if (!checked) {
            hasNext();
        }
        if (lastStatus) {
            checked = false;
            return using.next();
        } else {
            throw new NoSuchElementException();
        }
    }

    @Override
    public void remove() {
        using.remove();
    }
}
