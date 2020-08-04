/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/04 18:41:35
 *
 * kar-framework/kar-framework.common.main/ITagNumber.java
 */

package io.github.karlatemp.karframework.opennbt;

import org.jetbrains.annotations.NotNull;

public interface ITagNumber extends ITag {
    @NotNull ITagNumber clone();

    int asInt();

    byte asByte();

    short asShort();

    long asLong();

    double asDouble();

    float asFloat();
}
