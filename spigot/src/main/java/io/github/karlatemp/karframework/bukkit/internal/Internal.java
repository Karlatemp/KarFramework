/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/05 20:01:06
 *
 * kar-framework/kar-framework.spigot.main/Internal.java
 */

package io.github.karlatemp.karframework.bukkit.internal;

import io.github.karlatemp.karframework.IPluginProvider;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import java.util.LinkedList;

public class Internal {
    public static final ThreadLocal<CommentedConfigurationNode> CONFIG = new ThreadLocal<>();
    public static final ThreadLocal<ConfigurationLoader<? extends CommentedConfigurationNode>> CONFIG_LOADER = new ThreadLocal<>();
    public static final ThreadLocal<IPluginProvider> PLUGIN_PROVIDER = new ThreadLocal<>();

    // This field only can be access in first initialize
    public static final ThreadLocal<LinkedList<Runnable>> SHUTDOWN_HOOK = new ThreadLocal<>();

}
