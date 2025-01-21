package com.erebos.flu.utils;

import com.erebos.flu.exceptions.DateParsingException;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;

/**
 * Utility class providing helper methods for date operations.
 * This class contains methods for parsing, formatting, and converting dates between different formats.
 */
public final class DateUtilities {
    /** Regular expression for validating English date format (yyyy-MM-dd) */
    public static final String REG_EX_DATE_EN = "^(19|20)\\d\\d[-](0[1-9]|1[012]|[1-9])[-](0[1-9]|[12][0-9]|3[01]|[1-9])$";
    /** Regular expression for validating German date format (dd.MM.yyyy) */
    public static final String REG_EX_DATE_DE = "^(0[1-9]|[12][0-9]|3[01]|[1-9])[.](0[1-9]|1[012]|[1-9])[.](19|20)\\d\\d$";
    /** Pattern for German date format */
    public static final String GERMAN_DATE_PATTERN = "dd.MM.yyyy";
    /** Pattern for English date format */
    public static final String ENGLISH_DATE_PATTERN = "yyyy-MM-dd";

    /**
     * Private constructor to prevent instantiation of utility class.
     *
     * @throws IllegalStateException when called
     */
    private DateUtilities() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Determines the date pattern of a given date string.
     *
     * @param dateString the date string to analyze
     * @return the matching date pattern
     * @throws DateParsingException if the date format doesn't match any known pattern
     * @throws NullPointerException if dateString is null
     */
    static String getDatePattern(final String dateString) throws DateParsingException {
        requireNonNull(dateString, "dateString can't be null");
        if (dateString.matches(REG_EX_DATE_EN)) {
            return ENGLISH_DATE_PATTERN;
        } else if (dateString.matches(REG_EX_DATE_DE)) {
            return GERMAN_DATE_PATTERN;
        }
        throw new DateParsingException("Date format does not match requirements");
    }

    /**
     * Converts a string to LocalDate using automatic pattern detection.
     *
     * @param value the date string to convert
     * @return the parsed LocalDate
     * @throws DateParsingException if the date cannot be parsed
     * @throws NullPointerException if value is null
     */
    public static LocalDate stringToLocalDate(final String value) throws DateParsingException {
        requireNonNull(value, "value can't be null");
        final String pattern = getDatePattern(value);
        return stringToLocalDate(value, pattern);
    }

    /**
     * Converts a string to LocalDate using a specified pattern.
     *
     * @param value the date string to convert
     * @param pattern the date pattern to use
     * @return the parsed LocalDate
     * @throws DateParsingException if the date cannot be parsed
     * @throws NullPointerException if value or pattern is null
     */
    public static LocalDate stringToLocalDate(final String value, final String pattern) throws DateParsingException {
        requireNonNull(value, "value can't be null");
        requireNonNull(pattern, "pattern can't be null");
        try {
            return LocalDate.parse(value, DateTimeFormatter.ofPattern(pattern));
        } catch (final Exception ex) {
            throw new DateParsingException(ex.getMessage());
        }
    }

    /**
     * Formats a LocalDate to string using the English date pattern.
     *
     * @param date the date to format
     * @return the formatted date string
     * @throws DateParsingException if the date cannot be formatted
     * @throws NullPointerException if date is null
     */
    public static String formatToString(final LocalDate date) throws DateParsingException {
        requireNonNull(date, "date can't be null");
        try {
            return date.format(DateTimeFormatter.ofPattern(ENGLISH_DATE_PATTERN));
        } catch (final Exception ex) {
            throw new DateParsingException(ex.getMessage());
        }
    }

    /**
     * Converts a java.util.Date to LocalDate.
     *
     * @param date the Date to convert
     * @return the converted LocalDate
     * @throws NullPointerException if date is null
     */
    public static LocalDate convertToLocalDate(final Date date) {
        requireNonNull(date, "date can't be null");
        return LocalDate.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    /**
     * Converts a LocalDate to java.util.Date.
     *
     * @param localDate the LocalDate to convert
     * @return the converted Date
     * @throws NullPointerException if localDate is null
     */
    public static Date convertToDate(final LocalDate localDate) {
        requireNonNull(localDate, "localDate can't be null");
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Gets the year from a LocalDate. Returns 0 if the date is null.
     *
     * @param localDate the date to get the year from
     * @return the year, or 0 if the date is null
     */
    public static int getYear(final LocalDate localDate) {
        if(isNull(localDate)){
            return 0;
        }
        return localDate.getYear();
    }

    /**
     * Gets the month index (1-12) from a LocalDate.
     *
     * @param localDate the date to get the month from
     * @return the month index (1-12)
     * @throws NullPointerException if localDate is null
     */
    public static int getMonthIndex(final LocalDate localDate) {
        requireNonNull(localDate, "localDate can't be null");
        return localDate.getMonthValue();
    }
}
