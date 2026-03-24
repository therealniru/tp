package seedu.address.logic.parser;

import seedu.address.logic.commands.Command;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new SortDateCommand or SortPriorityCommand object
 */
public class SortCommandParser implements Parser<Command> {

    public static final String MESSAGE_INVALID_SORT_TYPE =
            "Invalid sort type. Please use 'sort date' or 'sort pr'.";

    /**
     * Parses the given {@code String} of arguments in the context of the Sort commands
     * and returns a Command object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public Command parse(String args) throws ParseException {
        // We peek at the first word of the arguments to see if it's 'date' or 'pr'
        String strippedArgs = args.trim();
        if (strippedArgs.toLowerCase().startsWith("date")) {
            return new SortDateCommandParser().parse(args);
        } else if (strippedArgs.toLowerCase().startsWith("pr")) {
            return new SortPriorityCommandParser().parse(args);
        } else {
            // Alternatively, delegate to SortDateCommandParser if not "pr",
            // but throwing an error for invalid sort type is better.
            if (!strippedArgs.isEmpty()) {
                String firstWord = strippedArgs.split("\\s+")[0].toLowerCase();
                if (firstWord.equals("date")) {
                    return new SortDateCommandParser().parse(args);
                } else if (firstWord.equals("pr")) {
                    return new SortPriorityCommandParser().parse(args);
                }
            }
            throw new ParseException(MESSAGE_INVALID_SORT_TYPE);
        }
    }
}
