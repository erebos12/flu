package com.erebos.flu.utils;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.concurrent.atomic.AtomicReference;

public class LambdaUtils {
    public static <T> void ifAvailableOrElse(final T value, final Consumer<T> ifCase, final Runnable elseCase) {
        Optional.ofNullable(value)
                .ifPresentOrElse(ifCase, elseCase);
    }

    /**
     * Executes one of two cases based on whether a value is available, allowing mutation of a value in both cases.
     *
     * @param value     the value to check for availability
     * @param ifCase    the action to execute if the value is available, can mutate the holder
     * @param elseCase  the action to execute if the value is not available, can mutate the holder
     * @param <T>       the type of the value
     * @return         the potentially modified value
     */
    public static <T> T ifAvailableOrElseMutable(final T value,
                                                final Consumer<AtomicReference<T>> ifCase,
                                                final Consumer<AtomicReference<T>> elseCase) {
        AtomicReference<T> holder = new AtomicReference<>(value);
        if (value != null) {
            ifCase.accept(holder);
        } else {
            elseCase.accept(holder);
        }
        return holder.get();
    }
}
