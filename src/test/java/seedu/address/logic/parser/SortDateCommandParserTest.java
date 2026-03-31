package seedu.address.logic.parser;

import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.SortDateCommand;

public class SortDateCommandParserTest {

    private SortDateCommandParser parser = new SortDateCommandParser();

    @Test
    public void parse_validArgs_returnsSortDateCommand() {
        // ascending order
        assertParseSuccess(parser, "date o/asc", new SortDateCommand(true));

        // descending order
        assertParseSuccess(parser, "date o/desc", new SortDateCommand(false));

        // mixed case arguments
        assertParseSuccess(parser, "date o/aSc", new SortDateCommand(true));
        assertParseSuccess(parser, "date o/DESC", new SortDateCommand(false));
        assertParseSuccess(parser, "dAtE o/AsC", new SortDateCommand(true));

        // valid spaces
        assertParseSuccess(parser, " date   o/asc ", new SortDateCommand(true));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        // missing preamble "date"
        assertParseFailure(parser, "o/asc", SortDateCommandParser.MESSAGE_INVALID_FORMAT);

        // wrong preamble
        assertParseFailure(parser, "time o/asc", SortDateCommandParser.MESSAGE_INVALID_FORMAT);

        // missing order prefix
        assertParseFailure(parser, "date asc", SortDateCommandParser.MESSAGE_INVALID_FORMAT);

        // invalid order value
        assertParseFailure(parser, "date o/random", SortDateCommandParser.MESSAGE_INVALID_ORDER);

        // empty arguments
        assertParseFailure(parser, "", SortDateCommandParser.MESSAGE_INVALID_FORMAT);

        // duplicate prefixes (not rigorously tested, but should be invalid format or error depending on parser)
        // duplicate prefixes
        assertParseFailure(parser, "date o/asc o/desc", 
                seedu.address.logic.Messages.getErrorMessageForDuplicatePrefixes(seedu.address.logic.parser.CliSyntax.PREFIX_ORDER));
    }
}
