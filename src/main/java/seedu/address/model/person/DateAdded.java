package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.time.temporal.ChronoUnit;

/**
 * Represents a Person's creation date in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidDateAdded(String)}
 */
public class DateAdded implements Comparable<DateAdded> {

    public static final String MESSAGE_CONSTRAINTS =
            "DateAdded should be in the format DD/MM/YYYY HH:mm (e.g. 12/03/2026 20:13)";

    // We strictly follow the 'DD/MM/YYYY HH:mm' format
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/uuuu HH:mm")
            .withResolverStyle(ResolverStyle.STRICT);
    public static final DateTimeFormatter DISPLAY_FORMATTER = DateTimeFormatter.ofPattern("dd MMM yyyy");

    public final String value;
    public final LocalDateTime date; // for comparisons and sorting

    /**
     * Constructs a {@code DateAdded} using the current system time.
     */
    public DateAdded() {
        this.date = LocalDateTime.now()
                .truncatedTo(ChronoUnit.MINUTES);
        this.value = this.date.format(FORMATTER);
    }

    /**
     * Constructs a {@code DateAdded} from a given string.
     *
     * @param dateAdded A valid date added string.
     */
    public DateAdded(String dateAdded) {
        requireNonNull(dateAdded);
        checkArgument(isValidDateAdded(dateAdded), MESSAGE_CONSTRAINTS);
        this.value = dateAdded;
        this.date = LocalDateTime.parse(dateAdded, FORMATTER);
    }

    /**
     * Returns true if a given string is a valid date added.
     */
    public static boolean isValidDateAdded(String test) {
        try {
            LocalDateTime.parse(test, FORMATTER);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Returns the date formatted for UI display.
     */
    public String getDisplayFormat() {
        return date.format(DISPLAY_FORMATTER);
    }

    @Override
    public int compareTo(DateAdded other) {
        return this.date.compareTo(other.date);
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
        if (!(other instanceof DateAdded)) {
            return false;
        }

        DateAdded otherDateAdded = (DateAdded) other;
        return this.date.equals(otherDateAdded.date);
    }

    @Override
    public int hashCode() {
        return date.hashCode();
    }
}
