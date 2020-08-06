/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/06 23:10:09
 *
 * kar-framework/kar-framework.common.main/GroovyExecute.java
 */

package io.github.karlatemp.karframework.groovy;

import javax.script.ScriptException;

public class GroovyExecute extends GroovyRequire {

    public GroovyExecute(GroovyScript script) {
        super(script);
    }

    @Override
    protected Object post(GroovyScript script) {
        String namespace = getNamespace(script.name);
        if (!canAccess(this.namespace, namespace)) {
            throw new IllegalArgumentException("Cannot access `" + namespace + "` from `" + this.namespace + "`");
        }
        try {
            return script.execute();
        } catch (ScriptException e) {
            throw new RuntimeException(e);
        }
    }
}
