package seedu.address.logic.parser;

import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.SortDateCommand;
import seedu.address.logic.commands.SortPriorityCommand;

public class SortCommandParserTest {

    private SortCommandParser parser = new SortCommandParser();

    @Test
    public void parse_validArgs_returnsCommand() {
        // Sort priority command
        assertParseSuccess(parser, "pr o/asc", new SortPriorityCommand(true));
        assertParseSuccess(parser, "pr o/desc", new SortPriorityCommand(false));

        // Sort date command
        assertParseSuccess(parser, "date o/asc", new SortDateCommand(true));
        assertParseSuccess(parser, "date o/desc", new SortDateCommand(false));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        // Invalid sort type
        assertParseFailure(parser, "invalid", SortCommandParser.MESSAGE_INVALID_SORT_TYPE);
        assertParseFailure(parser, "", SortCommandParser.MESSAGE_INVALID_SORT_TYPE);

        // Invalid date args
        assertParseFailure(parser, "date o/invalid", SortDateCommandParser.MESSAGE_INVALID_ORDER);

        // Invalid pr args
        assertParseFailure(parser, "pr", SortPriorityCommandParser.MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, "pr o/invalid", SortPriorityCommandParser.MESSAGE_INVALID_ORDER);
    }
}
