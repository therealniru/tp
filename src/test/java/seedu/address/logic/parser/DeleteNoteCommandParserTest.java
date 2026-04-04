package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeleteNoteCommand;

public class DeleteNoteCommandParserTest {

    private final DeleteNoteCommandParser parser = new DeleteNoteCommandParser();

    @Test
    public void parse_validArgs_success() throws Exception {
        DeleteNoteCommand command = parser.parse(" 1 2");
        assertEquals(Index.fromOneBased(1), command.getTargetIndex());
        assertEquals(Index.fromOneBased(2), command.getNoteIndex());
    }

    @Test
    public void parse_validArgsWithExtraSpaces_success() throws Exception {
        DeleteNoteCommand command = parser.parse("  3   1  ");
        assertEquals(Index.fromOneBased(3), command.getTargetIndex());
        assertEquals(Index.fromOneBased(1), command.getNoteIndex());
    }

    @Test
    public void parse_missingNoteIndex_throwsParseException() {
        assertParseFailure(parser, " 1", DeleteNoteCommandParser.MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_noArgs_throwsParseException() {
        assertParseFailure(parser, "   ", DeleteNoteCommandParser.MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_threeArgs_throwsParseException() {
        assertParseFailure(parser, " 1 2 3", DeleteNoteCommandParser.MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidPersonIndex_throwsParseException() {
        assertParseFailure(parser, " 0 1", DeleteNoteCommandParser.MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, " -1 1", DeleteNoteCommandParser.MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, " abc 1", DeleteNoteCommandParser.MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidNoteIndex_throwsParseException() {
        assertParseFailure(parser, " 1 0", DeleteNoteCommandParser.MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, " 1 -1", DeleteNoteCommandParser.MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, " 1 abc", DeleteNoteCommandParser.MESSAGE_INVALID_FORMAT);
    }
}
