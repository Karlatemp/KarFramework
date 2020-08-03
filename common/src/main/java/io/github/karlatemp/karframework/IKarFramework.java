package io.github.karlatemp.karframework;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IKarFramework {
    @Nullable IPluginProvider provide(@NotNull Object plugin);
}
