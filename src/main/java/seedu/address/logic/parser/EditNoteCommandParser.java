package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTE_CONTENT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTE_HEADING;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditNoteCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Note;

/**
 * Parses input arguments and creates a new EditNoteCommand object.
 */
public class EditNoteCommandParser implements Parser<EditNoteCommand> {

    public static final String MESSAGE_INVALID_FORMAT =
            "Error: Invalid format. Usage: editnote INDEX NOTE_INDEX [n/CONTENT] [h/HEADING]\n"
            + "Both INDEX and NOTE_INDEX must be positive integers. "
            + "At least one of n/CONTENT or h/HEADING must be provided.";

    /**
     * Parses the given {@code String} of arguments in the context of the EditNoteCommand
     * and returns an EditNoteCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format.
     */
    public EditNoteCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NOTE_CONTENT, PREFIX_NOTE_HEADING);

        String preamble = argMultimap.getPreamble().trim();
        if (preamble.isEmpty()) {
            throw new ParseException(MESSAGE_INVALID_FORMAT);
        }

        String[] tokens = preamble.split("\\s+");
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

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NOTE_CONTENT, PREFIX_NOTE_HEADING);

        boolean hasContent = argMultimap.getValue(PREFIX_NOTE_CONTENT).isPresent();
        boolean hasHeading = argMultimap.getValue(PREFIX_NOTE_HEADING).isPresent();

        if (!hasContent && !hasHeading) {
            throw new ParseException(EditNoteCommand.MESSAGE_NOT_EDITED);
        }

        String newContent = null;
        String newHeading = null;

        if (hasContent) {
            newContent = argMultimap.getValue(PREFIX_NOTE_CONTENT).get()
                    .replaceAll("\\r\\n|\\r|\\n", " ").trim();
            if (newContent.isEmpty()) {
                throw new ParseException("Error: Note content cannot be blank. "
                        + "Usage: editnote INDEX NOTE_INDEX [n/CONTENT] [h/HEADING]");
            }
            if (newContent.length() > Note.MAX_CONTENT_LENGTH) {
                throw new ParseException(String.format(
                        "Error: Note content must not exceed %d characters (currently %d).",
                        Note.MAX_CONTENT_LENGTH, newContent.length()));
            }
            if (!Note.isValidContent(newContent)) {
                throw new ParseException(Note.MESSAGE_CONTENT_CONSTRAINTS);
            }
        }

        if (hasHeading) {
            newHeading = argMultimap.getValue(PREFIX_NOTE_HEADING).get()
                    .replaceAll("\\r\\n|\\r|\\n", " ").trim();
            if (newHeading.isEmpty()) {
                throw new ParseException("Error: Note heading cannot be blank. "
                        + "Usage: editnote INDEX NOTE_INDEX [n/CONTENT] [h/HEADING]");
            }
            if (newHeading.length() > Note.MAX_HEADING_LENGTH) {
                throw new ParseException(String.format(
                        "Error: Note heading must not exceed %d characters (currently %d).",
                        Note.MAX_HEADING_LENGTH, newHeading.length()));
            }
            if (!Note.isValidHeading(newHeading)) {
                throw new ParseException(Note.MESSAGE_HEADING_CONSTRAINTS);
            }
        }

        return new EditNoteCommand(targetIndex, noteIndex, newContent, newHeading);
    }
}
