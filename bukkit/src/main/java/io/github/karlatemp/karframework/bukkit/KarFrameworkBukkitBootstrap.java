package io.github.karlatemp.karframework.bukkit;

import io.github.karlatemp.karframework.KarFramework;
import io.github.karlatemp.karframework.format.FormatAction;
import io.github.karlatemp.karframework.format.Translator;
import ninja.leaping.configurate.ConfigurationNode;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class KarFrameworkBukkitBootstrap
        extends JavaPlugin
        implements Translator {
    private Translator delegate;

    public KarFrameworkBukkitBootstrap() {
        delegate = Translator.nullTranslator();
        KarFramework.setInstance(KarFrameworkBukkit.INSTANCE = new KarFrameworkBukkit(this));
    }

    @Override
    public void onLoad() {
        BukkitPluginProvider provider = new BukkitPluginProvider(this);
        ConfigurationNode translate = provider.loadConfiguration(
                Objects.requireNonNull(
                        provider.loadConfiguration("translate.conf")
                )
        );
        this.delegate = Translator.from(translate).cached();
    }

    @Override
    public @Nullable FormatAction getTranslation(@NotNull String key) {
        return delegate.getTranslation(key);
    }
}
