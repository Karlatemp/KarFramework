/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/18 23:51:02
 *
 * kar-framework/kar-framework.common.main/SimpleServicesTable.java
 */

package io.github.karlatemp.karframework.services;

import com.google.common.collect.ImmutableList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.function.Function;

public class SimpleServicesTable implements IServicesTable {
    private final Object accessClassMappingLock = new Object();
    private final HashMap<Class<?>, ConcurrentLinkedDeque<RegisteredService<?>>> services = new HashMap<>();

    private static final Function<Object, ConcurrentLinkedDeque<RegisteredService<?>>> C_ALLOCATOR = $ -> new ConcurrentLinkedDeque<>();

    @Override
    public boolean hasService(@NotNull Class<?> service) {
        return services.containsKey(service);
    }

    @Override
    public @NotNull <T> RegisteredService<T> registerService(
            @NotNull Class<T> serviceType,
            @NotNull T service,
            @Nullable String name
    ) {
        ConcurrentLinkedDeque<RegisteredService<?>> services;
        synchronized (accessClassMappingLock) {
            services = this.services.computeIfAbsent(serviceType, C_ALLOCATOR);
        }
        RegisteredService<T> registeredService = new RegisteredService<>(
                serviceType, service, name
        );
        services.add(registeredService);
        registeredService.getFuture().thenRun(() -> {
            services.remove(registeredService);
            fixup(serviceType);
        });
        return registeredService;
    }

    private void fixup(Class<?> type) {
        synchronized (accessClassMappingLock) {
            ConcurrentLinkedDeque<RegisteredService<?>> services = this.services.get(type);
            if (services != null) {
                if (services.isEmpty()) {
                    this.services.remove(type);
                }
            }
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public @NotNull @Unmodifiable @UnmodifiableView <T> Iterable<RegisteredService<T>> findServices(@NotNull Class<T> serviceType) {
        return () -> {
            ConcurrentLinkedDeque<RegisteredService<?>> services = this.services.get(serviceType);
            if (services == null) {
                return Collections.emptyIterator();
            }
            return (Iterator<RegisteredService<T>>) (Iterator) ImmutableList.copyOf(services).iterator();
        };
    }

    @SuppressWarnings("unchecked")
    @Override
    public @NotNull @Unmodifiable @UnmodifiableView <T> Iterable<T> findServicesImpl(@NotNull Class<T> serviceType) {
        return () -> {
            ConcurrentLinkedDeque<RegisteredService<?>> services = this.services.get(serviceType);
            if (services == null) {
                return Collections.emptyIterator();
            }
            ImmutableList.Builder<Object> builder = ImmutableList.builder();
            services.stream().map(RegisteredService::getService).forEach(builder::add);
            return (Iterator<T>) builder.build().iterator();
        };
    }

    @Override
    public @NotNull <T> Optional<T> findService(@NotNull Class<T> service, @Nullable String name) {
        return findRegisteredService(service, name)
                .map(RegisteredService::getService);
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public @NotNull <T> Optional<RegisteredService<T>> findRegisteredService(@NotNull Class<T> service, @Nullable String name) {
        ConcurrentLinkedDeque<RegisteredService<T>> services = (ConcurrentLinkedDeque<RegisteredService<T>>) (ConcurrentLinkedDeque) this.services.get(service);
        if (services == null) {
            return Optional.empty();
        }
        if (name == null) {
            return Optional.ofNullable(services.peek());
        }
        for (RegisteredService<T> serviceX : services) {
            if (name.equals(serviceX.getName())) {
                return Optional.of(serviceX);
            }
        }

        return Optional.empty();
    }
}
