/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/05 11:30:35
 *
 * kar-framework/kar-framework.common.main/UnclosedableWriter.java
 */

package io.github.karlatemp.karframework.io;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Writer;

public class UnclosedableWriter extends Writer {
    private Writer writer;

    private Writer w() throws IOException {
        Writer w;
        if ((w = writer) == null) throw new IOException("Writer closed");
        return w;
    }

    public UnclosedableWriter(Writer writer) {
        this.writer = writer;
    }

    @Override
    public void close() throws IOException {
        Writer w;
        if ((w = writer) != (writer = null)) {
            w.flush();
        }
    }

    @Override
    public void write(int c) throws IOException {
        w().write(c);
    }

    @Override
    public void write(@NotNull String str) throws IOException {
        w().write(str);
    }

    @Override
    public void write(@NotNull char[] cbuf) throws IOException {
        w().write(cbuf);
    }

    @Override
    public void write(@NotNull String str, int off, int len) throws IOException {
        w().write(str, off, len);
    }

    @Override
    public void write(@NotNull char[] cbuf, int off, int len) throws IOException {
        w().write(cbuf, off, len);
    }

    @Override
    public Writer append(char c) throws IOException {
        w().append(c);
        return this;
    }

    @Override
    public Writer append(CharSequence csq) throws IOException {
        w().append(csq);
        return this;
    }

    @Override
    public Writer append(CharSequence csq, int start, int end) throws IOException {
        w().append(csq, start, end);
        return this;
    }

    @Override
    public void flush() throws IOException {
        w().flush();
    }
}
