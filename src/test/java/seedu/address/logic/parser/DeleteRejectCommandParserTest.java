package seedu.address.logic.parser;

import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeleteRejectCommand;

public class DeleteRejectCommandParserTest {

    private DeleteRejectCommandParser parser = new DeleteRejectCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        assertParseSuccess(parser, " 1 1",
                new DeleteRejectCommand(INDEX_FIRST_PERSON, Index.fromOneBased(1)));
    }

    @Test
    public void parse_emptyArgs_failure() {
        assertParseFailure(parser, "",
                DeleteRejectCommandParser.MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_onlyOneIndex_failure() {
        assertParseFailure(parser, " 1",
                DeleteRejectCommandParser.MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_tooManyArgs_failure() {
        assertParseFailure(parser, " 1 1 extra",
                DeleteRejectCommandParser.MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidPersonIndex_failure() {
        assertParseFailure(parser, " abc 1",
                DeleteRejectCommandParser.MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidRejectIndex_failure() {
        assertParseFailure(parser, " 1 abc",
                DeleteRejectCommandParser.MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_zeroPersonIndex_failure() {
        assertParseFailure(parser, " 0 1",
                DeleteRejectCommandParser.MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_zeroRejectIndex_failure() {
        assertParseFailure(parser, " 1 0",
                DeleteRejectCommandParser.MESSAGE_INVALID_FORMAT);
    }
}
