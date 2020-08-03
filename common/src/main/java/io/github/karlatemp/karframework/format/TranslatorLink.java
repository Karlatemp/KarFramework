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
