/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/21 13:37:22
 *
 * kar-framework/kar-framework.common.main/SubSandbox.java
 */

package io.github.karlatemp.karframework.services;

import com.google.common.collect.ImmutableList;
import io.github.karlatemp.karframework.utils.MultipleIterable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Arrays;
import java.util.Optional;

class SubSandbox extends SimpleServicesTable {
    private final IServicesTable parent;

    SubSandbox(IServicesTable parent) {
        this.parent = parent;
    }

    @Override
    public boolean hasService(@NotNull Class<?> service) {
        return super.hasService(service) || parent.hasService(service);
    }

    @Override
    public @NotNull @Unmodifiable @UnmodifiableView <T> Iterable<RegisteredService<T>> findServices(@NotNull Class<T> serviceType) {
        return new MultipleIterable<>(Arrays.asList(super.findServices(serviceType), parent.findServices(serviceType)));
    }

    @Override
    public @NotNull @Unmodifiable @UnmodifiableView <T> Iterable<T> findServicesImpl(@NotNull Class<T> serviceType) {
        return new MultipleIterable<>(Arrays.asList(super.findServicesImpl(serviceType), parent.findServicesImpl(serviceType)));
    }

    @Override
    public @NotNull <T> Optional<T> findService(@NotNull Class<T> service, @Nullable String name) {
        Optional<T> o = super.findService(service, name);
        if (o.isPresent()) return o;
        return parent.findService(service, name);
    }

    @Override
    public @NotNull <T> Optional<RegisteredService<T>> findRegisteredService(@NotNull Class<T> service, @Nullable String name) {
        Optional<RegisteredService<T>> o = super.findRegisteredService(service, name);
        if (o.isPresent()) return o;
        return parent.findRegisteredService(service, name);
    }
}
