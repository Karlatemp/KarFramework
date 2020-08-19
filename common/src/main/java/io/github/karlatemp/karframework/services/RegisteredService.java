/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/18 23:47:28
 *
 * kar-framework/kar-framework.common.main/RegisteredService.java
 */

package io.github.karlatemp.karframework.services;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class RegisteredService<T> {
    private final Class<T> type;
    private final T service;
    private final String name;
    private final CompletableFuture<Void> future = new CompletableFuture<>();

    public CompletableFuture<Void> getFuture() {
        return future;
    }

    public Class<T> getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public T getService() {
        return service;
    }

    public RegisteredService(
            @NotNull Class<T> type,
            @NotNull T service,
            @Nullable String name
    ) {
        this.type = type;
        this.service = service;
        this.name = name;
    }

    public void unregister() {
        future.complete(null);
    }
}
