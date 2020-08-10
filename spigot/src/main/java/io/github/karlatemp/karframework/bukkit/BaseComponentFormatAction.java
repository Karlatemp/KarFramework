/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/10 13:43:59
 *
 * kar-framework/kar-framework.spigot.main/BaseComponentFormatAction.java
 */

package io.github.karlatemp.karframework.bukkit;

import io.github.karlatemp.karframework.format.FormatAction;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.regex.Pattern;

public abstract class BaseComponentFormatAction {
    private static final BaseComponent[] ZERO_ARRAY = new BaseComponent[0];

    /**
     * Parses.
     *
     * @param arguments Except {@link BaseComponent}, {@link BaseComponent[]} or {@link net.md_5.bungee.api.chat.ComponentBuilder}
     * @throws IllegalArgumentException illegal arguments
     */
    public abstract void apply(@NotNull List<BaseComponent> builder, Object[] arguments);

    public final BaseComponent[] apply(Object[] arguments) {
        List<BaseComponent> components = new ArrayList<>(16);
        apply(components, arguments);
        return components.toArray(ZERO_ARRAY);
    }

    public abstract void toString(@NotNull StringBuilder builder);

    @Override
    public final String toString() {
        StringBuilder builder = new StringBuilder();
        toString(builder);
        return builder.toString();
    }

    public static class PlainComponent extends BaseComponentFormatAction {
        private final Collection<BaseComponent> components;

        public PlainComponent(BaseComponent[] components) {
            this(Arrays.asList(components));
        }

        public PlainComponent(Collection<BaseComponent> components) {
            this.components = components;
        }

        @Override
        public void apply(@NotNull List<BaseComponent> builder, Object[] arguments) {
            builder.addAll(components);
        }

        @Override
        public void toString(@NotNull StringBuilder builder) {
            for (BaseComponent component : components) {
                builder.append(component.toPlainText());
            }
        }
    }

    public static class ArgumentSlot extends BaseComponentFormatAction {
        private final int slot;

        public ArgumentSlot(int slot) {
            this.slot = slot;
        }

        @Override
        public void toString(@NotNull StringBuilder builder) {
            builder.append('{').append(slot).append('}');
        }

        @Override
        public void apply(@NotNull List<BaseComponent> builder, Object[] arguments) {
            if (slot > -1 && slot < arguments.length) {
                Object argument = arguments[slot];
                if (argument instanceof BaseComponent) {
                    builder.add((BaseComponent) argument);
                    return;
                } else if (argument instanceof BaseComponent[]) {
                    builder.addAll(Arrays.asList((BaseComponent[]) argument));
                    return;
                } else if (argument instanceof ComponentBuilder) {
                    builder.addAll(Arrays.asList(((ComponentBuilder) argument).create()));
                    return;
                } else if (argument != null)
                    throw new IllegalArgumentException("Illegal argument type `" + argument.getClass() + "` " + slot);
            }
            builder.add(new TextComponent("{" + slot + "}"));
        }
    }

    public static class LinkedActions extends BaseComponentFormatAction {
        private final Iterable<BaseComponentFormatAction> actions;

        public LinkedActions(@NotNull Iterable<BaseComponentFormatAction> actions) {
            this.actions = actions;
        }

        public LinkedActions(@NotNull BaseComponentFormatAction[] actions) {
            this(Arrays.asList(actions));
        }

        @Override
        public void toString(@NotNull StringBuilder builder) {
            for (BaseComponentFormatAction action : actions) {
                action.toString(builder);
            }
        }

        @Override
        public void apply(@NotNull List<BaseComponent> builder, Object[] arguments) {
            for (BaseComponentFormatAction action : actions) {
                action.apply(builder, arguments);
            }
        }
    }

    private static final Pattern PARSE_PATTERN = Pattern.compile(
            "\\{[0-9]+}"
    );

    @Contract(pure = true)
    public static BaseComponentFormatAction parse(FormatAction format) {
        LinkedList<BaseComponentFormatAction> actions = new LinkedList<>();
        parse(actions, format);
        return new LinkedActions(actions);
    }

    private static void parse(LinkedList<BaseComponentFormatAction> actions, FormatAction format) {
        if (format instanceof FormatAction.LinkedActions) {
            Iterable<FormatAction> iterable = ((FormatAction.LinkedActions) format).getActions();
            for (FormatAction action : iterable) {
                parse(actions, action);
            }
        } else if (format instanceof FormatAction.PlainText) {
            actions.add(new PlainComponent(
                    TextComponent.fromLegacyText(format.toString())
            ));
        } else if (format instanceof FormatAction.ArgumentSlot) {
            actions.add(new BaseComponentFormatAction.ArgumentSlot(((FormatAction.ArgumentSlot) format).getSlot()));
        }
    }
}
