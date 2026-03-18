package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's priority status in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidPriority(String)}
 */
public class Priority {

    public static final String MESSAGE_CONSTRAINTS =
            "Priority status must be either 'yes' or 'no' (case-insensitive).";

    /*
     * The priority string should be "yes" or "no".
     */
    public static final String VALIDATION_REGEX = "^(?i)(yes|no)$";

    public final String value;
    public final boolean isPriority;

    /**
     * Constructs a {@code Priority}.
     *
     * @param priority A valid priority status ("yes" or "no").
     */
    public Priority(String priority) {
        requireNonNull(priority);
        checkArgument(isValidPriority(priority), MESSAGE_CONSTRAINTS);
        this.value = priority.toLowerCase();
        this.isPriority = "yes".equals(this.value);
    }

    /**
     * Returns true if a given string is a valid priority status.
     */
    public static boolean isValidPriority(String test) {
        return test.matches(VALIDATION_REGEX);
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
        if (!(other instanceof Priority)) {
            return false;
        }

        Priority otherPriority = (Priority) other;
        return value.equals(otherPriority.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
