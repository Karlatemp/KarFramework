package io.github.karlatemp.karframework.format;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtils {
    @Contract()
    public static @NotNull String replace(
            @NotNull Pattern pattern,
            @NotNull String source,
            @NotNull BiFunction<@NotNull String, @NotNull Matcher, @Nullable String> replacer
    ) {
        StringBuilder builder = new StringBuilder(source.length());
        final Matcher matcher = pattern.matcher(source);
        int pos = 0;
        while (matcher.find()) {
            if (pos != matcher.start()) {
                builder.append(source, pos, matcher.start());
            }
            pos = matcher.end();
            String from = source.substring(matcher.start(), matcher.end());
            @Nullable String result = replacer.apply(from, matcher);
            if (result == null) {
                builder.append(from);
            } else {
                builder.append(result);
            }
        }
        if (pos != source.length()) {
            builder.append(source, pos, source.length());
        }
        return builder.toString();
    }

    public static void main(String[] args) {
        System.out.println(replace(
                Pattern.compile("\\{.+?}"),
                "Test {1} {2} OFF",
                (p, m) -> "p-" + p + "-s"));
    }
}
