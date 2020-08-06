/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/07 24:20:58
 *
 * kar-framework/kar-framework.groovy.main/startup.groovy
 */


import io.github.karlatemp.karframework.bukkit.KarFrameworkBukkit
import io.github.karlatemp.karframework.bukkit.KarFrameworkBukkitBootstrap
import org.bukkit.Bukkit
import org.bukkit.command.CommandExecutor
import org.bukkit.command.PluginCommand
import org.bukkit.command.TabCompleter
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin

def plugin = JavaPlugin.getPlugin(KarFrameworkBukkitBootstrap.class)

exports.plugin = plugin
exports.registerEvents = { Listener listener ->
    Bukkit.getPluginManager().registerEvents(listener, plugin)
}
exports.nmsProvider = KarFrameworkBukkit.getNmsProvider()
exports.nbtProvider = KarFrameworkBukkit.getNbtProvider()
exports.unregister = { Listener listener ->
    HandlerList.unregisterAll(listener)
}
exports.scheduler = Bukkit.getScheduler()

static <T, K> T checkType(Map<K, ?> arguments, K path, Class<T> type) {
    def res = arguments[path]
    if (res == null) return null;
    if (type.isInstance(res)) {
        return res as T
    }
    throw new IllegalArgumentException('Argument `' + path + '` except ' + type.name + ' but found ' + res.getClass().name)
}

def newPluginCommand = ({
    def cons = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class)
    cons.setAccessible(true)
    return { name ->
        return cons.newInstance(name, plugin)
    }
})()

exports.registerCommand = { arguments0 ->
    def arguments = arguments0 as Map<String, ?>
    def cmd = newPluginCommand(checkType(arguments, 'name', String.class))
    cmd.setPermission(checkType(arguments, 'permission', String.class))
    cmd.setDescription(checkType(arguments, 'description', String.class))
    cmd.setExecutor(checkType(arguments, 'executor', CommandExecutor.class))
    cmd.setTabCompleter(checkType(arguments, 'completer', TabCompleter.class))
    KarFrameworkBukkit.getNmsProvider().getCommandMap().register(
            "karframework", cmd
    )
    return cmd
}