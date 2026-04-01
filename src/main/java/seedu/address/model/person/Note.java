package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a Note attached to a Person in the address book.
 * Guarantees: immutable; heading and content are non-null and non-blank.
 */
public class Note {

    public static final int MAX_HEADING_LENGTH = 50;
    public static final int MAX_CONTENT_LENGTH = 500;

    public static final String MESSAGE_HEADING_CONSTRAINTS =
            "Note heading must not be blank, must not contain newline characters,"
            + " and must not exceed " + MAX_HEADING_LENGTH + " characters.";
    public static final String MESSAGE_CONTENT_CONSTRAINTS =
            "Note content must not be blank, must not contain newline characters,"
            + " and must not exceed " + MAX_CONTENT_LENGTH + " characters.";

    public final String heading;
    public final String content;
    public final LocalDateTime date;

    /**
     * Main constructor.
     *
     * @param heading Non-null, non-blank heading for the note.
     * @param content Non-null, non-blank content for the note.
     * @param date    Non-null timestamp for the note.
     */
    public Note(String heading, String content, LocalDateTime date) {
        requireNonNull(heading, "Heading must not be null");
        requireNonNull(content, "Content must not be null");
        requireNonNull(date, "Date must not be null");
        checkArgument(isValidHeading(heading), MESSAGE_HEADING_CONSTRAINTS);
        checkArgument(isValidContent(content), MESSAGE_CONTENT_CONSTRAINTS);
        this.heading = heading;
        this.content = content;
        this.date = date;
    }

    /**
     * Convenience constructor that auto-stamps the current time.
     *
     * @param heading Non-null, non-blank heading for the note.
     * @param content Non-null, non-blank content for the note.
     */
    public Note(String heading, String content) {
        this(heading, content, LocalDateTime.now());
    }

    /**
     * Returns true if the given heading string is valid (non-null, non-blank).
     */
    public static boolean isValidHeading(String heading) {
        return heading != null && !heading.isBlank()
                && !heading.contains("\n") && !heading.contains("\r")
                && heading.length() <= MAX_HEADING_LENGTH;
    }

    /**
     * Returns true if the given content string is valid (non-null, non-blank).
     */
    public static boolean isValidContent(String content) {
        return content != null && !content.isBlank()
                && !content.contains("\n") && !content.contains("\r")
                && content.length() <= MAX_CONTENT_LENGTH;
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
