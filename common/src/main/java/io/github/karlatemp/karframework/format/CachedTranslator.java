/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/03 13:08:30
 *
 * kar-framework/kar-framework.common.main/CachedTranslator.java
 */

package io.github.karlatemp.karframework.format;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class CachedTranslator implements Translator {
    private static final class VOID_ACTION extends FormatAction {
        static final VOID_ACTION INSTANCE = new VOID_ACTION();

        @Override
        public void apply(@NotNull StringBuilder builder, String[] arguments) {
        }

        @Override
        public void toString(@NotNull StringBuilder builder) {
        }
    }

    private final Translator translator;
    private final Map<String, FormatAction> cached = new ConcurrentHashMap<>();
    private final Function<String, FormatAction> FINDER = this::findAction;

    private FormatAction findAction(String key) {
        final FormatAction translation = translator.getTranslation(key);
        if (translation != null) return translation;
        return VOID_ACTION.INSTANCE;
    }

    public CachedTranslator(Translator translator) {
        this.translator = translator;
    }

    public void clearCache() {
        cached.clear();
    }

    @Override
    public @Nullable FormatAction getTranslation(@NotNull String key) {
        final FormatAction action = cached.computeIfAbsent(key, FINDER);
        if (action == VOID_ACTION.INSTANCE)
            return null;
        return action;
    }

    @Override
    public @NotNull CachedTranslator cached() {
        return this;
    }
}
