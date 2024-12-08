package com.erebos.flu.utils;

import com.google.common.collect.ImmutableList;
import com.erebos.flu.utils.pojo.CostCenter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;
import java.util.stream.Stream;

import static com.erebos.flu.utils.MathUtils.*;
import static com.erebos.flu.utils.PrivateConstructorTestUtil.testPrivateConstructor;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MathUtilsTest {

    @Test
    void testConstructorThrowsException() throws NoSuchMethodException {
        testPrivateConstructor(MathUtils.class);
    }

    @Test
    void calculatePercentageValueSimple() {
        final double percentage = calculatePercentageValue(1000.00, 100.00);
        assertThat(percentage, is(10.0));
    }

    @ParameterizedTest
    @CsvSource({"0.0, 100.00,0.0", "100.0, 0.0, 0.0", "0.0, 0.0, 0.0"})
    void calculatePercentageValueWithZero(final double total, final double value, final double expectedResult) {
        final double percentage = calculatePercentageValue(total, value);
        assertThat(percentage, is(expectedResult));
    }

    @ParameterizedTest
    @CsvSource({"100.00, -100.00", "-100.00,100.00", "-0.0, 0.0", "-1, 1.0"})
    void negateTest(final double value, final double expectedResult) {
        final double negated = negate(value);
        assertThat(negated, is(expectedResult));
    }

    @ParameterizedTest
    @CsvSource({"100.0000001, 100.00",
            "3.1415926535897,3.14",
            "1.799, 1.8",
            "2.98798,2.99",
            "2.000,2.00",
            "0.1111100,0.11"})
    void testRoundValue(final double value, final double expectedResult) {
        final double rounded = roundValue(value);
        assertThat(rounded, is(expectedResult));
    }

    @Test
    void testAvgFromList() {
        var list = List.of(new CostCenter("", 10.0, ""), new CostCenter("", 20.0, ""), new CostCenter("", 30.0, ""));
        double avg = calculateAverage(ImmutableList.copyOf(list), CostCenter::reimbursementNeeds);
        assertThat(avg, is(20.0));
        avg = calculateAverage(ImmutableList.copyOf(List.of()), CostCenter::reimbursementNeeds);
        assertThat(avg, is(Double.NaN));
        avg = calculateAverage(null, CostCenter::reimbursementNeeds);
        assertThat(avg, is(Double.NaN));
    }

    @Test
    void testAvg() {
        double avg = calculateAverage(2, 2);
        assertThat(avg, is(2.00));
    }

    @Test
    void testCalculateSum() {
        final var ccs = List.of(
                new CostCenter("KS1", 100, ""),
                new CostCenter("KS2", 100.00, ""),
                new CostCenter("KS3", 150.00, "")
        );
        final Stream<Double> doubleStream = ccs.stream().map(CostCenter::reimbursementNeeds);
        final double sum = calculateSum(doubleStream);
        assertThat(sum, is(350.00));
    }

    @ParameterizedTest
    @CsvSource({"0, false",
            "0.0,false",
            "0.0000000000000000000000, false",
            "0x0.0000000000000P-1022, false",
            "0.000000000000001, true",
            "0x0.0000000000001P-1022, true",
            "-0.00000000000000000001, true",
            "1.999900000000000000001, true",
            "-0.000000000000001, true"})
    void testIsNotZero(final double value, final boolean expectedResult) {
        assertThat(isNotZero(value), is(expectedResult));
    }

    @Test
    void testAddValueByPercentage() {
        final double base = 100;
        final double value = 200;
        final double percentage = 50;
        final double result = addValueByPercentage(base, value, percentage);
        assertThat(result, is(200.0));
    }

    @Test
    void testGetNullableDouble() {
        double result = getNullableDouble(null);
        assertEquals(Double.NaN, result);
        result = getNullableDouble(0.0);
        assertEquals(0.0, result);
        result = getNullableDouble(1.4);
        assertEquals(1.4, result);
    }

    @Test
    void testGetNullableDoubleElseZero() {
        double result = getNullableDoubleElseZero(null);
        assertEquals(0.0, result);
        result = getNullableDouble(0.0);
        assertEquals(0.0, result);
        result = getNullableDouble(1.4);
        assertEquals(1.4, result);
    }

    @Test
    void testCompoundPercentageYear1() {
        var newPercentage = calculateCompoundedPercentage(10, 1);
        assertThat(roundValue(newPercentage), is(10.00));
    }

    @Test
    void testCompoundPercentageMultipleYears() {
        var newPercentage = calculateCompoundedPercentage(10, 2);
        assertThat(roundValue(newPercentage), is(21.00));

        newPercentage = calculateCompoundedPercentage(10, 3);
        assertThat(roundValue(newPercentage), is(33.10));
    }

    @Test
    void testCompoundPercentageWith0s() {
        var newPercentage = calculateCompoundedPercentage(10, 0);
        assertThat(roundValue(newPercentage), is(0.00));

        newPercentage = calculateCompoundedPercentage(0, 2);
        assertThat(roundValue(newPercentage), is(0.00));
    }

    @ParameterizedTest
    @CsvSource({
            "0, true",
            "-1,false",
            "8, true",
            "9, false",
            "11, false",
            "-2, false"
    })
    void testIsEven(final int value, final boolean expectedResult) {
        assertThat(isEven(value), is(expectedResult));
    }
}
