package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Note;

public class JsonAdaptedNoteTest {

    private static final String VALID_HEADING = "Tech Round 1";
    private static final String VALID_CONTENT = "Passed the technical interview flawlessly.";

    @Test
    public void toModelType_validNote_roundTrip() throws Exception {
        Note original = new Note(VALID_HEADING, VALID_CONTENT);
        JsonAdaptedNote adapted = new JsonAdaptedNote(original);
        Note restored = adapted.toModelType();

        assertEquals(original.heading, restored.heading);
        assertEquals(original.content, restored.content);
        assertEquals(original, restored);
    }

    @Test
    public void toModelType_nullHeading_throwsIllegalValueException() {
        JsonAdaptedNote adapted = new JsonAdaptedNote(null, VALID_CONTENT);
        assertThrows(IllegalValueException.class, adapted::toModelType);
    }

    @Test
    public void toModelType_nullContent_throwsIllegalValueException() {
        JsonAdaptedNote adapted = new JsonAdaptedNote(VALID_HEADING, null);
        assertThrows(IllegalValueException.class, adapted::toModelType);
    }

    @Test
    public void constructor_fromNote_requiresNonNullNote() {
        assertThrows(NullPointerException.class, () -> new JsonAdaptedNote(null));
    }

    @Test
    public void toModelType_blankHeading_throwsIllegalValueException() {
        JsonAdaptedNote adapted = new JsonAdaptedNote("   ", VALID_CONTENT);
        assertThrows(IllegalValueException.class, adapted::toModelType);
    }

    @Test
    public void toModelType_blankContent_throwsIllegalValueException() {
        JsonAdaptedNote adapted = new JsonAdaptedNote(VALID_HEADING, "   ");
        assertThrows(IllegalValueException.class, adapted::toModelType);
    }

    @Test
    public void toModelType_headingExceedsMaxLength_throwsIllegalValueException() {
        String longHeading = "a".repeat(51);
        JsonAdaptedNote adapted = new JsonAdaptedNote(longHeading, VALID_CONTENT);
        assertThrows(IllegalValueException.class, adapted::toModelType);
    }

    @Test
    public void toModelType_contentExceedsMaxLength_throwsIllegalValueException() {
        String longContent = "a".repeat(501);
        JsonAdaptedNote adapted = new JsonAdaptedNote(VALID_HEADING, longContent);
        assertThrows(IllegalValueException.class, adapted::toModelType);
    }

    @Test
    public void toModelType_headingAtMaxLength_success() throws Exception {
        String maxHeading = "a".repeat(50);
        JsonAdaptedNote adapted = new JsonAdaptedNote(maxHeading, VALID_CONTENT);
        Note restored = adapted.toModelType();
        assertEquals(maxHeading, restored.heading);
    }

    @Test
    public void toModelType_contentAtMaxLength_success() throws Exception {
        String maxContent = "a".repeat(500);
        JsonAdaptedNote adapted = new JsonAdaptedNote(VALID_HEADING, maxContent);
        Note restored = adapted.toModelType();
        assertEquals(maxContent, restored.content);
    }

    @Test
    public void toModelType_contentWithNewline_success() throws Exception {
        JsonAdaptedNote adapted = new JsonAdaptedNote(VALID_HEADING, "line1\nline2");
        Note restored = adapted.toModelType();
        assertEquals("line1 line2", restored.content);
    }

    @Test
    public void toModelType_headingWithNewline_success() throws Exception {
        JsonAdaptedNote adapted = new JsonAdaptedNote("head\ning", VALID_CONTENT);
        Note restored = adapted.toModelType();
        assertEquals("head ing", restored.heading);
    }
}
