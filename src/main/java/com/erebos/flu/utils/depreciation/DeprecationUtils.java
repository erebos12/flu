package com.erebos.flu.utils.depreciation;

import java.time.LocalDate;

import static com.erebos.flu.utils.DateUtilities.getMonthIndex;
import static com.erebos.flu.utils.DateUtilities.getYear;
import static java.util.Objects.isNull;

public final class DeprecationUtils {
    public static final int MONTHS_IN_YEAR = 12;
    public static final int MAX_LIFETIME_IN_YEARS = 999;
    public static final int MIN_LIFETIME_IN_YEARS = 0;
    public static final double ZERO = 0.0;

    private DeprecationUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static double getLifeTimeInYears(final double lifeTimeInMonths) {
        return lifeTimeInMonths / MONTHS_IN_YEAR;
    }

    public static double getLastDepreciationYear(final LocalDate purchaseDate, final double lifeTimeInMonths) {
        if(isNull(purchaseDate)){
            return 0.0;
        }
        return purchaseDate.plusMonths((long) lifeTimeInMonths).getYear();
    }

    public static double getLastDepreciationMonth(final LocalDate purchaseDate, final double lifeTimeInMonths) {
        if(isNull(purchaseDate)){
            return 0.0;
        }
        return purchaseDate.plusMonths((long) lifeTimeInMonths).getMonthValue();
    }

    public static double getStraightLineDepreciation(final double purchaseAmount, final double lifeTimeInYears) {
        return purchaseAmount / lifeTimeInYears;
    }

    public static double getMidYearFactor(final double purchaseMonth) {
        return (13 - purchaseMonth) / MONTHS_IN_YEAR;
    }

    public static boolean isMinOrMaxLifeTimeInYears(final int lifeTimeInMonths) {
        return getLifeTimeInYears(lifeTimeInMonths) == MIN_LIFETIME_IN_YEARS || getLifeTimeInYears(lifeTimeInMonths) == MAX_LIFETIME_IN_YEARS;
    }

    public static double calculateNetBookValue(final int calculationYear, final int lifeTimeInMonths, final LocalDate purchasingDate, final double purchaseAmount) {
        return purchaseAmount
                - ((getMidYearFactor(getMonthIndex(purchasingDate))
                + (calculationYear - getYear(purchasingDate)))
                * getStraightLineDepreciation(purchaseAmount, getLifeTimeInYears(lifeTimeInMonths)));
    }
}
