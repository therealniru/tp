package seedu.address.logic.commands;

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

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.person.RejectionReason;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code EditRejectCommand}.
 */
public class EditRejectCommandTest {

    private static final RejectionReason REASON_ONE = new RejectionReason("Failed technical interview");
    private static final RejectionReason REASON_TWO = new RejectionReason("Insufficient experience");
    private static final String NEW_REASON = "Failed cultural fit interview";

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndices_success() {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        List<RejectionReason> reasons = new ArrayList<>();
        reasons.add(REASON_ONE);
        Person personWithRejection = new PersonBuilder(personToEdit)
                .withRejectionReasonsList(reasons).build();
        model.setPerson(personToEdit, personWithRejection);

        EditRejectCommand command = new EditRejectCommand(INDEX_FIRST_PERSON,
                Index.fromOneBased(1), NEW_REASON);

        List<RejectionReason> expectedReasons = new ArrayList<>();
        expectedReasons.add(new RejectionReason(NEW_REASON));
        Person expectedPerson = new PersonBuilder(personToEdit)
                .withRejectionReasonsList(expectedReasons).build();

        String expectedMessage = String.format(EditRejectCommand.MESSAGE_SUCCESS,
                personWithRejection.getName());

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(personWithRejection, expectedPerson);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_personIndexOutOfRange_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        EditRejectCommand command = new EditRejectCommand(outOfBoundIndex,
                Index.fromOneBased(1), NEW_REASON);

        String expectedMessage = String.format(Messages.MESSAGE_INDEX_OUT_OF_RANGE,
                outOfBoundIndex.getOneBased(), model.getFilteredPersonList().size());

        assertCommandFailure(command, model, expectedMessage);
    }

    @Test
    public void execute_personHasNoRejections_throwsCommandException() {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        EditRejectCommand command = new EditRejectCommand(INDEX_FIRST_PERSON,
                Index.fromOneBased(1), NEW_REASON);

        String expectedMessage = String.format(EditRejectCommand.MESSAGE_NO_REJECTIONS,
                personToEdit.getName());

        assertCommandFailure(command, model, expectedMessage);
    }

    @Test
    public void execute_rejectIndexOutOfRange_throwsCommandException() {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        List<RejectionReason> reasons = new ArrayList<>();
        reasons.add(REASON_ONE);
        Person personWithRejection = new PersonBuilder(personToEdit)
                .withRejectionReasonsList(reasons).build();
        model.setPerson(personToEdit, personWithRejection);

        EditRejectCommand command = new EditRejectCommand(INDEX_FIRST_PERSON,
                Index.fromOneBased(2), NEW_REASON);

        String expectedMessage = String.format(EditRejectCommand.MESSAGE_INVALID_REJECT_INDEX,
                2, personWithRejection.getName(), 1);

        assertCommandFailure(command, model, expectedMessage);
    }

    @Test
    public void execute_validIndicesFilteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        List<RejectionReason> reasons = new ArrayList<>();
        reasons.add(REASON_ONE);
        Person personWithRejection = new PersonBuilder(personToEdit)
                .withRejectionReasonsList(reasons).build();
        model.setPerson(personToEdit, personWithRejection);

        EditRejectCommand command = new EditRejectCommand(INDEX_FIRST_PERSON,
                Index.fromOneBased(1), NEW_REASON);

        List<RejectionReason> expectedReasons = new ArrayList<>();
        expectedReasons.add(new RejectionReason(NEW_REASON));
        Person expectedPerson = new PersonBuilder(personToEdit)
                .withRejectionReasonsList(expectedReasons).build();

        String expectedMessage = String.format(EditRejectCommand.MESSAGE_SUCCESS,
                personWithRejection.getName());

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(personWithRejection, expectedPerson);
        showPersonAtIndex(expectedModel, INDEX_FIRST_PERSON);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_editSecondRejection_success() {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        List<RejectionReason> reasons = new ArrayList<>();
        reasons.add(REASON_ONE);
        reasons.add(REASON_TWO);
        Person personWithRejections = new PersonBuilder(personToEdit)
                .withRejectionReasonsList(reasons).build();
        model.setPerson(personToEdit, personWithRejections);

        EditRejectCommand command = new EditRejectCommand(INDEX_FIRST_PERSON,
                Index.fromOneBased(2), NEW_REASON);

        List<RejectionReason> expectedReasons = new ArrayList<>();
        expectedReasons.add(REASON_ONE);
        expectedReasons.add(new RejectionReason(NEW_REASON));
        Person expectedPerson = new PersonBuilder(personToEdit)
                .withRejectionReasonsList(expectedReasons).build();

        String expectedMessage = String.format(EditRejectCommand.MESSAGE_SUCCESS,
                personWithRejections.getName());

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(personWithRejections, expectedPerson);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void equals() {
        EditRejectCommand command1 = new EditRejectCommand(INDEX_FIRST_PERSON,
                Index.fromOneBased(1), "Reason A");
        EditRejectCommand command2 = new EditRejectCommand(INDEX_SECOND_PERSON,
                Index.fromOneBased(1), "Reason A");
        EditRejectCommand command3 = new EditRejectCommand(INDEX_FIRST_PERSON,
                Index.fromOneBased(2), "Reason A");
        EditRejectCommand command4 = new EditRejectCommand(INDEX_FIRST_PERSON,
                Index.fromOneBased(1), "Reason B");

        // same object -> returns true
        assertTrue(command1.equals(command1));

        // same values -> returns true
        EditRejectCommand command1Copy = new EditRejectCommand(INDEX_FIRST_PERSON,
                Index.fromOneBased(1), "Reason A");
        assertTrue(command1.equals(command1Copy));

        // different types -> returns false
        assertFalse(command1.equals(1));

        // null -> returns false
        assertFalse(command1.equals(null));

        // different person index -> returns false
        assertFalse(command1.equals(command2));

        // different reject index -> returns false
        assertFalse(command1.equals(command3));

        // different reason -> returns false
        assertFalse(command1.equals(command4));
    }

    @Test
    public void toString_containsFields() {
        EditRejectCommand command = new EditRejectCommand(INDEX_FIRST_PERSON,
                Index.fromOneBased(1), NEW_REASON);
        String result = command.toString();
        assertTrue(result.contains("targetIndex"));
        assertTrue(result.contains("rejectIndex"));
        assertTrue(result.contains("newReason"));
    }
}
