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

public class RandomAccessFileOutputStream extends OutputStream implements DataOutput {
    private RandomAccessFile file;
    private final boolean flag, flag2;

    public RandomAccessFileOutputStream(
            @NotNull RandomAccessFile file,
            boolean closeRandomAccessFileAfterClosing,
            boolean setFileLengthWhenClosing
    ) {
        this.file = file;
        this.flag = closeRandomAccessFileAfterClosing;
        this.flag2 = setFileLengthWhenClosing;
    }

    public RandomAccessFileOutputStream(@NotNull RandomAccessFile file) {
        this(file, true, true);
    }

    public RandomAccessFileOutputStream(@NotNull File file) throws IOException {
        this(new RandomAccessFile(file, "rw"));
    }

    public RandomAccessFileOutputStream(@NotNull String file) throws IOException {
        this(new RandomAccessFile(file, "rw"));
    }

    public RandomAccessFileOutputStream(@NotNull File file, @NotNull String mode) throws IOException {
        this(new RandomAccessFile(file, mode));
    }

    public RandomAccessFileOutputStream(@NotNull String file, @NotNull String mode) throws IOException {
        this(new RandomAccessFile(file, mode));
    }

    private RandomAccessFile check() throws IOException {
        RandomAccessFile raf;
        if ((raf = file) == null) throw new IOException("Stream closed.");
        return raf;
    }

    @Override
    public void close() throws IOException {
        RandomAccessFile raf;
        if ((raf = file) != (file = null)) {
            if (flag2) {
                raf.setLength(raf.getFilePointer());
            }
            if (flag) {
                raf.close();
            }
        }
    }

    @Override
    public void write(int b) throws IOException {
        check().write(b);
    }

    @Override
    public void write(@NotNull byte[] b) throws IOException {
        check().write(b);
    }

    @Override
    public void write(@NotNull byte[] b, int off, int len) throws IOException {
        check().write(b, off, len);
    }

    @Override
    public void writeBoolean(boolean v) throws IOException {
        check().writeBoolean(v);
    }

    @Override
    public void writeByte(int v) throws IOException {
        check().writeByte(v);
    }

    @Override
    public void writeShort(int v) throws IOException {
        check().writeShort(v);
    }

    @Override
    public void writeChar(int v) throws IOException {
        check().writeChar(v);
    }

    @Override
    public void writeInt(int v) throws IOException {
        check().writeInt(v);
    }

    @Override
    public void writeLong(long v) throws IOException {
        check().writeLong(v);
    }

    @Override
    public void writeFloat(float v) throws IOException {
        check().writeFloat(v);
    }

    @Override
    public void writeDouble(double v) throws IOException {
        check().writeDouble(v);
    }

    @Override
    public void writeBytes(@NotNull String s) throws IOException {
        check().writeBytes(s);
    }

    @Override
    public void writeChars(@NotNull String s) throws IOException {
        check().writeChars(s);
    }

    @Override
    public void writeUTF(@NotNull String s) throws IOException {
        check().writeUTF(s);
    }
}
