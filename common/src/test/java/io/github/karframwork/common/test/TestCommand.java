/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/03 13:08:30
 *
 * kar-framework/kar-framework.common.test/TestCommand.java
 */

package io.github.karframwork.common.test;

import io.github.karlatemp.karframework.command.CommandTree;
import io.github.karlatemp.karframework.command.ICommandFramework;
import io.github.karlatemp.karframework.command.ICommandNode;
import io.github.karlatemp.karframework.format.FormatAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Test;

import java.io.PrintStream;
import java.util.Arrays;

public class TestCommand implements ICommandFramework<PrintStream> {
    @Test
    public void test() {
        final ICommandNode<PrintStream> command = new CommandTree<>(this)
                .registerSubCommand(newSingleCommand()
                        .setName("test")
                        .setDescription("Description")
                        .setExecutor((sender, arguments, sourceArguments) -> {
                            sender.println("Hello!");
                            sender.println(arguments);
                            sender.println(sourceArguments);
                        })
                        .build())
                .registerSubCommand(newSingleCommand()
                        .setName("text")
                        .setDescription("D2")
                        .setExecutor((sender, arguments, sourceArguments) -> {
                            sender.println("FUCKQ");
                        })
                        .build());
        command.execute(System.out, Arrays.asList("te", "i"));
    }

    @Override
    public void sendMessage(@NotNull PrintStream target, @NotNull String message) {
        target.println(message);
    }

    @Override
    public boolean hasPermission(@NotNull PrintStream target, @Nullable String permission) {
        return true;
    }

    @Override
    public @NotNull FormatAction getTranslate(@NotNull String key) {
        switch (key) {
            case "kframe.command.help.line":
                return FormatAction.parse("{0} - {1}");
            case "kframe.command.unknown-command-meat.no-command":
                return FormatAction.parse("Unknown command: {0}, The most similar commands are");
            case "kframe.command.unknown-command-meat.line":
                return FormatAction.parse("    {0}");
        }
        return new FormatAction.PlainText(key);
    }

    @Override
    public @NotNull String getName(@NotNull PrintStream target) {
        return "null";
    }
}
