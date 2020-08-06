/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/05 19:30:43
 *
 * kar-framework/kar-framework.common.main/OpenByteArrayOutputStream.java
 */

package io.github.karlatemp.karframework.io;

import java.io.ByteArrayInputStream;

public class OpenByteArrayInputStream extends ByteArrayInputStream {
    public OpenByteArrayInputStream(byte[] array) {
        super(array);
    }

    public OpenByteArrayInputStream(byte[] array, int offset, int length) {
        super(array, offset, length);
    }

    public byte[] getBuffer() {
        return buf;
    }

    public void setBuffer(byte[] buffer) {
        this.buf = buffer;
    }

    public int getEnding() {
        return this.count;
    }

    public void setEnding(int pos) {
        this.count = pos;
    }

    public int getPos() {
        return this.pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

}
