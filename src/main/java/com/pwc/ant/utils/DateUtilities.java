package com.erebos.ant.utils;

import com.erebos.ant.exceptions.DateParsingException;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;

public final class DateUtilities {
    public static final String REG_EX_DATE_EN = "^(19|20)\\d\\d[-](0[1-9]|1[012]|[1-9])[-](0[1-9]|[12][0-9]|3[01]|[1-9])$";
    public static final String REG_EX_DATE_DE = "^(0[1-9]|[12][0-9]|3[01]|[1-9])[.](0[1-9]|1[012]|[1-9])[.](19|20)\\d\\d$";
    public static final String GERMAN_DATE_PATTERN = "dd.MM.yyyy";
    public static final String ENGLISH_DATE_PATTERN = "yyyy-MM-dd";

    private DateUtilities() {
        throw new IllegalStateException("Utility class");
    }

    static String getDatePattern(final String dateString) throws DateParsingException {
        requireNonNull(dateString, "dateString can't be null");
        if (dateString.matches(REG_EX_DATE_EN)) {
            return ENGLISH_DATE_PATTERN;
        } else if (dateString.matches(REG_EX_DATE_DE)) {
            return GERMAN_DATE_PATTERN;
        }
        throw new DateParsingException("Date format does not match requirements");
    }

    public static LocalDate stringToLocalDate(final String value) throws DateParsingException {
        requireNonNull(value, "value can't be null");
        final String pattern = getDatePattern(value);
        return stringToLocalDate(value, pattern);
    }

    public static LocalDate stringToLocalDate(final String value, final String pattern) throws DateParsingException {
        requireNonNull(value, "value can't be null");
        requireNonNull(pattern, "pattern can't be null");
        try {
            return LocalDate.parse(value, DateTimeFormatter.ofPattern(pattern));
        } catch (final Exception ex) {
            throw new DateParsingException(ex.getMessage());
        }
    }

    public static String formatToString(final LocalDate date) throws DateParsingException {
        requireNonNull(date, "date can't be null");
        try {
            return date.format(DateTimeFormatter.ofPattern(ENGLISH_DATE_PATTERN));
        } catch (final Exception ex) {
            throw new DateParsingException(ex.getMessage());
        }
    }

    public static LocalDate convertToLocalDate(final Date date) {
        requireNonNull(date, "date can't be null");
        return LocalDate.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    public static Date convertToDate(final LocalDate localDate) {
        requireNonNull(localDate, "localDate can't be null");
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    public static int getYear(final LocalDate localDate) {
        if(isNull(localDate)){
            return 0;
        }
        return localDate.getYear();
    }

    public static int getMonthIndex(final LocalDate localDate) {
        requireNonNull(localDate, "localDate can't be null");
        return localDate.getMonthValue();
    }
}
