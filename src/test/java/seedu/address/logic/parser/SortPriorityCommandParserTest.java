package seedu.address.logic.parser;

import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.SortPriorityCommand;

public class SortPriorityCommandParserTest {

    private SortPriorityCommandParser parser = new SortPriorityCommandParser();

    @Test
    public void parse_validArgs_returnsSortPriorityCommand() {

        // explicit ascending
        assertParseSuccess(parser, "pr o/asc", new SortPriorityCommand(true));

        // explicit descending
        assertParseSuccess(parser, "pr o/desc", new SortPriorityCommand(false));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        // missing order prefix
        assertParseFailure(parser, "pr", SortPriorityCommandParser.MESSAGE_INVALID_FORMAT);

        // invalid preamble
        assertParseFailure(parser, "abc", SortPriorityCommandParser.MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, "prr", SortPriorityCommandParser.MESSAGE_INVALID_FORMAT);

        // invalid order
        assertParseFailure(parser, "pr o/xyz", SortPriorityCommandParser.MESSAGE_INVALID_ORDER);

        // duplicate prefixes
        assertParseFailure(parser, "pr o/asc o/desc", 
                seedu.address.logic.Messages.getErrorMessageForDuplicatePrefixes(seedu.address.logic.parser.CliSyntax.PREFIX_ORDER));
    }
}
