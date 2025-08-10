package converter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

/**
 * Interface for converting numeric amounts into their corresponding
 * word representations, with support for localization, formatting, and validation.
 */
public interface NumberToWordsConverter {

    /**
     * Converts the given whole number to words in the specified locale.
     *
     * @param number the number to convert
     * @param locale the desired locale for the words output
     * @return the number in words
     * @throws IllegalArgumentException if number is negative or locale is unsupported
     */
    String convert(long number, Locale locale);

    /**
     * Converts the given decimal amount to words in the specified locale,
     * including fractional units if applicable.
     *
     * @param amount the amount to convert
     * @param locale the desired locale for the words output
     * @return the amount in words, including fractional part if present
     * @throws IllegalArgumentException if amount is negative or locale is unsupported
     */
    String convert(BigDecimal amount, Locale locale);

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
     * @return a list of supported locales
     */
    List<Locale> getSupportedLocales();

    /**
     * Converts the given number to ordinal words (e.g., 1 -> "first").
     *
     * @param number the number to convert
     * @param locale the locale for the output
     * @return the ordinal form in words
     */
    String convertToOrdinal(long number, Locale locale);

    /**
     * Converts a numeric string to words, parsing and validating it first.
     *
     * @param numberString the string containing a numeric value
     * @param locale the desired locale for the words output
     * @return the number in words
     * @throws NumberFormatException if the string cannot be parsed to a valid number
     */
    String convertFromString(String numberString, Locale locale);

    /**
     * Formats a number into a localized string representation without converting to words.
     * Useful for displaying alongside the words.
     *
     * @param number the number to format
     * @param locale the desired locale for formatting
     * @return the formatted number string
     */
    String formatNumber(long number, Locale locale);
}
