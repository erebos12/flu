package com.erebos.flu.utils;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Utility class providing helper methods for grouping collections.
 * This class contains methods for grouping lists by various criteria.
 */
public final class GrouperUtils {

    /**
     * Private constructor to prevent instantiation of utility class.
     *
     * @throws IllegalStateException when called
     */
    private GrouperUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Groups a list of objects by a string member extracted using the provided function.
     * If the extracted string is null or empty, the group key will be "NA".
     *
     * @param list the list to group
     * @param func the function to extract the string member
     * @param <T> the type of elements in the list
     * @return a map where keys are strings and values are lists of objects
     */
    public static <T> Map<String, List<T>> groupByStringMember(final List<T> list, final Function<T, String> func) {
        return list.parallelStream()
                .collect(Collectors.groupingBy(trx -> StringUtils.isStringNullOrEmpty(func.apply(trx)) ? "NA" : func.apply(trx)));
    }

    /**
     * Groups a list of objects by an enum member extracted using the provided function.
     *
     * @param list the list to group
     * @param func the function to extract the enum member
     * @param <T> the type of elements in the list
     * @return a map where keys are enums and values are lists of objects
     */
    public static <T> Map<Enum, List<T>> groupByEnumMember(final List<T> list, final Function<T, Enum> func) {
        return list.stream().collect(Collectors.groupingBy(func));
    }
}
