/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/03 13:08:30
 *
 * kar-framework/kar-framework.bukkit.main/KarFrameworkBukkitBootstrap.java
 */

package io.github.karlatemp.karframework.bukkit;

import com.google.common.base.Splitter;
import com.google.common.io.Files;
import io.github.karlatemp.karframework.IPluginProvider;
import io.github.karlatemp.karframework.KarFramework;
import io.github.karlatemp.karframework.bukkit.internal.Internal;
import io.github.karlatemp.karframework.bukkit.resources.*;
import io.github.karlatemp.karframework.command.CommandTree;
import io.github.karlatemp.karframework.command.InterruptCommand;
import io.github.karlatemp.karframework.format.FormatAction;
import io.github.karlatemp.karframework.format.Translator;
import net.md_5.bungee.api.chat.TranslatableComponent;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("UnstableApiUsage")
public class KarFrameworkBukkitBootstrap
        extends JavaPlugin
        implements Translator {
    private Translator delegate;
    private IPluginProvider provider;
    private final LinkedList<Runnable> shutdownHooks = new LinkedList<>();

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
    public void reloadConfig() {
        // HOCON is CommentedConfigurationNode
        @SuppressWarnings("unchecked") final ConfigurationLoader<? extends CommentedConfigurationNode> loader = (ConfigurationLoader<? extends CommentedConfigurationNode>)
                provider.loadConfiguration(
                        "karframework/config.conf", "config.conf"
                );
        assert loader != null;
        final CommentedConfigurationNode node = (CommentedConfigurationNode) provider.loadConfiguration(loader);
        Internal.CONFIG.set(node);
        Internal.CONFIG_LOADER.set(loader);
        Internal.PLUGIN_PROVIDER.set(this.provider);
        DownloadProviders.preInit();
        provider.restoreConfiguration(loader, node);
        DownloadProviders.init();
        ResourcePackLoader.initialize();
        ExternalLanguages.initialize();
        Internal.CONFIG.remove();
        Internal.CONFIG_LOADER.remove();
        Internal.PLUGIN_PROVIDER.remove();
        provider.restoreConfiguration(loader, node);
    }

    @Override
    public void onEnable() {
        Internal.SHUTDOWN_HOOK.set(shutdownHooks);
        KarFrameworkBukkit framework = KarFrameworkBukkit.getInstance();
        provider.provideCommand("karframework", new CommandTree<>(framework, "<ROOT>", null, "karframework.command.use")
                .registerSubCommand(framework.newSingleCommand().setName("hello")
                        .setDescription("Hello World!")
                        .setExecutor((sender, arguments, sourceArguments) -> {
                            sender.sendMessage("Hello World!");
                            sender.sendMessage("Arguments = " + arguments);
                            sender.sendMessage("SourceArguments = " + sourceArguments);
                        }).build()
                ).registerSubCommand(new CommandTree<>(framework, "sub", "here is sub tree", null)
                        .registerSubCommand(framework.newSingleCommand()
                                .setName("t")
                                .setExecutor((sender, arguments, sourceArguments) -> {
                                    sender.sendMessage("Hello! Here is sub command tree.");
                                    sender.sendMessage("Arguments = " + arguments);
                                    sender.sendMessage("SourceArguments = " + sourceArguments);
                                })
                                .build())
                ).registerSubCommand(framework.newSingleCommand().setName("reload")
                        .setDescription("Reload KarFramework's Configuration")
                        .setExecutor(((sender, arguments, sourceArguments) -> {
                            reloadConfig();
                            sender.sendMessage("§bReload configuration compiled.");
                        })).build()
                ).registerSubCommand(new CommandTree<>(framework, "resources", "Configuration resource packs", null)
                        .registerSubCommand(framework.newSingleCommand().setName("reload")
                                .setDescription("Reload resource packs")
                                .setExecutor(((sender, arguments, sourceArguments) -> {
                                    // HOCON is CommentedConfigurationNode
                                    @SuppressWarnings("unchecked") final ConfigurationLoader<? extends CommentedConfigurationNode> loader = (ConfigurationLoader<? extends CommentedConfigurationNode>)
                                            provider.loadConfiguration(
                                                    "karframework/config.conf", "config.conf"
                                            );
                                    assert loader != null;
                                    final CommentedConfigurationNode node = (CommentedConfigurationNode) provider.loadConfiguration(loader);
                                    Internal.CONFIG_LOADER.set(loader);
                                    Internal.CONFIG.set(node);
                                    Internal.PLUGIN_PROVIDER.set(this.provider);
                                    ResourcePackLoader.initialize();
                                    ExternalLanguages.initialize();
                                    Internal.CONFIG.remove();
                                    Internal.CONFIG_LOADER.remove();
                                    Internal.PLUGIN_PROVIDER.remove();
                                    provider.restoreConfiguration(loader, node);
                                    sender.sendMessage("§bResource pack reload compiled.");
                                })).build()
                        ).access(OpenCommand::registerResources)
                ).registerSubCommand(new CommandTree<>(framework, "test")
                        .registerSubCommand(framework.newSingleCommand().setName("language")
                                .setExecutor((sender, arguments, sourceArguments) -> {
                                    if (arguments.isEmpty()) {
                                        sender.sendMessage("Missing language.");
                                        throw InterruptCommand.INSTANCE;
                                    }
                                    Map<String, String> data = ExternalLanguages.getLanguage(arguments.peek());
                                    if (data == null) {
                                        sender.sendMessage("Language " + arguments.peek() + " not found.");
                                        throw InterruptCommand.INSTANCE;
                                    }
                                    final File file = new File(provider.getPluginDataFolder(), "lang-dump.txt");
                                    try {
                                        Files.createParentDirs(file);
                                        int longest = 0;
                                        for (String key : data.keySet()) {
                                            longest = Math.max(key.length(), longest);
                                        }
                                        char[] emptyBuffer = new char[longest];
                                        Arrays.fill(emptyBuffer, ' ');
                                        try (Writer writer = Files.newWriter(file, StandardCharsets.UTF_8)) {
                                            for (Map.Entry<String, String> entry : data.entrySet().stream().sorted(
                                                    Map.Entry.comparingByKey()
                                            ).collect(Collectors.toCollection(LinkedList::new))) {
                                                String key = entry.getKey();
                                                String value = entry.getValue();
                                                writer.append(key).write(emptyBuffer, 0, longest - key.length());
                                                writer.append(':').append(' ');
                                                if (value.indexOf('\n') > -1) {
                                                    final Iterator<String> lines = Splitter.on('\n').split(value).iterator();
                                                    writer.append(lines.next()).append('\n');
                                                    while (lines.hasNext()) {
                                                        writer.write(emptyBuffer);
                                                        writer.append('|').append(' ').append(lines.next()).append('\n');
                                                    }
                                                } else {
                                                    writer.append(value).append('\n');
                                                }
                                            }
                                        }
                                    } catch (IOException ioException) {
                                        throw new RuntimeException(ioException);
                                    }
                                }).build()
                        ).registerSubCommand(framework.newSingleCommand().setName("bccapi")
                                .setExecutor((sender, arguments, sourceArguments) -> {
                                    sender.sendMessage(
                                            new TranslatableComponent("addServer.title").toPlainText()
                                    );
                                }).build()
                        )
                )
        );
        OpenMCLang.preInit();
        reloadConfig();
        Internal.SHUTDOWN_HOOK.set(null);
    }

    @Override
    public @Nullable FormatAction getTranslation(@NotNull String key) {
        return delegate.getTranslation(key);
    }

    @Override
    public void onDisable() {
        shutdownHooks.removeIf(it -> {
            it.run();
            return true;
        });
    }
}
