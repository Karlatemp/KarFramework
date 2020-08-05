/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/05 11:25:22
 *
 * kar-framework/kar-framework.common.main/UnclosedableOutputStream.java
 */

package io.github.karlatemp.karframework.io;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.OutputStream;

public class UnclosedableOutputStream extends OutputStream {
    private OutputStream out;

    private OutputStream os() throws IOException {
        OutputStream outputStream;
        if ((outputStream = out) == null) throw new IOException("Stream closed.");
        return outputStream;
    }

    public UnclosedableOutputStream(OutputStream out) {
        this.out = out;
    }

    @Override
    public void write(int b) throws IOException {
        os().write(b);
    }

    @Override
    public void write(@NotNull byte[] b, int off, int len) throws IOException {
        os().write(b, off, len);
    }

    @Override
    public void write(@NotNull byte[] b) throws IOException {
        os().write(b);
    }

    @Override
    public void close() throws IOException {
        OutputStream o;
        if ((o = out) != (out = null)) {
            o.flush();
        }
    }

    @Override
    public void flush() throws IOException {
        os().flush();
    }
}
