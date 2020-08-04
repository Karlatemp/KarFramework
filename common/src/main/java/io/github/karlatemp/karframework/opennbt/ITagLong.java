/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/04 18:38:16
 *
 * kar-framework/kar-framework.common.main/ITagLong.java
 */

package io.github.karlatemp.karframework.opennbt;

import org.jetbrains.annotations.NotNull;

public interface ITagLong extends ITagNumber {
    @NotNull ITagLong clone();

    long getValue();
}
