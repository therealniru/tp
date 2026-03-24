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
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Note;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for {@code NoteCommand}.
 */
public class NoteCommandTest {

    private static final LocalDateTime FIXED_DATE = LocalDateTime.of(2026, 1, 1, 10, 0, 0);
    private static final Note VALID_NOTE = new Note("Tech Round 1", "Passed interview", FIXED_DATE);
    private static final Note ANOTHER_NOTE = new Note("HR Round", "Good culture fit", FIXED_DATE);

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        NoteCommand noteCommand = new NoteCommand(INDEX_FIRST_PERSON, VALID_NOTE);

        List<Note> expectedNotes = new ArrayList<>(personToEdit.getNotes());
        expectedNotes.add(VALID_NOTE);
        Person editedPerson = new PersonBuilder(personToEdit).withNotes(expectedNotes).build();

        String expectedMessage = String.format(NoteCommand.MESSAGE_SUCCESS, personToEdit.getName());

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(personToEdit, editedPerson);

        assertCommandSuccess(noteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        NoteCommand noteCommand = new NoteCommand(outOfBoundIndex, VALID_NOTE);
        assertCommandFailure(noteCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        NoteCommand noteCommand = new NoteCommand(INDEX_FIRST_PERSON, VALID_NOTE);

        List<Note> expectedNotes = new ArrayList<>(personToEdit.getNotes());
        expectedNotes.add(VALID_NOTE);
        Person editedPerson = new PersonBuilder(personToEdit).withNotes(expectedNotes).build();

        String expectedMessage = String.format(NoteCommand.MESSAGE_SUCCESS, personToEdit.getName());

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(personToEdit, editedPerson);
        showPersonAtIndex(expectedModel, INDEX_FIRST_PERSON);

        assertCommandSuccess(noteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        // INDEX_SECOND_PERSON is out of bounds for the filtered list (size 1) but in bounds for full list
        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        NoteCommand noteCommand = new NoteCommand(outOfBoundIndex, VALID_NOTE);
        assertCommandFailure(noteCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_appendsToExistingNotes() throws Exception {
        // Add the first note
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        List<Note> firstNoteList = new ArrayList<>();
        firstNoteList.add(VALID_NOTE);
        Person personWithFirstNote = new PersonBuilder(personToEdit).withNotes(firstNoteList).build();
        model.setPerson(personToEdit, personWithFirstNote);

        // Add a second note
        NoteCommand secondNoteCommand = new NoteCommand(INDEX_FIRST_PERSON, ANOTHER_NOTE);

        List<Note> expectedNotes = new ArrayList<>();
        expectedNotes.add(VALID_NOTE);
        expectedNotes.add(ANOTHER_NOTE);
        Person personWithBothNotes = new PersonBuilder(personWithFirstNote).withNotes(expectedNotes).build();

        String expectedMessage = String.format(NoteCommand.MESSAGE_SUCCESS, personWithFirstNote.getName());

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(personWithFirstNote, personWithBothNotes);

        assertCommandSuccess(secondNoteCommand, model, expectedMessage, expectedModel);

        // Verify both notes are present
        Person result = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        assertEquals(2, result.getNotes().size());
        assertEquals(VALID_NOTE, result.getNotes().get(0));
        assertEquals(ANOTHER_NOTE, result.getNotes().get(1));
    }

    @Test
    public void equals() {
        NoteCommand noteFirstCommand = new NoteCommand(INDEX_FIRST_PERSON, VALID_NOTE);
        NoteCommand noteSecondCommand = new NoteCommand(INDEX_SECOND_PERSON, VALID_NOTE);
        NoteCommand noteFirstDiffNote = new NoteCommand(INDEX_FIRST_PERSON, ANOTHER_NOTE);

        // same object -> returns true
        assertTrue(noteFirstCommand.equals(noteFirstCommand));

        // same values -> returns true
        NoteCommand noteFirstCommandCopy = new NoteCommand(INDEX_FIRST_PERSON, VALID_NOTE);
        assertTrue(noteFirstCommand.equals(noteFirstCommandCopy));

        // different types -> returns false
        assertFalse(noteFirstCommand.equals(1));

        // null -> returns false
        assertFalse(noteFirstCommand.equals(null));

        // different index -> returns false
        assertFalse(noteFirstCommand.equals(noteSecondCommand));

        // different note -> returns false
        assertFalse(noteFirstCommand.equals(noteFirstDiffNote));
    }

    @Test
    public void getNote_returnsCorrectNote() {
        NoteCommand noteCommand = new NoteCommand(INDEX_FIRST_PERSON, VALID_NOTE);
        assertEquals(VALID_NOTE, noteCommand.getNote());
    }

    @Test
    public void getTargetIndex_returnsCorrectIndex() {
        NoteCommand noteCommand = new NoteCommand(INDEX_FIRST_PERSON, VALID_NOTE);
        assertEquals(INDEX_FIRST_PERSON, noteCommand.getTargetIndex());
    }
}
