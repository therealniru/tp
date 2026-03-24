package seedu.address.logic.parser;

import static seedu.address.logic.parser.CliSyntax.PREFIX_ORDER;

import seedu.address.logic.commands.SortPriorityCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new SortPriorityCommand object
 */
public class SortPriorityCommandParser implements Parser<SortPriorityCommand> {

    public static final String MESSAGE_INVALID_ORDER =
            "Invalid sort order. Please use 'asc' for oldest-first or 'desc' for newest-first.";

    public static final String MESSAGE_INVALID_FORMAT =
            "Invalid command format! \nFormat: sort pr \nExample: sort pr";

    /**
     * Parses the given {@code String} of arguments in the context of the SortPriorityCommand
     * and returns a SortPriorityCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public SortPriorityCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_ORDER);

        // Preamble must be exactly "pr"
        String preamble = argMultimap.getPreamble().trim();
        if (!preamble.toLowerCase().equals("pr")) {
            throw new ParseException(MESSAGE_INVALID_FORMAT);
        }

        if (!argMultimap.getValue(PREFIX_ORDER).isPresent()) {
            throw new ParseException(MESSAGE_INVALID_FORMAT);
        }

        String orderStr = argMultimap.getValue(PREFIX_ORDER).get().trim().toLowerCase();

        if (orderStr.equals("asc")) {
            return new SortPriorityCommand(true);
        } else if (orderStr.equals("desc")) {
            return new SortPriorityCommand(false);
        } else {
            throw new ParseException(MESSAGE_INVALID_ORDER);
        }
    }
}
