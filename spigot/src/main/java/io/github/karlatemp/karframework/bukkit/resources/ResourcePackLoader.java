/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/06 12:51:13
 *
 * kar-framework/kar-framework.spigot.main/ResourcePackLoader.java
 */

package io.github.karlatemp.karframework.bukkit.resources;

import com.google.common.io.Files;
import io.github.karlatemp.karframework.IPluginProvider;
import io.github.karlatemp.karframework.annotation.Warning;
import io.github.karlatemp.karframework.bukkit.internal.Internal;
import ninja.leaping.configurate.ValueType;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.zip.ZipFile;

@Warning({
        "WARNING: This class is internal class.",
        "This class will be CHANGE/REMOVE ANYTIME!",
        "DONT use this anywhere."
})
public /*internal*/ class ResourcePackLoader {
    static File resourcePacksLocation;
    static List<File> resourcePacks;
    static List<ZipFile> resourceZipFiles = new ArrayList<>(16);

    @SuppressWarnings("UnstableApiUsage")
    public /*internal*/ static void initialize() {
        final CommentedConfigurationNode configurationNode = Internal.CONFIG.get();
        final ConfigurationLoader<? extends CommentedConfigurationNode> loader =
                Internal.CONFIG_LOADER.get();
        final IPluginProvider pprovider = Internal.PLUGIN_PROVIDER.get();
        resourcePacks = new LinkedList<>();
        resourceZipFiles.removeIf(it -> {
            String name = it.getName();
            try {
                it.close();
            } catch (IOException exception) {
                pprovider.getLogger().log(Level.WARNING, "Exception in closing " + name, exception);
            }
            return true;
        });

        final CommentedConfigurationNode resources = configurationNode.getNode("resources");
        boolean zh = Locale.getDefault().getLanguage().equalsIgnoreCase("zh");
        if (resources.isVirtual() || resources.getValueType() != ValueType.LIST) {
            resources.setValue(new ArrayList<>());
            resources.setComment(zh ?
                    "加载的资源包顺序" :
                    "Resource pack loading order");
        }
        final LinkedList<String> resourcesList =
                new LinkedList<>(resources.getList(Helper.transformer));
        resourcePacksLocation = new File(pprovider.getPluginDataFolder(), "resources");
        resourcePacksLocation.mkdirs();
        File[] resourceZips = resourcePacksLocation.listFiles();
        Map<String, ZipFile> loadedResourcePacks = new HashMap<>();
        if (resourceZips != null) {
            for (File resourceZip : resourceZips) {
                if (resourceZip.isFile() && resourceZip.getName().endsWith(".zip")) {
                    try {
                        ZipFile zipFile = new ZipFile(resourceZip);
                        // resourceZipFiles.add(zipFile);
                        resourcePacks.add(resourceZip);
                        loadedResourcePacks.put(Files.getNameWithoutExtension(zipFile.getName()), zipFile);
                    } catch (IOException ioException) {
                        pprovider.getLogger().log(Level.WARNING, "Exception in loading resource pack " + resourceZip, ioException);
                    }
                }
            }
        }
        resourcesList.removeIf(s -> {
            if (!loadedResourcePacks.containsKey(s)) {
                return true;
            }
            return false;
        });
        for (String k : loadedResourcePacks.keySet()) {
            if (!resourcesList.contains(k)) {
                resourcesList.add(k);
            }
        }
        resources.setValue(resourcesList);
        final Iterator<String> iterator = resourcesList.descendingIterator();
        while (iterator.hasNext()) {
            String loading = iterator.next();
            resourceZipFiles.add(loadedResourcePacks.get(loading));
        }
    }
}
