/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/04 18:07:43
 *
 * kar-framework/kar-framework.common.main/ITagString.java
 */

package io.github.karlatemp.karframework.opennbt;

import org.jetbrains.annotations.NotNull;

public interface ITagString extends ITag {
    @NotNull String getValue();

    @NotNull ITagString clone();
}
