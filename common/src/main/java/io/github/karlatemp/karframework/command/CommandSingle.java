package io.github.karlatemp.karframework.command;

import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.LinkedList;
import java.util.List;

public class CommandSingle<T> implements ICommandNode<T> {
    private final String name;
    private final String permission;
    private final String description;
    private final ICommandFramework<T> framework;
    private final ICommandExecutor<T> executor;
    private final ITabCompiler<T> tabCompiler;
    private ICommandNode<T> parent;

    CommandSingle(
            String name,
            String permission,
            String description,
            ICommandFramework<T> framework,
            ICommandExecutor<T> executor,
            ITabCompiler<T> tabCompiler
    ) {
        this.name = name;
        this.permission = permission;
        this.description = description;
        this.framework = framework;
        this.executor = executor;
        if (tabCompiler == null) {
            if (executor instanceof ITabCompiler) {
                //noinspection unchecked
                tabCompiler = (ITabCompiler<T>) executor;
            }
        }
        this.tabCompiler = tabCompiler;
    }

    public static Builder<?> builder() {
        return new Builder<>();
    }

    public static class Builder<T> {
        private String name, permission, description;
        private ICommandFramework<T> framework;
        private ICommandExecutor<T> executor;
        private ITabCompiler<T> tabCompiler;

        public Builder<T> setName(String name) {
            this.name = name;
            return this;
        }

        public Builder<T> setPermission(String permission) {
            this.permission = permission;
            return this;
        }

        public Builder<T> setDescription(String description) {
            this.description = description;
            return this;
        }

        @SuppressWarnings("unchecked")
        public <T2> Builder<T2> setFramework(ICommandFramework<T2> framework) {
            Builder<T2> b = (Builder<T2>) this;
            b.framework = framework;
            return b;
        }

        public Builder<T> setExecutor(ICommandExecutor<T> executor) {
            this.executor = executor;
            return this;
        }

        public Builder<T> setTabCompiler(ITabCompiler<T> tabCompiler) {
            this.tabCompiler = tabCompiler;
            return this;
        }

        public CommandSingle<T> build() {
            Validate.isTrue(name != null, "`name` cannot be null.");
            Validate.isTrue(framework != null, "`framework` cannot be null.");
            Validate.isTrue(executor != null, "`executor` cannot be null.");
            return new CommandSingle<>(name, permission, description, framework, executor, tabCompiler);
        }
    }

    @Override
    public @Nullable ICommandNode<T> getSubNode(@NotNull String name) {
        return null;
    }

    @Override
    public @Nullable String getPermission() {
        return permission;
    }

    @Override
    public void tabCompile(@NotNull T sender, @NotNull LinkedList<@NotNull String> result, @NotNull LinkedList<@NotNull String> arguments, @NotNull @Unmodifiable List<@NotNull String> sourceArguments) {
        if (tabCompiler != null)
            tabCompiler.tabCompile(sender, result, arguments, sourceArguments);
    }

    @Override
    public @Nullable ICommandNode<T> getParent() {
        return parent;
    }

    @Override
    public void setParent(@NotNull ICommandNode<T> parent) {
        this.parent = parent;
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
    public void invokeCommand(@NotNull T sender, @NotNull LinkedList<@NotNull String> arguments, @Unmodifiable @NotNull List<@NotNull String> sourceArguments) throws InterruptCommand {
        executor.invokeCommand(sender, arguments, sourceArguments);
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull ICommandNode<T> registerSubCommand(@NotNull ICommandNode<T> node, @Nullable String[] alias) {
        return this;
    }
}
