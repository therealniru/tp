package seedu.address.storage;

import static java.util.Objects.requireNonNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Note;

/**
 * Jackson-friendly version of {@link Note}.
 */
class JsonAdaptedNote {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Note's %s field is missing!";

    private final String heading;
    private final String content;

    /**
     * Constructs a {@code JsonAdaptedNote} with the given note details.
     */
    @JsonCreator
    public JsonAdaptedNote(@JsonProperty("heading") String heading,
                           @JsonProperty("content") String content) {
        this.heading = heading;
        this.content = content;
    }

    /**
     * Converts a given {@code Note} into this class for Jackson use.
     */
    public JsonAdaptedNote(Note source) {
        requireNonNull(source);
        heading = source.heading;
        content = source.content;
    }

    /**
     * Converts this Jackson-friendly adapted note object into the model's {@code Note} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted note.
     */
    public Note toModelType() throws IllegalValueException {
        if (heading == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "heading"));
        }
        String sanitizedHeading = heading.replace("\r\n", " ").replace("\n", " ").replace("\r", " ");
        if (!Note.isValidHeading(sanitizedHeading)) {
            throw new IllegalValueException(Note.MESSAGE_HEADING_CONSTRAINTS);
        }
        if (content == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "content"));
        }
        String sanitizedContent = content.replace("\r\n", " ").replace("\n", " ").replace("\r", " ");
        if (!Note.isValidContent(sanitizedContent)) {
            throw new IllegalValueException(Note.MESSAGE_CONTENT_CONSTRAINTS);
        }
        return new Note(sanitizedHeading, sanitizedContent);
    }
}
