package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's address in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidAddress(String)}
 */
public class Address {

    public static final int MAX_LENGTH = 100;
    public static final String MESSAGE_CONSTRAINTS =
            "Addresses must not be blank, must contain only printable ASCII characters"
            + " (letters, digits, spaces, and common punctuation), and must not exceed "
            + MAX_LENGTH + " characters.";

    /*
     * The first character of the address must not be a whitespace (otherwise " " becomes valid),
     * and every character must be a printable ASCII character (0x21-0x7E for the first char,
     * 0x20-0x7E for the rest). This blocks accented letters, CJK, emojis, and other non-ASCII input.
     */
    public static final String VALIDATION_REGEX = "[\\x21-\\x7E][\\x20-\\x7E]*";

    public final String value;

    /**
     * Constructs an {@code Address}.
     *
     * @param address A valid address.
     */
    public Address(String address) {
        requireNonNull(address);
        checkArgument(isValidAddress(address), MESSAGE_CONSTRAINTS);
        value = address;
    }

    /**
     * Returns true if a given string is a valid address.
     */
    public static boolean isValidAddress(String test) {
        return test.matches(VALIDATION_REGEX) && test.length() <= MAX_LENGTH;
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
        if (!(other instanceof Address)) {
            return false;
        }

        Address otherAddress = (Address) other;
        return value.equals(otherAddress.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
