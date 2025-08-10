package exception;

/**
 * Custom exception thrown when an invalid monetary amount is encountered.
 * Includes utility factory methods and validation helpers for consistent usage.
 */
public class InvalidAmountException extends RuntimeException {

    public InvalidAmountException() {
        super();
    }

    public InvalidAmountException(String message) {
        super(message);
    }

    public InvalidAmountException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidAmountException(Throwable cause) {
        super(cause);
    }

    public static InvalidAmountException forNullAmount() {
        return new InvalidAmountException("Amount cannot be null");
    }

    public static InvalidAmountException forNegativeAmount() {
        return new InvalidAmountException("Amount cannot be negative");
    }

    public static InvalidAmountException forExceedingLimit(String limitDescription) {
        return new InvalidAmountException("Amount exceeds allowed limit: " + limitDescription);
    }

    public static InvalidAmountException forNonNumericAmount(String input) {
        return new InvalidAmountException("Invalid numeric format for amount: '" + input + "'");
    }

    public static InvalidAmountException forZeroAmountNotAllowed() {
        return new InvalidAmountException("Amount cannot be zero for this operation");
    }

    /**
     * Validates that the provided amount is non-null, numeric, non-negative, and within optional limit.
     *
     * @param amount the monetary amount to validate
     * @param maxLimit optional maximum allowed value (nullable)
     * @throws InvalidAmountException if validation fails
     */
    public static void validateAmount(Double amount, Double maxLimit) {
        if (amount == null) throw forNullAmount();
        if (amount < 0) throw forNegativeAmount();
        if (amount == 0) throw forZeroAmountNotAllowed();
        if (maxLimit != null && amount > maxLimit) throw forExceedingLimit(maxLimit.toString());
    }
}
