/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/06 14:42:32
 *
 * kar-framework/kar-framework.spigot.main/OpenCommand.java
 */

package io.github.karlatemp.karframework.bukkit.resources;

import io.github.karlatemp.karframework.bukkit.KarFrameworkBukkit;
import io.github.karlatemp.karframework.command.ICommandNode;
import io.github.karlatemp.karframework.format.FormatAction;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.zip.ZipFile;

public class OpenCommand {
    public static void registerResources(@NotNull ICommandNode<CommandSender> node) {
        KarFrameworkBukkit framework = KarFrameworkBukkit.getInstance();
        node.registerSubCommand(framework.newSingleCommand().setName("drop-all")
                .setDescription("Drop all resource packs.")
                .setExecutor((sender, arguments, sourceArguments) -> {
                    ExternalLanguages.dropAll();
                    sender.sendMessage("§bDrop resource packs compiled.");
                    sender.sendMessage("§bIf you want to reload all resource pack. Please use");
                    sender.sendMessage("§6/karframework reload");
                })
                .build()
        ).registerSubCommand(framework.newSingleCommand().setName("info")
                .setDescription("View all loaded resource packs")
                .setExecutor((sender, arguments, sourceArguments) -> {
                    final List<ZipFile> files = ResourcePackLoader.resourceZipFiles;
                    if (files.isEmpty()) {
                        sender.sendMessage("§cOops. Server not loaded any resource pack.");
                    } else {
                        final FormatAction.LinkedActions parse = FormatAction.parse(" - §b{0}");
                        String[] sw = new String[1];

                        for (int start = files.size() - 1; start > 0; ) {
                            ZipFile zipFile = files.get(start--);
                            String fileName = zipFile.getName().replace('\\', '/');
                            {
                                int index;
                                if ((index = fileName.lastIndexOf('/')) > -1) {
                                    fileName = fileName.substring(index + 1);
                                }
                            }
                            sw[0] = fileName;
                            sender.sendMessage(parse.apply(sw));
                        }
                    }
                }).build()
        );
    }
}
