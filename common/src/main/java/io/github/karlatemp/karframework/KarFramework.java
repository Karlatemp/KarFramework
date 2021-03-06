/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/03 13:08:30
 *
 * kar-framework/kar-framework.common.main/KarFramework.java
 */

package io.github.karlatemp.karframework;

import io.github.karlatemp.karframework.services.IServicesTable;
import io.github.karlatemp.karframework.services.SimpleServicesTable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicReference;

public class KarFramework {
    private static final AtomicReference<IKarFramework> INSTANCE = new AtomicReference<>();
    private static final IServicesTable SERVICES_TABLE = new SimpleServicesTable();

    public static IServicesTable getSharedServicesTable() {
        return SERVICES_TABLE;
    }

    public static void setInstance(IKarFramework framework) {
        if (!INSTANCE.compareAndSet(null, framework)) {
            throw new IllegalStateException("Framework initialized.");
        }
    }

    @Contract(pure = true)
    @NotNull
    public static IKarFramework getInstance() {
        final IKarFramework framework = INSTANCE.get();
        if (framework == null) throw new IllegalStateException("Framework not initialized");
        return framework;
    }
}
