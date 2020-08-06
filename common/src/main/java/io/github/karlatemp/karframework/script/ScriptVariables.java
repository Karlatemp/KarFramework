/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/05 18:15:09
 *
 * kar-framework/kar-framework.common.main/ScriptVariables.java
 */

package io.github.karlatemp.karframework.script;

import javax.script.Bindings;
import javax.script.ScriptException;
import javax.script.SimpleScriptContext;
import java.io.Reader;

public class ScriptVariables extends SimpleScriptContext {
    private final Bindings bindings;
    private final ScriptProvider provider;

    ScriptVariables(Bindings bindings, ScriptProvider provider) {
        this.bindings = bindings;
        this.provider = provider;
        setBindings(bindings, ENGINE_SCOPE);
    }

    @Override
    public Bindings getBindings(int scope) {
        return bindings;
    }


    public Object setVariable(String key, Object value) {
        return bindings.put(key, value);
    }

    public Object getVariable(String key) {
        return bindings.get(key);
    }

    public Object removeVariable(String key) {
        return bindings.remove(key);
    }

    public Object eval(Reader reader) throws ScriptException {
        return provider.getEngine().eval(reader, this);
    }
}
