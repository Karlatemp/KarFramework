package io.github.karlatemp.karframework.format;

import com.google.common.base.Splitter;
import ninja.leaping.configurate.ConfigurationNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Function;

public interface Translator extends Function<@NotNull String, @Nullable FormatAction> {
    static Translator nullTranslator() {
        return TranslatorElse.NULL;
    }

    static TranslatorLink link() {
        return new TranslatorLink();
    }

    static TranslatorLink concurrentLink() {
        return new TranslatorLink(new ConcurrentLinkedQueue<>());
    }

    static Translator from(@NotNull ConfigurationNode node) {
        return new ConfigTranslator(node);
    }

    @Nullable FormatAction getTranslation(@NotNull String key);

    @Override
    default @Nullable FormatAction apply(@NotNull String s) {
        return getTranslation(s);
    }

    default @NotNull Translator orElse(Translator next) {
        return new TranslatorElse(this, next);
    }

    default @NotNull CachedTranslator cached() {
        return new CachedTranslator(this);
    }
}

class ConfigTranslator implements Translator {
    private final ConfigurationNode root;

    ConfigTranslator(ConfigurationNode root) {
        this.root = root;
    }

    @Override
    public @Nullable FormatAction getTranslation(@NotNull String key) {
        Object value = root.getNode(Splitter.on('.').split(key)).getValue();
        if (value != null) return FormatAction.parse(value.toString());
        return null;
    }
}

class TranslatorElse implements Translator {
    static final CachedTranslator NULL = new CachedTranslator(c -> null) {
        @Override
        public @Nullable FormatAction getTranslation(@NotNull String key) {
            return null;
        }
    };

    private final Translator else_;
    private final Translator from;

    TranslatorElse(Translator from, Translator else_) {
        this.from = from;
        this.else_ = else_;
    }

    @Override
    public @Nullable FormatAction getTranslation(@NotNull String key) {
        final FormatAction translation = from.getTranslation(key);
        if (translation == null) return else_.getTranslation(key);
        return translation;
    }
}
