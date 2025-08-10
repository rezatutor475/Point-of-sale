package model;

import exception.InvalidAmountException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Represents a monetary amount with validation, conversions, and basic arithmetic operations.
 * Immutable and thread-safe.
 */
public final class Amount {

    private final BigDecimal value;

    /**
     * Constructs a new Amount.
     *
     * @param value the monetary value, must not be null or negative
     */
    public Amount(BigDecimal value) {
        if (value == null) {
            throw InvalidAmountException.forNullAmount();
        }
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw InvalidAmountException.forNegativeAmount();
        }
        this.value = value.setScale(2, RoundingMode.HALF_EVEN);
    }

    public static Amount fromDouble(double value) {
        return new Amount(BigDecimal.valueOf(value));
    }

    public static Amount fromString(String value) {
        return new Amount(new BigDecimal(value));
    }

    public BigDecimal getValue() {
        return value;
    }

    public Amount add(Amount other) {
        Objects.requireNonNull(other, "Other amount must not be null");
        return new Amount(this.value.add(other.value));
    }

    public Amount subtract(Amount other) {
        Objects.requireNonNull(other, "Other amount must not be null");
        BigDecimal result = this.value.subtract(other.value);
        if (result.compareTo(BigDecimal.ZERO) < 0) {
            throw InvalidAmountException.forNegativeAmount();
        }
        return new Amount(result);
    }

    public Amount multiply(BigDecimal factor) {
        Objects.requireNonNull(factor, "Factor must not be null");
        return new Amount(this.value.multiply(factor));
    }

    public Amount divide(BigDecimal divisor) {
        Objects.requireNonNull(divisor, "Divisor must not be null");
        if (divisor.compareTo(BigDecimal.ZERO) == 0) {
            throw new ArithmeticException("Division by zero");
        }
        return new Amount(this.value.divide(divisor, 2, RoundingMode.HALF_EVEN));
    }

    public boolean isZero() {
        return value.compareTo(BigDecimal.ZERO) == 0;
    }

    public boolean isPositive() {
        return value.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean isNegative() {
        return value.compareTo(BigDecimal.ZERO) < 0;
    }

    @Override
    public String toString() {
        return value.toPlainString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Amount)) return false;
        Amount amount = (Amount) o;
        return value.compareTo(amount.value) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
