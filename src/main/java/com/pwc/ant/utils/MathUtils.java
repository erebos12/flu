package com.erebos.ant.utils;

import com.google.common.collect.ImmutableList;

import java.util.function.ToDoubleFunction;
import java.util.stream.Stream;

import static com.erebos.ant.utils.ListUtils.getNullableList;

public class MathUtils {

    private MathUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static double roundValue(final double value) {
        return roundValue(value, 2);
    }

    public static double roundValue(final double value, final int scale) {
        final double s = Math.pow(10, scale);
        return Math.round(value * s) / s;
    }

    public static double calculatePercentageValue(final double total, final double value) {
        return total != 0.0 ? (100 * value / total) : 0.0;
    }

    public static <T> Double calculateAverage(final ImmutableList<T> list,
                                              final ToDoubleFunction<? super T> doubleGetMethod) {
        return getNullableList(list).stream()
                .mapToDouble(doubleGetMethod)
                .average()
                .orElse(Double.NaN);
    }

    public static double calculateAverage(final double value1, final double value2) {
        return (value1 + value2) / 2.0;
    }

    public static double calculateValueFromPercentage(final double total, final double percentage) {
        return total * percentage / 100.0;
    }

    public static double negate(final double value) {
        return value != 0.0 ? (-1 * value) : 0.0;
    }

    public static Double calculateSum(final Stream<Double> doubleStream) {
        return doubleStream.reduce(0.0, Double::sum);
    }

    public static boolean isNotZero(final double value) {
        return value != 0.0;
    }

    public static double addValueByPercentage(final double base, final double value, final double percentage) {
        return base + calculateValueFromPercentage(value, percentage);
    }

    public static Double getNullableDouble(Double value) {
        return (value != null ? value : Double.NaN);
    }

    public static Double getNullableDoubleElseZero(Double value) {
        return (value != null ? value : 0.0);
    }

    public static double calculateCompoundedPercentage(final double percentage, final double exponent) {
        return 100 * (Math.pow(1 + percentage / 100.0, exponent) - 1);
    }
}

