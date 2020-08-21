/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/21 12:53:04
 *
 * kar-framework/kar-framework.tester.main/TestingAutoListener.java
 */

package io.github.karlatemp.karframework.tester;

import io.github.karlatemp.karframework.annotation.Service;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

@Service.KService
@Service.KAuto
public class TestingAutoListener implements Listener {
    @Service.KInject("s1")
    private ITestingService testingService;
    @Service.KInject("s2")
    private ITestingService testingService2;
    @Service.KInject("unknown")
    private ITestingService testingServiceUnknown;
    @Service.KInject()
    private ITestingService testingServiceDefault;
    @Service.KInject
    private KTester plugin;

    @EventHandler
    public void on(PlayerJoinEvent event) {
        event.getPlayer().sendMessage("Hello!");
        event.getPlayer().sendMessage("S1 = " + testingService);
        event.getPlayer().sendMessage("S2 = " + testingService2);
        event.getPlayer().sendMessage("Un = " + testingServiceUnknown);
        event.getPlayer().sendMessage("De = " + testingServiceDefault);
        event.getPlayer().sendMessage("Plugin = " + plugin);
    }
}
