/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/05 11:27:49
 *
 * kar-framework/kar-framework.common.main/UnclosedableReader.java
 */

package io.github.karlatemp.karframework.io;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Reader;
import java.nio.CharBuffer;

public class UnclosedableReader extends Reader {
    private Reader reader;
    private Reader r() throws IOException {
        Reader r;
        if((r = reader) == null)throw new IOException("Reader closed");
        return r;
    }
    public UnclosedableReader(Reader reader){
        this.reader=reader;
    }

    @Override
    public int read() throws IOException {
        return r().read();
    }

    @Override
    public boolean ready() throws IOException {
        return r().ready();
    }

    @Override
    public int read(@NotNull char[] cbuf) throws IOException {
        return r().read(cbuf);
    }

    @Override
    public int read(@NotNull CharBuffer target) throws IOException {
        return r().read(target);
    }

    @Override
    public int read(@NotNull char[] cbuf, int off, int len) throws IOException {
        return r().read(cbuf, off, len);
    }

    @Override
    public void close() throws IOException {
        reader=null;
    }

    @Override
    public void mark(int readAheadLimit) throws IOException {
        r().mark(readAheadLimit);
    }

    @Override
    public boolean markSupported() {
        Reader r;
        if((r = reader)==null)return false;
        return r.markSupported();
    }

    @Override
    public long skip(long n) throws IOException {
        return r().skip(n);
    }

    @Override
    public void reset() throws IOException {
        r().reset();
    }
}
