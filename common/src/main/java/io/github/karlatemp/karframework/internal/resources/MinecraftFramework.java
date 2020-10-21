/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/10/21 12:09:56
 *
 * kar-framework/kar-framework.common.main/MinecraftFramework.java
 */

package io.github.karlatemp.karframework.internal.resources;

import io.github.karlatemp.karframework.IKarFramework;
import io.github.karlatemp.karframework.KarFramework;

import java.lang.reflect.Method;

/*internal*/ class MinecraftFramework {
    static final IMinecraftFramework minecraftFramework;

    static {
        IKarFramework instance = KarFramework.getInstance();
        if (instance instanceof IMinecraftFramework) {
            minecraftFramework = (IMinecraftFramework) instance;
        } else {
            try {
                Method method = instance.getClass().getDeclaredMethod("minecraftFramework");
                method.setAccessible(true);
                minecraftFramework = (IMinecraftFramework) method.invoke(instance);
            } catch (Exception e) {
                throw (ExceptionInInitializerError) new ExceptionInInitializerError("No IMinecraftFramework found.").initCause(e);
            }
        }
    }
}
