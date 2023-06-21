package com.erebos.flu.utils.depreciation;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static com.erebos.flu.utils.MathUtils.roundValue;
import static com.erebos.flu.utils.depreciation.DeprecationUtils.calculateNetBookValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

final class DeprecationUtilsTest {
    /**
     * based on scenario 1 calculation for group 1, CC02
     * https://docs.google.com/spreadsheets/d/1nc3H1mpreg3ILyVurEPPxU1CzniAVkS8IP6EY1uUun0/edit#gid=0
     */
    private static final int CALCULATION_YEAR = 2020;

    @Test
    void shouldProperCalculateNetBookEndValue() {
        // given
        final var lifeTimeInMonths = 120;
        final var purchasingDate = LocalDate.of(2011, 3, 1);
        final var purchaseAmount = 200000;
        // when
        final var netBookBeginValue = calculateNetBookValue(CALCULATION_YEAR - 1, lifeTimeInMonths, purchasingDate, purchaseAmount);
        final var netBookEndValue = calculateNetBookValue(CALCULATION_YEAR, lifeTimeInMonths, purchasingDate, purchaseAmount);
        // then
        assertThat(roundValue(netBookBeginValue), is(23333.33));
        assertThat(roundValue(netBookEndValue), is(3333.33));
    }
}
