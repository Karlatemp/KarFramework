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
