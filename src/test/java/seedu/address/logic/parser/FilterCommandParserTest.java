package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FilterCommand;
import seedu.address.model.person.PersonHasTagPredicate;
import seedu.address.model.tag.Tag;

public class FilterCommandParserTest {

    private final FilterCommandParser parser = new FilterCommandParser();

    @Test
    public void parse_validArgs_returnsFilterCommand() {
        FilterCommand expectedFilterCommand = new FilterCommand(new PersonHasTagPredicate(new Tag("friends")));
        assertParseSuccess(parser, "friends", expectedFilterCommand);
        assertParseSuccess(parser, "  FRIENDS   ", expectedFilterCommand);
    }

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "   ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_multipleArgs_throwsParseException() {
        assertParseFailure(parser, "friends work",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidTag_throwsParseException() {
        assertParseFailure(parser, "fri#ends",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
    }
}
