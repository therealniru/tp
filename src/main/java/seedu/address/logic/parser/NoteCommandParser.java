package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTE_CONTENT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTE_HEADING;

import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.NoteCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Note;

/**
 * Parses input arguments and creates a new NoteCommand object.
 */
public class NoteCommandParser implements Parser<NoteCommand> {

    public static final String MESSAGE_INVALID_FORMAT =
            "Error: Note content cannot be empty. Usage: addnote INDEX c/CONTENT [h/HEADING]";
    public static final String MESSAGE_INVALID_INDEX =
            "Error: Invalid index. Please provide a valid positive integer.\n"
                    + "Usage: addnote INDEX c/CONTENT [h/HEADING]";

    private static final Logger logger = LogsCenter.getLogger(NoteCommandParser.class);
    private static final String DEFAULT_HEADING = "General Note";

    /**
     * Parses the given {@code String} of arguments in the context of the NoteCommand
     * and returns a NoteCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format.
     */
    public NoteCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NOTE_CONTENT, PREFIX_NOTE_HEADING);

        Index index;
        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            String preamble = argMultimap.getPreamble().trim();
            if (preamble.matches("^\\d+\\s+.+")) {
                throw new ParseException("Invalid command format. Did you forget a prefix? (e.g. c/) \n"
                        + "Usage: addnote INDEX c/CONTENT [h/HEADING]");
            }
            throw new ParseException(MESSAGE_INVALID_INDEX, pe);
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NOTE_CONTENT, PREFIX_NOTE_HEADING);

        if (argMultimap.getValue(PREFIX_NOTE_CONTENT).isEmpty()) {
            throw new ParseException(MESSAGE_INVALID_FORMAT);
        }

        String content = argMultimap.getValue(PREFIX_NOTE_CONTENT).get()
                .replaceAll("\\r\\n|\\r|\\n", " ").trim();
        if (content.isEmpty()) {
            throw new ParseException(MESSAGE_INVALID_FORMAT);
        }
        if (content.length() > Note.MAX_CONTENT_LENGTH) {
            throw new ParseException(String.format(
                    "Error: Note content must not exceed %d characters (currently %d).",
                    Note.MAX_CONTENT_LENGTH, content.length()));
        }
        if (!Note.isValidContent(content)) {
            throw new ParseException(Note.MESSAGE_CONTENT_CONSTRAINTS);
        }

        String heading;
        if (argMultimap.getValue(PREFIX_NOTE_HEADING).isPresent()) {
            heading = argMultimap.getValue(PREFIX_NOTE_HEADING).get()
                    .replaceAll("\\r\\n|\\r|\\n", " ").trim();
            if (heading.isEmpty()) {
                heading = DEFAULT_HEADING;
            }
        } else {
            heading = DEFAULT_HEADING;
        }

        if (heading.length() > Note.MAX_HEADING_LENGTH) {
            throw new ParseException(String.format(
                    "Error: Note heading must not exceed %d characters (currently %d).",
                    Note.MAX_HEADING_LENGTH, heading.length()));
        }
        if (!Note.isValidHeading(heading)) {
            throw new ParseException(Note.MESSAGE_HEADING_CONSTRAINTS);
        }

        logger.fine("Parsed note command: index=" + index.getOneBased()
                + ", heading=" + heading + ", content=" + content);

        return new NoteCommand(index, new Note(heading, content));
    }
}
