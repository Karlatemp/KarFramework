/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/05 19:44:16
 *
 * kar-framework/kar-framework.spigot.main/MojangDownloadProvider.java
 */

package io.github.karlatemp.karframework.bukkit.resources;

/* internal */ class MojangDownloadProvider implements DownloadProvider {
    @Override
    public String getAssetBaseURL() {
        return "https://resources.download.minecraft.net/";
    }

    @Override
    public String getVersionListURL() {
        return "https://launchermeta.mojang.com/mc/game/version_manifest.json";
    }

    @Override
    public String injectURL(String url) {
        return url;
    }

    @Override
    public String toString() {
        return "DownloadProvider{type=Mojang}";
    }
}
