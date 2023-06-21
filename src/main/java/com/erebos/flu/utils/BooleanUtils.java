package com.erebos.flu.utils;

import java.util.Optional;

public class BooleanUtils {

    private BooleanUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static boolean getNullableBoolean(final Boolean value) {
        return Optional.ofNullable(value).orElse(false);
    }
}
