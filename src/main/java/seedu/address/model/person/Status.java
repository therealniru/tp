package seedu.address.model.person;

/**
 * Represents the status of a candidate in the address book.
 */
public enum Status {
    ACTIVE,
    REJECTED,
    HIRED,
    BLACKLISTED;

    public static final String MESSAGE_CONSTRAINTS =
            "Status must be one of: active, rejected, hired, blacklisted (case-insensitive).";

    /**
     * Returns true if the given string is a valid status.
     */
    public static boolean isValidStatus(String test) {
        try {
            Status.valueOf(test.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Parses the given string into a {@code Status}, case-insensitively.
     *
     * @throws IllegalArgumentException if the string does not match any Status value.
     */
    public static Status parse(String statusString) {
        if (statusString == null) {
            throw new IllegalArgumentException(MESSAGE_CONSTRAINTS);
        }
        try {
            return Status.valueOf(statusString.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(MESSAGE_CONSTRAINTS);
        }
    }
}
