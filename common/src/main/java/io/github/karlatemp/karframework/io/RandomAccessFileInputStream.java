/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/05 11:35:15
 *
 * kar-framework/kar-framework.common.main/RandomAccessFileInputStream.java
 */

package io.github.karlatemp.karframework.io;

import org.jetbrains.annotations.NotNull;

import java.io.*;

public class RandomAccessFileInputStream extends InputStream implements DataInput {
    private RandomAccessFile file;
    private final boolean flag;

    public RandomAccessFileInputStream(
            @NotNull RandomAccessFile file,
            boolean closeRandomAccessFileAfterClosing
    ) {
        this.file = file;
        this.flag = closeRandomAccessFileAfterClosing;
    }

    public RandomAccessFileInputStream(@NotNull RandomAccessFile file) {
        this(file, true);
    }

    public RandomAccessFileInputStream(@NotNull File file) throws IOException {
        this(new RandomAccessFile(file, "r"));
    }

    public RandomAccessFileInputStream(@NotNull String file) throws IOException {
        this(new RandomAccessFile(file, "r"));
    }

    public RandomAccessFileInputStream(@NotNull File file, @NotNull String mode) throws IOException {
        this(new RandomAccessFile(file, mode));
    }

    public RandomAccessFileInputStream(@NotNull String file, @NotNull String mode) throws IOException {
        this(new RandomAccessFile(file, mode));
    }

    private RandomAccessFile check() throws IOException {
        RandomAccessFile raf;
        if ((raf = file) == null) throw new IOException("Stream closed.");
        return raf;
    }

    @Override
    public int read() throws IOException {
        return check().read();
    }

    @Override
    public int read(@NotNull byte[] b, int off, int len) throws IOException {
        return check().read(b, off, len);
    }

    @Override
    public int read(@NotNull byte[] b) throws IOException {
        return check().read(b);
    }


    @Override
    public void close() throws IOException {
        RandomAccessFile raf;
        if ((raf = file) != (file = null)) {
            if (flag) {
                raf.close();
            }
        }
    }

    @Override
    public void readFully(@NotNull byte[] b) throws IOException {
        check().readFully(b);
    }

    @Override
    public void readFully(@NotNull byte[] b, int off, int len) throws IOException {
        check().readFully(b, off, len);
    }

    @Override
    public int skipBytes(int n) throws IOException {
        return check().skipBytes(n);
    }

    @Override
    public boolean readBoolean() throws IOException {
        return check().readBoolean();
    }

    @Override
    public byte readByte() throws IOException {
        return check().readByte();
    }

    @Override
    public int readUnsignedByte() throws IOException {
        return check().readUnsignedByte();
    }

    @Override
    public short readShort() throws IOException {
        return check().readShort();
    }

    @Override
    public int readUnsignedShort() throws IOException {
        return check().readUnsignedShort();
    }

    @Override
    public char readChar() throws IOException {
        return check().readChar();
    }

    @Override
    public int readInt() throws IOException {
        return check().readInt();
    }

    @Override
    public long readLong() throws IOException {
        return check().readLong();
    }

    @Override
    public float readFloat() throws IOException {
        return check().readFloat();
    }

    @Override
    public double readDouble() throws IOException {
        return check().readDouble();
    }

    @Override
    public String readLine() throws IOException {
        return check().readLine();
    }

    @NotNull
    @Override
    public String readUTF() throws IOException {
        return check().readUTF();
    }
}
