/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/05 18:14:05
 *
 * kar-framework/kar-framework.common.main/ScriptProvider.java
 */

package io.github.karlatemp.karframework.script;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.jetbrains.annotations.NotNull;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import java.util.concurrent.TimeUnit;

public class ScriptProvider {
    private final ScriptEngine engine;
    private final LoadingCache<String, ScriptVariables> scriptVariablesCache = CacheBuilder
            .newBuilder()
            .expireAfterAccess(30, TimeUnit.MINUTES)
            .build(new CacheLoader<String, ScriptVariables>() {
                @Override
                public ScriptVariables load(@NotNull String key) throws Exception {
                    return newScriptVariables(key);
                }
            });

    public ScriptProvider(@NotNull ScriptEngine engine) {
        this.engine = engine;
    }

    public void invalidateCache() {
        scriptVariablesCache.invalidateAll();
    }

    public ScriptEngine getEngine() {
        return engine;
    }

    public ScriptVariables getOrCreateScriptVariables(String key) {
        return scriptVariablesCache.getUnchecked(key);
    }

    public ScriptVariables newScriptVariables(String key) {
        ScriptVariables sv = newScriptVariables();
        sv.setAttribute(ScriptEngine.FILENAME, key, ScriptContext.ENGINE_SCOPE);
        return sv;
    }

    public ScriptVariables newScriptVariables() {
        return new ScriptVariables(engine.createBindings(), this);
    }
}
