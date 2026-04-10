package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.NoteCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Note;

public class NoteCommandParserTest {

    private final NoteCommandParser parser = new NoteCommandParser();

    @Test
    public void parse_emptyContent_throwsParseException() {
        assertParseFailure(parser, " 1 c/",
                NoteCommandParser.MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_blankContent_throwsParseException() {
        assertParseFailure(parser, " 1 c/   ",
                NoteCommandParser.MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_missingContentPrefix_throwsParseException() {
        assertParseFailure(parser, " 1 some content",
                "Invalid command format. Did you forget a prefix? (e.g. c/) \n"
                        + "Usage: addnote INDEX c/CONTENT [h/HEADING]");
    }

    @Test
    public void parse_noContentPrefixWithValidIndex_throwsParseException() {
        assertParseFailure(parser, " 1",
                NoteCommandParser.MESSAGE_MISSING_CONTENT);
        assertParseFailure(parser, " 1 h/Some Heading",
                NoteCommandParser.MESSAGE_MISSING_CONTENT);
    }

    @Test
    public void parse_invalidIndex_throwsParseException() {
        assertParseFailure(parser, " 0 c/content",
                NoteCommandParser.MESSAGE_INVALID_INDEX);
        assertParseFailure(parser, " -1 c/content",
                NoteCommandParser.MESSAGE_INVALID_INDEX);
        assertParseFailure(parser, " abc c/content",
                NoteCommandParser.MESSAGE_INVALID_INDEX);
    }

    @Test
    public void parse_trailingGarbageInPreamble_throwsParseException() {
        // preamble "1 oops" is not a valid index
        assertParseFailure(parser, " 1 oops c/text",
                "Invalid command format. Did you forget a prefix? (e.g. c/) \n"
                        + "Usage: addnote INDEX c/CONTENT [h/HEADING]");
    }

    @Test
    public void parse_defaultHeading_usesGeneralNote() throws Exception {
        NoteCommand cmd = parser.parse(" 1 c/Some content");
        assertEquals("General Note", cmd.getNote().heading);
        assertEquals("Some content", cmd.getNote().content);
        assertEquals(Index.fromOneBased(1), cmd.getTargetIndex());
    }

    @Test
    public void parse_withExplicitHeading_usesGivenHeading() throws Exception {
        NoteCommand cmd = parser.parse(" 2 c/Great candidate h/HR Round");
        assertEquals("HR Round", cmd.getNote().heading);
        assertEquals("Great candidate", cmd.getNote().content);
        assertEquals(Index.fromOneBased(2), cmd.getTargetIndex());
    }

    @Test
    public void parse_fullExample_success() throws Exception {
        NoteCommand cmd = parser.parse(" 1 c/Passed the technical interview flawlessly. h/Tech Round 1");
        assertEquals("Tech Round 1", cmd.getNote().heading);
        assertEquals("Passed the technical interview flawlessly.", cmd.getNote().content);
        assertEquals(Index.fromOneBased(1), cmd.getTargetIndex());
    }

    @Test
    public void parse_missingIndex_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parse(" c/content"));
    }

    @Test
    public void parse_blankHeading_usesDefaultHeading() throws Exception {
        // Blank h/ prefix should fall back to the default heading "General Note"
        NoteCommand cmd = parser.parse(" 1 c/Some content h/   ");
        assertEquals("General Note", cmd.getNote().heading);
        assertEquals("Some content", cmd.getNote().content);
        assertEquals(Index.fromOneBased(1), cmd.getTargetIndex());
    }

    @Test
    public void parse_duplicateContentPrefix_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parse(" 1 c/first c/second"));
    }

    @Test
    public void parse_duplicateHeadingPrefix_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parse(" 1 c/content h/first h/second"));
    }

    @Test
    public void parse_nullArgument_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> parser.parse(null));
    }

    @Test
    public void parse_contentExceedsMaxLength_throwsParseException() {
        // Content with 501 characters (exceeds MAX_CONTENT_LENGTH of 500)
        String longContent = "a".repeat(501);
        assertParseFailure(parser, " 1 c/" + longContent,
                String.format("Error: Note content must not exceed %d characters (currently %d).",
                        500, 501));
    }

    @Test
    public void parse_contentAtMaxLength_success() throws Exception {
        // Content with exactly 500 characters (at MAX_CONTENT_LENGTH)
        String maxContent = "a".repeat(500);
        NoteCommand cmd = parser.parse(" 1 c/" + maxContent);
        assertEquals(maxContent, cmd.getNote().content);
    }

    @Test
    public void parse_headingExceedsMaxLength_throwsParseException() {
        // Heading with 51 characters (exceeds MAX_HEADING_LENGTH of 50)
        String longHeading = "a".repeat(51);
        assertParseFailure(parser, " 1 c/content h/" + longHeading,
                String.format("Error: Note heading must not exceed %d characters (currently %d).",
                        50, 51));
    }

    @Test
    public void parse_headingAtMaxLength_success() throws Exception {
        // Heading with exactly 50 characters (at MAX_HEADING_LENGTH)
        String maxHeading = "a".repeat(50);
        NoteCommand cmd = parser.parse(" 1 c/content h/" + maxHeading);
        assertEquals(maxHeading, cmd.getNote().heading);
    }

    @Test
    public void parse_contentWithNewlines_strippedToSpaces() throws Exception {
        // Newlines in pasted content should be converted to spaces
        NoteCommand cmd = parser.parse(" 1 c/line1\nline2\r\nline3");
        assertEquals("line1 line2 line3", cmd.getNote().content);
    }

    @Test
    public void parse_headingWithNewlines_strippedToSpaces() throws Exception {
        NoteCommand cmd = parser.parse(" 1 c/content h/Tech\nRound");
        assertEquals("Tech Round", cmd.getNote().heading);
    }

    @Test
    public void parse_invalidCharacters_throwsParseException() {
        // Non-ASCII characters should be rejected
        assertParseFailure(parser, " 1 c/Invalid content ©", Note.MESSAGE_CONTENT_CONSTRAINTS);
        assertParseFailure(parser, " 1 c/content h/Invalid heading ™", Note.MESSAGE_HEADING_CONSTRAINTS);
    }
}
