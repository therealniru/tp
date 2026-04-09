package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

public class ClearCommandTest {

    @Test
    public void execute_emptyAddressBook_success() {
        Model model = new ModelManager();
        Model expectedModel = new ModelManager();

        CommandResult result = new ClearCommand().execute(model);
        assertEquals(ClearCommand.MESSAGE_SUCCESS, result.getFeedbackToUser());
        assertEquals(expectedModel, model);
    }

    @Test
    public void execute_nonEmptyAddressBook_clearsAddressBook() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel.setAddressBook(new AddressBook());

        CommandResult result = new ClearCommand().execute(model);
        assertEquals(ClearCommand.MESSAGE_SUCCESS, result.getFeedbackToUser());
        assertEquals(expectedModel, model);
    }
}
