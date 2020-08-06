/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/07 24:28:46
 *
 * kar-framework/kar-framework.groovy.main/hello.groovy
 */

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender


// First. Import bukkit base.
importAll 'libraries/bukkit.groovy'

registerCommand(name: 'hgroovy', executor: new CommandExecutor() {
    @Override
    boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage("Hello! My Groovy Command!")
        return true
    }
}, permission: 'karframework.groovy.use')

