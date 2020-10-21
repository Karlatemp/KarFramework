/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/10/21 05:34:19
 *
 * kar-framework/kar-framework.common.main/DownloadProvider.java
 */

package io.github.karlatemp.karframework.internal.resources;

import io.github.karlatemp.karframework.utils.NetworkUtils;

import java.net.URL;
import java.util.Collections;
import java.util.List;

/* internal */ interface DownloadProvider {
    String getVersionListURL();
    String getAssetBaseURL();

    default List<URL> getAssetObjectCandidates(String assetObjectLocation) {
        return Collections.singletonList(NetworkUtils.toURL(getAssetBaseURL() + assetObjectLocation));
    }
    String injectURL(String url);
}
