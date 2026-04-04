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
 * Edits a note of a person in the address book.
 * The original timestamp of the note is preserved.
 */
public class EditNoteCommand extends Command {

    public static final String COMMAND_WORD = "editnote";

    public static final String MESSAGE_USAGE = "editnote INDEX NOTE_INDEX [n/CONTENT] [h/HEADING]\n"
            + "At least one of n/CONTENT or h/HEADING must be provided.";

    public static final String MESSAGE_SUCCESS = "Successfully edited note for candidate: %1$s";

    public static final String MESSAGE_NOT_EDITED =
            "Error: At least one of n/CONTENT or h/HEADING must be provided.";

    public static final String MESSAGE_NO_NOTES = "Error: Candidate %1$s has no notes.";

    public static final String MESSAGE_INVALID_NOTE_INDEX =
            "Error: Note index %1$d is out of range. Candidate %2$s has %3$d note(s). "
            + "Please provide a note index between 1 and %3$d.";

    private static final Logger logger = LogsCenter.getLogger(EditNoteCommand.class);

    private final Index targetIndex;
    private final Index noteIndex;
    private final String newContent;
    private final String newHeading;

    /**
     * Creates an EditNoteCommand to edit the note at {@code noteIndex} for the person at {@code targetIndex}.
     * At least one of {@code newContent} or {@code newHeading} must be non-null.
     */
    public EditNoteCommand(Index targetIndex, Index noteIndex, String newContent, String newHeading) {
        requireNonNull(targetIndex);
        requireNonNull(noteIndex);
        assert newContent != null || newHeading != null : "At least one field must be provided";
        this.targetIndex = targetIndex;
        this.noteIndex = noteIndex;
        this.newContent = newContent;
        this.newHeading = newHeading;
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
        List<Note> currentNotes = personToEdit.getNotes();

        if (currentNotes.isEmpty()) {
            throw new CommandException(String.format(MESSAGE_NO_NOTES, personToEdit.getName()));
        }

        if (noteIndex.getZeroBased() >= currentNotes.size()) {
            throw new CommandException(String.format(MESSAGE_INVALID_NOTE_INDEX,
                    noteIndex.getOneBased(), personToEdit.getName(), currentNotes.size()));
        }

        Note originalNote = currentNotes.get(noteIndex.getZeroBased());
        String effectiveHeading = (newHeading != null) ? newHeading : originalNote.heading;
        String effectiveContent = (newContent != null) ? newContent : originalNote.content;

        Note editedNote = new Note(effectiveHeading, effectiveContent, originalNote.date);

        List<Note> updatedNotes = new ArrayList<>(currentNotes);
        updatedNotes.set(noteIndex.getZeroBased(), editedNote);

        Person editedPerson = new Person(
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

        model.setPerson(personToEdit, editedPerson);
        logger.info("Edited note " + noteIndex.getOneBased() + " for candidate: " + personToEdit.getName());
        return new CommandResult(String.format(MESSAGE_SUCCESS, personToEdit.getName()));
    }

    public Index getTargetIndex() {
        return targetIndex;
    }

    public Index getNoteIndex() {
        return noteIndex;
    }

    public String getNewContent() {
        return newContent;
    }

    public String getNewHeading() {
        return newHeading;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof EditNoteCommand)) {
            return false;
        }
        EditNoteCommand otherCmd = (EditNoteCommand) other;
        return targetIndex.equals(otherCmd.targetIndex)
                && noteIndex.equals(otherCmd.noteIndex)
                && java.util.Objects.equals(newContent, otherCmd.newContent)
                && java.util.Objects.equals(newHeading, otherCmd.newHeading);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .add("noteIndex", noteIndex)
                .add("newContent", newContent)
                .add("newHeading", newHeading)
                .toString();
    }
}
