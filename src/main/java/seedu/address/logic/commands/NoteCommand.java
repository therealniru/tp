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
 * Adds a note to a person in the address book.
 */
public class NoteCommand extends Command {

    public static final String COMMAND_WORD = "addnote";

    public static final String MESSAGE_USAGE = "addnote INDEX c/CONTENT [h/HEADING]";

    public static final int MAX_NOTES_PER_CANDIDATE = 50;
    public static final String MESSAGE_NOTES_LIMIT_REACHED =
            "Cannot add note: candidate already has the maximum of " + MAX_NOTES_PER_CANDIDATE + " notes. "
            + "Delete an existing note with `deletenote` before adding a new one.";

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

        if (lastShownList.isEmpty()) {
            throw new CommandException(Messages.MESSAGE_EMPTY_LIST);
        }

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(String.format(Messages.MESSAGE_INDEX_OUT_OF_RANGE,
                    targetIndex.getOneBased(), lastShownList.size(), lastShownList.size()));
        }

        Person personToEdit = lastShownList.get(targetIndex.getZeroBased());
        if (personToEdit.getNotes().size() >= MAX_NOTES_PER_CANDIDATE) {
            throw new CommandException(MESSAGE_NOTES_LIMIT_REACHED);
        }
        Person editedPerson = createEditedPerson(personToEdit, createUpdatedNotes(personToEdit.getNotes(), note));

        model.setPerson(personToEdit, editedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        model.sortFilteredPersonList(ListCommand.DEFAULT_SORT);
        logger.info("Added note to candidate: " + personToEdit.getName());
        return new CommandResult(String.format(MESSAGE_SUCCESS, personToEdit.getName()));
    }

    private static List<Note> createUpdatedNotes(List<Note> currentNotes, Note newNote) {
        List<Note> updatedNotes = new ArrayList<>(currentNotes);
        updatedNotes.add(newNote);
        return updatedNotes;
    }

    /**
     * Creates and returns a {@code Person} with the given list of {@code Note}s.
     */
    public static Person createEditedPerson(Person personToEdit, List<Note> updatedNotes) {
        assert personToEdit != null;
        assert updatedNotes != null;

        return new Person(
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
