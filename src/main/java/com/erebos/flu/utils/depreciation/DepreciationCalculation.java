package com.erebos.flu.utils.depreciation;

import java.time.LocalDate;

import static com.erebos.flu.utils.DateUtilities.getMonthIndex;
import static com.erebos.flu.utils.DateUtilities.getYear;

public class DepreciationCalculation {

    private static final double DECREMENTOR = 1.0;

    private DepreciationCalculation() {
        throw new IllegalStateException("Utility class");
    }

    public static double calculateDepreciationValue(
            final int lifeTimeInMonths, final LocalDate purchasingDate, final double purchaseAmount, final int calculationYear) {
        if (DeprecationUtils.isMinOrMaxLifeTimeInYears(lifeTimeInMonths)) {
            return DeprecationUtils.ZERO;
        } else if (DeprecationUtils.getLastDepreciationYear(purchasingDate, lifeTimeInMonths - DECREMENTOR) < (calculationYear)) {
            return DeprecationUtils.ZERO;
        } else if (getYear(purchasingDate) > calculationYear) {
            return DeprecationUtils.ZERO;
        } else if (getYear(purchasingDate) == calculationYear) {
            return DeprecationUtils.getStraightLineDepreciation(purchaseAmount, DeprecationUtils.getLifeTimeInYears(lifeTimeInMonths))
                    * DeprecationUtils.getMidYearFactor(getMonthIndex(purchasingDate));
        } else if (DeprecationUtils.getLastDepreciationYear(purchasingDate, lifeTimeInMonths - DECREMENTOR) == calculationYear) {
            return DeprecationUtils.getStraightLineDepreciation(purchaseAmount, DeprecationUtils.getLifeTimeInYears(lifeTimeInMonths))
                    * (DeprecationUtils.getLastDepreciationMonth(purchasingDate, lifeTimeInMonths - DECREMENTOR) / DeprecationUtils.MONTHS_IN_YEAR);
        }
        return DeprecationUtils.getStraightLineDepreciation(purchaseAmount, DeprecationUtils.getLifeTimeInYears(lifeTimeInMonths));
    }

    public static double calculateNetBookEndValue(
            final int lifeTimeInMonths, final LocalDate purchasingDate, final double purchaseAmount, final int calculationYear) {
        if (getYear(purchasingDate) > calculationYear) {
            return DeprecationUtils.ZERO;
        } else if (DeprecationUtils.isMinOrMaxLifeTimeInYears(lifeTimeInMonths)) {
            return purchaseAmount;
        } else if (DeprecationUtils.getLastDepreciationYear(purchasingDate, lifeTimeInMonths - DECREMENTOR) < (calculationYear + 1)) {
            return DeprecationUtils.ZERO;
        }
        return DeprecationUtils.calculateNetBookValue(calculationYear, lifeTimeInMonths, purchasingDate, purchaseAmount);
    }

    public static double calculateNetBookBeginValue(
            final int lifeTimeInMonths, final LocalDate purchasingDate, final double purchaseAmount, final int calculationYear) {
        if (getYear(purchasingDate) >= calculationYear) {
            return DeprecationUtils.ZERO;
        } else if (DeprecationUtils.isMinOrMaxLifeTimeInYears(lifeTimeInMonths)) {
            return purchaseAmount;
        } else if (DeprecationUtils.getLastDepreciationYear(purchasingDate, lifeTimeInMonths - DECREMENTOR) < calculationYear) {
            return DeprecationUtils.ZERO;
        }
        return DeprecationUtils.calculateNetBookValue(calculationYear - 1, lifeTimeInMonths, purchasingDate, purchaseAmount);
    }
}
