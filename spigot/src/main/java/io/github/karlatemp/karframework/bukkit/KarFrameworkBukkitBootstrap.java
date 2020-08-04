/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/03 13:08:30
 *
 * kar-framework/kar-framework.bukkit.main/KarFrameworkBukkitBootstrap.java
 */

package io.github.karlatemp.karframework.bukkit;

import io.github.karlatemp.karframework.IPluginProvider;
import io.github.karlatemp.karframework.KarFramework;
import io.github.karlatemp.karframework.command.CommandTree;
import io.github.karlatemp.karframework.format.FormatAction;
import io.github.karlatemp.karframework.format.Translator;
import io.github.karlatemp.karframework.opennbt.ITagCompound;
import net.md_5.bungee.api.chat.TextComponent;
import ninja.leaping.configurate.ConfigurationNode;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class KarFrameworkBukkitBootstrap
        extends JavaPlugin
        implements Translator {
    private Translator delegate;
    private IPluginProvider provider;

    public KarFrameworkBukkitBootstrap() {
        delegate = Translator.nullTranslator();
        KarFramework.setInstance(KarFrameworkBukkit.INSTANCE = new KarFrameworkBukkit(this));
    }

    @Override
    public void onLoad() {
        provider = new BukkitPluginProvider(this);
        ConfigurationNode translate = provider.loadConfiguration(
                Objects.requireNonNull(
                        provider.loadConfiguration("karframework/translate.conf", "translate.conf")
                )
        );
        this.delegate = Translator.from(translate).cached();
    }

    @Override
    public void onEnable() {
        KarFrameworkBukkit framework = KarFrameworkBukkit.getInstance();
        provider.provideCommand("karframework",
                new CommandTree<>(
                        framework,
                        "<ROOT>",
                        null,
                        "karframework.command.use"
                ).registerSubCommand(framework.newSingleCommand().setName("c1")
                        .setExecutor((sender, arguments, sourceArguments) -> {
                            sender.sendMessage("Hello World!");
                            sender.sendMessage("Arguments = " + arguments);
                            sender.sendMessage("SourceArguments = " + sourceArguments);
                        }).build()
                ).registerSubCommand(
                        new CommandTree<>(framework, "c2")
                                .registerSubCommand(framework.newSingleCommand()
                                        .setName("t")
                                        .setExecutor((sender, arguments, sourceArguments) -> {
                                            sender.sendMessage("Hello! Here is sub command tree.");
                                            sender.sendMessage("Arguments = " + arguments);
                                            sender.sendMessage("SourceArguments = " + sourceArguments);
                                        })
                                        .build()
                                )
                ).registerSubCommand(framework.newSingleCommand().setName("c3")
                        .setExecutor((sender, arguments, sourceArguments) -> {
                            if (sender instanceof Player) {
                                KarFrameworkBukkit.getNmsProvider().sendAction(
                                        (Player) sender,
                                        new TextComponent(TextComponent.fromLegacyText("HiHi!"))
                                );
                            } else {
                                sender.sendMessage("Oops. You are not a player.");
                            }
                        })
                        .build()
                ).registerSubCommand(framework.newSingleCommand().setName("c4")
                        .setExecutor((sender, arguments, sourceArguments) -> {
                            if (sender instanceof Player) {
                                KarFrameworkBukkit.getNmsProvider().sendTitle(
                                        (Player) sender,
                                        new TextComponent(TextComponent.fromLegacyText("Title!")),
                                        new TextComponent(TextComponent.fromLegacyText("Sub Title!")),
                                        20, 100, 20
                                );
                            } else {
                                sender.sendMessage("Oops. You are not a player.");
                            }
                        })
                        .build()
                ).registerSubCommand(framework.newSingleCommand().setName("c5")
                        .setExecutor((sender, arguments, sourceArguments) -> {
                            if (sender instanceof Player) {
                                final ITagCompound tagCompound = KarFrameworkBukkit.getNbtProvider().readCompound((Player) sender);
                                System.out.println(tagCompound.get("Inventory"));
                                System.out.println(tagCompound.get("Inventory").getClass());
                            } else {
                                sender.sendMessage("Oops. You are not a player.");
                            }
                        })
                        .build()
                )
        );
    }

    @Override
    public @Nullable FormatAction getTranslation(@NotNull String key) {
        return delegate.getTranslation(key);
    }
}
