package com.erebos.flu.utils;

import java.util.Optional;

/**
 * Utility class providing helper methods for Boolean operations.
 * This class contains methods for handling Boolean values safely.
 */
public class BooleanUtils {

    /**
     * Private constructor to prevent instantiation of utility class.
     *
     * @throws IllegalStateException when called
     */
    private BooleanUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Returns a boolean value if it is not null, otherwise returns false.
     *
     * @param value the Boolean value to check
     * @return the boolean value if it is not null, otherwise false
     */
    public static boolean getNullableBoolean(final Boolean value) {
        return Optional.ofNullable(value).orElse(false);
    }
}
