/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/21 12:48:39
 *
 * kar-framework/kar-framework.tester.main/KTester.java
 */

package io.github.karlatemp.karframework.tester;

import io.github.karlatemp.karframework.IPluginProvider;
import io.github.karlatemp.karframework.KarFramework;
import io.github.karlatemp.karframework.bukkit.KarFrameworkBukkit;
import io.github.karlatemp.karframework.services.RegisteredService;
import io.github.karlatemp.karframework.services.ServiceHelper;
import org.bukkit.plugin.java.JavaPlugin;

public class KTester extends JavaPlugin {
    private IPluginProvider provider;

    @Override
    public void onLoad() {
        provider = KarFrameworkBukkit.getInstance().provide(this);
    }

    @Override
    public void onEnable() {
        ServiceHelper.register(provider, KarFramework.getSharedServicesTable());
        Iterable<RegisteredService<ITestingService>> registeredServices = KarFramework.getSharedServicesTable().findServices(ITestingService.class);
        for (RegisteredService<ITestingService> rs : registeredServices) {
            System.out.println(rs);
        }
    }
}
