package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
}
