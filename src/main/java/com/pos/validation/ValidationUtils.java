package com.phoenix.pos.validation;

import java.util.Objects;

/**
 * Utility class containing reusable validation methods for POS system input checks.
 * <p>
 * This final class offers null-safe, concise operations for string-based validations
 * such as blank checks, digit verification, padding, and equality comparison.
 */
public final class ValidationUtils {

    // Prevents instantiation
    private ValidationUtils() {
        throw new UnsupportedOperationException("ValidationUtils should not be instantiated");
    }

    /**
     * Determines if a string is null, empty, or contains only whitespace.
     *
     * @param input the string to examine
     * @return true if input is null, empty, or blank
     */
    public static boolean isNullOrBlank(String input) {
        return input == null || input.trim().isEmpty();
    }

    /**
     * Verifies whether a string is composed entirely of digits.
     *
     * @param input the string to verify
     * @return true if the string is non-null and matches \d+
     */
    public static boolean isNumeric(String input) {
        return input != null && input.matches("\\d+");
    }

    /**
     * Left-pads a string with zeroes to achieve a target length.
     *
     * @param input  the string to pad
     * @param length the intended total length
     * @return a left-zero-padded string, or null if input is null
     */
    public static String leftPadWithZeros(String input, int length) {
        if (input == null) return null;
        return String.format("%" + length + "s", input).replace(' ', '0');
    }

    /**
     * Performs a null-safe comparison of two strings.
     *
     * @param a the first string
     * @param b the second string
     * @return true if both are equal or both null
     */
    public static boolean safeEquals(String a, String b) {
        return Objects.equals(a, b);
    }
}
