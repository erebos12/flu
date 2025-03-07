package com.erebos.flu.utils;

import java.util.*;
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
     * @param obj  the source object
     * @param func the function to extract the string
     * @param <T>  the type of the source object
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

    /**
     * Reverses the given string.
     * <p>
     * This method takes an input string and returns a new string with the characters
     * in reverse order. If the input is null, it returns null.
     *
     * @param input the string to reverse
     * @return the reversed string, or null if the input is null
     */
    public static String reverseString(String input) {
        if (input == null) {
            return null;
        }
        int first = 0;
        int last = input.length() - 1;
        char[] c = input.toCharArray();
        while (first < last) {
            char temp = c[first];
            c[first] = c[last];
            c[last] = temp;
            first++;
            last--;
        }
        return new String(c);
    }

    /**
     * Determines if a string has all unique characters using a brute-force approach.
     * <p>
     * This method compares every character in the string with every other character.
     * If a duplicate character is found, it returns false; otherwise, it returns true.
     * <p>
     * Time Complexity:
     * - Worst-case: O(n²) due to the nested loop checking all pairs of characters.
     * - Best-case: O(1) if an early duplicate is found.
     * - Space Complexity: O(1) (no additional data structures used).
     *
     * @param input the input string to check
     * @return true if all characters in the string are unique, false otherwise
     */
    public static boolean hasUniqueChars(String input) {
        if (input == null) {
            return false;
        }
        for (int i = 0; i < input.length(); i++) {
            for (int j = i + 1; j < input.length(); j++) {
                if (input.charAt(i) == input.charAt(j)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Determines if a string has all unique characters using a HashSet.
     * <p>
     * This method iterates through the string and stores each character in a HashSet.
     * If a duplicate character is found (i.e., already in the HashSet), it returns false.
     * Otherwise, it returns true.
     * <p>
     * Time Complexity:
     * - Average-case: O(n) because HashSet operations (add and contains) run in O(1) time.
     * - Worst-case: O(n) in case of hash collisions, but still significantly faster than O(n²).
     * - Space Complexity: O(n) due to storing up to n characters in the HashSet.
     *
     * @param input the input string to check
     * @return true if all characters in the string are unique, false otherwise
     */
    public static boolean hasUniqueChars2(String input) {
        if (input == null) {
            return false;
        }
        Set<Character> seen = new HashSet<>();
        for (char c : input.toCharArray()) {
            if (!seen.add(c)) {  // HashSet returns false if c is already in the set
                return false;
            }
        }
        return true;
    }
}