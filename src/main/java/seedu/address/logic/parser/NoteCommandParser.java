package seedu.address.logic.parser;

import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTE_CONTENT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTE_HEADING;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.NoteCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Note;

/**
 * Parses input arguments and creates a new NoteCommand object.
 */
public class NoteCommandParser implements Parser<NoteCommand> {

    public static final String MESSAGE_INVALID_FORMAT =
            "Error: Note content cannot be empty. Usage: note INDEX n/CONTENT [h/HEADING]";
    private static final String DEFAULT_HEADING = "General Note";

    /**
     * Parses the given {@code String} of arguments in the context of the NoteCommand
     * and returns a NoteCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format.
     */
    public NoteCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NOTE_CONTENT, PREFIX_NOTE_HEADING);

        Index index;
        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(MESSAGE_INVALID_FORMAT, pe);
        }

        if (argMultimap.getValue(PREFIX_NOTE_CONTENT).isEmpty()) {
            throw new ParseException(MESSAGE_INVALID_FORMAT);
        }

        String content = argMultimap.getValue(PREFIX_NOTE_CONTENT).get().trim();
        if (content.isEmpty()) {
            throw new ParseException(MESSAGE_INVALID_FORMAT);
        }

        String heading = argMultimap.getValue(PREFIX_NOTE_HEADING)
                .map(String::trim)
                .orElse(DEFAULT_HEADING);

        return new NoteCommand(index, new Note(heading, content));
    }
}
