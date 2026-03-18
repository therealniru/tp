package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.TagCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.tag.Tag;

public class TagCommandParserTest {

    private final TagCommandParser parser = new TagCommandParser();

    // ── Success paths ─────────────────────────────────────────────────────────

    @Test
    public void parse_validSingleAdd_success() throws Exception {
        TagCommand result = parser.parse(" 1 a/Java");
        TagCommand expected = new TagCommand(Index.fromOneBased(1),
                List.of(new Tag("Java")), Collections.emptyList());
        assertEquals(expected, result);
    }

    @Test
    public void parse_validSingleDelete_success() throws Exception {
        TagCommand result = parser.parse(" 1 d/Java");
        TagCommand expected = new TagCommand(Index.fromOneBased(1),
                Collections.emptyList(), List.of(new Tag("Java")));
        assertEquals(expected, result);
    }

    @Test
    public void parse_validMixedAddAndDelete_success() throws Exception {
        TagCommand result = parser.parse(" 2 a/Java d/Python");
        TagCommand expected = new TagCommand(Index.fromOneBased(2),
                List.of(new Tag("Java")), List.of(new Tag("Python")));
        assertEquals(expected, result);
    }

    @Test
    public void parse_multipleAdds_success() throws Exception {
        TagCommand result = parser.parse(" 3 a/Java a/Python");
        TagCommand expected = new TagCommand(Index.fromOneBased(3),
                List.of(new Tag("Java"), new Tag("Python")), Collections.emptyList());
        assertEquals(expected, result);
    }

    @Test
    public void parse_multipleDeletes_success() throws Exception {
        TagCommand result = parser.parse(" 1 d/Java d/Python");
        TagCommand expected = new TagCommand(Index.fromOneBased(1),
                Collections.emptyList(), List.of(new Tag("Java"), new Tag("Python")));
        assertEquals(expected, result);
    }

    @Test
    public void parse_exactly10Tags_success() throws Exception {
        String args = " 1 a/T1 a/T2 a/T3 a/T4 a/T5 d/T6 d/T7 d/T8 d/T9 d/T10";
        // Should not throw — exactly at the limit
        parser.parse(args);
    }

    // ── Failure paths ─────────────────────────────────────────────────────────

    @Test
    public void parse_missingIndex_throwsParseException() {
        assertThrows(ParseException.class,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagCommand.MESSAGE_USAGE), ()
                -> parser.parse(" a/Java"));
    }

    @Test
    public void parse_invalidIndex_throwsParseException() {
        assertThrows(ParseException.class,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagCommand.MESSAGE_USAGE), ()
                -> parser.parse(" 0 a/Java"));
    }

    @Test
    public void parse_nonNumericIndex_throwsParseException() {
        assertThrows(ParseException.class,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagCommand.MESSAGE_USAGE), ()
                -> parser.parse(" abc a/Java"));
    }

    @Test
    public void parse_garbageAfterIndex_throwsParseException() {
        assertThrows(ParseException.class,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagCommand.MESSAGE_USAGE), ()
                -> parser.parse(" 1 randomtext a/Java"));
    }

    @Test
    public void parse_noPrefixes_throwsParseException() {
        assertThrows(ParseException.class,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagCommand.MESSAGE_USAGE), ()
                -> parser.parse(" 1"));
    }

    @Test
    public void parse_emptyArgs_throwsParseException() {
        assertThrows(ParseException.class,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagCommand.MESSAGE_USAGE), ()
                -> parser.parse(""));
    }

    @Test
    public void parse_exceeds10Tags_throwsParseException() {
        String args = " 1 a/T1 a/T2 a/T3 a/T4 a/T5 a/T6 a/T7 a/T8 a/T9 a/T10 a/T11";
        assertThrows(ParseException.class, TagCommandParser.MESSAGE_EXCEEDED_LIMIT, ()
                -> parser.parse(args));
    }

    @Test
    public void parse_invalidTagName_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parse(" 1 a/#invalid"));
    }
}
