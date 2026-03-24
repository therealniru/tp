package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Note;

public class JsonAdaptedNoteTest {

    private static final String VALID_HEADING = "Tech Round 1";
    private static final String VALID_CONTENT = "Passed the technical interview flawlessly.";
    private static final LocalDateTime VALID_DATE =
            LocalDateTime.of(2026, 3, 23, 14, 30, 0);

    @Test
    public void toModelType_validNote_retainsExactDateTime() throws Exception {
        Note original = new Note(VALID_HEADING, VALID_CONTENT, VALID_DATE);
        JsonAdaptedNote adapted = new JsonAdaptedNote(original);
        Note restored = adapted.toModelType();

        assertEquals(original.heading, restored.heading);
        assertEquals(original.content, restored.content);
        // Assert exact LocalDateTime down to the second
        assertEquals(original.date.withNano(0), restored.date.withNano(0));
        assertEquals(original, restored);
    }

    @Test
    public void toModelType_roundTrip_preservesAllFields() throws Exception {
        LocalDateTime fixedTime = LocalDateTime.of(2025, 12, 1, 9, 0, 0);
        Note original = new Note("General Note", "Some content", fixedTime);
        JsonAdaptedNote adapted = new JsonAdaptedNote(original);
        Note restored = adapted.toModelType();

        assertEquals("General Note", restored.heading);
        assertEquals("Some content", restored.content);
        assertEquals(fixedTime, restored.date);
    }

    @Test
    public void toModelType_nullHeading_throwsIllegalValueException() {
        JsonAdaptedNote adapted = new JsonAdaptedNote(null, VALID_CONTENT, VALID_DATE.toString());
        assertThrows(IllegalValueException.class, adapted::toModelType);
    }

    @Test
    public void toModelType_nullContent_throwsIllegalValueException() {
        JsonAdaptedNote adapted = new JsonAdaptedNote(VALID_HEADING, null, VALID_DATE.toString());
        assertThrows(IllegalValueException.class, adapted::toModelType);
    }

    @Test
    public void toModelType_nullDate_throwsIllegalValueException() {
        JsonAdaptedNote adapted = new JsonAdaptedNote(VALID_HEADING, VALID_CONTENT, null);
        assertThrows(IllegalValueException.class, adapted::toModelType);
    }

    @Test
    public void constructor_fromNote_storesIso8601DateString() throws Exception {
        Note note = new Note(VALID_HEADING, VALID_CONTENT, VALID_DATE);
        JsonAdaptedNote adapted = new JsonAdaptedNote(note);
        // Verify round-trip: the ISO string can be parsed back to the original date
        Note restored = adapted.toModelType();
        assertEquals(VALID_DATE, restored.date);
    }
}
