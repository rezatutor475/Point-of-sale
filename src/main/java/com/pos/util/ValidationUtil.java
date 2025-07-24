package com.pos.util;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Utility class for validating various input formats used in the POS system.
 * Includes validation for emails, phone numbers, IBANs, card numbers, names,
 * monetary values, national IDs, postal codes, addresses, dates, times,
 * IP addresses, usernames, colors, geographic coordinates, and general string properties.
 */
public final class ValidationUtil {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?[0-9]{10,15}$");
    private static final Pattern IBAN_PATTERN = Pattern.compile("^IR[0-9]{24}$");
    private static final Pattern CARD_NUMBER_PATTERN = Pattern.compile("^[0-9]{16}$");
    private static final Pattern NAME_PATTERN = Pattern.compile("^[\\u0600-\\u06FFa-zA-Z\\s]{2,50}$");
    private static final Pattern AMOUNT_PATTERN = Pattern.compile("^[1-9][0-9]*$|^0$");
    private static final Pattern NATIONAL_ID_PATTERN = Pattern.compile("^\\d{10}$");
    private static final Pattern POSTAL_CODE_PATTERN = Pattern.compile("^\\d{10}$");
    private static final Pattern ADDRESS_PATTERN = Pattern.compile("^[\\w\\s,.-]{5,100}$");
    private static final Pattern DATE_PATTERN = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$");
    private static final Pattern TIME_PATTERN = Pattern.compile("^([01]?[0-9]|2[0-3]):[0-5][0-9]$");
    private static final Pattern IP_ADDRESS_PATTERN = Pattern.compile("^(?:[0-9]{1,3}\\.){3}[0-9]{1,3}$");
    private static final Pattern HEX_COLOR_PATTERN = Pattern.compile("^#?([a-fA-F0-9]{6}|[a-fA-F0-9]{3})$");
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_.-]{3,20}$");

    private ValidationUtil() {
        throw new UnsupportedOperationException("Utility class should not be instantiated");
    }

    public static boolean isValidEmail(String email) {
        return matchPattern(email, EMAIL_PATTERN);
    }

    public static boolean isValidPhoneNumber(String phoneNumber) {
        return matchPattern(phoneNumber, PHONE_PATTERN);
    }

    public static boolean isValidIBAN(String iban) {
        return matchPattern(iban, IBAN_PATTERN);
    }

    public static boolean isValidCardNumber(String cardNumber) {
        return matchPattern(cardNumber, CARD_NUMBER_PATTERN);
    }

    public static boolean isValidName(String name) {
        return matchPattern(name, NAME_PATTERN);
    }

    public static boolean isValidAmount(String amountStr) {
        return matchPattern(amountStr, AMOUNT_PATTERN);
    }

    public static boolean isValidNationalId(String nationalId) {
        if (!matchPattern(nationalId, NATIONAL_ID_PATTERN)) return false;
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            sum += Character.getNumericValue(nationalId.charAt(i)) * (10 - i);
        }
        int remainder = sum % 11;
        int checkDigit = Character.getNumericValue(nationalId.charAt(9));
        return (remainder < 2 && checkDigit == remainder) || (remainder >= 2 && checkDigit == (11 - remainder));
    }

    public static boolean isValidPostalCode(String postalCode) {
        return matchPattern(postalCode, POSTAL_CODE_PATTERN);
    }

    public static boolean isValidAddress(String address) {
        return matchPattern(address, ADDRESS_PATTERN);
    }

    public static boolean isValidDate(String date) {
        return matchPattern(date, DATE_PATTERN);
    }

    public static boolean isValidTime(String time) {
        return matchPattern(time, TIME_PATTERN);
    }

    public static boolean isValidIPAddress(String ip) {
        return matchPattern(ip, IP_ADDRESS_PATTERN);
    }

    public static boolean isValidHexColor(String hexColor) {
        return matchPattern(hexColor, HEX_COLOR_PATTERN);
    }

    public static boolean isValidUsername(String username) {
        return matchPattern(username, USERNAME_PATTERN);
    }

    public static boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    public static boolean isPositive(double value) {
        return value > 0;
    }

    public static boolean isNonNegative(double value) {
        return value >= 0;
    }

    public static boolean areEqual(Object a, Object b) {
        return Objects.equals(a, b);
    }

    public static boolean isLengthBetween(String value, int min, int max) {
        return value != null && value.length() >= min && value.length() <= max;
    }

    public static boolean containsOnlyDigits(String value) {
        return value != null && value.matches("^\\d+$");
    }

    public static boolean startsWith(String value, String prefix) {
        return value != null && prefix != null && value.startsWith(prefix);
    }

    public static boolean endsWith(String value, String suffix) {
        return value != null && suffix != null && value.endsWith(suffix);
    }

    public static boolean contains(String value, String sequence) {
        return value != null && sequence != null && value.contains(sequence);
    }

    public static boolean isValidLatitude(double latitude) {
        return latitude >= -90 && latitude <= 90;
    }

    public static boolean isValidLongitude(double longitude) {
        return longitude >= -180 && longitude <= 180;
    }

    public static boolean isWithinRange(int value, int min, int max) {
        return value >= min && value <= max;
    }

    public static boolean hasValidDecimalPlaces(double value, int maxDecimalPlaces) {
        String text = Double.toString(Math.abs(value));
        int index = text.indexOf(".");
        return index == -1 || text.length() - index - 1 <= maxDecimalPlaces;
    }

    public static boolean isAlphabetic(String value) {
        return matchPattern(value, Pattern.compile("^[a-zA-Z]+$"));
    }

    public static boolean isAlphanumeric(String value) {
        return matchPattern(value, Pattern.compile("^[a-zA-Z0-9]+$"));
    }

    public static boolean isBooleanString(String value) {
        return "true".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value);
    }

    public static boolean isValidJsonKey(String key) {
        return matchPattern(key, Pattern.compile("^[a-zA-Z0-9_]+$"));
    }

    public static boolean isSafeText(String input) {
        return input != null && !input.matches(".*[<>\\\\\"'%;()&+].*");
    }

    private static boolean matchPattern(String input, Pattern pattern) {
        return input != null && pattern.matcher(input).matches();
    }
}
