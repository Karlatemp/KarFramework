/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/03 13:08:30
 *
 * kar-framework/kar-framework.bungee.main/KarFrameworkBungeeBootstrap.java
 */

package io.github.karlatemp.karframework.bungee;

import io.github.karlatemp.karframework.KarFramework;
import io.github.karlatemp.karframework.format.FormatAction;
import io.github.karlatemp.karframework.format.Translator;
import net.md_5.bungee.api.plugin.Plugin;
import ninja.leaping.configurate.ConfigurationNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class KarFrameworkBungeeBootstrap extends Plugin implements Translator {
    private Translator delegate = Translator.nullTranslator();

    public KarFrameworkBungeeBootstrap() {
        KarFramework.setInstance(KarFrameworkBungee.INSTANCE = new KarFrameworkBungee(this));
    }

    @Override
    public void onLoad() {
        BungeePluginProvider provider = new BungeePluginProvider(this);
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
