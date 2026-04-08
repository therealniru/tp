package seedu.address.logic.parser;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.AddRejectCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.RejectionReason;

/**
 * Parses input arguments and creates a new AddRejectCommand object.
 */
public class AddRejectCommandParser implements Parser<AddRejectCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddRejectCommand
     * and returns an AddRejectCommand object for execution.
     * Format: addreject INDEX REASON (no prefix for reason)
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddRejectCommand parse(String args) throws ParseException {
        String trimmed = args.trim();
        if (trimmed.isEmpty()) {
            throw new ParseException(Messages.MESSAGE_REJECT_INVALID_FORMAT);
        }

        // Split on any whitespace (space, tab, etc.) to match EditRejectCommandParser behaviour.
        String[] tokens = trimmed.split("\\s+", 2);
        if (tokens.length < 2) {
            throw new ParseException(Messages.MESSAGE_REJECT_INVALID_FORMAT);
        }

        String indexToken = tokens[0];
        String reasonString = tokens[1].trim();

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
            throw new ParseException(RejectionReason.MESSAGE_CONSTRAINTS);
        }

        RejectionReason reason = new RejectionReason(reasonString);
        return new AddRejectCommand(index, reason);
    }
}
