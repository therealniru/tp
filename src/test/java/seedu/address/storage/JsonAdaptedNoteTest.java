package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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

    @Test
    public void toModelType_invalidDateFormat_throwsIllegalValueException() {
        JsonAdaptedNote adapted = new JsonAdaptedNote(VALID_HEADING, VALID_CONTENT, "invalid-date-format");
        assertThrows(IllegalValueException.class, adapted::toModelType);
    }

    @Test
    public void constructor_fromNote_requiresNonNullNote() {
        assertThrows(NullPointerException.class, () -> new JsonAdaptedNote(null));
    }

    @Test
    public void toModelType_blankHeading_throwsIllegalValueException() {
        JsonAdaptedNote adapted = new JsonAdaptedNote("   ", VALID_CONTENT, VALID_DATE.toString());
        assertThrows(IllegalValueException.class, adapted::toModelType);
    }

    @Test
    public void toModelType_blankContent_throwsIllegalValueException() {
        JsonAdaptedNote adapted = new JsonAdaptedNote(VALID_HEADING, "   ", VALID_DATE.toString());
        assertThrows(IllegalValueException.class, adapted::toModelType);
    }

    @Test
    public void toModelType_headingExceedsMaxLength_throwsIllegalValueException() {
        String longHeading = "a".repeat(51); // Exceeds MAX_HEADING_LENGTH of 50
        JsonAdaptedNote adapted = new JsonAdaptedNote(longHeading, VALID_CONTENT, VALID_DATE.toString());
        assertThrows(IllegalValueException.class, adapted::toModelType);
    }

    @Test
    public void toModelType_contentExceedsMaxLength_throwsIllegalValueException() {
        String longContent = "a".repeat(501); // Exceeds MAX_CONTENT_LENGTH of 500
        JsonAdaptedNote adapted = new JsonAdaptedNote(VALID_HEADING, longContent, VALID_DATE.toString());
        assertThrows(IllegalValueException.class, adapted::toModelType);
    }

    @Test
    public void toModelType_headingAtMaxLength_success() throws Exception {
        String maxHeading = "a".repeat(50); // At MAX_HEADING_LENGTH of 50
        JsonAdaptedNote adapted = new JsonAdaptedNote(maxHeading, VALID_CONTENT, VALID_DATE.toString());
        Note restored = adapted.toModelType();
        assertEquals(maxHeading, restored.heading);
    }

    @Test
    public void toModelType_contentAtMaxLength_success() throws Exception {
        String maxContent = "a".repeat(500); // At MAX_CONTENT_LENGTH of 500
        JsonAdaptedNote adapted = new JsonAdaptedNote(VALID_HEADING, maxContent, VALID_DATE.toString());
        Note restored = adapted.toModelType();
        assertEquals(maxContent, restored.content);
    }

    @Test
    public void toModelType_invalidDateWithTimezone_throwsIllegalValueException() {
        // LocalDateTime.parse() does not accept timezone suffixes like Z or +08:00
        JsonAdaptedNote adapted = new JsonAdaptedNote(VALID_HEADING, VALID_CONTENT,
                "2025-01-01T10:00:00Z");
        assertThrows(IllegalValueException.class, adapted::toModelType);
    }

    @Test
    public void toModelType_invalidDateNonExistentDay_throwsIllegalValueException() {
        // Feb 30 does not exist
        JsonAdaptedNote adapted = new JsonAdaptedNote(VALID_HEADING, VALID_CONTENT,
                "2025-02-30T10:00:00");
        assertThrows(IllegalValueException.class, adapted::toModelType);
    }

    @Test
    public void toModelType_contentWithNewline_throwsIllegalValueException() {
        JsonAdaptedNote adapted = new JsonAdaptedNote(VALID_HEADING, "line1\nline2", VALID_DATE.toString());
        assertThrows(IllegalValueException.class, adapted::toModelType);
    }

    @Test
    public void toModelType_headingWithNewline_throwsIllegalValueException() {
        JsonAdaptedNote adapted = new JsonAdaptedNote("head\ning", VALID_CONTENT, VALID_DATE.toString());
        assertThrows(IllegalValueException.class, adapted::toModelType);
    }

    @Test
    public void toModelType_futureDateNote_clampedToCurrentTime() throws Exception {
        LocalDateTime futureDate = LocalDateTime.now().plusYears(100);
        JsonAdaptedNote adapted = new JsonAdaptedNote(VALID_HEADING, VALID_CONTENT, futureDate.toString());
        Note restored = adapted.toModelType();
        assertEquals(VALID_HEADING, restored.heading);
        assertEquals(VALID_CONTENT, restored.content);
        assertFalse(restored.date.isAfter(LocalDateTime.now()));
    }
}
