package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Note;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for {@code DeleteNoteCommand}.
 */
public class DeleteNoteCommandTest {

    private static final LocalDateTime FIXED_DATE = LocalDateTime.of(2026, 1, 1, 10, 0, 0);
    private static final Note NOTE_ONE = new Note("Tech Round 1", "Passed interview", FIXED_DATE);
    private static final Note NOTE_TWO = new Note("HR Round", "Good culture fit", FIXED_DATE);

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndices_success() {
        // Add a note to the first person first
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        List<Note> notesWithOne = new ArrayList<>();
        notesWithOne.add(NOTE_ONE);
        Person personWithNote = new PersonBuilder(personToEdit).withNotes(notesWithOne).build();
        model.setPerson(personToEdit, personWithNote);

        DeleteNoteCommand command = new DeleteNoteCommand(INDEX_FIRST_PERSON, Index.fromOneBased(1));

        Person expectedPerson = new PersonBuilder(personToEdit).withNotes(new ArrayList<>()).build();
        String expectedMessage = String.format(DeleteNoteCommand.MESSAGE_SUCCESS, personWithNote.getName());

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(personWithNote, expectedPerson);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_deleteMiddleNote_success() {
        // Add two notes
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        List<Note> twoNotes = new ArrayList<>();
        twoNotes.add(NOTE_ONE);
        twoNotes.add(NOTE_TWO);
        Person personWithNotes = new PersonBuilder(personToEdit).withNotes(twoNotes).build();
        model.setPerson(personToEdit, personWithNotes);

        // Delete note 1 (the first one)
        DeleteNoteCommand command = new DeleteNoteCommand(INDEX_FIRST_PERSON, Index.fromOneBased(1));

        List<Note> remainingNotes = new ArrayList<>();
        remainingNotes.add(NOTE_TWO);
        Person expectedPerson = new PersonBuilder(personToEdit).withNotes(remainingNotes).build();
        String expectedMessage = String.format(DeleteNoteCommand.MESSAGE_SUCCESS, personWithNotes.getName());

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(personWithNotes, expectedPerson);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_personIndexOutOfRange_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        DeleteNoteCommand command = new DeleteNoteCommand(outOfBoundIndex, Index.fromOneBased(1));
        String expectedMessage = String.format(
                "Error: Index %d is out of range. The current list has %d candidate(s). "
                + "Please provide an index between 1 and %d.",
                outOfBoundIndex.getOneBased(), model.getFilteredPersonList().size(),
                model.getFilteredPersonList().size());
        assertCommandFailure(command, model, expectedMessage);
    }

    @Test
    public void execute_personHasNoNotes_throwsCommandException() {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        // Default typical persons have no notes
        DeleteNoteCommand command = new DeleteNoteCommand(INDEX_FIRST_PERSON, Index.fromOneBased(1));
        String expectedMessage = String.format(DeleteNoteCommand.MESSAGE_NO_NOTES, personToEdit.getName());
        assertCommandFailure(command, model, expectedMessage);
    }

    @Test
    public void execute_noteIndexOutOfRange_throwsCommandException() {
        // Add one note
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        List<Note> oneNote = new ArrayList<>();
        oneNote.add(NOTE_ONE);
        Person personWithNote = new PersonBuilder(personToEdit).withNotes(oneNote).build();
        model.setPerson(personToEdit, personWithNote);

        // Try to delete note 2 (out of range)
        DeleteNoteCommand command = new DeleteNoteCommand(INDEX_FIRST_PERSON, Index.fromOneBased(2));
        String expectedMessage = String.format(DeleteNoteCommand.MESSAGE_INVALID_NOTE_INDEX,
                2, personWithNote.getName(), 1);
        assertCommandFailure(command, model, expectedMessage);
    }

    @Test
    public void execute_validIndicesFilteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        List<Note> notesWithOne = new ArrayList<>();
        notesWithOne.add(NOTE_ONE);
        Person personWithNote = new PersonBuilder(personToEdit).withNotes(notesWithOne).build();
        model.setPerson(personToEdit, personWithNote);

        DeleteNoteCommand command = new DeleteNoteCommand(INDEX_FIRST_PERSON, Index.fromOneBased(1));

        Person expectedPerson = new PersonBuilder(personToEdit).withNotes(new ArrayList<>()).build();
        String expectedMessage = String.format(DeleteNoteCommand.MESSAGE_SUCCESS, personWithNote.getName());

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(personWithNote, expectedPerson);
        showPersonAtIndex(expectedModel, INDEX_FIRST_PERSON);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void equals() {
        DeleteNoteCommand command1 = new DeleteNoteCommand(INDEX_FIRST_PERSON, Index.fromOneBased(1));
        DeleteNoteCommand command2 = new DeleteNoteCommand(INDEX_SECOND_PERSON, Index.fromOneBased(1));
        DeleteNoteCommand command3 = new DeleteNoteCommand(INDEX_FIRST_PERSON, Index.fromOneBased(2));

        // same object -> returns true
        assertTrue(command1.equals(command1));

        // same values -> returns true
        DeleteNoteCommand command1Copy = new DeleteNoteCommand(INDEX_FIRST_PERSON, Index.fromOneBased(1));
        assertTrue(command1.equals(command1Copy));

        // different types -> returns false
        assertFalse(command1.equals(1));

        // null -> returns false
        assertFalse(command1.equals(null));

        // different person index -> returns false
        assertFalse(command1.equals(command2));

        // different note index -> returns false
        assertFalse(command1.equals(command3));
    }

    @Test
    public void getters_returnCorrectValues() {
        Index personIndex = INDEX_FIRST_PERSON;
        Index noteIdx = Index.fromOneBased(3);
        DeleteNoteCommand command = new DeleteNoteCommand(personIndex, noteIdx);
        assertEquals(personIndex, command.getTargetIndex());
        assertEquals(noteIdx, command.getNoteIndex());
    }

    @Test
    public void toString_containsFields() {
        DeleteNoteCommand command = new DeleteNoteCommand(INDEX_FIRST_PERSON, Index.fromOneBased(1));
        String result = command.toString();
        assertTrue(result.contains("targetIndex"));
        assertTrue(result.contains("noteIndex"));
    }
}
