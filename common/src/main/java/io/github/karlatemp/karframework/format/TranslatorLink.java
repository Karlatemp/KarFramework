/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/03 13:08:30
 *
 * kar-framework/kar-framework.common.main/TranslatorLink.java
 */

package io.github.karlatemp.karframework.format;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.LinkedList;

public class TranslatorLink implements Translator {
    private final Collection<Translator> translators;

    public TranslatorLink(Collection<Translator> translators) {
        this.translators = translators;
    }

    public TranslatorLink() {
        this(new LinkedList<>());
    }

    @Override
    public @Nullable FormatAction getTranslation(@NotNull String key) {
        for (Translator translator : translators) {
            final FormatAction translation = translator.getTranslation(key);
            if (translation != null) return translation;
        }
        return null;
    }

    public TranslatorLink with(@NotNull Translator translator) {
        translators.add(translator);
        return this;
    }
}
