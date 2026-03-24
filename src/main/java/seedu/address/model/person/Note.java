package seedu.address.model.person;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a Note attached to a Person in the address book.
 * Guarantees: immutable.
 */
public class Note {
    public final String heading;
    public final String content;
    public final LocalDateTime date;

    /**
     * Main constructor.
     */
    public Note(String heading, String content, LocalDateTime date) {
        this.heading = heading;
        this.content = content;
        this.date = date;
    }

    /**
     * Convenience constructor that auto-stamps the current time.
     */
    public Note(String heading, String content) {
        this(heading, content, LocalDateTime.now());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Note)) {
            return false;
        }
        Note otherNote = (Note) other;
        return heading.equals(otherNote.heading)
                && content.equals(otherNote.content)
                && date.equals(otherNote.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(heading, content, date);
    }

    @Override
    public String toString() {
        return "[" + date + "] " + heading + ": " + content;
    }
}
