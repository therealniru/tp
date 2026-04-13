package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's email in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidEmail(String)}
 */
public class Email {

    public static final String MESSAGE_CONSTRAINTS =
            "Error: Invalid email address. Must be in the format local@domain "
            + "(e.g. user@example.com). The local part may contain letters, digits, "
            + "and the special characters + _ . - but must start and end with a letter or digit, "
            + "and consecutive special characters are not allowed (e.g. a..b@x.com is invalid). "
            + "The domain must have at least one '.' and a top-level domain of at least 2 letters. "
            + "Max 254 characters. Automatically lowercased.";
    public static final int MAX_LENGTH = 254;
    public static final int MAX_LOCAL_PART_LENGTH = 64;

    private static final String SPECIAL_CHARACTERS = "+_.-";
    // alphanumeric and special characters
    private static final String ALPHANUMERIC_NO_UNDERSCORE = "[^\\W_]+"; // alphanumeric characters except underscore
    private static final String LOCAL_PART_REGEX = "^" + ALPHANUMERIC_NO_UNDERSCORE + "([" + SPECIAL_CHARACTERS + "]"
            + ALPHANUMERIC_NO_UNDERSCORE + ")*";
    private static final String DOMAIN_PART_REGEX = ALPHANUMERIC_NO_UNDERSCORE
            + "(-" + ALPHANUMERIC_NO_UNDERSCORE + ")*";
    // Top-Level Domain must be at least two letters
    private static final String DOMAIN_LAST_PART_REGEX = "[a-zA-Z]{2,}$";
    private static final String DOMAIN_REGEX = "(" + DOMAIN_PART_REGEX + "\\.)+" + DOMAIN_LAST_PART_REGEX;
    public static final String VALIDATION_REGEX = LOCAL_PART_REGEX + "@" + DOMAIN_REGEX;

    public final String value;

    /**
     * Constructs an {@code Email}.
     *
     * @param email A valid email address.
     */
    public Email(String email) {
        requireNonNull(email);
        String trimmedEmail = email.trim().toLowerCase();
        checkArgument(isValidEmail(trimmedEmail), MESSAGE_CONSTRAINTS);
        value = trimmedEmail;
    }

    /**
     * Returns if a given string is a valid email.
     */
    public static boolean isValidEmail(String test) {
        return isWithinLengthLimits(test) && test.matches(VALIDATION_REGEX);
    }

    private static boolean isWithinLengthLimits(String test) {
        return test.length() <= MAX_LENGTH;
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
        if (!(other instanceof Email)) {
            return false;
        }

        Email otherEmail = (Email) other;
        return value.equals(otherEmail.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
