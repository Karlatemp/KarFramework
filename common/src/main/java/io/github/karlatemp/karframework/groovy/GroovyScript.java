/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/06 22:40:54
 *
 * kar-framework/kar-framework.common.main/GroovyScript.java
 */

package io.github.karlatemp.karframework.groovy;

import io.github.karlatemp.karframework.IKarFramework;
import io.github.karlatemp.karframework.IPluginProvider;
import io.github.karlatemp.unsafeaccessor.Unsafe;
import org.codehaus.groovy.jsr223.GroovyScriptEngineImpl;
import org.jetbrains.annotations.NotNull;

import javax.script.*;
import java.io.Reader;
import java.io.Writer;
import java.util.*;

public class GroovyScript implements ScriptContext, Bindings {
    final IPluginProvider provider;
    final CompiledScript script;
    final Map<String, Object> variables = new HashMap<>();
    final Map<String, Object> exports = new HashMap<>();
    final GroovyScriptManager manager;
    final String name;
    boolean runBefore = false;

    public GroovyScript(
            Reader reader,
            IPluginProvider provider,
            IKarFramework framework,
            GroovyScriptManager groovyScriptManager,
            String name) throws ScriptException {
        this.provider = provider;
        this.name = name;
        variables.put("logger", new GroovyScriptLogger(name, groovyScriptManager.getLogger()));
        variables.put("framework", framework);
        variables.put("exports", exports);
        variables.put("require", new GroovyRequire(this));
        variables.put("execute", new GroovyExecute(this));
        variables.put("importAll", new GroovyImports(this));
        if (GroovyRequire.getNamespace(name) == null) { // root
            variables.put("provider", provider);
            variables.put("unsafe", Unsafe.getUnsafe());
        }
        this.manager = groovyScriptManager;
        GroovyScriptEngineImpl engine = groovyScriptManager.getEngine();
        ScriptContext context = engine.getContext();
        try {
            engine.setContext(this);
            variables.put(ScriptEngine.FILENAME, name);
            this.script = engine.compile(reader);
            variables.remove(ScriptEngine.FILENAME);
        } finally {
            engine.setContext(context);
        }

    }

    public void setup(Map<String, String> variables) {
        if (variables != null) {
            this.variables.putAll(variables);
        }
    }

    public Object execute() throws ScriptException {
        runBefore = true;
        return script.eval((ScriptContext) this);
    }

    @Override
    public void setBindings(Bindings bindings, int scope) {
    }

    @Override
    public Bindings getBindings(int scope) {
        return this;
    }

    @Override
    public void setAttribute(String name, Object value, int scope) {
        variables.put(name, value);
    }

    @Override
    public Object getAttribute(String name, int scope) {
        return variables.get(name);
    }

    @Override
    public Object removeAttribute(String name, int scope) {
        return variables.remove(name);
    }

    @Override
    public Object getAttribute(String name) {
        return variables.get(name);
    }

    @Override
    public int getAttributesScope(String name) {
        if (variables.containsKey(name))
            return ENGINE_SCOPE;
        return -1;
    }

    @Override
    public Writer getWriter() {
        return null;
    }

    @Override
    public Writer getErrorWriter() {
        return null;
    }

    @Override
    public void setWriter(Writer writer) {
    }

    @Override
    public void setErrorWriter(Writer writer) {
    }

    @Override
    public Reader getReader() {
        return null;
    }

    @Override
    public void setReader(Reader reader) {
    }

    @Override
    public List<Integer> getScopes() {
        return null;
    }

    @Override
    public Object put(String name, Object value) {
        return variables.put(name, value);
    }

    @Override
    public void putAll(Map<? extends String, ?> toMerge) {
        variables.putAll(toMerge);
    }

    @Override
    public void clear() {
        variables.clear();
    }

    @NotNull
    @Override
    public Set<String> keySet() {
        return variables.keySet();
    }

    @NotNull
    @Override
    public Collection<Object> values() {
        return variables.values();
    }

    @NotNull
    @Override
    public Set<Entry<String, Object>> entrySet() {
        return variables.entrySet();
    }

    @Override
    public int size() {
        return variables.size();
    }

    @Override
    public boolean isEmpty() {
        return variables.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return variables.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return variables.containsValue(value);
    }

    @Override
    public Object get(Object key) {
        return variables.get(key);
    }

    @Override
    public Object remove(Object key) {
        return variables.remove(key);
    }

    @Override
    public String toString() {
        return "GroovyScript{variables=" + variables + "}";
    }
}
