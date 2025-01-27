package com.erebos.flu.utils;

import java.util.Optional;
import java.util.function.Consumer;

public class LambdaUtils {
    public static <T> void ifAvailableOrElse(final T value, final Consumer<T> ifCase, final Runnable elseCase) {
        Optional.ofNullable(value)
                .ifPresentOrElse(ifCase, elseCase);
    }
}




