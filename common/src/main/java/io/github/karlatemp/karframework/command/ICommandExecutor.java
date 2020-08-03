package io.github.karlatemp.karframework.command;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.LinkedList;
import java.util.List;

public interface ICommandExecutor<T> {

    void invokeCommand(
            @NotNull T sender,
            @NotNull LinkedList<@NotNull String> arguments,
            @Unmodifiable @NotNull List<@NotNull String> sourceArguments
    ) throws InterruptCommand;

}
