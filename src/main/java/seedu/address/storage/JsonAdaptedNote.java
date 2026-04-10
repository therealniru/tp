package seedu.address.storage;

import static java.util.Objects.requireNonNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Note;

/**
 * Jackson-friendly version of {@link Note}.
 */
class JsonAdaptedNote {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Note's %s field is missing!";

    private static final Logger logger = LogsCenter.getLogger(JsonAdaptedNote.class);

    private final String heading;
    private final String content;
    private final String date;

    /**
     * Constructs a {@code JsonAdaptedNote} with the given note details.
     */
    @JsonCreator
    public JsonAdaptedNote(@JsonProperty("heading") String heading,
                           @JsonProperty("content") String content,
                           @JsonProperty("date") String date) {
        this.heading = heading;
        this.content = content;
        this.date = date;
    }

    /**
     * Converts a given {@code Note} into this class for Jackson use.
     */
    public JsonAdaptedNote(Note source) {
        requireNonNull(source);
        heading = source.heading;
        content = source.content;
        date = source.date.toString();
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
        if (!Note.isValidHeading(heading)) {
            throw new IllegalValueException(Note.MESSAGE_HEADING_CONSTRAINTS);
        }
        if (content == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "content"));
        }
        if (!Note.isValidContent(content)) {
            throw new IllegalValueException(Note.MESSAGE_CONTENT_CONSTRAINTS);
        }
        if (date == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "date"));
        }

        LocalDateTime parsedDate;
        try {
            parsedDate = LocalDateTime.parse(date);
        } catch (DateTimeParseException e) {
            logger.warning("Invalid date format in note: " + date);
            throw new IllegalValueException("Note's date is in an invalid format: " + date);
        }

        LocalDateTime now = LocalDateTime.now();
        if (parsedDate.isAfter(now)) {
            logger.warning("Note date is in the future (" + parsedDate + "). Clamping to current time.");
            parsedDate = now;
        }

        return new Note(heading, content, parsedDate);
    }
}
