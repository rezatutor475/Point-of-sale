package util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Utility class for comprehensive number operations: rounding, formatting,
 * parsing, validation, comparisons, and additional financial-safe calculations.
 * Designed for financial, localization, and general POS number handling.
 */
public final class NumberUtils {

    private NumberUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    public static BigDecimal round(BigDecimal value, int scale) {
        if (value == null) return null;
        return value.setScale(scale, RoundingMode.HALF_UP);
    }

    public static String formatWithGrouping(BigDecimal value, Locale locale, int fractionDigits) {
        if (value == null) return "";
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(locale);
        DecimalFormat df = new DecimalFormat();
        df.setDecimalFormatSymbols(symbols);
        df.setGroupingUsed(true);
        df.setMaximumFractionDigits(fractionDigits);
        df.setMinimumFractionDigits(fractionDigits);
        return df.format(value);
    }

    public static boolean isPositive(BigDecimal value) {
        return value != null && value.compareTo(BigDecimal.ZERO) > 0;
    }

    public static boolean isNonNegative(BigDecimal value) {
        return value != null && value.compareTo(BigDecimal.ZERO) >= 0;
    }

    public static boolean isNegative(BigDecimal value) {
        return value != null && value.compareTo(BigDecimal.ZERO) < 0;
    }

    public static boolean isZero(BigDecimal value) {
        return value != null && value.compareTo(BigDecimal.ZERO) == 0;
    }

    public static BigDecimal toBigDecimal(String value) {
        if (value == null || value.trim().isEmpty()) return null;
        try {
            return new BigDecimal(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static int safeCompare(BigDecimal a, BigDecimal b) {
        BigDecimal valA = (a == null) ? BigDecimal.ZERO : a;
        BigDecimal valB = (b == null) ? BigDecimal.ZERO : b;
        return valA.compareTo(valB);
    }

    public static BigDecimal percentOf(BigDecimal base, BigDecimal percent) {
        if (base == null || percent == null) return null;
        return base.multiply(percent).divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);
    }

    public static BigDecimal addSafe(BigDecimal a, BigDecimal b) {
        return (a == null ? BigDecimal.ZERO : a).add(b == null ? BigDecimal.ZERO : b);
    }

    public static BigDecimal subtractSafe(BigDecimal a, BigDecimal b) {
        return (a == null ? BigDecimal.ZERO : a).subtract(b == null ? BigDecimal.ZERO : b);
    }

    public static BigDecimal multiplySafe(BigDecimal a, BigDecimal b) {
        return (a == null ? BigDecimal.ZERO : a).multiply(b == null ? BigDecimal.ZERO : b);
    }

    public static BigDecimal divideSafe(BigDecimal a, BigDecimal b, int scale) {
        if (b == null || BigDecimal.ZERO.compareTo(b) == 0) return null;
        return (a == null ? BigDecimal.ZERO : a).divide(b, scale, RoundingMode.HALF_UP);
    }

    public static BigDecimal abs(BigDecimal value) {
        return value == null ? null : value.abs();
    }
}
