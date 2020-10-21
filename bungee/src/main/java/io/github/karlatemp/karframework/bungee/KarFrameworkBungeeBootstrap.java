/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/03 13:08:30
 *
 * kar-framework/kar-framework.bungee.main/KarFrameworkBungeeBootstrap.java
 */

package io.github.karlatemp.karframework.bungee;

import com.google.common.base.Splitter;
import com.google.common.io.Files;
import com.google.gson.Gson;
import io.github.karlatemp.karframework.IPluginProvider;
import io.github.karlatemp.karframework.KarFramework;
import io.github.karlatemp.karframework.command.CommandTree;
import io.github.karlatemp.karframework.command.InterruptCommand;
import io.github.karlatemp.karframework.format.FormatAction;
import io.github.karlatemp.karframework.format.Translator;
import io.github.karlatemp.karframework.internal.Internal;
import io.github.karlatemp.karframework.internal.resources.*;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TranslatableComponent;
import net.md_5.bungee.api.plugin.Plugin;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class KarFrameworkBungeeBootstrap extends Plugin implements Translator {
    private Translator delegate = Translator.nullTranslator();
    static KarFrameworkBungeeBootstrap INSTANCE;
    final BungeePluginProvider provider = new BungeePluginProvider(this);
    private final LinkedList<Runnable> shutdownHooks = new LinkedList<>();

    public KarFrameworkBungeeBootstrap() {
        KarFramework.setInstance(KarFrameworkBungee.INSTANCE = new KarFrameworkBungee(this));
        INSTANCE = this;
    }

    @Override
    public void onLoad() {
        ConfigurationNode translate = provider.loadConfiguration(
                Objects.requireNonNull(
                        provider.loadConfiguration("translate.conf")
                )
        );
        this.delegate = Translator.from(translate).cached();
        if (System.getProperty("karframework.debug") != null) {
            try {
                //noinspection unchecked
                ((Consumer<IPluginProvider>) Class.forName("io.github.karlatemp.karframework.internal.KotlinCodeTest")
                        .getField("INSTANCE").get(null)).accept(provider);
            } catch (Throwable excep) {
                getLogger().log(Level.WARNING, "DEBUG", excep);
            }
        }

        // region Initialize commands
        Internal.SHUTDOWN_HOOK.set(shutdownHooks);
        KarFrameworkBungee framework = KarFrameworkBungee.INSTANCE;
        // region Command
        provider.provideCommand("karframework", new CommandTree<>(framework, "<ROOT>", null, "karframework.command.use")
                .registerSubCommand(new CommandTree<>(framework, "resources", "Configuration resource packs", null)
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
                                    //noinspection deprecation
                                    sender.sendMessage("§bResource pack reload compiled.");
                                })).build()
                        ).access(node -> OpenCommand.registerResources(node, framework))
                )
        );
        // endregion
        OpenMCLang.preInit();
        reloadConfig();
        Internal.SHUTDOWN_HOOK.set(null);
        // endregion
    }

    private void reloadConfig() {
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
    public @Nullable FormatAction getTranslation(@NotNull String key) {
        return delegate.getTranslation(key);
    }
}
