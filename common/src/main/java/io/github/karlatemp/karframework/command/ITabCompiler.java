/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/03 13:08:30
 *
 * kar-framework/kar-framework.common.main/ITabCompiler.java
 */

package io.github.karlatemp.karframework.command;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.LinkedList;
import java.util.List;

public interface ITabCompiler<T> {
    void tabCompile(
            @NotNull T sender,
            @NotNull LinkedList<@NotNull String> result,
            @NotNull LinkedList<@NotNull String> arguments,
            @NotNull @Unmodifiable List<@NotNull String> sourceArguments
    );
}
