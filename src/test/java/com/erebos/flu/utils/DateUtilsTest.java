package com.erebos.flu.utils;

import com.erebos.flu.exceptions.DateParsingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

import static com.erebos.flu.utils.PrivateConstructorTestUtil.testPrivateConstructor;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DateUtilsTest {

    @Test
    void testConstructorThrowsException() throws NoSuchMethodException {
        testPrivateConstructor(DateUtilities.class);
    }

    @ParameterizedTest
    @CsvSource({"01.12.2000,dd.MM.yyyy", "1.1.2000,dd.MM.yyyy", "2000-12-01,yyyy-MM-dd", "2000-12-01,yyyy-MM-dd"})
    void testDateRegExes(final String dateString, final String expectedPattern) {
        final String datePattern = DateUtilities.getDatePattern(dateString);
        assertThat(datePattern, is(expectedPattern));
    }

    @ParameterizedTest
    @ValueSource(strings = {"2000-12-41", "01.14.2000", "01/12.2000"})
    void testGetDatePatternInvalid(final String dateString) {
        assertThrows(DateParsingException.class, () -> {
            DateUtilities.getDatePattern(dateString);
        });
    }

    @Test
    void testGetDateFromStringWithMissingString() {
        assertThrows(NullPointerException.class, () ->
                DateUtilities.stringToLocalDate(null, "dd.MM.yyyy"));
    }

    @Test
    void testGetDateFromStringWithMissingPattern() {
        assertThrows(NullPointerException.class, () ->
                DateUtilities.stringToLocalDate("2000-12-01", null));
    }

    @Test
    void testFormatToString() {
        final LocalDate date = LocalDate.of(2020, 03, 1);
        assertThat(DateUtilities.formatToString(date), is("2020-03-01"));
    }

    @Test
    void testFormatToStringWithMissingString() {
        assertThrows(NullPointerException.class, () ->
                DateUtilities.formatToString(null));
    }

    @ParameterizedTest
    @CsvSource({"2019-12-13,2019", "01.01.2018,2018"})
    void testGetYear(final String dateString, final int expectedYear) {
        final LocalDate date = DateUtilities.stringToLocalDate(dateString);
        assertThat(DateUtilities.getYear(date), is(expectedYear));
    }

    @ParameterizedTest
    @ValueSource(strings = {"99-12-13", "01.01.21"})
    void getInvalidYear(final String date) {
        final DateParsingException thrown = assertThrows(DateParsingException.class, () ->
                DateUtilities.stringToLocalDate(date));
    }

    @ParameterizedTest
    @CsvSource({"2019-12-13,12", "01.01.2018,01"})
    void testGetMonthIndex(final String dateString, final int expectedMonth) {
        final LocalDate date2 = DateUtilities.stringToLocalDate(dateString);
        assertThat(DateUtilities.getMonthIndex(date2), is(expectedMonth));
    }

    @Test
    void testConvertToLocalDate() throws ParseException {
        final Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2020-01-01");
        final LocalDate localDate = LocalDate.of(2020, 01, 1);
        assertThat(DateUtilities.convertToLocalDate(date), equalTo(localDate));
    }

    @Test
    void testConvertToDate() throws ParseException {
        final LocalDate localDate = LocalDate.of(2020, 12, 31);
        final Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2020-12-31");
        assertThat(DateUtilities.convertToDate(localDate), equalTo(date));
    }
}