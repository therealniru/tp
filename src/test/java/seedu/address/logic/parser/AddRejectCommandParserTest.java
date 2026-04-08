package seedu.address.logic.parser;

import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.AddRejectCommand;
import seedu.address.model.person.RejectionReason;

public class AddRejectCommandParserTest {

    private AddRejectCommandParser parser = new AddRejectCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        RejectionReason expectedReason = new RejectionReason("Failed technical interview");
        assertParseSuccess(parser, " 1 Failed technical interview",
                new AddRejectCommand(INDEX_FIRST_PERSON, expectedReason));
    }

    @Test
    public void parse_reasonWithSpecialChars_success() {
        RejectionReason reason = new RejectionReason("Didn't meet req's, score 3/5");
        assertParseSuccess(parser, " 1 Didn't meet req's, score 3/5",
                new AddRejectCommand(INDEX_FIRST_PERSON, reason));
    }

    @Test
    public void parse_missingReason_failure() {
        assertParseFailure(parser, " 1",
                Messages.MESSAGE_REJECT_INVALID_FORMAT);
    }

    @Test
    public void parse_missingIndex_failure() {
        assertParseFailure(parser, "",
                Messages.MESSAGE_REJECT_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidIndex_failure() {
        assertParseFailure(parser, " abc Failed technical interview",
                Messages.MESSAGE_REJECT_INVALID_INDEX);

        assertParseFailure(parser, " 0 Failed technical interview",
                Messages.MESSAGE_REJECT_INVALID_INDEX);
    }

    @Test
    public void parse_reasonExceeds200Chars_failure() {
        String longReason = "a".repeat(201);
        assertParseFailure(parser, " 1 " + longReason,
                RejectionReason.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_reasonExactly200Chars_success() {
        String reason200 = "a".repeat(200);
        RejectionReason expectedReason = new RejectionReason(reason200);
        assertParseSuccess(parser, " 1 " + reason200,
                new AddRejectCommand(INDEX_FIRST_PERSON, expectedReason));
    }
}
