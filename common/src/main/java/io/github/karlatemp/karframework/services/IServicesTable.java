/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/18 23:42:47
 *
 * kar-framework/kar-framework.common.main/IServicesTable.java
 */

package io.github.karlatemp.karframework.services;

import com.google.common.annotations.Beta;
import com.google.common.io.Files;
import org.jetbrains.annotations.*;

import java.util.Optional;

public interface IServicesTable {
    @Contract(pure = true)
    boolean hasService(@NotNull Class<?> service);

    <T> @NotNull RegisteredService<T> registerService(
            @NotNull Class<T> serviceType,
            @NotNull T service,
            @Nullable String name
    );

    @NotNull @Unmodifiable @UnmodifiableView <T> Iterable<RegisteredService<T>> findServices(
            @NotNull Class<T> serviceType
    );

    @NotNull @Unmodifiable @UnmodifiableView <T> Iterable<T> findServicesImpl(
            @NotNull Class<T> serviceType
    );

    @NotNull <T> Optional<T> findService(@NotNull Class<T> service, @Nullable String name);

    @NotNull <T> Optional<RegisteredService<T>> findRegisteredService(@NotNull Class<T> service, @Nullable String name);

    @Beta
    default IServicesTable newSubSandbox() {
        return new SubSandbox(this);
    }
}
