/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/03 13:08:30
 *
 * kar-framework/kar-framework.common.main/IKarFramework.java
 */

package io.github.karlatemp.karframework;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IKarFramework {
    @Nullable IPluginProvider provide(@NotNull Object plugin);
}
