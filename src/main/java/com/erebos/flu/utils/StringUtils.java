package com.erebos.flu.utils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.function.Function;

import static com.erebos.flu.utils.ListUtils.getNullableList;
import static java.util.Objects.isNull;

public class StringUtils {

    public static final String SLASH_DELIMITER = "/";
    public static final String EMPTY_STRING = "";

    private StringUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static boolean isStringNullOrEmpty(final String value) {
        return isNull(value) || value.trim().isEmpty();
    }

    public static <T> String getStringMemberFromObj(final T obj, final Function<T, String> func) {
        return Optional.ofNullable(obj).isPresent()
                ? Optional.ofNullable(func.apply(obj)).orElseGet(String::new)
                : "";
    }

    public static boolean stringsNotNullAndEqual(final String string1, final String string2) {
        if (isNull(string1) || isNull(string2)) {
            return false;
        }
        return string1.equals(string2);
    }

    public static String getNullableString(final String s) {
        return isStringNullOrEmpty(s) ? "" : s;
    }

    public static String getNullableStringWithNA(final String s) {
        return isStringNullOrEmpty(s) ? "NA" : s;
    }

    @Deprecated
    /**
     * Concatenate two strings with delimiter (i.e. backslash) between s1 and s2 if second string is not empty or null
     * <p></p>
     *
     * @param s1        String one
     * @param s2        String two
     * @param delimiter String delimiter
     * @return s1($delimiter)s2 (if s2 is not empty or null) else s1
     */
    public static String concatStringsWithDelimiter(String s1, String s2, String delimiter) {
        StringBuilder sb = new StringBuilder((!isStringNullOrEmpty(s1) ? s1 : EMPTY_STRING));
        sb.append((!isStringNullOrEmpty(s2) ? delimiter + s2 : EMPTY_STRING));
        return sb.toString();
    }

    /**
     * Concatenate all strings in a list with delimiter (i.e. backslash)
     * <p></p>
     *
     * @param strings   List of Strings [s1, s2,...sN]
     * @param delimiter String delimiter
     * @return s1($delimiter)s2($delimiter)...($delimiter)sN
     */
    public static String concatStringsWithDelimiter(final List<String> strings, final String delimiter) {
        StringJoiner joiner = new StringJoiner(delimiter);
        getNullableList(strings).stream().filter(Objects::nonNull).forEach(joiner::add);
        return joiner.toString();
    }

    /**
     * Check if string field is set correctly.
     * <p></p>
     *
     * @param parameterValue string parameter value to check
     * @param parameterName  parameter name for error message
     * @param aClass         caller class for error message
     * @throws IllegalStateException
     */
    public static void validateStringParameter(final String parameterValue, final String parameterName, final Class aClass) {
        if (isStringNullOrEmpty(parameterValue)) {
            throw new IllegalStateException("Mandatory parameter '" + parameterName + "' not set in " + aClass.getSimpleName());
        }
    }
}