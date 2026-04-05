package seedu.address.logic.parser;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.RejectCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.RejectionReason;

/**
 * Parses input arguments and creates a new RejectCommand object.
 */
public class RejectCommandParser implements Parser<RejectCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the RejectCommand
     * and returns a RejectCommand object for execution.
     * Format: reject INDEX REASON (no prefix for reason)
     * @throws ParseException if the user input does not conform the expected format
     */
    public RejectCommand parse(String args) throws ParseException {
        String trimmed = args.trim();
        if (trimmed.isEmpty()) {
            throw new ParseException(Messages.MESSAGE_REJECT_INVALID_FORMAT);
        }

        // Split into index token and remainder (the reason)
        int spaceIndex = trimmed.indexOf(' ');
        if (spaceIndex == -1) {
            // Only an index was provided, no reason
            throw new ParseException(Messages.MESSAGE_REJECT_INVALID_FORMAT);
        }

        String indexToken = trimmed.substring(0, spaceIndex);
        String reasonString = trimmed.substring(spaceIndex + 1).trim();

        if (reasonString.isEmpty()) {
            throw new ParseException(Messages.MESSAGE_REJECT_INVALID_FORMAT);
        }

        Index index;
        try {
            index = ParserUtil.parseIndex(indexToken);
        } catch (ParseException pe) {
            throw new ParseException(Messages.MESSAGE_REJECT_INVALID_INDEX, pe);
        }

        if (!RejectionReason.isValidReason(reasonString)) {
            throw new ParseException(Messages.MESSAGE_REJECT_INVALID_REASON);
        }

        RejectionReason reason = new RejectionReason(reasonString);
        return new RejectCommand(index, reason);
    }
}
