package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditNoteCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Note;

public class EditNoteCommandParserTest {

    private final EditNoteCommandParser parser = new EditNoteCommandParser();

    @Test
    public void parse_contentOnly_success() throws Exception {
        EditNoteCommand command = parser.parse(" 1 2 c/New content");
        assertEquals(Index.fromOneBased(1), command.getTargetIndex());
        assertEquals(Index.fromOneBased(2), command.getNoteIndex());
        assertEquals("New content", command.getNewContent());
        assertNull(command.getNewHeading());
    }

    @Test
    public void parse_headingOnly_success() throws Exception {
        EditNoteCommand command = parser.parse(" 1 2 h/New heading");
        assertEquals(Index.fromOneBased(1), command.getTargetIndex());
        assertEquals(Index.fromOneBased(2), command.getNoteIndex());
        assertNull(command.getNewContent());
        assertEquals("New heading", command.getNewHeading());
    }

    @Test
    public void parse_bothContentAndHeading_success() throws Exception {
        EditNoteCommand command = parser.parse(" 1 2 c/New content h/New heading");
        assertEquals(Index.fromOneBased(1), command.getTargetIndex());
        assertEquals(Index.fromOneBased(2), command.getNoteIndex());
        assertEquals("New content", command.getNewContent());
        assertEquals("New heading", command.getNewHeading());
    }

    @Test
    public void parse_neitherContentNorHeading_throwsParseException() {
        assertParseFailure(parser, " 1 2", EditNoteCommand.MESSAGE_NOT_EDITED);
    }

    @Test
    public void parse_invalidPersonIndex_throwsParseException() {
        assertParseFailure(parser, " 0 1 c/content", EditNoteCommandParser.MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, " -1 1 c/content", EditNoteCommandParser.MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, " abc 1 c/content", EditNoteCommandParser.MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidNoteIndex_throwsParseException() {
        assertParseFailure(parser, " 1 0 c/content", EditNoteCommandParser.MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, " 1 -1 c/content", EditNoteCommandParser.MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, " 1 abc c/content", EditNoteCommandParser.MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_missingNoteIndex_throwsParseException() {
        assertParseFailure(parser, " 1 c/content", EditNoteCommandParser.MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_noArgs_throwsParseException() {
        assertParseFailure(parser, "   ", EditNoteCommandParser.MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_blankContent_throwsParseException() {
        assertParseFailure(parser, " 1 2 c/   ",
                "Error: Note content cannot be blank. "
                + "Usage: editnote INDEX NOTE_INDEX [c/CONTENT] [h/HEADING]");
    }

    @Test
    public void parse_blankHeading_usesDefaultHeading() throws Exception {
        // Blank h/ should silently default to "General Note", consistent with addnote behaviour.
        EditNoteCommand command = parser.parse(" 1 2 h/   ");
        assertEquals(NoteCommandParser.DEFAULT_HEADING, command.getNewHeading());
    }

    @Test
    public void parse_contentExceedsMaxLength_throwsParseException() {
        String longContent = "a".repeat(501);
        assertParseFailure(parser, " 1 2 c/" + longContent,
                String.format("Error: Note content must not exceed %d characters (currently %d).",
                        Note.MAX_CONTENT_LENGTH, 501));
    }

    @Test
    public void parse_headingExceedsMaxLength_throwsParseException() {
        String longHeading = "a".repeat(51);
        assertParseFailure(parser, " 1 2 h/" + longHeading,
                String.format("Error: Note heading must not exceed %d characters (currently %d).",
                        Note.MAX_HEADING_LENGTH, 51));
    }

    @Test
    public void parse_duplicateContentPrefix_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parse(" 1 2 c/first c/second"));
    }

    @Test
    public void parse_duplicateHeadingPrefix_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parse(" 1 2 c/content h/first h/second"));
    }

    @Test
    public void parse_contentWithNewlines_strippedToSpaces() throws Exception {
        EditNoteCommand command = parser.parse(" 1 2 c/line1\nline2\r\nline3");
        assertEquals("line1 line2 line3", command.getNewContent());
    }

    @Test
    public void parse_headingWithNewlines_strippedToSpaces() throws Exception {
        EditNoteCommand command = parser.parse(" 1 2 h/Tech\nRound");
        assertEquals("Tech Round", command.getNewHeading());
    }

    @Test
    public void parse_contentAtMaxLength_success() throws Exception {
        String maxContent = "a".repeat(500);
        EditNoteCommand command = parser.parse(" 1 2 c/" + maxContent);
        assertEquals(maxContent, command.getNewContent());
    }

    @Test
    public void parse_headingAtMaxLength_success() throws Exception {
        String maxHeading = "a".repeat(50);
        EditNoteCommand command = parser.parse(" 1 2 h/" + maxHeading);
        assertEquals(maxHeading, command.getNewHeading());
    }

    @Test
    public void parse_invalidCharacters_throwsParseException() {
        // Non-ASCII characters should be rejected
        assertParseFailure(parser, " 1 2 c/Invalid content ©", Note.MESSAGE_CONTENT_CONSTRAINTS);
        assertParseFailure(parser, " 1 2 h/Invalid heading ™", Note.MESSAGE_HEADING_CONSTRAINTS);
    }

    @Test
    public void parse_preambleWrongTokenCount_throwsParseException() {
        // Only 1 token in preamble (need 2: index and noteIndex)
        assertParseFailure(parser, " 1 c/content", EditNoteCommandParser.MESSAGE_INVALID_FORMAT);
        // 3 tokens in preamble
        assertParseFailure(parser, " 1 2 3 c/content", EditNoteCommandParser.MESSAGE_INVALID_FORMAT);
    }
}
