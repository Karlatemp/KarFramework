/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/03 13:08:30
 *
 * kar-framework/kar-framework.common.main/AbstractCommandFramework.java
 */

package io.github.karlatemp.karframework.command;

import io.github.karlatemp.karframework.format.FormatAction;
import io.github.karlatemp.karframework.format.Translator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public abstract class AbstractCommandFramework<T> implements ICommandFramework<T> {
    private Translator translator;

    public AbstractCommandFramework(@NotNull Translator translator) {
        this.translator = translator;
    }

    public void setTranslator(@NotNull Translator translator) {
        this.translator = translator;
    }

    public @NotNull Translator getTranslator() {
        return translator;
    }

    @Override
    public @NotNull FormatAction getTranslate(@NotNull String key) {
        final FormatAction translation = translator.getTranslation(key);
        if (translation == null) return new UnknownTrans(key);
        return translation;
    }

    private static final class UnknownTrans extends FormatAction {
        private final String translate;

        UnknownTrans(String translate) {
            this.translate = translate;
        }

        @Override
        public void apply(@NotNull StringBuilder builder, String[] arguments) {
            builder.append('$').append(translate);
            if (arguments != null) {
                for (String s : arguments) {
                    builder.append(',').append(' ').append(s);
                }
            }
        }

        @Override
        public void toString(@NotNull StringBuilder builder) {
            builder.append('{').append(translate).append('}');
        }
    }

    public AbstractCommandFramework<T> override(@NotNull Translator translator) {
        return newTranslator(translator.orElse(this.translator));
    }

    public AbstractCommandFramework<T> newTranslator(@NotNull Translator translator) {
        return new OverrideTransFrameWork<>(this, translator);
    }

    private static class OverrideTransFrameWork<T> extends AbstractCommandFramework<T> {
        private final ICommandFramework<T> parent;

        OverrideTransFrameWork(ICommandFramework<T> parent, Translator translator) {
            super(translator);
            this.parent = parent;
        }

        @Override
        public AbstractCommandFramework<T> newTranslator(@NotNull Translator translator) {
            return new OverrideTransFrameWork<>(parent, translator);
        }

        @Override
        public void sendMessage(@NotNull T target, @NotNull String message) {
            parent.sendMessage(target, message);
        }

        @Override
        public void sendTranslate(@NotNull T target, @NotNull String key, @Nullable String... values) {
            parent.sendTranslate(target, key, values);
        }

        @Override
        public void sendTranslate(@NotNull T target, @NotNull FormatAction key, @Nullable String... values) {
            parent.sendTranslate(target, key, values);
        }

        @Override
        public boolean hasPermission(@NotNull T target, @Nullable String permission) {
            return parent.hasPermission(target, permission);
        }

        @Override
        @NotNull
        public String getName(@NotNull T target) {
            return parent.getName(target);
        }

        @Override
        @NotNull
        public CommandSingle.@NotNull Builder<T> newSingleCommand() {
            return parent.newSingleCommand();
        }

        @Override
        public CommandTree.@NotNull Builder<T> newCommandTree() {
            return parent.newCommandTree();
        }
    }
}
