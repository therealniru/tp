package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeleteNoteCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new DeleteNoteCommand object.
 */
public class DeleteNoteCommandParser implements Parser<DeleteNoteCommand> {

    public static final String MESSAGE_INVALID_FORMAT =
            "Error: Invalid format. Usage: deletenote INDEX NOTE_INDEX\n"
            + "Both INDEX and NOTE_INDEX must be positive integers.";

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteNoteCommand
     * and returns a DeleteNoteCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format.
     */
    public DeleteNoteCommand parse(String args) throws ParseException {
        requireNonNull(args);
        String trimmedArgs = args.trim();

        if (trimmedArgs.isEmpty()) {
            throw new ParseException(MESSAGE_INVALID_FORMAT);
        }

        String[] tokens = trimmedArgs.split("\\s+");

        if (tokens.length != 2) {
            throw new ParseException(MESSAGE_INVALID_FORMAT);
        }

        Index targetIndex;
        Index noteIndex;
        try {
            targetIndex = ParserUtil.parseIndex(tokens[0]);
        } catch (ParseException pe) {
            throw new ParseException(MESSAGE_INVALID_FORMAT, pe);
        }
        try {
            noteIndex = ParserUtil.parseIndex(tokens[1]);
        } catch (ParseException pe) {
            throw new ParseException(MESSAGE_INVALID_FORMAT, pe);
        }

        return new DeleteNoteCommand(targetIndex, noteIndex);
    }
}
