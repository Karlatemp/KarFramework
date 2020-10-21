/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/10/21 05:34:19
 *
 * kar-framework/kar-framework.common.main/BMCLAPIDownloadProvider.java
 */

package io.github.karlatemp.karframework.internal.resources;

/* internal */ class BMCLAPIDownloadProvider implements DownloadProvider {
    private final String apiRoot;

    public BMCLAPIDownloadProvider(String apiRoot) {
        this.apiRoot = apiRoot;
    }

    @Override
    public String getVersionListURL() {
        return apiRoot + "/mc/game/version_manifest.json";
    }

    @Override
    public String getAssetBaseURL() {
        return apiRoot + "/assets/";
    }

    @Override
    public String injectURL(String url) {
        return url
                .replace("https://resources.download.minecraft.net/", getAssetBaseURL())
                .replace("https://bmclapi2.bangbang93.com", apiRoot)
                .replace("https://launchermeta.mojang.com", apiRoot)
                .replace("https://launcher.mojang.com", apiRoot)
                .replace("https://libraries.minecraft.net", apiRoot + "/libraries")
                .replaceFirst("https?://files\\.minecraftforge\\.net/maven", apiRoot + "/maven")
                .replace("http://dl.liteloader.com/versions/versions.json", apiRoot + "/maven/com/mumfrey/liteloader/versions.json")
                .replace("http://dl.liteloader.com/versions", apiRoot + "/maven")
                .replace("https://meta.fabricmc.net", apiRoot + "/fabric-meta")
                .replace("https://maven.fabricmc.net", apiRoot + "/maven")
                .replace("https://authlib-injector.yushi.moe", apiRoot + "/mirrors/authlib-injector");

    }

    @Override
    public String toString() {
        return "BMCLAPIDownloadProvider{" +
                "apiRoot='" + apiRoot + '\'' +
                '}';
    }
}
