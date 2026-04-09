package seedu.address;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import seedu.address.model.person.Name;
import seedu.address.model.person.Address;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Email;

/**
 * Tests that verify current NFR claims and expose gaps.
 */
public class NFRValidationTest {

    // ── NFR 8: ASCII-only rejection ──
    @Test
    void nfr8_name_rejectsNonAscii() {
        assertFalse(Name.isValidName("caf\u00e9"),       "accented char should be rejected");
        assertFalse(Name.isValidName("\u65e5\u672c"),     "CJK should be rejected");
        assertFalse(Name.isValidName("smile\uD83D\uDE00"),"emoji should be rejected");
        assertFalse(Name.isValidName("\u2018smart"),      "smart quote should be rejected");
    }

    @Test
    void nfr8_address_rejectsNonAscii() {
        assertFalse(Address.isValidAddress("caf\u00e9 Street"), "accented char in address rejected");
        assertFalse(Address.isValidAddress("\u65e5\u672c"),      "CJK in address rejected");
    }

    // ── Name: what the FRIEND's UG explicitly allows that WE currently block ──
    @Test
    void name_parentheses_allowed() {
        // Friend's UG allows () in names e.g. "James Wong (Jimmy)"
        assertTrue(Name.isValidName("James Wong (Jimmy)"));
    }

    @Test
    void name_numbers_allowed() {
        // Friend's UG allows numbers e.g. "John Smith 3rd"
        assertTrue(Name.isValidName("John Smith 3rd"));
    }

    @Test
    void name_apostropheAndComma_alreadyAllowed() {
        assertTrue(Name.isValidName("O'Brien"),    "apostrophe should be allowed");
        assertTrue(Name.isValidName("James, Jr."), "comma should be allowed");
        assertTrue(Name.isValidName("Mary-Jane"),  "hyphen should be allowed");
        assertTrue(Name.isValidName("Dr. Smith"),  "period should be allowed");
    }

    @Test
    void name_atSymbol_alreadyAllowed() {
        assertTrue(Name.isValidName("user@domain"), "@ should be allowed");
    }

    // ── Address: 100-char limit too short for real addresses ──
    @Test
    void address_200charLimitAllowsLongRealAddr() {
        String longRealAddr = "Flat 12, Block C, Prestige Shantiniketan, Whitefield Main Road, Mahadevapura, Bangalore, Karnataka 560048";
        assertTrue(longRealAddr.length() > 100, "test address must be >100 chars, actual=" + longRealAddr.length());
        assertTrue(longRealAddr.length() <= 200, "test address fits in 200 chars");
        // Address is now allowed because limit is 200
        assertTrue(Address.isValidAddress(longRealAddr));
        // All characters are printable ASCII — only the length cap is the problem
        assertTrue(longRealAddr.chars().allMatch(c -> c >= 0x20 && c <= 0x7E),
                "all chars are printable ASCII; only length cap causes failure");
    }

    @Test
    void address_hashAndSpecialChars_allowed() {
        assertTrue(Address.isValidAddress("311, Clementi Ave 2, #02-25"), "# allowed in address");
        assertTrue(Address.isValidAddress("Blk 5, Level 3/F, Tower A"),   "/ allowed in address");
    }

    // ── Phone: real international formats ──
    @Test
    void phone_internationalFormats_accepted() {
        assertTrue(Phone.isValidPhone("+65-9123-4567"),    "SG international format");
        assertTrue(Phone.isValidPhone("+1 (415) 555-2671"),"US international format");
        assertTrue(Phone.isValidPhone("+62 812 5555 1234"),"ID international format");
        assertTrue(Phone.isValidPhone("91234567"),         "basic 8-digit");
    }

    // ── JAR size: NFR 1 ──
    @Test
    void nfr1_jarSizeUnder100MB() {
        java.io.File jar = new java.io.File("build/libs/talently.jar");
        if (jar.exists()) {
            long sizeMB = jar.length() / (1024 * 1024);
            assertTrue(sizeMB < 100, "JAR size " + sizeMB + "MB exceeds 100MB NFR");
        }
        // If jar doesn't exist yet, skip (not a failure)
    }
}
