package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.TagPoolCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.tag.Tag;

public class TagPoolCommandParserTest {

    private final TagPoolCommandParser parser = new TagPoolCommandParser();

    @Test
    public void parse_validSingleAdd_success() throws Exception {
        TagPoolCommand expected = new TagPoolCommand(List.of(new Tag("Frontend")), Collections.emptyList());
        TagPoolCommand result = parser.parse(" at/Frontend");
        assert(expected.equals(result));
    }

    @Test
    public void parse_validSingleDelete_success() throws Exception {
        TagPoolCommand expected = new TagPoolCommand(Collections.emptyList(), List.of(new Tag("Backend")));
        TagPoolCommand result = parser.parse(" dt/Backend");
        assert(expected.equals(result));
    }

    @Test
    public void parse_validMixed_success() throws Exception {
        TagPoolCommand expected = new TagPoolCommand(
                List.of(new Tag("Frontend")), List.of(new Tag("Backend")));
        TagPoolCommand result = parser.parse(" at/Frontend dt/Backend");
        assert(expected.equals(result));
    }

    @Test
    public void parse_noPrefixes_returnsListMode() throws Exception {
        TagPoolCommand expected = new TagPoolCommand();
        TagPoolCommand result = parser.parse("");
        assert(expected.equals(result));
    }

    @Test
    public void parse_blankArgs_returnsListMode() throws Exception {
        TagPoolCommand expected = new TagPoolCommand();
        TagPoolCommand result = parser.parse("   ");
        assert(expected.equals(result));
    }

    @Test
    public void parse_preamblePresent_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                TagPoolCommand.MESSAGE_USAGE), () -> parser.parse("randomPreamble at/Frontend"));
    }

    @Test
    public void parse_invalidTagName_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parse(" at/#invalid"));
    }

    @Test
    public void parse_exceeds10Tags_throwsParseException() {
        String args = " at/T1 at/T2 at/T3 at/T4 at/T5 at/T6 at/T7 at/T8 at/T9 at/T10 at/T11";
        assertThrows(ParseException.class, TagPoolCommandParser.MESSAGE_EXCEEDED_LIMIT, () ->
            parser.parse(args));
    }

    @Test
    public void parse_exactly10Tags_success() throws Exception {
        String args = " at/T1 at/T2 at/T3 at/T4 at/T5 dt/T6 dt/T7 dt/T8 dt/T9 dt/T10";
        // Should not throw
        parser.parse(args);
    }
}
