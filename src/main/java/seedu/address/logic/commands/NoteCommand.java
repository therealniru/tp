package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Note;
import seedu.address.model.person.Person;

/**
 * Adds a note to a person in the address book.
 */
public class NoteCommand extends Command {

    public static final String COMMAND_WORD = "note";

    public static final String MESSAGE_USAGE = "note INDEX n/CONTENT [h/HEADING]";

    public static final String MESSAGE_SUCCESS = "Successfully added note to candidate: %1$s";

    private static final Logger logger = LogsCenter.getLogger(NoteCommand.class);

    private final Index targetIndex;
    private final Note note;

    /**
     * Creates a NoteCommand to add the given {@code note} to the person at {@code targetIndex}.
     */
    public NoteCommand(Index targetIndex, Note note) {
        requireNonNull(targetIndex);
        requireNonNull(note);
        this.targetIndex = targetIndex;
        this.note = note;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(String.format(
                    "Error: Index %d is out of range. The current list has %d candidate(s). "
                    + "Please provide an index between 1 and %d.",
                    targetIndex.getOneBased(), lastShownList.size(), lastShownList.size()));
        }

        Person personToEdit = lastShownList.get(targetIndex.getZeroBased());
        Person editedPerson = createEditedPerson(personToEdit, note);

        model.setPerson(personToEdit, editedPerson);
        logger.info("Added note to candidate: " + personToEdit.getName());
        return new CommandResult(String.format(MESSAGE_SUCCESS, personToEdit.getName()));
    }

    /**
     * Creates and returns a {@code Person} with the given {@code note} appended to its notes list.
     */
    private static Person createEditedPerson(Person personToEdit, Note note) {
        assert personToEdit != null;
        assert note != null;

        List<Note> updatedNotes = new ArrayList<>(personToEdit.getNotes());
        updatedNotes.add(note);

        return new Person(
                personToEdit.getName(),
                personToEdit.getPhone(),
                personToEdit.getEmail(),
                personToEdit.getAddress(),
                personToEdit.getTags(),
                personToEdit.getStatus(),
                personToEdit.getRejectionReasons(),
                personToEdit.getDateAdded(),
                personToEdit.getPriority(),
                updatedNotes
        );
    }

    public Note getNote() {
        return note;
    }

    public Index getTargetIndex() {
        return targetIndex;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof NoteCommand)) {
            return false;
        }
        NoteCommand otherCmd = (NoteCommand) other;
        return targetIndex.equals(otherCmd.targetIndex) && note.equals(otherCmd.note);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .add("note", note)
                .toString();
    }
}
