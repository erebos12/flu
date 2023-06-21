package com.erebos.ant.utils.depreciation;

import java.time.LocalDate;

import static com.erebos.ant.utils.DateUtilities.getMonthIndex;
import static com.erebos.ant.utils.DateUtilities.getYear;
import static com.erebos.ant.utils.depreciation.DeprecationUtils.*;

public class DepreciationCalculation {

    private static final double DECREMENTOR = 1.0;

    private DepreciationCalculation() {
        throw new IllegalStateException("Utility class");
    }

    public static double calculateDepreciationValue(
            final int lifeTimeInMonths, final LocalDate purchasingDate, final double purchaseAmount, final int calculationYear) {
        if (isMinOrMaxLifeTimeInYears(lifeTimeInMonths)) {
            return ZERO;
        } else if (getLastDepreciationYear(purchasingDate, lifeTimeInMonths - DECREMENTOR) < (calculationYear)) {
            return ZERO;
        } else if (getYear(purchasingDate) > calculationYear) {
            return ZERO;
        } else if (getYear(purchasingDate) == calculationYear) {
            return getStraightLineDepreciation(purchaseAmount, getLifeTimeInYears(lifeTimeInMonths))
                    * getMidYearFactor(getMonthIndex(purchasingDate));
        } else if (getLastDepreciationYear(purchasingDate, lifeTimeInMonths - DECREMENTOR) == calculationYear) {
            return getStraightLineDepreciation(purchaseAmount, getLifeTimeInYears(lifeTimeInMonths))
                    * (getLastDepreciationMonth(purchasingDate, lifeTimeInMonths - DECREMENTOR) / MONTHS_IN_YEAR);
        }
        return getStraightLineDepreciation(purchaseAmount, getLifeTimeInYears(lifeTimeInMonths));
    }

    public static double calculateNetBookEndValue(
            final int lifeTimeInMonths, final LocalDate purchasingDate, final double purchaseAmount, final int calculationYear) {
        if (getYear(purchasingDate) > calculationYear) {
            return ZERO;
        } else if (isMinOrMaxLifeTimeInYears(lifeTimeInMonths)) {
            return purchaseAmount;
        } else if (getLastDepreciationYear(purchasingDate, lifeTimeInMonths - DECREMENTOR) < (calculationYear + 1)) {
            return ZERO;
        }
        return calculateNetBookValue(calculationYear, lifeTimeInMonths, purchasingDate, purchaseAmount);
    }

    public static double calculateNetBookBeginValue(
            final int lifeTimeInMonths, final LocalDate purchasingDate, final double purchaseAmount, final int calculationYear) {
        if (getYear(purchasingDate) >= calculationYear) {
            return ZERO;
        } else if (isMinOrMaxLifeTimeInYears(lifeTimeInMonths)) {
            return purchaseAmount;
        } else if (getLastDepreciationYear(purchasingDate, lifeTimeInMonths - DECREMENTOR) < calculationYear) {
            return ZERO;
        }
        return calculateNetBookValue(calculationYear - 1, lifeTimeInMonths, purchasingDate, purchaseAmount);
    }
}
