/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/04 19:57:48
 *
 * kar-framework/kar-framework.spigot.main/TestCodes.java
 */

package io.github.karlatemp.karframework.bukkit;

import io.github.karlatemp.karframework.opennbt.ITagCompound;
import org.bukkit.entity.Player;

public interface TestCodes {
    ITagCompound invokeTest(Player player);
}
