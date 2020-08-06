/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/05 21:04:31
 *
 * kar-framework/kar-framework.spigot.main/DownloadingTask.java
 */

package io.github.karlatemp.karframework.bukkit.resources;

import java.io.File;
import java.util.concurrent.CompletableFuture;

/* internal */     class DownloadingTask {
    /* internal */ final String url;
    /* internal */ final String sha1;
    /* internal */ final CompletableFuture<Void> future = new CompletableFuture<>();
    /* internal */ final File file;
    /* internal */ final long size;
    /* internal */ final String resName;

    /* internal */ DownloadingTask(
            String sha1, String url, File file,
            long size, String resName
    ) {
        this.sha1 = sha1;
        this.url = url;
        this.file = file;
        this.size = size;
        this.resName = resName;
    }
}
