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

    public static final String MESSAGE_MISSING_CONTENT =
            "Error: Note content is required. Usage: addnote INDEX c/CONTENT [h/HEADING]";
    public static final String MESSAGE_INVALID_FORMAT =
            "Error: Note content cannot be blank. Usage: addnote INDEX c/CONTENT [h/HEADING]";
    public static final String MESSAGE_INVALID_INDEX =
            "Error: Invalid index. Please provide a valid positive integer.\n"
                    + "Usage: addnote INDEX c/CONTENT [h/HEADING]";
    public static final String DEFAULT_HEADING = "General Note";

    private static final Logger logger = LogsCenter.getLogger(NoteCommandParser.class);

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
            throw new ParseException(MESSAGE_INVALID_INDEX, pe);
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NOTE_CONTENT, PREFIX_NOTE_HEADING);

        if (argMultimap.getValue(PREFIX_NOTE_CONTENT).isEmpty()) {
            throw new ParseException(MESSAGE_MISSING_CONTENT);
        }

        String content = parseNoteContent(argMultimap.getValue(PREFIX_NOTE_CONTENT).get(), MESSAGE_INVALID_FORMAT);
        String heading = argMultimap.getValue(PREFIX_NOTE_HEADING).isPresent()
                ? parseNoteHeading(argMultimap.getValue(PREFIX_NOTE_HEADING).get()) : DEFAULT_HEADING;

        logger.fine("Parsed note command: index=" + index.getOneBased()
                + ", heading=" + heading + ", content=" + content);

        return new NoteCommand(index, new Note(heading, content));
    }

    /**
     * Parses the note content.
     */
    public static String parseNoteContent(String content, String emptyErrMsg) throws ParseException {
        String clean = content.replaceAll("\\r\\n|\\r|\\n", " ").trim();
        if (clean.isEmpty()) {
            throw new ParseException(emptyErrMsg);
        }
        if (clean.length() > Note.MAX_CONTENT_LENGTH) {
            throw new ParseException(String.format(
                    "Error: Note content must not exceed %d characters (currently %d).",
                    Note.MAX_CONTENT_LENGTH, clean.length()));
        }
        if (!Note.isValidContent(clean)) {
            throw new ParseException("Error: Note content must contain only printable "
                    + "ASCII characters (no accented letters, emojis, or other non-ASCII input).");
        }
        return clean;
    }

    /**
     * Parses the note heading.
     */
    public static String parseNoteHeading(String heading) throws ParseException {
        String clean = heading.replaceAll("\\r\\n|\\r|\\n", " ").trim();
        if (clean.isEmpty()) {
            return DEFAULT_HEADING;
        }
        if (clean.length() > Note.MAX_HEADING_LENGTH) {
            throw new ParseException(String.format(
                    "Error: Note heading must not exceed %d characters (currently %d).",
                    Note.MAX_HEADING_LENGTH, clean.length()));
        }
        if (!Note.isValidHeading(clean)) {
            throw new ParseException("Error: Note heading must contain only printable "
                    + "ASCII characters (no accented letters, emojis, or other non-ASCII input).");
        }
        return clean;
    }
}
