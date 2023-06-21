package com.erebos.ant.utils;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.erebos.ant.utils.StringUtils.isStringNullOrEmpty;

public final class GrouperUtils {

    private GrouperUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static <T> Map<String, List<T>> groupByStringMember(final List<T> list, final Function<T, String> func) {
        return list.parallelStream()
                .collect(Collectors.groupingBy(trx -> isStringNullOrEmpty(func.apply(trx)) ? "NA" : func.apply(trx)));
    }

    public static <T> Map<Enum, List<T>> groupByEnumMember(final List<T> list, final Function<T, Enum> func) {
        return list.stream().collect(Collectors.groupingBy(func));
    }
}
