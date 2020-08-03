/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/03 13:08:30
 *
 * kar-framework/kar-framework.common.main/ICommandNode.java
 */

package io.github.karlatemp.karframework.command;

import com.google.common.collect.ImmutableList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("unused")
public interface ICommandNode<T> extends ICommandExecutor<T>, ITabCompiler<T> {
    @Nullable ICommandNode<T> getSubNode(
            @NotNull String name
    );

    @Nullable String getPermission();

    @Nullable ICommandNode<T> getParent();

    /**
     * Unsafe action
     */
    @SuppressWarnings("DeprecatedIsStillUsed")
    @Deprecated
    void setParent(@NotNull ICommandNode<T> parent);


    @Nullable String getDescription();

    @NotNull String getName();

    @NotNull ICommandFramework<T> getCommandFramework();

    boolean isSingle();

    default @NotNull ICommandNode<T> registerSubCommand(@NotNull ICommandNode<T> node) {
        return registerSubCommand(node, null);
    }

    @NotNull ICommandNode<T> registerSubCommand(
            @NotNull ICommandNode<T> node,
            @Nullable String[] alias
    );

    default void execute(
            @NotNull T sender,
            @NotNull List<String> arguments
    ) {
        try {
            invokeCommand(
                    sender,
                    new LinkedList<>(arguments),
                    ImmutableList.copyOf(arguments)
            );
        } catch (InterruptCommand ignored) {
        }
    }

    default List<String> tabCompile(
            @NotNull T sender, @NotNull List<@NotNull String> arguments
    ) {
        LinkedList<String> result = new LinkedList<>();
        tabCompile(sender, result, new LinkedList<>(arguments), ImmutableList.copyOf(arguments));
        return result;
    }
}
