package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.util.Objects;

/**
 * Represents a Note attached to a Person in the address book.
 * Guarantees: immutable; heading and content are non-null and non-blank.
 */
public class Note {

    public static final int MAX_HEADING_LENGTH = 50;
    public static final int MAX_CONTENT_LENGTH = 500;

    public static final String MESSAGE_HEADING_CONSTRAINTS =
            "Note heading must not be blank, must contain only printable ASCII characters"
            + " (no newlines, tabs, accented letters, emojis, or other non-ASCII input),"
            + " and must not exceed " + MAX_HEADING_LENGTH + " characters.";
    public static final String MESSAGE_CONTENT_CONSTRAINTS =
            "Note content must not be blank, must contain only printable ASCII characters"
            + " (no newlines, tabs, accented letters, emojis, or other non-ASCII input),"
            + " and must not exceed " + MAX_CONTENT_LENGTH + " characters.";

    public final String heading;
    public final String content;

    /**
     * Constructs a {@code Note}.
     *
     * @param heading Non-null, non-blank heading for the note.
     * @param content Non-null, non-blank content for the note.
     */
    public Note(String heading, String content) {
        requireNonNull(heading, "Heading must not be null");
        requireNonNull(content, "Content must not be null");
        checkArgument(isValidHeading(heading), MESSAGE_HEADING_CONSTRAINTS);
        checkArgument(isValidContent(content), MESSAGE_CONTENT_CONSTRAINTS);
        this.heading = heading;
        this.content = content;
    }

    /**
     * Returns true if every character in {@code s} is a printable ASCII character (0x20-0x7E).
     * Rejects tabs, newlines, control characters, and all non-ASCII input (accented letters,
     * CJK, emojis, right-to-left scripts, smart quotes, etc.).
     */
    private static boolean isPrintableAscii(String s) {
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c < 0x20 || c > 0x7E) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns true if the given heading string is valid: non-null, non-blank,
     * printable-ASCII only, and within the maximum length.
     */
    public static boolean isValidHeading(String heading) {
        return heading != null && !heading.isBlank()
                && isPrintableAscii(heading)
                && heading.length() <= MAX_HEADING_LENGTH;
    }

    /**
     * Returns true if the given content string is valid: non-null, non-blank,
     * printable-ASCII only, and within the maximum length.
     */
    public static boolean isValidContent(String content) {
        return content != null && !content.isBlank()
                && isPrintableAscii(content)
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
                && content.equals(otherNote.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(heading, content);
    }

    @Override
    public String toString() {
        return heading + ": " + content;
    }
}
