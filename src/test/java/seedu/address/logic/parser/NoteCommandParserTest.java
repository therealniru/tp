package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.NoteCommand;
import seedu.address.logic.parser.exceptions.ParseException;

public class NoteCommandParserTest {

    private final NoteCommandParser parser = new NoteCommandParser();

    @Test
    public void parse_emptyContent_throwsParseException() {
        assertParseFailure(parser, " 1 n/",
                NoteCommandParser.MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_blankContent_throwsParseException() {
        assertParseFailure(parser, " 1 n/   ",
                NoteCommandParser.MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_missingContentPrefix_throwsParseException() {
        assertParseFailure(parser, " 1 some content",
                NoteCommandParser.MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidIndex_throwsParseException() {
        assertParseFailure(parser, " 0 n/content",
                NoteCommandParser.MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, " -1 n/content",
                NoteCommandParser.MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, " abc n/content",
                NoteCommandParser.MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_trailingGarbageInPreamble_throwsParseException() {
        // preamble "1 oops" is not a valid index
        assertParseFailure(parser, " 1 oops n/text",
                NoteCommandParser.MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_defaultHeading_usesGeneralNote() throws Exception {
        NoteCommand cmd = parser.parse(" 1 n/Some content");
        assertEquals("General Note", cmd.getNote().heading);
        assertEquals("Some content", cmd.getNote().content);
        assertEquals(Index.fromOneBased(1), cmd.getTargetIndex());
    }

    @Test
    public void parse_withExplicitHeading_usesGivenHeading() throws Exception {
        NoteCommand cmd = parser.parse(" 2 n/Great candidate h/HR Round");
        assertEquals("HR Round", cmd.getNote().heading);
        assertEquals("Great candidate", cmd.getNote().content);
        assertEquals(Index.fromOneBased(2), cmd.getTargetIndex());
    }

    @Test
    public void parse_fullExample_success() throws Exception {
        NoteCommand cmd = parser.parse(" 1 n/Passed the technical interview flawlessly. h/Tech Round 1");
        assertEquals("Tech Round 1", cmd.getNote().heading);
        assertEquals("Passed the technical interview flawlessly.", cmd.getNote().content);
        assertEquals(Index.fromOneBased(1), cmd.getTargetIndex());
    }

    @Test
    public void parse_missingIndex_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parse(" n/content"));
    }
}
