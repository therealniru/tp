package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class DateAddedTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new DateAdded((String) null));
    }

    @Test
    public void constructor_invalidDateAdded_throwsIllegalArgumentException() {
        String invalidDate = "2024-12-01";
        assertThrows(IllegalArgumentException.class, () -> new DateAdded(invalidDate));
    }

    @Test
    public void constructor_truncatesToMinutes() {
        DateAdded defaultDate = new DateAdded();
        assertEquals(0, defaultDate.date.getSecond(), "Seconds should be truncated to 0");
        assertEquals(0, defaultDate.date.getNano(), "Nanoseconds should be truncated to 0");
    }

    @Test
    public void isValidDateAdded() {
        // null date
        assertThrows(NullPointerException.class, () -> DateAdded.isValidDateAdded(null));

        // invalid dates
        assertFalse(DateAdded.isValidDateAdded("")); // empty string
        assertFalse(DateAdded.isValidDateAdded(" ")); // spaces only
        assertFalse(DateAdded.isValidDateAdded("2024-01-01")); // incorrect format
        assertFalse(DateAdded.isValidDateAdded("01-01-2024 10:00")); // dashes instead of slashes
        assertFalse(DateAdded.isValidDateAdded("32/01/2024 10:00")); // invalid day
        assertFalse(DateAdded.isValidDateAdded("01/13/2024 10:00")); // invalid month
        assertFalse(DateAdded.isValidDateAdded("31/04/2024 10:00")); // April has 30 days
        assertFalse(DateAdded.isValidDateAdded("29/02/2023 10:00")); // 2023 is not a leap year

        // valid dates
        assertTrue(DateAdded.isValidDateAdded("01/01/2024 10:00")); // Typical valid format
        assertTrue(DateAdded.isValidDateAdded("31/12/2025 23:59")); // End of year
    }

    @Test
    public void compareTo() {
        DateAdded earlier = new DateAdded("01/01/2024 10:00");
        DateAdded later = new DateAdded("02/01/2024 10:00");
        DateAdded sameAsEarlier = new DateAdded("01/01/2024 10:00");

        assertTrue(earlier.compareTo(later) < 0);
        assertTrue(later.compareTo(earlier) > 0);
        assertTrue(earlier.compareTo(sameAsEarlier) == 0);
    }

    @Test
    public void stringRepresentation() {
        DateAdded date = new DateAdded("01/01/2024 10:00");
        assertEquals("01/01/2024 10:00", date.toString());
        assertEquals("01/01/2024 10:00", date.value);
    }

    @Test
    public void getDisplayFormat() {
        DateAdded date = new DateAdded("01/01/2024 10:00");
        assertEquals("01 Jan 2024", date.getDisplayFormat());
    }

    @Test
    public void equals() {
        DateAdded date1 = new DateAdded("01/01/2024 10:00");
        DateAdded date2 = new DateAdded("01/01/2024 10:00");
        DateAdded date3 = new DateAdded("02/01/2024 10:00");

        // same object -> returns true
        assertTrue(date1.equals(date1));

        // same values -> returns true
        assertTrue(date1.equals(date2));

        // different types -> returns false
        assertFalse(date1.equals(1));

        // null -> returns false
        assertFalse(date1.equals(null));

        // different values -> returns false
        assertFalse(date1.equals(date3));
    }

    @Test
    public void hashCodeMethod() {
        DateAdded date1 = new DateAdded("01/01/2024 10:00");
        DateAdded date2 = new DateAdded("01/01/2024 10:00");

        // same values -> same hash code
        assertEquals(date1.hashCode(), date2.hashCode());
    }
}
