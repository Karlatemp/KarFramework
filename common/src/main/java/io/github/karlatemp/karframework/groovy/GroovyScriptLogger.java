/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/06 22:42:42
 *
 * kar-framework/kar-framework.common.main/GroovyScriptLogger.java
 */

package io.github.karlatemp.karframework.groovy;

import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class GroovyScriptLogger extends Logger {
    protected GroovyScriptLogger(String name, Logger parent) {
        super(name, null);
        setParent(parent);
    }

    @Override
    public void log(LogRecord record) {
        record.setMessage("[" + getName() + "] " + record.getMessage());
        record.setLoggerName(getParent().getName());
        getParent().log(record);
    }
}
