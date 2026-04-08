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
 * {@code DeleteRejectCommand}.
 */
public class DeleteRejectCommandTest {

    private static final RejectionReason REASON_ONE = new RejectionReason("Failed technical interview");
    private static final RejectionReason REASON_TWO = new RejectionReason("Insufficient experience");

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndices_success() {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        List<RejectionReason> reasons = new ArrayList<>();
        reasons.add(REASON_ONE);
        Person personWithRejection = new PersonBuilder(personToEdit)
                .withRejectionReasonsList(reasons).build();
        model.setPerson(personToEdit, personWithRejection);

        DeleteRejectCommand command = new DeleteRejectCommand(INDEX_FIRST_PERSON, Index.fromOneBased(1));

        Person expectedPerson = new PersonBuilder(personToEdit)
                .withRejectionReasonsList(new ArrayList<>()).build();
        String expectedMessage = String.format(DeleteRejectCommand.MESSAGE_SUCCESS,
                personWithRejection.getName());

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(personWithRejection, expectedPerson);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_deleteFirstOfTwo_success() {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        List<RejectionReason> reasons = new ArrayList<>();
        reasons.add(REASON_ONE);
        reasons.add(REASON_TWO);
        Person personWithRejections = new PersonBuilder(personToEdit)
                .withRejectionReasonsList(reasons).build();
        model.setPerson(personToEdit, personWithRejections);

        DeleteRejectCommand command = new DeleteRejectCommand(INDEX_FIRST_PERSON, Index.fromOneBased(1));

        List<RejectionReason> expectedReasons = new ArrayList<>();
        expectedReasons.add(REASON_TWO);
        Person expectedPerson = new PersonBuilder(personToEdit)
                .withRejectionReasonsList(expectedReasons).build();

        String expectedMessage = String.format(DeleteRejectCommand.MESSAGE_SUCCESS,
                personWithRejections.getName());

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(personWithRejections, expectedPerson);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_personIndexOutOfRange_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        DeleteRejectCommand command = new DeleteRejectCommand(outOfBoundIndex, Index.fromOneBased(1));

        String expectedMessage = String.format(Messages.MESSAGE_INDEX_OUT_OF_RANGE,
                outOfBoundIndex.getOneBased(), model.getFilteredPersonList().size());

        assertCommandFailure(command, model, expectedMessage);
    }

    @Test
    public void execute_personHasNoRejections_throwsCommandException() {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteRejectCommand command = new DeleteRejectCommand(INDEX_FIRST_PERSON, Index.fromOneBased(1));

        String expectedMessage = String.format(DeleteRejectCommand.MESSAGE_NO_REJECTIONS,
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

        DeleteRejectCommand command = new DeleteRejectCommand(INDEX_FIRST_PERSON, Index.fromOneBased(2));

        String expectedMessage = String.format(DeleteRejectCommand.MESSAGE_INVALID_REJECT_INDEX,
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

        DeleteRejectCommand command = new DeleteRejectCommand(INDEX_FIRST_PERSON, Index.fromOneBased(1));

        Person expectedPerson = new PersonBuilder(personToEdit)
                .withRejectionReasonsList(new ArrayList<>()).build();
        String expectedMessage = String.format(DeleteRejectCommand.MESSAGE_SUCCESS,
                personWithRejection.getName());

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(personWithRejection, expectedPerson);
        showPersonAtIndex(expectedModel, INDEX_FIRST_PERSON);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void equals() {
        DeleteRejectCommand command1 = new DeleteRejectCommand(INDEX_FIRST_PERSON, Index.fromOneBased(1));
        DeleteRejectCommand command2 = new DeleteRejectCommand(INDEX_SECOND_PERSON, Index.fromOneBased(1));
        DeleteRejectCommand command3 = new DeleteRejectCommand(INDEX_FIRST_PERSON, Index.fromOneBased(2));

        // same object -> returns true
        assertTrue(command1.equals(command1));

        // same values -> returns true
        DeleteRejectCommand command1Copy = new DeleteRejectCommand(INDEX_FIRST_PERSON, Index.fromOneBased(1));
        assertTrue(command1.equals(command1Copy));

        // different types -> returns false
        assertFalse(command1.equals(1));

        // null -> returns false
        assertFalse(command1.equals(null));

        // different person index -> returns false
        assertFalse(command1.equals(command2));

        // different reject index -> returns false
        assertFalse(command1.equals(command3));
    }

    @Test
    public void toString_containsFields() {
        DeleteRejectCommand command = new DeleteRejectCommand(INDEX_FIRST_PERSON, Index.fromOneBased(1));
        String result = command.toString();
        assertTrue(result.contains("targetIndex"));
        assertTrue(result.contains("rejectIndex"));
    }
}
