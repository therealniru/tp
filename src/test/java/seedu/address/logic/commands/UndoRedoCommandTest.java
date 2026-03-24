package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class UndoRedoCommandTest {

    @Test
    public void undo_noHistory_throwsCommandException() {
        Model model = new ModelManager();
        UndoCommand undoCommand = new UndoCommand();

        assertThrows(CommandException.class,
            UndoCommand.MESSAGE_NO_UNDOABLE_COMMAND, () -> undoCommand.execute(model));
    }

    @Test
    public void undo_afterCommit_success() throws Exception {
        Model model = new ModelManager();
        Person person = new PersonBuilder().build();
        model.addPerson(person);
        model.commitAddressBook();

        UndoCommand undoCommand = new UndoCommand();
        undoCommand.execute(model);

        assertFalse(model.hasPerson(person));
    }

    @Test
    public void redo_noHistory_throwsCommandException() {
        Model model = new ModelManager();
        RedoCommand redoCommand = new RedoCommand();

        assertThrows(CommandException.class,
            RedoCommand.MESSAGE_NO_REDOABLE_COMMAND, () -> redoCommand.execute(model));
    }

    @Test
    public void redo_afterCommitAndUndo_success() throws Exception {
        Model model = new ModelManager();
        Person person = new PersonBuilder().build();
        model.addPerson(person);
        model.commitAddressBook();

        UndoCommand undoCommand = new UndoCommand();
        undoCommand.execute(model);
        assertFalse(model.hasPerson(person));

        RedoCommand redoCommand = new RedoCommand();
        redoCommand.execute(model);

        assertTrue(model.hasPerson(person));
    }
}
