/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/05 11:18:17
 *
 * kar-framework/kar-framework.common.main/UnclosedableInputStream.java
 */

package io.github.karlatemp.karframework.io;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;

public class UnclosedableInputStream extends InputStream {
    private InputStream delegate;

    public void close() {
        delegate = null;
    }

    private InputStream is() throws IOException {
        final InputStream stream = delegate;
        if (stream == null) throw new IOException("Stream closed");
        return stream;
    }

    public UnclosedableInputStream(InputStream delegate) {
        this.delegate = delegate;
    }

    @Override
    public int available() throws IOException {
        final InputStream stream = delegate;
        if (stream == null) return 0;
        return stream.available();
    }

    @Override
    public int read() throws IOException {
        return is().read();
    }

    @Override
    public int read(@NotNull byte[] b) throws IOException {
        return is().read(b);
    }

    @Override
    public int read(@NotNull byte[] b, int off, int len) throws IOException {
        return is().read(b, off, len);
    }

    @Override
    public long skip(long n) throws IOException {
        return is().skip(n);
    }

    @Override
    public boolean markSupported() {
        InputStream dele = delegate;
        if (dele == null) return false;
        return dele.markSupported();
    }

    @Override
    public void mark(int readlimit) {
        InputStream dele = delegate;
        if (dele == null) return;
        dele.mark(readlimit);
    }

    @Override
    public void reset() throws IOException {
        is().reset();
    }
}
