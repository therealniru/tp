package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class PriorityTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Priority(null));
    }

    @Test
    public void constructor_invalidPriority_throwsIllegalArgumentException() {
        String invalidPriority = "invalid";
        assertThrows(IllegalArgumentException.class, () -> new Priority(invalidPriority));
    }

    @Test
    public void isValidPriority() {
        // null priority status
        assertThrows(NullPointerException.class, () -> Priority.isValidPriority(null));

        // invalid priority status
        assertFalse(Priority.isValidPriority("")); // empty string
        assertFalse(Priority.isValidPriority(" ")); // spaces only
        assertFalse(Priority.isValidPriority("maybe")); // invalid word
        assertFalse(Priority.isValidPriority("yesno")); // extra characters

        // valid priority status
        assertTrue(Priority.isValidPriority("yes"));
        assertTrue(Priority.isValidPriority("no"));
        assertTrue(Priority.isValidPriority("Yes")); // mixed case
        assertTrue(Priority.isValidPriority("nO")); // mixed case
        assertTrue(Priority.isValidPriority("YES")); // uppercase
    }

    @Test
    public void isPriority_correctlyAssigned() {
        Priority priorityYes = new Priority("yes");
        assertTrue(priorityYes.isPriority);

        Priority priorityNo = new Priority("no");
        assertFalse(priorityNo.isPriority);
    }

    @Test
    public void equals() {
        Priority priority = new Priority("yes");

        // same values -> returns true
        assertTrue(priority.equals(new Priority("yes")));
        assertTrue(priority.equals(new Priority("YES")));

        // same object -> returns true
        assertTrue(priority.equals(priority));

        // null -> returns false
        assertFalse(priority.equals(null));

        // different types -> returns false
        assertFalse(priority.equals(5.0f));

        // different values -> returns false
        assertFalse(priority.equals(new Priority("no")));
    }

    @Test
    public void hashCode_correctlyCalculated() {
        Priority priority1 = new Priority("yes");
        Priority priority2 = new Priority("yes");
        Priority priority3 = new Priority("no");

        assertEquals(priority1.hashCode(), priority2.hashCode());
        assertFalse(priority1.hashCode() == priority3.hashCode());
    }

    @Test
    public void toString_returnsCorrectValue() {
        Priority priority = new Priority("yes");
        assertEquals("yes", priority.toString());

        Priority priorityMixedCase = new Priority("YeS");
        assertEquals("yes", priorityMixedCase.toString()); // Constructor converts to lowercase
    }
}
