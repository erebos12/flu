package com.erebos.flu.utils;

import com.google.common.collect.ImmutableList;

import java.util.function.ToDoubleFunction;
import java.util.stream.Stream;

/**
 * Utility class providing helper methods for mathematical operations.
 * This class contains methods for common mathematical calculations and transformations.
 */
public class MathUtils {

    /**
     * Private constructor to prevent instantiation of utility class.
     *
     * @throws IllegalStateException when called
     */
    private MathUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Rounds a double value to 2 decimal places.
     *
     * @param value the value to round
     * @return the rounded value
     */
    public static double roundValue(final double value) {
        return roundValue(value, 2);
    }

    /**
     * Rounds a double value to a specified number of decimal places.
     *
     * @param value the value to round
     * @param scale the number of decimal places
     * @return the rounded value
     */
    public static double roundValue(final double value, final int scale) {
        final double s = Math.pow(10, scale);
        return Math.round(value * s) / s;
    }

    /**
     * Calculates what percentage value is of total.
     * Returns 0 if total is 0.
     *
     * @param total the total value
     * @param value the value to calculate percentage of
     * @return the percentage value
     */
    public static double calculatePercentageValue(final double total, final double value) {
        return total != 0.0 ? (100 * value / total) : 0.0;
    }

    /**
     * Calculates the average of values in a list using a provided function to extract double values.
     *
     * @param list the list of values
     * @param doubleGetMethod the function to extract double values
     * @param <T> the type of elements in the list
     * @return the average value, or Double.NaN if the list is empty
     */
    public static <T> Double calculateAverage(final ImmutableList<T> list,
                                              final ToDoubleFunction<? super T> doubleGetMethod) {
        return ListUtils.getNullableList(list).stream()
                .mapToDouble(doubleGetMethod)
                .average()
                .orElse(Double.NaN);
    }

    /**
     * Calculates the average of two double values.
     *
     * @param value1 the first value
     * @param value2 the second value
     * @return the average value
     */
    public static double calculateAverage(final double value1, final double value2) {
        return (value1 + value2) / 2.0;
    }

    /**
     * Calculates a value from a percentage of a total.
     *
     * @param total the total value
     * @param percentage the percentage to calculate
     * @return the calculated value
     */
    public static double calculateValueFromPercentage(final double total, final double percentage) {
        return total * percentage / 100.0;
    }

    /**
     * Negates a double value. Returns 0 if the input is 0.
     *
     * @param value the value to negate
     * @return the negated value
     */
    public static double negate(final double value) {
        return value != 0.0 ? (-1 * value) : 0.0;
    }

    /**
     * Calculates the sum of a stream of Double values.
     *
     * @param doubleStream the stream of values to sum
     * @return the sum of all values
     */
    public static Double calculateSum(final Stream<Double> doubleStream) {
        return doubleStream.reduce(0.0, Double::sum);
    }

    /**
     * Checks if a double value is not zero.
     *
     * @param value the value to check
     * @return true if the value is not zero
     */
    public static boolean isNotZero(final double value) {
        return value != 0.0;
    }

    /**
     * Adds a percentage of a value to a base value.
     *
     * @param base the base value
     * @param value the value to calculate percentage from
     * @param percentage the percentage to add
     * @return the result of adding the percentage
     */
    public static double addValueByPercentage(final double base, final double value, final double percentage) {
        return base + calculateValueFromPercentage(value, percentage);
    }

    /**
     * Returns a Double value if it is not null, otherwise returns Double.NaN.
     *
     * @param value the value to check
     * @return the value or Double.NaN if null
     */
    public static Double getNullableDouble(Double value) {
        return (value != null ? value : Double.NaN);
    }

    /**
     * Returns a Double value if it is not null, otherwise returns 0.0.
     *
     * @param value the value to check
     * @return the value or 0.0 if null
     */
    public static Double getNullableDoubleElseZero(Double value) {
        return (value != null ? value : 0.0);
    }

    /**
     * Calculates a compounded percentage over multiple periods.
     *
     * @param percentage the percentage per period
     * @param exponent the number of periods
     * @return the compounded percentage
     */
    public static double calculateCompoundedPercentage(final double percentage, final double exponent) {
        return 100 * (Math.pow(1 + percentage / 100.0, exponent) - 1);
    }

    /**
     * Checks if a number is even using bit operations.
     * Only works for non-negative numbers.
     *
     * @param n the number to check
     * @return true if the number is even
     */
    public static boolean isEven(int n) {
        return n >= 0 && (n & (n - 1)) == 0;
    }
}
