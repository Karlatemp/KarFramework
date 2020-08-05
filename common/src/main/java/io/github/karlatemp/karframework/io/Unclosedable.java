/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/05 11:33:37
 *
 * kar-framework/kar-framework.common.main/Unclosedable.java
 */

package io.github.karlatemp.karframework.io;

import java.io.Closeable;
import java.io.IOException;

public class Unclosedable<T> implements AutoCloseable, Closeable {
    private T delegate;

    public Unclosedable(T delegate) {
        this.delegate = delegate;
    }

    public T getDelegate() {
        return delegate;
    }

    @Override
    public void close() throws IOException {
        delegate = null;
    }
}
