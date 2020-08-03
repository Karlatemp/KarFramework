/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/03 13:08:30
 *
 * kar-framework/kar-framework.common.main/InterruptCommand.java
 */

package io.github.karlatemp.karframework.command;

/**
 * Interrupt command execution.
 *
 * <pre>{@code throw InterruptCommand.INSTANCE;}</pre>
 */
public class InterruptCommand extends RuntimeException {
    private InterruptCommand() {
        super(null, null, false, false);
    }

    public static final InterruptCommand INSTANCE = new InterruptCommand();
}
