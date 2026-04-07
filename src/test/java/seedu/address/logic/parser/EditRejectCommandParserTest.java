package seedu.address.logic.parser;

import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditRejectCommand;
import seedu.address.model.person.RejectionReason;

public class EditRejectCommandParserTest {

    private EditRejectCommandParser parser = new EditRejectCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        assertParseSuccess(parser, " 1 1 Failed cultural fit interview",
                new EditRejectCommand(INDEX_FIRST_PERSON, Index.fromOneBased(1),
                        "Failed cultural fit interview"));
    }

    @Test
    public void parse_emptyArgs_failure() {
        assertParseFailure(parser, "",
                EditRejectCommandParser.MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_missingReason_failure() {
        assertParseFailure(parser, " 1 1",
                EditRejectCommandParser.MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_missingRejectIndex_failure() {
        assertParseFailure(parser, " 1",
                EditRejectCommandParser.MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidPersonIndex_failure() {
        assertParseFailure(parser, " abc 1 Some reason",
                EditRejectCommandParser.MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidRejectIndex_failure() {
        assertParseFailure(parser, " 1 abc Some reason",
                EditRejectCommandParser.MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_zeroPersonIndex_failure() {
        assertParseFailure(parser, " 0 1 Some reason",
                EditRejectCommandParser.MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_zeroRejectIndex_failure() {
        assertParseFailure(parser, " 1 0 Some reason",
                EditRejectCommandParser.MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_reasonExceeds200Chars_failure() {
        String longReason = "a".repeat(201);
        assertParseFailure(parser, " 1 1 " + longReason,
                RejectionReason.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_reasonExactly200Chars_success() {
        String reason200 = "a".repeat(200);
        assertParseSuccess(parser, " 1 1 " + reason200,
                new EditRejectCommand(INDEX_FIRST_PERSON, Index.fromOneBased(1), reason200));
    }
}
