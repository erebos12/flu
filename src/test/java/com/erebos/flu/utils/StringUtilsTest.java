package com.erebos.flu.utils;

import com.erebos.flu.utils.pojo.CostCenter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;

import static com.erebos.flu.utils.PrivateConstructorTestUtil.testPrivateConstructor;
import static com.erebos.flu.utils.StringUtils.*;
import static com.erebos.flu.utils.StringUtils.concatStringsWithDelimiter;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StringUtilsTest {

    record TestConfig(
            String firstName,
            String lastName) {
    }

    @Test
    void testConstructorThrowsException() throws NoSuchMethodException {
        testPrivateConstructor(StringUtils.class);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t", "\n"})
    void checkStringNullOrEmpty(final String value) {
        final boolean result = isStringNullOrEmpty(value);
        assertThat(result, is(true));
    }

    @Test
    void testGetStringMemberFromObj() {
        var s = getStringMemberFromObj(null, null);
        assertThat(s, is(""));
        final CostCenter cc = new CostCenter("CC01", 100, "");
        s = getStringMemberFromObj(cc, CostCenter::shortName);
        assertThat(s, is("CC01"));
        s = getStringMemberFromObj(cc, CostCenter::longName);
        assertThat(s, is(""));
    }

    @ParameterizedTest
    @CsvSource({", , false",
            ",test1, false",
            "test1, , false",
            "test1, test1, true",
            "'', '', true",
            "test2, test1, false"})
    void TestStringsNotNullAndEqual(final String stringOne, final String stringTwo, final boolean isEqual) {
        assertThat(stringsNotNullAndEqual(stringOne, stringTwo), is(isEqual));
    }

    @ParameterizedTest
    @CsvSource({"s1, s2, s1/s2",
            "s1, , s1",
            " , , ''",
            " , s2, /s2",})
    void testConcatStrings(String s1, String s2, String expected) {
        assertThat(concatStringsWithDelimiter(s1, s2, SLASH_DELIMITER), is(expected));
    }

    @ParameterizedTest
    @CsvSource({"s1, s1", "  , ''", "'', ''"})
    void testGetNullableString(String s, String expected) {
        assertEquals(expected, getNullableString(s));
    }

    @ParameterizedTest
    @CsvSource({"s1, s1", "  , 'NA'", "'', 'NA'"})
    void testGetNullableStringWithNA(String s, String expected) {
        assertEquals(expected, getNullableStringWithNA(s));
    }

    @Test
    void testConcatStringsNew() {
        var concat = concatStringsWithDelimiter(List.of("s1", "s2", "s3"), SLASH_DELIMITER);
        assertThat(concat, is("s1/s2/s3"));
        concat = concatStringsWithDelimiter(List.of("s1", "s2", ""), SLASH_DELIMITER);
        assertThat(concat, is("s1/s2/"));
        concat = concatStringsWithDelimiter(List.of("s1"), SLASH_DELIMITER);
        assertThat(concat, is("s1"));
        concat = concatStringsWithDelimiter(List.of("s1", "s2", "s3"), EMPTY_STRING);
        assertThat(concat, is("s1s2s3"));
        // null in the list
        var list = new ArrayList<String>();
        list.add("s1");
        list.add(null);
        list.add("s2");
        list.add(null);
        concat = concatStringsWithDelimiter(list, EMPTY_STRING);
        assertThat(concat, is("s1s2"));
        // list is null
        concatStringsWithDelimiter(null, EMPTY_STRING);
    }

    @Test
    void testValidateStringParameter() {
        TestConfig testConfig = new TestConfig("", "lastName");
        Throwable expectedException = assertThrows(IllegalStateException.class, () -> validateStringParameter(testConfig.firstName, "firstName", TestConfig.class));
        assertEquals("Mandatory parameter 'firstName' not set in TestConfig", expectedException.getMessage());

        TestConfig testConfig2 = new TestConfig("firstName", null);
        expectedException = assertThrows(IllegalStateException.class, () -> validateStringParameter(testConfig2.lastName, "lastName", TestConfig.class));
        assertEquals("Mandatory parameter 'lastName' not set in TestConfig", expectedException.getMessage());
    }

    @ParameterizedTest
    @CsvSource({"anna, anna",
            "hello, olleh",
            "world, dlrow",
            ", ",
            "'', ''",
    })
    void testReverseString(final String input, final String reversed) {
        assertThat(reverseString(input), is(reversed));

    }

    private String reverseString(String input) {
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
}

