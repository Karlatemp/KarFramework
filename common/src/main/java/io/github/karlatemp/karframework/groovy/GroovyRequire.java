/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/06 23:03:23
 *
 * kar-framework/kar-framework.common.main/GroovyRequire.java
 */

package io.github.karlatemp.karframework.groovy;

import groovy.lang.Closure;
import org.codehaus.groovy.GroovyException;

import javax.script.ScriptException;
import java.util.Map;

public class GroovyRequire extends Closure<Object> {

    protected final GroovyScript script;
    protected String namespace;

    static String getNamespace(String name) {
        int split = name.indexOf('/');
        if (split == -1) {
            return null;
        }
        return name.substring(0, split);
    }

    static boolean canAccess(String from, String target) {
        if (from == null) return true;
        if (from.equals("libraries")) {
            if (target == null) return true;
            return target.equals("libraries");
        }
        if (target == null) return false;
        if (target.contains("-lib"))
            return true;
        if (target.equals("libraries"))
            return true;
        return from.equals(target);
    }

    public GroovyRequire(GroovyScript script) {
        super(script);
        this.script = script;
        namespace = getNamespace(script.name);
    }

    @Override
    public Object call(Object... args) {
        return post(String.valueOf(args[0]));
    }


    protected Object post(GroovyScript script) {
        if (!script.runBefore) {
            if (!canAccess(namespace, getNamespace(script.name))) {
                throw new RuntimeException(
                        new GroovyException("Script " + script.runBefore + " not initialize and `" + namespace + "` cannot access `" + getNamespace(script.name) + "`")
                );
            }
            try {
                script.execute();
            } catch (ScriptException se) {
                throw new RuntimeException(se);
            }
        }
        return script.exports;
    }

    protected Object post(String args) {
        GroovyScript script = this.script.manager.loadScript(args);
        if (script != null) {
            return post(script);
        }
        return null;
    }
}
