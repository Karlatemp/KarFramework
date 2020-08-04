/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/04 18:24:13
 *
 * kar-framework/kar-framework.common.main/ITagInt.java
 */

package io.github.karlatemp.karframework.opennbt;

import org.jetbrains.annotations.NotNull;

public interface ITagInt extends ITagNumber {
    int getValue();

    @NotNull ITagInt clone();
}
