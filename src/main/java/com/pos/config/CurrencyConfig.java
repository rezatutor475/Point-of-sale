package config;

import model.CurrencyInfo;
import model.LocaleType;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * CurrencyConfig centralizes configuration for supported currencies
 * and provides easy retrieval, formatting, parsing, and validation utilities.
 */
public class CurrencyConfig {

    private static final Map<LocaleType, CurrencyInfo> currencyRegistry = new HashMap<>();

    static {
        registerCurrency(LocaleType.US, Locale.US);
        registerCurrency(LocaleType.UK, Locale.UK);
        registerCurrency(LocaleType.GERMANY, Locale.GERMANY);
        registerCurrency(LocaleType.FRANCE, Locale.FRANCE);
        registerCurrency(LocaleType.JAPAN, Locale.JAPAN);
    }

    private CurrencyConfig() {
        // Utility class, no instantiation
    }

    public static void registerCurrency(LocaleType localeType, Locale locale) {
        Currency currency = Currency.getInstance(locale);
        currencyRegistry.put(localeType, new CurrencyInfo(
                currency.getCurrencyCode(),
                currency.getSymbol(locale),
                currency.getDefaultFractionDigits(),
                locale
        ));
    }

    public static CurrencyInfo getCurrencyInfo(LocaleType localeType) {
        return currencyRegistry.get(localeType);
    }

    public static boolean isCurrencySupported(LocaleType localeType) {
        return currencyRegistry.containsKey(localeType);
    }

    public static String formatAmount(BigDecimal amount, LocaleType localeType) {
        validateCurrencySupport(localeType);
        CurrencyInfo info = currencyRegistry.get(localeType);
        NumberFormat format = NumberFormat.getCurrencyInstance(info.getLocale());
        format.setCurrency(Currency.getInstance(info.getCode()));
        format.setMinimumFractionDigits(info.getFractionDigits());
        format.setMaximumFractionDigits(info.getFractionDigits());
        return format.format(amount);
    }

    public static BigDecimal parseAmount(String formattedAmount, LocaleType localeType) {
        validateCurrencySupport(localeType);
        try {
            CurrencyInfo info = currencyRegistry.get(localeType);
            NumberFormat format = NumberFormat.getCurrencyInstance(info.getLocale());
            return BigDecimal.valueOf(format.parse(formattedAmount).doubleValue());
        } catch (Exception e) {
            throw new IllegalArgumentException("Unable to parse amount: " + formattedAmount, e);
        }
    }

    public static void validateCurrencySupport(LocaleType localeType) {
        if (!isCurrencySupported(localeType)) {
            throw new IllegalArgumentException("Unsupported locale type: " + localeType);
        }
    }

    public static void removeCurrency(LocaleType localeType) {
        currencyRegistry.remove(localeType);
    }

    public static void clearCurrencies() {
        currencyRegistry.clear();
    }
}
