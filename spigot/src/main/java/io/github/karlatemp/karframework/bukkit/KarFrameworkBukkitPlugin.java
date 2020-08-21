/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/21 14:36:09
 *
 * kar-framework/kar-framework.spigot.main/KarFrameworkBukkitPlugin.java
 */

package io.github.karlatemp.karframework.bukkit;

import cn.mcres.karlatemp.unsafe.Unsafe;
import io.github.karlatemp.karframework.IPluginProvider;
import io.github.karlatemp.karframework.KarFramework;
import io.github.karlatemp.karframework.services.ServiceHelper;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class KarFrameworkBukkitPlugin extends JavaPlugin {
    @SuppressWarnings("FieldMayBeFinal")
    private static Object NIL = null;
    protected final @NotNull IPluginProvider provider = (IPluginProvider) NIL;
    private static final long PROVIDER_OFFSET;
    private static final Unsafe UNSAFE = Unsafe.getUnsafe();

    static {
        try {
            PROVIDER_OFFSET = UNSAFE.objectFieldOffset(KarFrameworkBukkitPlugin.class.getDeclaredField("provider"));
        } catch (NoSuchFieldException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    @Override
    public final void onLoad() {
        UNSAFE.putReference(this, PROVIDER_OFFSET, new BukkitPluginProvider(this));
        onLoad0();
    }

    protected void onLoad0() {
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public final void onEnable() {
        KarFramework.getSharedServicesTable().registerService(
                (Class) getClass(), (Object) this, null
        );
        preEnable();
        onEnable0();
        postEnable();
    }

    protected void preEnable() {
    }

    protected void onEnable0() {
    }

    protected void postEnable() {
        ServiceHelper.register(provider, KarFramework.getSharedServicesTable());
    }
}
