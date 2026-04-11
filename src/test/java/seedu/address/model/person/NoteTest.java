package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class NoteTest {

    private static final String HEADING = "Tech Round 1";
    private static final String CONTENT = "Passed the technical interview.";

    @Test
    public void constructor_twoArgs_setsFields() {
        Note note = new Note(HEADING, CONTENT);
        assertEquals(HEADING, note.heading);
        assertEquals(CONTENT, note.content);
    }

    @Test
    public void equals_sameObject_returnsTrue() {
        Note note = new Note(HEADING, CONTENT);
        assertTrue(note.equals(note));
    }

    @Test
    public void equals_sameValues_returnsTrue() {
        Note note1 = new Note(HEADING, CONTENT);
        Note note2 = new Note(HEADING, CONTENT);
        assertTrue(note1.equals(note2));
    }

    @Test
    public void equals_null_returnsFalse() {
        Note note = new Note(HEADING, CONTENT);
        assertFalse(note.equals(null));
    }

    @Test
    public void equals_differentType_returnsFalse() {
        Note note = new Note(HEADING, CONTENT);
        assertFalse(note.equals("not a note"));
        assertFalse(note.equals(42));
    }

    @Test
    public void equals_differentHeading_returnsFalse() {
        Note note1 = new Note(HEADING, CONTENT);
        Note note2 = new Note("Different Heading", CONTENT);
        assertFalse(note1.equals(note2));
    }

    @Test
    public void equals_differentContent_returnsFalse() {
        Note note1 = new Note(HEADING, CONTENT);
        Note note2 = new Note(HEADING, "Different content.");
        assertFalse(note1.equals(note2));
    }

    @Test
    public void hashCode_equalNotes_haveSameHashCode() {
        Note note1 = new Note(HEADING, CONTENT);
        Note note2 = new Note(HEADING, CONTENT);
        assertEquals(note1.hashCode(), note2.hashCode());
    }

    @Test
    public void hashCode_unequalNotes_typicallyDifferentHashCodes() {
        Note note1 = new Note(HEADING, CONTENT);
        Note note2 = new Note("Other", CONTENT);
        assertNotEquals(note1.hashCode(), note2.hashCode());
    }

    @Test
    public void toString_formatsCorrectly() {
        Note note = new Note(HEADING, CONTENT);
        assertEquals(HEADING + ": " + CONTENT, note.toString());
    }

    @Test
    public void hashCode_notNull() {
        Note note = new Note(HEADING, CONTENT);
        assertNotNull(note.hashCode());
    }

    @Test
    public void isValidHeading_validHeadings_returnsTrue() {
        assertTrue(Note.isValidHeading("Tech Round 1"));
        assertTrue(Note.isValidHeading("General Note"));
        assertTrue(Note.isValidHeading("a"));
        assertTrue(Note.isValidHeading("   text with spaces   "));
    }

    @Test
    public void isValidHeading_blankHeadings_returnsFalse() {
        assertFalse(Note.isValidHeading(""));
        assertFalse(Note.isValidHeading("   "));
        assertFalse(Note.isValidHeading("\t"));
        assertFalse(Note.isValidHeading("\n"));
    }

    @Test
    public void isValidHeading_nullHeading_returnsFalse() {
        assertFalse(Note.isValidHeading(null));
    }

    @Test
    public void isValidContent_validContents_returnsTrue() {
        assertTrue(Note.isValidContent("Passed the interview"));
        assertTrue(Note.isValidContent("Some content here"));
        assertTrue(Note.isValidContent("x"));
        assertTrue(Note.isValidContent("   text with spaces   "));
    }

    @Test
    public void isValidContent_blankContents_returnsFalse() {
        assertFalse(Note.isValidContent(""));
        assertFalse(Note.isValidContent("   "));
        assertFalse(Note.isValidContent("\t"));
        assertFalse(Note.isValidContent("\n"));
    }

    @Test
    public void isValidContent_nullContent_returnsFalse() {
        assertFalse(Note.isValidContent(null));
    }

    @Test
    public void isValidContent_containsNewline_returnsFalse() {
        assertFalse(Note.isValidContent("hello\nworld"));
        assertFalse(Note.isValidContent("hello\rworld"));
        assertFalse(Note.isValidContent("hello\r\nworld"));
    }

    @Test
    public void isValidHeading_containsNewline_returnsFalse() {
        assertFalse(Note.isValidHeading("Tech\nRound"));
        assertFalse(Note.isValidHeading("Tech\rRound"));
    }

    @Test
    public void constructor_requiresNonNullHeading() {
        assertThrows(NullPointerException.class, () -> new Note(null, CONTENT));
    }

    @Test
    public void constructor_requiresNonNullContent() {
        assertThrows(NullPointerException.class, () -> new Note(HEADING, null));
    }

    @Test
    public void constructor_rejectsBlankHeading() {
        assertThrows(IllegalArgumentException.class, () -> new Note("   ", CONTENT));
    }

    @Test
    public void constructor_rejectsBlankContent() {
        assertThrows(IllegalArgumentException.class, () -> new Note(HEADING, "   "));
    }

    @Test
    public void isValidHeading_atMaxLength_returnsTrue() {
        String maxHeading = "a".repeat(50);
        assertTrue(Note.isValidHeading(maxHeading));
    }

    @Test
    public void isValidHeading_exceedsMaxLength_returnsFalse() {
        String tooLongHeading = "a".repeat(51);
        assertFalse(Note.isValidHeading(tooLongHeading));
    }

    @Test
    public void isValidContent_atMaxLength_returnsTrue() {
        String maxContent = "a".repeat(500);
        assertTrue(Note.isValidContent(maxContent));
    }

    @Test
    public void isValidContent_exceedsMaxLength_returnsFalse() {
        String tooLongContent = "a".repeat(501);
        assertFalse(Note.isValidContent(tooLongContent));
    }

    @Test
    public void constructor_rejectsHeadingExceedingMaxLength() {
        String tooLongHeading = "a".repeat(51);
        assertThrows(IllegalArgumentException.class, () -> new Note(tooLongHeading, CONTENT));
    }

    @Test
    public void constructor_rejectsContentExceedingMaxLength() {
        String tooLongContent = "a".repeat(501);
        assertThrows(IllegalArgumentException.class, () -> new Note(HEADING, tooLongContent));
    }

    @Test
    public void constructor_acceptsHeadingAtMaxLength() {
        String maxHeading = "a".repeat(50);
        Note note = new Note(maxHeading, CONTENT);
        assertEquals(maxHeading, note.heading);
    }

    @Test
    public void constructor_acceptsContentAtMaxLength() {
        String maxContent = "a".repeat(500);
        Note note = new Note(HEADING, maxContent);
        assertEquals(maxContent, note.content);
    }
}
