package io.github.karlatemp.karframework.command;

/**
 * Interrupt command execution.
 *
 * <pre>{@code throw InterruptCommand.INSTANCE;}</pre>
 */
public class InterruptCommand extends RuntimeException {
    private InterruptCommand() {
        super(null, null, false, false);
    }

    public static final InterruptCommand INSTANCE = new InterruptCommand();
}
