package io.github.karlatemp.karframework.format;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class FormatAction {
    public abstract void apply(@NotNull StringBuilder builder, String[] arguments);

    public final String apply(String[] arguments) {
        StringBuilder builder = new StringBuilder();
        apply(builder, arguments);
        return builder.toString();
    }

    public abstract void toString(@NotNull StringBuilder builder);

    @Override
    public final String toString() {
        StringBuilder builder = new StringBuilder();
        toString(builder);
        return builder.toString();
    }

    public static class PlainText extends FormatAction {
        private final String text;
        private final int start;
        private final int end;

        public PlainText(String text) {
            this(text, 0, text.length());
        }

        public PlainText(String text, int start) {
            this(text, start, text.length());
        }

        public PlainText(String text, int start, int end) {
            this.text = text;
            this.start = start;
            this.end = end;
        }

        @Override
        public void toString(@NotNull StringBuilder builder) {
            builder.append(text, start, end);
        }

        @Override
        public void apply(@NotNull StringBuilder builder, String[] arguments) {
            toString(builder);
        }
    }

    public static class ArgumentSlot extends FormatAction {
        private final int slot;

        public ArgumentSlot(int slot) {
            this.slot = slot;
        }

        @Override
        public void toString(@NotNull StringBuilder builder) {
            builder.append('{').append(slot).append('}');
        }

        @Override
        public void apply(@NotNull StringBuilder builder, String[] arguments) {
            if (arguments == null) {
                toString(builder);
                return;
            }
            if (slot >= 0 && slot < arguments.length) {
                builder.append(arguments[slot]);
            } else toString(builder);
        }
    }

    public static class LinkedActions extends FormatAction {
        private final Iterable<FormatAction> actions;

        public LinkedActions(@NotNull Iterable<FormatAction> actions) {
            this.actions = actions;
        }

        public LinkedActions(@NotNull FormatAction[] actions) {
            this(Arrays.asList(actions));
        }

        @Override
        public void toString(@NotNull StringBuilder builder) {
            for (FormatAction action : actions) {
                action.toString(builder);
            }
        }

        @Override
        public void apply(@NotNull StringBuilder builder, String[] arguments) {
            for (FormatAction action : actions) {
                action.apply(builder, arguments);
            }
        }
    }

    private static final Pattern PARSE_PATTERN = Pattern.compile(
            "\\{[0-9]+}"
    );

    @Contract(pure = true)
    public static @NotNull LinkedActions parse(@NotNull String template) {
        LinkedList<FormatAction> actions = new LinkedList<>();

        final Matcher matcher = PARSE_PATTERN.matcher(template);
        int pos = 0;
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            try {
                int slot = Integer.parseInt(template.substring(start + 1, end - 1));
                if (pos != start)
                    actions.add(new FormatAction.PlainText(template, pos, start));
                pos = end;
                actions.add(new FormatAction.ArgumentSlot(slot));
            } catch (Throwable ignored) {
            }
        }
        if (pos != template.length()) {
            actions.add(new FormatAction.PlainText(template, pos));
        }

        return new LinkedActions(actions);
    }
}
