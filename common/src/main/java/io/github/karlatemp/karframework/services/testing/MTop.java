/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/19 13:09:07
 *
 * kar-framework/kar-framework.common.main/MTop.java
 */

package io.github.karlatemp.karframework.services.testing;

import io.github.karlatemp.karframework.annotation.Service;

@Service.KService(STop.class)
public class MTop implements STop {
    @Service.KInject
    public VVT vvt;
}
