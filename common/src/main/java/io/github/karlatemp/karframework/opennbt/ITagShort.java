/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/04 18:47:52
 *
 * kar-framework/kar-framework.common.main/ITagShort.java
 */

package io.github.karlatemp.karframework.opennbt;

import org.jetbrains.annotations.NotNull;

public interface ITagShort extends ITagNumber {
    short getValue();

    @NotNull ITagShort clone();
}
