/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/03 13:08:30
 *
 * kar-framework/kar-framework.common.main/ICommandFramework.java
 */

package io.github.karlatemp.karframework.command;

import io.github.karlatemp.karframework.format.FormatAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ICommandFramework<T> {
    void sendMessage(@NotNull T target, @NotNull String message);

    default void sendTranslate(@NotNull T target, @NotNull String key, @Nullable String... values) {
        sendTranslate(target, getTranslate(key), values);
    }

    default void sendTranslate(@NotNull T target, @NotNull FormatAction key, @Nullable String... values) {
        sendMessage(target, key.apply(values));
    }

    boolean hasPermission(@NotNull T target, @Nullable String permission);

    @NotNull FormatAction getTranslate(@NotNull String key);

    @NotNull String getName(@NotNull T target);

    default @NotNull CommandSingle.Builder<T> newSingleCommand() {
        return CommandSingle.builder().setFramework(this);
    }

    default @NotNull CommandTree.Builder<T> newCommandTree() {
        return CommandTree.builder(this);
    }
}
