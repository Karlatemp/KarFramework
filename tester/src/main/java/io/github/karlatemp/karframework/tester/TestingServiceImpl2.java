/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/21 12:51:44
 *
 * kar-framework/kar-framework.tester.main/TestingServiceImpl.java
 */

package io.github.karlatemp.karframework.tester;

import io.github.karlatemp.karframework.annotation.Service;
import org.bukkit.command.CommandMap;

@Service.KService(value = ITestingService.class, name = "s2")
public class TestingServiceImpl2 implements ITestingService {
    @Service.KInject
    private CommandMap commandMap;

    @Override
    public CommandMap getCommandMap() {
        return commandMap;
    }

    @Override
    public String toString() {
        return "TestingServiceImpl2{" + commandMap + ", name=`?????`}";
    }
}
