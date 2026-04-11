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

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Note;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for {@code EditNoteCommand}.
 */
public class EditNoteCommandTest {

    private static final Note ORIGINAL_NOTE = new Note("Tech Round 1", "Passed interview");

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        // Add a note to the first person
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        List<Note> notes = new ArrayList<>();
        notes.add(ORIGINAL_NOTE);
        Person personWithNote = new PersonBuilder(personToEdit).withNotes(notes).build();
        model.setPerson(personToEdit, personWithNote);
    }

    @Test
    public void execute_emptyList_failure() {
        Model emptyModel = new ModelManager();
        EditNoteCommand command = new EditNoteCommand(INDEX_FIRST_PERSON, Index.fromOneBased(1),
                "content", null);

        assertCommandFailure(command, emptyModel, seedu.address.logic.Messages.MESSAGE_EMPTY_LIST);
    }

    @Test
    public void execute_editContentOnly_success() {
        EditNoteCommand command = new EditNoteCommand(INDEX_FIRST_PERSON, Index.fromOneBased(1),
                "Failed interview", null);

        Person personWithNote = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Note editedNote = new Note("Tech Round 1", "Failed interview");
        List<Note> updatedNotes = new ArrayList<>();
        updatedNotes.add(editedNote);
        Person expectedPerson = new PersonBuilder(personWithNote).withNotes(updatedNotes).build();

        String expectedMessage = String.format(EditNoteCommand.MESSAGE_SUCCESS, personWithNote.getName());

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(personWithNote, expectedPerson);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_editHeadingOnly_success() {
        EditNoteCommand command = new EditNoteCommand(INDEX_FIRST_PERSON, Index.fromOneBased(1),
                null, "HR Round");

        Person personWithNote = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Note editedNote = new Note("HR Round", "Passed interview");
        List<Note> updatedNotes = new ArrayList<>();
        updatedNotes.add(editedNote);
        Person expectedPerson = new PersonBuilder(personWithNote).withNotes(updatedNotes).build();

        String expectedMessage = String.format(EditNoteCommand.MESSAGE_SUCCESS, personWithNote.getName());

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(personWithNote, expectedPerson);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_editBoth_success() {
        EditNoteCommand command = new EditNoteCommand(INDEX_FIRST_PERSON, Index.fromOneBased(1),
                "New content", "New heading");

        Person personWithNote = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Note editedNote = new Note("New heading", "New content");
        List<Note> updatedNotes = new ArrayList<>();
        updatedNotes.add(editedNote);
        Person expectedPerson = new PersonBuilder(personWithNote).withNotes(updatedNotes).build();

        String expectedMessage = String.format(EditNoteCommand.MESSAGE_SUCCESS, personWithNote.getName());

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(personWithNote, expectedPerson);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noChanges_returnsNoChangesMessage() {
        // Edit both heading and content to be same as original
        EditNoteCommand command = new EditNoteCommand(INDEX_FIRST_PERSON, Index.fromOneBased(1),
                ORIGINAL_NOTE.content, ORIGINAL_NOTE.heading);
        String expectedMessage = "Note: No changes detected; note details remain the same.";
        assertCommandSuccess(command, model, expectedMessage, model);
    }

    @Test
    public void execute_personIndexOutOfRange_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        EditNoteCommand command = new EditNoteCommand(outOfBoundIndex, Index.fromOneBased(1),
                "content", null);
        String expectedMessage = String.format(
                "Error: Index %d is out of range. The current list has %d candidate(s). "
                + "Please provide an index between 1 and %d.",
                outOfBoundIndex.getOneBased(), model.getFilteredPersonList().size(),
                model.getFilteredPersonList().size());
        assertCommandFailure(command, model, expectedMessage);
    }

    @Test
    public void execute_personHasNoNotes_throwsCommandException() {
        // Use second person (has no notes)
        Person secondPerson = model.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        EditNoteCommand command = new EditNoteCommand(INDEX_SECOND_PERSON, Index.fromOneBased(1),
                "content", null);
        String expectedMessage = String.format(EditNoteCommand.MESSAGE_NO_NOTES, secondPerson.getName());
        assertCommandFailure(command, model, expectedMessage);
    }

    @Test
    public void execute_noteIndexOutOfRange_throwsCommandException() {
        Person personWithNote = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        // Person has 1 note, try to edit note 2
        EditNoteCommand command = new EditNoteCommand(INDEX_FIRST_PERSON, Index.fromOneBased(2),
                "content", null);
        String expectedMessage = String.format(EditNoteCommand.MESSAGE_INVALID_NOTE_INDEX,
                2, personWithNote.getName(), 1);
        assertCommandFailure(command, model, expectedMessage);
    }

    @Test
    public void execute_validIndicesFilteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        EditNoteCommand command = new EditNoteCommand(INDEX_FIRST_PERSON, Index.fromOneBased(1),
                "Edited content", null);

        Person personWithNote = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Note editedNote = new Note("Tech Round 1", "Edited content");
        List<Note> updatedNotes = new ArrayList<>();
        updatedNotes.add(editedNote);
        Person expectedPerson = new PersonBuilder(personWithNote).withNotes(updatedNotes).build();

        String expectedMessage = String.format(EditNoteCommand.MESSAGE_SUCCESS, personWithNote.getName());

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(personWithNote, expectedPerson);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void equals() {
        EditNoteCommand command1 = new EditNoteCommand(INDEX_FIRST_PERSON, Index.fromOneBased(1),
                "content", null);
        EditNoteCommand command2 = new EditNoteCommand(INDEX_SECOND_PERSON, Index.fromOneBased(1),
                "content", null);
        EditNoteCommand command3 = new EditNoteCommand(INDEX_FIRST_PERSON, Index.fromOneBased(2),
                "content", null);
        EditNoteCommand command4 = new EditNoteCommand(INDEX_FIRST_PERSON, Index.fromOneBased(1),
                "different", null);
        EditNoteCommand command5 = new EditNoteCommand(INDEX_FIRST_PERSON, Index.fromOneBased(1),
                "content", "heading");

        // same object -> returns true
        assertTrue(command1.equals(command1));

        // same values -> returns true
        EditNoteCommand command1Copy = new EditNoteCommand(INDEX_FIRST_PERSON, Index.fromOneBased(1),
                "content", null);
        assertTrue(command1.equals(command1Copy));

        // different types -> returns false
        assertFalse(command1.equals(1));

        // null -> returns false
        assertFalse(command1.equals(null));

        // different person index -> returns false
        assertFalse(command1.equals(command2));

        // different note index -> returns false
        assertFalse(command1.equals(command3));

        // different content -> returns false
        assertFalse(command1.equals(command4));

        // different heading -> returns false
        assertFalse(command1.equals(command5));
    }

    @Test
    public void getters_returnCorrectValues() {
        EditNoteCommand command = new EditNoteCommand(INDEX_FIRST_PERSON, Index.fromOneBased(3),
                "content", "heading");
        assertEquals(INDEX_FIRST_PERSON, command.getTargetIndex());
        assertEquals(Index.fromOneBased(3), command.getNoteIndex());
        assertEquals("content", command.getNewContent());
        assertEquals("heading", command.getNewHeading());
    }

    @Test
    public void toString_containsFields() {
        EditNoteCommand command = new EditNoteCommand(INDEX_FIRST_PERSON, Index.fromOneBased(1),
                "content", "heading");
        String result = command.toString();
        assertTrue(result.contains("targetIndex"));
        assertTrue(result.contains("noteIndex"));
        assertTrue(result.contains("newContent"));
        assertTrue(result.contains("newHeading"));
    }
}
