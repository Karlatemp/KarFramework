/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/10/21 05:43:52
 *
 * kar-framework/kar-framework.common.main/IMinecraftFramework.java
 */

package io.github.karlatemp.karframework.internal.resources;

import io.github.karlatemp.karframework.annotation.InternalAPI;

import java.util.logging.Logger;

@InternalAPI
public /*internal*/ interface IMinecraftFramework {
    String getMinecraftVersion();

    boolean isBukkit();

    String getNmsVersion();

    Logger getLogger();
}
