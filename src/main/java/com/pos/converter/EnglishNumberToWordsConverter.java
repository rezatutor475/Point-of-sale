package converter;

import java.math.BigDecimal;
import java.util.Locale;

/**
 * Interface for converting numeric amounts into their corresponding
 * word representations, supporting localization and extended utilities.
 * Includes features for handling ordinals, negatives, and sample outputs.
 */
public interface NumberToWordsConverter {

    /**
     * Converts the given whole number to words in the specified locale.
     *
     * @param number the number to convert (non-negative)
     * @param locale the desired locale for the words output
     * @return the number in words
     * @throws IllegalArgumentException if number is negative or locale is unsupported
     */
    String convert(long number, Locale locale);

    /**
     * Converts the given decimal amount to words in the specified locale.
     * Useful for currency and fractional values.
     *
     * @param amount the amount to convert (non-negative)
     * @param locale the desired locale for the words output
     * @return the amount in words, including fractional part if applicable
     * @throws IllegalArgumentException if amount is negative or locale is unsupported
     */
    String convert(BigDecimal amount, Locale locale);

    /**
     * Converts a numeric string to words in the specified locale.
     *
     * @param numericString the numeric value as a string
     * @param locale the desired locale for the words output
     * @return the number in words
     * @throws NumberFormatException if the string is not a valid number
     */
    String convert(String numericString, Locale locale);

    /**
     * Converts a number to its ordinal word representation (e.g., "first", "second").
     *
     * @param number the number to convert
     * @param locale the desired locale for the words output
     * @return the ordinal form of the number in words
     */
    String convertToOrdinal(long number, Locale locale);

    /**
     * Converts a negative number to its absolute value in words, optionally adding a minus prefix.
     *
     * @param number the negative number to convert
     * @param locale the desired locale for the words output
     * @param includeMinusPrefix whether to prefix with "minus" (localized)
     * @return the absolute value in words, optionally prefixed with minus
     */
    String convertNegative(long number, Locale locale, boolean includeMinusPrefix);

    /**
     * Converts a range of numbers to a single string representation (e.g., "ten to twenty").
     *
     * @param start the starting number in the range
     * @param end the ending number in the range
     * @param locale the desired locale for the words output
     * @return the range in words
     */
    String convertRange(long start, long end, Locale locale);

    /**
     * Checks if the given locale is supported by the converter.
     *
     * @param locale the locale to check
     * @return true if supported, false otherwise
     */
    boolean isLocaleSupported(Locale locale);

    /**
     * Returns a sample conversion output for demonstration purposes.
     *
     * @param locale the locale to use
     * @return a sample string showing how numbers are expressed in words
     */
    String sampleConversion(Locale locale);

    /**
     * Lists all supported locales for number-to-words conversion.
     *
     * @return an array of supported locales
     */
    Locale[] getSupportedLocales();
}
