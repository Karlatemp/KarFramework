/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/18 23:35:16
 *
 * kar-framework/kar-framework.common.main/IServiceRegister.java
 */

package io.github.karlatemp.karframework.services;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IServiceRegister {
    enum ResultType {
        COMPILED, COMPILED_AND_CONTINUE, SKIPPED, FAILED
    }

    class Result {
        private final ResultType type;
        private final Throwable cause;

        public Result(@NotNull ResultType type) {
            this(type, null);
        }

        public Result(@NotNull ResultType type, @Nullable Throwable cause) {
            this.type = type;
            this.cause = cause;
        }

        public @NotNull ResultType getType() {
            return type;
        }

        public @Nullable Throwable getCause() {
            return cause;
        }
    }

    @NotNull ResultType register(@NotNull Object service) throws Throwable;

    static Result register(
            @NotNull Object service,
            @NotNull Iterable<@NotNull IServiceRegister> registers
    ) {
        boolean compiled = false;
        for (IServiceRegister register : registers) {
            try {
                ResultType type = register.register(service);
                switch (type) {
                    case COMPILED:
                    case FAILED:
                        return new Result(type);
                    case COMPILED_AND_CONTINUE: {
                        compiled = true;
                        break;
                    }
                }
            } catch (Throwable any) {
                return new Result(ResultType.FAILED, any);
            }
        }
        return new Result(compiled ? ResultType.COMPILED : ResultType.SKIPPED);
    }
}
