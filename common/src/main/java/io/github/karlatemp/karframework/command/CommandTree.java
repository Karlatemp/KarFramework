/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/03 13:08:30
 *
 * kar-framework/kar-framework.common.main/CommandTree.java
 */

package io.github.karlatemp.karframework.command;

import io.github.karlatemp.karframework.format.FormatAction;
import io.github.karlatemp.karframework.internal.ListUtils;
import io.github.karlatemp.karframework.internal.StringUtils;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CommandTree<T> implements ICommandNode<T> {
    public static @NotNull Builder<?> builder() {
        return new Builder<>();
    }

    public static @NotNull <T> Builder<T> builder(@NotNull ICommandFramework<T> framework) {
        return builder().framework(framework);
    }

    public static class Builder<T> {
        private String name;
        private ICommandFramework<T> framework;
        private String permission;
        private String description;
        private boolean openDistances;

        @SuppressWarnings("unchecked")
        public @NotNull <V> Builder<V> framework(ICommandFramework<V> framework) {
            Builder<V> v = (Builder<V>) this;
            v.framework = framework;
            return v;
        }

        public @NotNull Builder<T> name(String name) {
            this.name = name;
            return this;
        }

        public @NotNull Builder<T> permission(String permission) {
            this.permission = permission;
            return this;
        }

        public @NotNull Builder<T> description(String description) {
            this.description = description;
            return this;
        }

        public @NotNull Builder<T> openDistances(boolean openDistances) {
            this.openDistances = openDistances;
            return this;
        }

        public @NotNull CommandTree<T> build() {
            Validate.notNull(name, "`name` cannot be null.");
            Validate.notNull(framework, "`framework` cannot be null.");
            return new CommandTree<>(framework, name, description, permission,
                    new HashMap<>(), new HashMap<>(), openDistances);
        }
    }

    private final String name;
    private final ICommandFramework<T> framework;
    private final String permission;
    private final String description;
    private ICommandNode<T> parent;
    private final Map<String, ICommandNode<T>> subNodes, subAlias;
    private List<List<Map.Entry<String, ICommandNode<T>>>> sortedNames;
    private Map<String, List<String>> distances;
    private Function<String, List<String>> distancesFinder;

    public CommandTree(
            @NotNull ICommandFramework<T> framework,
            @NotNull String name,
            @Nullable String description,
            @Nullable String permission,
            @NotNull Map<String, ICommandNode<T>> subNodes,
            @NotNull Map<String, ICommandNode<T>> subAlias,
            boolean openDistances
    ) {
        this.name = name;
        this.framework = framework;
        this.description = description;
        this.permission = permission;
        this.subNodes = subNodes;
        this.subAlias = subAlias;
        if (openDistances) {
            distances = new ConcurrentHashMap<>();
            distancesFinder = this::findDistance;
        }
        refreshNodes();
    }

    public CommandTree(@NotNull ICommandFramework<T> framework) {
        this(
                framework,
                "<ROOT>",
                "The root tree",
                null
        );
    }

    public CommandTree(@NotNull ICommandFramework<T> framework, @NotNull String name) {
        this(framework, name, null, null);
    }

    public CommandTree(
            @NotNull ICommandFramework<T> framework,
            @NotNull String name,
            @Nullable String description,
            @Nullable String permission
    ) {
        this(framework, name, description, permission, new HashMap<>(), new HashMap<>(), true);
    }

    public void refreshNodes() {
        List<Map.Entry<String, ICommandNode<T>>> sorted = subNodes.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toList());
        List<List<Map.Entry<String, ICommandNode<T>>>> sortedNames = this.sortedNames = new LinkedList<>();
        int pages = 7;
        for (int i = 0; i < sorted.size(); i += pages) {
            sortedNames.add(sorted.subList(0, Math.min(i + pages, sorted.size())));
        }
        if (distances != null) {
            distances.clear();
        }
    }

    @Override
    public @Nullable ICommandNode<T> getSubNode(@NotNull String name) {
        return subNodes.get(name.toLowerCase());
    }

    @Override
    public void setParent(@NotNull ICommandNode<T> parent) {
        this.parent = parent;
    }

    @Override
    public @Nullable String getPermission() {
        return permission;
    }

    @Override
    public @Nullable ICommandNode<T> getParent() {
        return parent;
    }

    @Override
    public @NotNull ICommandFramework<T> getCommandFramework() {
        return framework;
    }

    @Override
    public @Nullable String getDescription() {
        return description;
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public void invokeCommand(
            @NotNull T sender,
            @NotNull LinkedList<@NotNull String> arguments,
            @Unmodifiable @NotNull List<@NotNull String> sourceArguments
    ) throws InterruptCommand {
        if (parent == null) {
            if (!framework.hasPermission(sender, permission)) {
                framework.sendTranslate(sender, "kframe.command.permission-denied");
                return;
            }
        }
        String first = arguments.pollFirst();
        if (first == null || first.equals("?")) {
            FormatAction translate = framework.getTranslate("kframe.command.help.line");
            framework.sendTranslate(sender, "kframe.command.help.header");
            if (first == null) {
                framework.sendTranslate(sender, "kframe.command.help.command-line", String.join(" ", sourceArguments));
            } else {
                framework.sendTranslate(sender, "kframe.command.help.command-line", String.join(" ",
                        sourceArguments.subList(0, sourceArguments.size() - arguments.size() - 1)
                ));
            }
            String next = arguments.pollFirst();
            boolean doPages = true;
            int pages = 1;
            if (next != null) {
                try {
                    pages = Integer.parseInt(next);
                } catch (Throwable ignored) {
                    doPages = false;
                }
            }
            if (doPages) {
                final List<Map.Entry<String, ICommandNode<T>>> list = ListUtils.getOrNull(sortedNames, pages - 1);
                if (list != null) {
                    for (Map.Entry<String, ICommandNode<T>> entry : list) {
                        framework.sendTranslate(
                                sender,
                                translate,
                                entry.getKey(),
                                entry.getValue().getDescription()
                        );
                    }
                }
                framework.sendTranslate(sender, "kframe.command.help.footer",
                        String.valueOf(pages),
                        String.valueOf(sortedNames.size()));
            } else {
                ICommandNode<T> commandNode = subNodes.get(next = next.toLowerCase());
                if (commandNode == null) commandNode = subAlias.get(next);
                if (commandNode == null) {
                    framework.sendTranslate(sender, "kframe.command.help.no-command", next);
                } else {
                    framework.sendTranslate(sender,
                            translate,
                            commandNode.getName(),
                            commandNode.getDescription()
                    );
                }
                framework.sendTranslate(sender, "kframe.command.help.footer", "1", "1");
            }
            throw InterruptCommand.INSTANCE;
        }
        String token = first.toLowerCase();
        ICommandNode<T> command = subNodes.get(token);
        if (command == null) command = subAlias.get(token);
        if (command == null) {
            String path;
            String prev;
            if (arguments.isEmpty()) {
                path = String.join(" ", sourceArguments);
                prev = String.join(" ", sourceArguments.subList(0, sourceArguments.size() - 1));
            } else {
                path = String.join(" ", sourceArguments.subList(0, sourceArguments.size() - arguments.size()));
                prev = String.join(" ", sourceArguments.subList(0, sourceArguments.size() - arguments.size() - 1));
            }
            if (distances == null) {
                framework.sendTranslate(
                        sender,
                        "kframe.command.unknown-command",
                        path
                );
            } else {
                final List<String> list = distances.computeIfAbsent(token, distancesFinder);
                FormatAction meatLine = framework.getTranslate("kframe.command.unknown-command-meat.line");
                framework.sendTranslate(sender, "kframe.command.unknown-command-meat.no-command", path);
                for (String like : list) {
                    framework.sendTranslate(sender, meatLine, prev, like);
                }
            }
            return;
        }
        if (!framework.hasPermission(sender, command.getPermission())) {
            framework.sendTranslate(sender, "kframe.command.permission-denied");
            return;
        }
        command.invokeCommand(sender, arguments, sourceArguments);
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public void tabCompile(
            @NotNull T sender,
            @NotNull LinkedList<@NotNull String> result,
            @NotNull LinkedList<@NotNull String> arguments,
            @NotNull @Unmodifiable List<@NotNull String> sourceArguments) {
        if (arguments.size() < 2) {
            result.add("?");
            for (Map.Entry<String, ICommandNode<T>> node : subNodes.entrySet()) {
                if (framework.hasPermission(sender, node.getValue().getPermission())) {
                    result.add(node.getKey());
                }
            }
        } else {
            String token = arguments.pollFirst().toLowerCase();
            ICommandNode<T> commandNode = subNodes.get(token);
            if (commandNode == null) commandNode = subAlias.get(token);
            if (commandNode != null)
                commandNode.tabCompile(sender, result, arguments, sourceArguments);
        }
        if (parent == null) { // ROOT
            result.sort(String::compareToIgnoreCase);
            String last = ListUtils.getOrNull(sourceArguments, sourceArguments.size() - 1);
            if (last != null) {
                String lastLow = last.toLowerCase();
                result.removeIf(it -> !it.toLowerCase().startsWith(lastLow));
            }
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull ICommandNode<T> registerSubCommand(
            @NotNull ICommandNode<T> node,
            @Nullable String[] alias
    ) {
        node.setParent(this); // Unsafe Action
        subNodes.put(node.getName().toLowerCase(), node);
        if (alias != null) {
            for (String s : alias) {
                if (s != null) {
                    subAlias.put(s.toLowerCase(), node);
                }
            }
        }
        refreshNodes();
        return this;
    }

    private List<String> findDistance(String name) {
        HashSet<String> all = new HashSet<>(subNodes.keySet());
        all.addAll(subAlias.keySet());
        int distance = Integer.MAX_VALUE;
        List<String> list = new ArrayList<>(all.size() / 2);
        for (String word : all) {
            int d = StringUtils.compare(name, word);
            if (d == distance) {
                list.add(word);
            } else if (d < distance) {
                distance = d;
                list.clear();
                list.add(word);
            }
        }
        return list;
    }
}
