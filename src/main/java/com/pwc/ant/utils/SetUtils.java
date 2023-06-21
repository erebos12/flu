package com.erebos.ant.utils;

import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;


public class SetUtils {

    private SetUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static <T, U> Set<U> extractMembersAsSet(Set<T> set, final Function<T, U> getter) {
        return getNullableSet(set).stream().map(getter).collect(Collectors.toSet());
    }

    public static <T> Set<T> getNullableSet(final Set<T> set) {
        return Optional.ofNullable(set).orElseGet(Set::of);
    }
}