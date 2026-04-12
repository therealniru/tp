package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Note;
import seedu.address.model.person.Person;

/**
 * Deletes a note from a person in the address book.
 */
public class DeleteNoteCommand extends Command {

    public static final String COMMAND_WORD = "deletenote";

    public static final String MESSAGE_USAGE = "deletenote INDEX NOTE_INDEX";

    public static final String MESSAGE_SUCCESS = "Successfully deleted note from candidate: %1$s";

    public static final String MESSAGE_NO_NOTES = "Error: Candidate %1$s has no notes.";

    public static final String MESSAGE_INVALID_NOTE_INDEX =
            "Error: Note index %1$d is out of range. Candidate %2$s has %3$d note(s). "
            + "Please provide a note index from 1 to %3$d.";

    private static final Logger logger = LogsCenter.getLogger(DeleteNoteCommand.class);

    private final Index targetIndex;
    private final Index noteIndex;

    /**
     * Creates a DeleteNoteCommand to delete the note at {@code noteIndex} from the person at {@code targetIndex}.
     */
    public DeleteNoteCommand(Index targetIndex, Index noteIndex) {
        requireNonNull(targetIndex);
        requireNonNull(noteIndex);
        this.targetIndex = targetIndex;
        this.noteIndex = noteIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (lastShownList.isEmpty()) {
            throw new CommandException(Messages.MESSAGE_EMPTY_LIST);
        }

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(String.format(Messages.MESSAGE_INDEX_OUT_OF_RANGE,
                    targetIndex.getOneBased(), lastShownList.size(), lastShownList.size()));
        }

        Person personToEdit = lastShownList.get(targetIndex.getZeroBased());
        List<Note> currentNotes = personToEdit.getNotes();

        if (currentNotes.isEmpty()) {
            throw new CommandException(String.format(MESSAGE_NO_NOTES, personToEdit.getName()));
        }

        if (noteIndex.getZeroBased() >= currentNotes.size()) {
            throw new CommandException(String.format(MESSAGE_INVALID_NOTE_INDEX,
                    noteIndex.getOneBased(), personToEdit.getName(), currentNotes.size()));
        }

        List<Note> updatedNotes = new ArrayList<>(currentNotes);
        updatedNotes.remove(noteIndex.getZeroBased());

        Person editedPerson = new Person(
                personToEdit.getName(),
                personToEdit.getPhone(),
                personToEdit.getEmail(),
                personToEdit.getAddress(),
                personToEdit.getTags(),
                personToEdit.getRejectionReasons(),
                personToEdit.getDateAdded(),
                personToEdit.getPriority(),
                updatedNotes
        );

        model.setPerson(personToEdit, editedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        model.sortFilteredPersonList(ListCommand.DEFAULT_SORT);
        logger.info("Deleted note " + noteIndex.getOneBased() + " from candidate: " + personToEdit.getName());
        return new CommandResult(String.format(MESSAGE_SUCCESS, personToEdit.getName()));
    }

    public Index getTargetIndex() {
        return targetIndex;
    }

    public Index getNoteIndex() {
        return noteIndex;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof DeleteNoteCommand)) {
            return false;
        }
        DeleteNoteCommand otherCmd = (DeleteNoteCommand) other;
        return targetIndex.equals(otherCmd.targetIndex) && noteIndex.equals(otherCmd.noteIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .add("noteIndex", noteIndex)
                .toString();
    }
}
