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
        TagCommand result = parser.parse(" 1 at/Java");
        TagCommand expected = new TagCommand(Index.fromOneBased(1),
                List.of(new Tag("Java")), Collections.emptyList());
        assertEquals(expected, result);
    }

    @Test
    public void parse_validSingleDelete_success() throws Exception {
        TagCommand result = parser.parse(" 1 dt/Java");
        TagCommand expected = new TagCommand(Index.fromOneBased(1),
                Collections.emptyList(), List.of(new Tag("Java")));
        assertEquals(expected, result);
    }

    @Test
    public void parse_validMixedAddAndDelete_success() throws Exception {
        TagCommand result = parser.parse(" 2 at/Java dt/Python");
        TagCommand expected = new TagCommand(Index.fromOneBased(2),
                List.of(new Tag("Java")), List.of(new Tag("Python")));
        assertEquals(expected, result);
    }

    @Test
    public void parse_multipleIndices_success() throws Exception {
        TagCommand result = parser.parse(" 1,2,3 at/Java dt/Python");
        TagCommand expected = new TagCommand(List.of(
                Index.fromOneBased(1), Index.fromOneBased(2), Index.fromOneBased(3)),
                List.of(new Tag("Java")), List.of(new Tag("Python")));
        assertEquals(expected, result);
    }

    @Test
    public void parse_multipleAdds_success() throws Exception {
        TagCommand result = parser.parse(" 3 at/Java at/Python");
        TagCommand expected = new TagCommand(Index.fromOneBased(3),
                List.of(new Tag("Java"), new Tag("Python")), Collections.emptyList());
        assertEquals(expected, result);
    }

    @Test
    public void parse_multipleDeletes_success() throws Exception {
        TagCommand result = parser.parse(" 1 dt/Java dt/Python");
        TagCommand expected = new TagCommand(Index.fromOneBased(1),
                Collections.emptyList(), List.of(new Tag("Java"), new Tag("Python")));
        assertEquals(expected, result);
    }

    @Test
    public void parse_exactly10Tags_success() throws Exception {
        String args = " 1 at/T1 at/T2 at/T3 at/T4 at/T5 dt/T6 dt/T7 dt/T8 dt/T9 dt/T10";
        // Should not throw — exactly at the limit
        parser.parse(args);
    }

    // ── Failure paths ─────────────────────────────────────────────────────────

    @Test
    public void parse_missingIndex_throwsParseException() {
        assertThrows(ParseException.class,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagCommand.MESSAGE_USAGE), ()
                        -> parser.parse(" at/Java"));
    }

    @Test
    public void parse_invalidIndex_throwsParseException() {
        assertThrows(ParseException.class,
                ParserUtil.MESSAGE_INVALID_INDEX, ()
                        -> parser.parse(" 0 at/Java"));
    }

    @Test
    public void parse_nonNumericIndex_throwsParseException() {
        assertThrows(ParseException.class,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagCommand.MESSAGE_USAGE), ()
                        -> parser.parse(" abc at/Java"));
    }

    @Test
    public void parse_garbageAfterIndex_throwsParseException() {
        assertThrows(ParseException.class,
                "Invalid command format. Did you forget a prefix? (e.g. at/ or dt/) \n"
                        + TagCommand.MESSAGE_USAGE, ()
                        -> parser.parse(" 1 randomtext at/Java"));
    }

    @Test
    public void parse_invalidCommaSeparatedIndices_throwsParseException() {
        assertThrows(ParseException.class,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagCommand.MESSAGE_USAGE), ()
                        -> parser.parse(" 1,,2 at/Java"));
    }

    @Test
    public void parse_duplicateIndices_throwsParseException() {
        assertThrows(ParseException.class,
                TagCommandParser.MESSAGE_DUPLICATE_INDICES, ()
                        -> parser.parse(" 1,1 at/Java"));
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
        String args = " 1 at/T1 at/T2 at/T3 at/T4 at/T5 at/T6 at/T7 at/T8 at/T9 at/T10 at/T11";
        assertThrows(ParseException.class, TagCommandParser.MESSAGE_EXCEEDED_LIMIT, ()
                -> parser.parse(args));
    }

    @Test
    public void parse_invalidTagName_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parse(" 1 at/#invalid"));
    }
}
