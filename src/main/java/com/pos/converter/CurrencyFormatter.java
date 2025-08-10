package converter;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Currency;
import java.util.Locale;

/**
 * Utility class for formatting, parsing, and validating monetary amounts.
 * Supports multiple locales, custom currencies, and flexible precision levels.
 */
public class CurrencyFormatter {

    /**
     * Formats a given amount using the locale's default currency.
     */
    public static String format(BigDecimal amount, Locale locale) {
        validateAmount(amount);
        return NumberFormat.getCurrencyInstance(locale).format(amount);
    }

    /**
     * Formats a given amount using the specified currency code.
     */
    public static String format(BigDecimal amount, String currencyCode, Locale locale) {
        validateAmount(amount);
        NumberFormat format = NumberFormat.getCurrencyInstance(locale);
        format.setCurrency(getValidCurrency(currencyCode));
        return format.format(amount);
    }

    /**
     * Formats a given amount using specified currency code and fraction digits.
     */
    public static String format(BigDecimal amount, String currencyCode, Locale locale, int fractionDigits) {
        validateAmount(amount);
        NumberFormat format = NumberFormat.getCurrencyInstance(locale);
        format.setCurrency(getValidCurrency(currencyCode));
        format.setMinimumFractionDigits(fractionDigits);
        format.setMaximumFractionDigits(fractionDigits);
        return format.format(amount);
    }

    /**
     * Formats a given amount without currency symbols, for plain numeric display.
     */
    public static String formatPlain(BigDecimal amount, Locale locale, int fractionDigits) {
        validateAmount(amount);
        NumberFormat format = NumberFormat.getNumberInstance(locale);
        format.setMinimumFractionDigits(fractionDigits);
        format.setMaximumFractionDigits(fractionDigits);
        return format.format(amount);
    }

    /**
     * Parses a currency string into BigDecimal using the given locale.
     */
    public static BigDecimal parse(String currencyString, Locale locale) throws ParseException {
        if (currencyString == null || currencyString.trim().isEmpty()) {
            throw new IllegalArgumentException("Currency string cannot be null or empty");
        }
        NumberFormat format = NumberFormat.getCurrencyInstance(locale);
        Number number = format.parse(currencyString.trim());
        return BigDecimal.valueOf(number.doubleValue());
    }

    /**
     * Checks if a currency code is valid for the JVM environment.
     */
    public static boolean isValidCurrencyCode(String currencyCode) {
        try {
            Currency.getInstance(currencyCode);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private static void validateAmount(BigDecimal amount) {
        if (amount == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
    }

    private static Currency getValidCurrency(String currencyCode) {
        try {
            return Currency.getInstance(currencyCode);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid currency code: " + currencyCode, e);
        }
    }
}
