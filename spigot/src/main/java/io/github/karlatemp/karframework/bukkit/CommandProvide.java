/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/03 14:11:16
 *
 * kar-framework/kar-framework.bukkit.main/CommandProvide.java
 */

package io.github.karlatemp.karframework.bukkit;

import io.github.karlatemp.karframework.command.ICommandNode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.Arrays;
import java.util.List;

class CommandProvide implements CommandExecutor, TabCompleter {
    private final ICommandNode<CommandSender> node;

    CommandProvide(ICommandNode<CommandSender> node) {
        this.node = node;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        node.execute(sender, Arrays.asList(args));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return node.tabCompile(sender, Arrays.asList(args));
    }
}
