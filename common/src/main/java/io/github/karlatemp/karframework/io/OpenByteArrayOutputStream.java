/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/05 19:30:43
 *
 * kar-framework/kar-framework.common.main/OpenByteArrayOutputStream.java
 */

package io.github.karlatemp.karframework.io;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class OpenByteArrayOutputStream extends ByteArrayOutputStream {
    public OpenByteArrayOutputStream() {
    }

    public OpenByteArrayOutputStream(int size) {
        super(size);
    }

    public byte[] getBuffer() {
        return buf;
    }

    public void setBuffer(byte[] buffer) {
        this.buf = buffer;
    }

    public int getPos() {
        return this.count;
    }

    public void setPos(int pos) {
        this.count = pos;
    }

    public String toUTF8() {
        return new String(buf, 0, count, StandardCharsets.UTF_8);
    }

    public String toStr(Charset charset) {
        return new String(buf, 0, count, charset);
    }
}
