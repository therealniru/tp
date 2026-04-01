package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's phone number in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidPhone(String)}
 */
public class Phone {


    public static final String MESSAGE_CONSTRAINTS =
            "Error: Invalid phone number. Phone number must contain only digits "
            + "(with optional '+' prefix), and must be between 3 and 15 digits long.";
    public static final String VALIDATION_REGEX = "\\+?\\d{3,15}";
    public final String value;

    /**
     * Constructs a {@code Phone}.
     *
     * @param phone A valid phone number.
     */
    public Phone(String phone) {
        requireNonNull(phone);
        String trimmedPhone = phone.trim();
        checkArgument(isValidPhone(trimmedPhone), MESSAGE_CONSTRAINTS);
        value = trimmedPhone;
    }

    /**
     * Returns true if a given string is a valid phone number.
     * Rejects all-zero or leading-zero patterns (e.g., 000, 0000000).
     */
    public static boolean isValidPhone(String test) {
        if (!test.matches(VALIDATION_REGEX)) {
            return false;
        }
        // Reject all-zero patterns (000, 0000, etc.)
        String digitsOnly = test.replaceAll("\\+", "");
        return !digitsOnly.matches("0+");
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Phone)) {
            return false;
        }

        Phone otherPhone = (Phone) other;
        return normalizedDigits().equals(otherPhone.normalizedDigits());
    }

    /**
     * Returns the digits-only form of this phone number (strips leading '+').
     * Used for duplicate detection so that +6591234567 and 6591234567 are considered the same.
     */
    private String normalizedDigits() {
        return value.startsWith("+") ? value.substring(1) : value;
    }

    @Override
    public int hashCode() {
        return normalizedDigits().hashCode();
    }

}
