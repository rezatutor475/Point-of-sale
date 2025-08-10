package model;

import java.util.Currency;
import java.util.Locale;
import java.util.Objects;

/**
 * Represents metadata about a specific currency, including its code, symbol, fraction digits, and locale.
 * Provides utility methods for currency validation and formatting.
 * This class is immutable and thread-safe.
 */
public final class CurrencyInfo {

    private final String currencyCode;
    private final String currencySymbol;
    private final int defaultFractionDigits;
    private final Locale locale;

    /**
     * Constructs a CurrencyInfo object based on a currency code and locale.
     *
     * @param currencyCode the ISO 4217 currency code (e.g., "USD", "EUR"). Must not be null or blank.
     * @param locale the locale associated with the currency. Must not be null.
     * @throws IllegalArgumentException if the currency code is invalid or arguments are null.
     */
    public CurrencyInfo(String currencyCode, Locale locale) {
        if (currencyCode == null || currencyCode.trim().isEmpty()) {
            throw new IllegalArgumentException("Currency code must not be null or empty");
        }
        Objects.requireNonNull(locale, "Locale must not be null");

        Currency currency;
        try {
            currency = Currency.getInstance(currencyCode);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid currency code: " + currencyCode, e);
        }

        this.currencyCode = currency.getCurrencyCode();
        this.currencySymbol = currency.getSymbol(locale);
        this.defaultFractionDigits = currency.getDefaultFractionDigits();
        this.locale = locale;
    }

    /**
     * Creates a CurrencyInfo instance from a given locale.
     * @param locale the locale whose currency information will be retrieved
     * @return a new CurrencyInfo instance
     */
    public static CurrencyInfo fromLocale(Locale locale) {
        Objects.requireNonNull(locale, "Locale must not be null");
        Currency currency = Currency.getInstance(locale);
        return new CurrencyInfo(currency.getCurrencyCode(), locale);
    }

    /**
     * Checks if a currency code is valid.
     * @param code the currency code to validate
     * @return true if the currency code is valid, false otherwise
     */
    public static boolean isValidCurrencyCode(String code) {
        if (code == null || code.trim().isEmpty()) {
            return false;
        }
        try {
            Currency.getInstance(code);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public int getDefaultFractionDigits() {
        return defaultFractionDigits;
    }

    public Locale getLocale() {
        return locale;
    }

    @Override
    public String toString() {
        return String.format("CurrencyInfo[code=%s, symbol=%s, fractionDigits=%d, locale=%s]",
                currencyCode, currencySymbol, defaultFractionDigits, locale);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CurrencyInfo)) return false;
        CurrencyInfo that = (CurrencyInfo) o;
        return defaultFractionDigits == that.defaultFractionDigits &&
                Objects.equals(currencyCode, that.currencyCode) &&
                Objects.equals(currencySymbol, that.currencySymbol) &&
                Objects.equals(locale, that.locale);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currencyCode, currencySymbol, defaultFractionDigits, locale);
    }
}
