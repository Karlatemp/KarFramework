package io.github.karlatemp.karframework;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicReference;

public class KarFramework {
    private static final AtomicReference<IKarFramework> INSTANCE = new AtomicReference<>();

    public static void setInstance(IKarFramework framework) {
        if (!INSTANCE.compareAndSet(null, framework)) {
            throw new IllegalStateException("Framework initialized.");
        }
    }

    @Contract(pure = true)
    @NotNull
    public static IKarFramework getInstance() {
        final IKarFramework framework = INSTANCE.get();
        if (framework == null) throw new IllegalStateException("Framework not initialized");
        return framework;
    }
}
