package com.erebos.flu.utils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.function.Function;

import static com.erebos.flu.utils.ListUtils.getNullableList;
import static java.util.Objects.isNull;

/**
 * Utility class for string operations.
 */
public class StringUtils {

    /**
     * Constant for slash delimiter.
     */
    public static final String SLASH_DELIMITER = "/";

    /**
     * Constant for empty string.
     */
    public static final String EMPTY_STRING = "";

    /**
     * Private constructor to prevent instantiation of utility class.
     *
     * @throws IllegalStateException when called
     */
    private StringUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Checks if a string is null or empty (contains only whitespace).
     *
     * @param value the string to check
     * @return true if the string is null or empty, false otherwise
     */
    public static boolean isStringNullOrEmpty(final String value) {
        return isNull(value) || value.trim().isEmpty();
    }

    /**
     * Safely extracts a string member from an object using a provided function.
     * If the object is null or the function returns null, returns an empty string.
     *
     * @param obj the source object
     * @param func the function to extract the string
     * @param <T> the type of the source object
     * @return the extracted string or empty string if null
     */
    public static <T> String getStringMemberFromObj(final T obj, final Function<T, String> func) {
        return Optional.ofNullable(obj).isPresent()
                ? Optional.ofNullable(func.apply(obj)).orElseGet(String::new)
                : "";
    }

    /**
     * Checks if two strings are both non-null and equal.
     *
     * @param string1 the first string to compare
     * @param string2 the second string to compare
     * @return true if both strings are non-null and equal, false otherwise
     */
    public static boolean stringsNotNullAndEqual(final String string1, final String string2) {
        if (isNull(string1) || isNull(string2)) {
            return false;
        }
        return string1.equals(string2);
    }

    /**
     * Returns the input string if it is not null or empty, otherwise returns an empty string.
     *
     * @param s the input string
     * @return the input string or empty string if null or empty
     */
    public static String getNullableString(final String s) {
        return isStringNullOrEmpty(s) ? "" : s;
    }

    /**
     * Returns the input string if it is not null or empty, otherwise returns "NA".
     *
     * @param s the input string
     * @return the input string or "NA" if null or empty
     */
    public static String getNullableStringWithNA(final String s) {
        return isStringNullOrEmpty(s) ? "NA" : s;
    }

    /**
     * Concatenates two strings with a delimiter (i.e. backslash) between them if the second string is not empty or null.
     *
     * @param s1        the first string
     * @param s2        the second string
     * @param delimiter the delimiter to use
     * @return the concatenated string
     * @deprecated
     */
    @Deprecated
    public static String concatStringsWithDelimiter(String s1, String s2, String delimiter) {
        StringBuilder sb = new StringBuilder((!isStringNullOrEmpty(s1) ? s1 : EMPTY_STRING));
        sb.append((!isStringNullOrEmpty(s2) ? delimiter + s2 : EMPTY_STRING));
        return sb.toString();
    }

    /**
     * Concatenates all strings in a list with a delimiter (i.e. backslash).
     *
     * @param strings   the list of strings to concatenate
     * @param delimiter the delimiter to use
     * @return the concatenated string
     */
    public static String concatStringsWithDelimiter(final List<String> strings, final String delimiter) {
        StringJoiner joiner = new StringJoiner(delimiter);
        getNullableList(strings).stream().filter(Objects::nonNull).forEach(joiner::add);
        return joiner.toString();
    }

    /**
     * Checks if a string parameter is set correctly.
     *
     * @param parameterValue the string parameter value to check
     * @param parameterName  the name of the parameter
     * @param aClass         the class that called this method
     * @throws IllegalStateException if the parameter is not set
     */
    public static void validateStringParameter(final String parameterValue, final String parameterName, final Class aClass) {
        if (isStringNullOrEmpty(parameterValue)) {
            throw new IllegalStateException("Mandatory parameter '" + parameterName + "' not set in " + aClass.getSimpleName());
        }
    }
}