package com.erebos.ant.utils.depreciation;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.params.provider.Arguments.arguments;


class DepreciationCalculationTest {

    public static Stream<Arguments> calculateNetBookValueBeginSource() {
        return Stream.of(
                arguments(2022, LocalDate.of(2023, 3, 1), 200000.0, 120, 0.0, "Year(purchasingDate) > calculationYear"),
                arguments(2022, LocalDate.of(2022, 3, 1), 200000.0, 120, 0.0, "Year(purchasingDate) == calculationYear"),
                arguments(2022, LocalDate.of(2011, 3, 1), 200000.0, 0, 200000.0, "LifeTimeInYears == 0"),
                arguments(2022, LocalDate.of(2011, 3, 1), 200000.0, 11988, 200000.0, "LifeTimeInYears == 999"),
                arguments(2022, LocalDate.of(2011, 4, 1), 200000.0, 120, 0.0, "LastDepreciationYear < calculationYear"),
                arguments(2022, LocalDate.of(2020, 5, 1), 200000.0, 120, 166666.6666666667, "else"),
                arguments(2022, null, 200000.0, 120, 0, "null LocalDate")
        );
    }

    public static Stream<Arguments> calculateNetBookValueEndSource() {
        return Stream.of(
                arguments(2022, LocalDate.of(2023, 3, 1), 200000.0, 0, 0.0, "Year(purchasingDate) > calculationYear"),
                arguments(2022, LocalDate.of(2011, 3, 1), 200000.0, 0, 200000.0, "LifeTimeInYears == 0"),
                arguments(2022, LocalDate.of(2011, 3, 1), 200000.0, 11988, 200000.0, "LifeTimeInYears == 999"),
                arguments(2022, LocalDate.of(2012, 6, 1), 200000.0, 120, 0.0, "LastDepreciationYear < calculationYear + 1"),
                arguments(2022, LocalDate.of(2020, 5, 1), 200000.0, 120, 146666.6666666667, "else"),
                arguments(2022, null, 200000.0, 120, 0, "null LocalDate")
        );
    }

    public static Stream<Arguments> calculateDepreciationValueSource() {
        return Stream.of(
                arguments(2022, LocalDate.of(2023, 3, 1), 200000.0, 0, 0.0, "Year(purchasingDate) > calculationYear"),
                arguments(2022, LocalDate.of(2011, 3, 1), 200000.0, 0, 0.0, "LifeTimeInYears == 0"),
                arguments(2022, LocalDate.of(2011, 3, 1), 200000.0, 11988, 0.0, "LifeTimeInYears == 999"),
                arguments(2022, LocalDate.of(2011, 4, 1), 200000.0, 120, 0.0, "LastDepreciationYear < calculationYear"),
                arguments(2022, LocalDate.of(2022, 3, 1), 200000.0, 120, 16666.666666666668, "Year(purchasingDate) == calculationYear"),
                arguments(2022, LocalDate.of(2012, 10, 1), 200000.0, 120, 15000.0, "LastDepreciationYear == calculationYear"),
                arguments(2022, LocalDate.of(2020, 5, 1), 200000.0, 120, 20000.0, "else"),
                arguments(2022, null, 200000.0, 120, 0, "null LocalDate")
        );
    }

    @ParameterizedTest
    @MethodSource("calculateNetBookValueBeginSource")
    void calculateNetBookValueBegin(final int calculationYear, final LocalDate purchasingDate, final double purchaseAmount,
                                    final int lifeTimeInMonths, final double expectedNetBookValueBegin, final String testCase) {
        var netBookValueBegin = DepreciationCalculation.calculateNetBookBeginValue(lifeTimeInMonths, purchasingDate, purchaseAmount, calculationYear);
        assertThat(testCase, netBookValueBegin, is(expectedNetBookValueBegin));
    }

    @ParameterizedTest
    @MethodSource("calculateNetBookValueEndSource")
    void calculateNetBookValueEnd(final int calculationYear, final LocalDate purchasingDate, final double purchaseAmount,
                                  final int lifeTimeInMonths, final double expectedNetBookValueEnd, final String testCase) {
        var netBookValueEnd = DepreciationCalculation.calculateNetBookEndValue(lifeTimeInMonths, purchasingDate, purchaseAmount, calculationYear);
        assertThat(testCase, netBookValueEnd, is(expectedNetBookValueEnd));
    }

    @ParameterizedTest
    @MethodSource("calculateDepreciationValueSource")
    void calculateDepreciationValue(final int calculationYear, final LocalDate purchasingDate, final double purchaseAmount,
                                    final int lifeTimeInMonths, final double expectedDepreciationValue, final String testCase) {
        var depreciationValue = DepreciationCalculation.calculateDepreciationValue(lifeTimeInMonths, purchasingDate, purchaseAmount, calculationYear);
        assertThat(testCase, depreciationValue, is(expectedDepreciationValue));
    }
}