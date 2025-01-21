package com.erebos.flu.utils;

import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Utility class providing helper methods for Set operations.
 * This class contains methods for manipulating and querying Set objects.
 */
public class SetUtils {

    /**
     * Private constructor to prevent instantiation of utility class.
     *
     * @throws IllegalStateException when called
     */
    private SetUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Extracts members from a Set using a getter function and collects them into a new Set.
     *
     * @param set the source set
     * @param getter the function to extract members
     * @param <T> the type of elements in the source set
     * @param <U> the type of elements in the resulting set
     * @return a Set containing the extracted members
     */
    public static <T, U> Set<U> extractMembersAsSet(Set<T> set, final Function<T, U> getter) {
        return getNullableSet(set).stream().map(getter).collect(Collectors.toSet());
    }

    /**
     * Returns a set if it is not null, otherwise returns an empty set.
     *
     * @param set the set to check
     * @param <T> the type of elements in the set
     * @return the set if it is not null, otherwise an empty set
     */
    public static <T> Set<T> getNullableSet(final Set<T> set) {
        return Optional.ofNullable(set).orElseGet(Set::of);
    }
}