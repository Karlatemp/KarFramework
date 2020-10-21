/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/10/21 05:34:19
 *
 * kar-framework/kar-framework.common.main/OpenCommand.java
 */

package io.github.karlatemp.karframework.internal.resources;

import io.github.karlatemp.karframework.annotation.InternalAPI;
import io.github.karlatemp.karframework.command.ICommandFramework;
import io.github.karlatemp.karframework.command.ICommandNode;
import io.github.karlatemp.karframework.format.FormatAction;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.zip.ZipFile;

@InternalAPI
public class OpenCommand {
    public static <T> void registerResources(@NotNull ICommandNode<T> node, @NotNull ICommandFramework<T> framework) {
        node.registerSubCommand(framework.newSingleCommand().setName("drop-all")
                .setDescription("Drop all resource packs.")
                .setExecutor((sender, arguments, sourceArguments) -> {
                    ExternalLanguages.dropAll();
                    framework.sendMessage(sender, "§bDrop resource packs compiled.");
                    framework.sendMessage(sender, "§bIf you want to reload all resource pack. Please use");
                    framework.sendMessage(sender, "§6/karframework reload");
                })
                .build()
        ).registerSubCommand(framework.newSingleCommand().setName("info")
                .setDescription("View all loaded resource packs")
                .setExecutor((sender, arguments, sourceArguments) -> {
                    framework.sendMessage(sender, "§cServer language: " + DownloadProviders.systemLanguage);
                    framework.sendMessage(sender, "§cConfig language: " + DownloadProviders.language);
                    final List<ZipFile> files = ResourcePackLoader.resourceZipFiles;
                    if (files.isEmpty()) {
                        framework.sendMessage(sender, "§cOops. Server not loaded any resource pack.");
                    } else {
                        final FormatAction.LinkedActions parse = FormatAction.parse(" - §b{0}");
                        String[] sw = new String[1];

                        for (int start = files.size() - 1; start > -1; ) {
                            ZipFile zipFile = files.get(start--);
                            String fileName = zipFile.getName().replace('\\', '/');
                            {
                                int index;
                                if ((index = fileName.lastIndexOf('/')) > -1) {
                                    fileName = fileName.substring(index + 1);
                                }
                            }
                            sw[0] = fileName;
                            framework.sendMessage(sender, parse.apply(sw));
                        }
                    }
                }).build()
        );
    }
}
