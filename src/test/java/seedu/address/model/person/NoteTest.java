package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

public class NoteTest {

    private static final LocalDateTime FIXED_DATE = LocalDateTime.of(2026, 3, 23, 14, 30, 0);
    private static final String HEADING = "Tech Round 1";
    private static final String CONTENT = "Passed the technical interview.";

    @Test
    public void constructor_threeArgs_setsAllFields() {
        Note note = new Note(HEADING, CONTENT, FIXED_DATE);
        assertEquals(HEADING, note.heading);
        assertEquals(CONTENT, note.content);
        assertEquals(FIXED_DATE, note.date);
    }

    @Test
    public void constructor_twoArgs_setsDateToNow() {
        LocalDateTime before = LocalDateTime.now();
        Note note = new Note(HEADING, CONTENT);
        LocalDateTime after = LocalDateTime.now();

        assertEquals(HEADING, note.heading);
        assertEquals(CONTENT, note.content);
        // date should be between before and after (inclusive)
        assertFalse(note.date.isBefore(before));
        assertFalse(note.date.isAfter(after));
    }

    @Test
    public void equals_sameObject_returnsTrue() {
        Note note = new Note(HEADING, CONTENT, FIXED_DATE);
        assertTrue(note.equals(note));
    }

    @Test
    public void equals_sameValues_returnsTrue() {
        Note note1 = new Note(HEADING, CONTENT, FIXED_DATE);
        Note note2 = new Note(HEADING, CONTENT, FIXED_DATE);
        assertTrue(note1.equals(note2));
    }

    @Test
    public void equals_null_returnsFalse() {
        Note note = new Note(HEADING, CONTENT, FIXED_DATE);
        assertFalse(note.equals(null));
    }

    @Test
    public void equals_differentType_returnsFalse() {
        Note note = new Note(HEADING, CONTENT, FIXED_DATE);
        assertFalse(note.equals("not a note"));
        assertFalse(note.equals(42));
    }

    @Test
    public void equals_differentHeading_returnsFalse() {
        Note note1 = new Note(HEADING, CONTENT, FIXED_DATE);
        Note note2 = new Note("Different Heading", CONTENT, FIXED_DATE);
        assertFalse(note1.equals(note2));
    }

    @Test
    public void equals_differentContent_returnsFalse() {
        Note note1 = new Note(HEADING, CONTENT, FIXED_DATE);
        Note note2 = new Note(HEADING, "Different content.", FIXED_DATE);
        assertFalse(note1.equals(note2));
    }

    @Test
    public void equals_differentDate_returnsFalse() {
        Note note1 = new Note(HEADING, CONTENT, FIXED_DATE);
        Note note2 = new Note(HEADING, CONTENT, FIXED_DATE.plusDays(1));
        assertFalse(note1.equals(note2));
    }

    @Test
    public void hashCode_equalNotes_haveSameHashCode() {
        Note note1 = new Note(HEADING, CONTENT, FIXED_DATE);
        Note note2 = new Note(HEADING, CONTENT, FIXED_DATE);
        assertEquals(note1.hashCode(), note2.hashCode());
    }

    @Test
    public void hashCode_unequalNotes_typicallyDifferentHashCodes() {
        Note note1 = new Note(HEADING, CONTENT, FIXED_DATE);
        Note note2 = new Note("Other", CONTENT, FIXED_DATE);
        assertNotEquals(note1.hashCode(), note2.hashCode());
    }

    @Test
    public void toString_formatsCorrectly() {
        Note note = new Note(HEADING, CONTENT, FIXED_DATE);
        String result = note.toString();
        assertTrue(result.contains(HEADING));
        assertTrue(result.contains(CONTENT));
        assertTrue(result.contains(FIXED_DATE.toString()));
        assertEquals("[" + FIXED_DATE + "] " + HEADING + ": " + CONTENT, result);
    }

    @Test
    public void hashCode_notNull() {
        Note note = new Note(HEADING, CONTENT, FIXED_DATE);
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
        assertThrows(NullPointerException.class, () -> new Note(null, CONTENT, FIXED_DATE));
    }

    @Test
    public void constructor_requiresNonNullContent() {
        assertThrows(NullPointerException.class, () -> new Note(HEADING, null, FIXED_DATE));
    }

    @Test
    public void constructor_requiresNonNullDate() {
        assertThrows(NullPointerException.class, () -> new Note(HEADING, CONTENT, null));
    }

    @Test
    public void constructor_rejectsBlankHeading() {
        assertThrows(IllegalArgumentException.class, () -> new Note("   ", CONTENT, FIXED_DATE));
    }

    @Test
    public void constructor_rejectsBlankContent() {
        assertThrows(IllegalArgumentException.class, () -> new Note(HEADING, "   ", FIXED_DATE));
    }

    @Test
    public void isValidHeading_atMaxLength_returnsTrue() {
        String maxHeading = "a".repeat(50); // Exactly MAX_HEADING_LENGTH
        assertTrue(Note.isValidHeading(maxHeading));
    }

    @Test
    public void isValidHeading_exceedsMaxLength_returnsFalse() {
        String tooLongHeading = "a".repeat(51); // Exceeds MAX_HEADING_LENGTH
        assertFalse(Note.isValidHeading(tooLongHeading));
    }

    @Test
    public void isValidContent_atMaxLength_returnsTrue() {
        String maxContent = "a".repeat(500); // Exactly MAX_CONTENT_LENGTH
        assertTrue(Note.isValidContent(maxContent));
    }

    @Test
    public void isValidContent_exceedsMaxLength_returnsFalse() {
        String tooLongContent = "a".repeat(501); // Exceeds MAX_CONTENT_LENGTH
        assertFalse(Note.isValidContent(tooLongContent));
    }

    @Test
    public void constructor_rejectsHeadingExceedingMaxLength() {
        String tooLongHeading = "a".repeat(51); // Exceeds MAX_HEADING_LENGTH
        assertThrows(IllegalArgumentException.class, () -> new Note(tooLongHeading, CONTENT, FIXED_DATE));
    }

    @Test
    public void constructor_rejectsContentExceedingMaxLength() {
        String tooLongContent = "a".repeat(501); // Exceeds MAX_CONTENT_LENGTH
        assertThrows(IllegalArgumentException.class, () -> new Note(HEADING, tooLongContent, FIXED_DATE));
    }

    @Test
    public void constructor_acceptsHeadingAtMaxLength() {
        String maxHeading = "a".repeat(50); // Exactly MAX_HEADING_LENGTH
        Note note = new Note(maxHeading, CONTENT, FIXED_DATE);
        assertEquals(maxHeading, note.heading);
    }

    @Test
    public void constructor_acceptsContentAtMaxLength() {
        String maxContent = "a".repeat(500); // Exactly MAX_CONTENT_LENGTH
        Note note = new Note(HEADING, maxContent, FIXED_DATE);
        assertEquals(maxContent, note.content);
    }
}
