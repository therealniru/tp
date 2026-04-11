package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's phone number in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidPhone(String)}
 */
public class Phone {


    public static final String MESSAGE_CONSTRAINTS =
            "Error: Invalid phone number. Phone numbers may start with an optional '+', "
            + "followed by digits, spaces, hyphens '-', or parentheses '()' as separators "
            + "(e.g. +65-9123-4567, +1 (415) 555-2671, 91234567). "
            + "Must contain 3 to 15 digits (separators excluded).";
    /**
     * Matches an optional leading '+', then a digit, then any mix of digits/spaces/hyphens/parentheses.
     * Digit-count check is performed separately in {@link #isValidPhone(String)}.
     */
    public static final String VALIDATION_REGEX = "\\+?\\d([\\d ()\\-]*\\d)?";
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
     * Accepts international formats with '+', spaces, hyphens, and parentheses as separators.
     * Rejects numbers with fewer than 3 or more than 15 digits.
     */
    public static boolean isValidPhone(String test) {
        if (!test.matches(VALIDATION_REGEX)) {
            return false;
        }
        String digitsOnly = test.replaceAll("[^\\d]", "");
        if (digitsOnly.length() < 3 || digitsOnly.length() > 15) {
            return false;
        }
        return true;
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
     * Returns the digits-only form of this phone number (strips '+', spaces, hyphens, parentheses).
     * Used for duplicate detection so that +65-9123-4567, +6591234567, and 6591234567 are
     * all considered the same number.
     */
    private String normalizedDigits() {
        return value.replaceAll("[^\\d]", "");
    }

    @Override
    public int hashCode() {
        return normalizedDigits().hashCode();
    }

}
