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
 * {@code AddRejectCommand}.
 */
public class AddRejectCommandTest {

    private static final RejectionReason VALID_REASON = new RejectionReason("Failed technical interview");
    private static final RejectionReason ANOTHER_REASON = new RejectionReason("Insufficient experience");

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Person personToReject = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        AddRejectCommand rejectCommand = new AddRejectCommand(INDEX_FIRST_PERSON, VALID_REASON);

        List<RejectionReason> expectedReasons = new ArrayList<>();
        expectedReasons.add(VALID_REASON);

        Person rejectedPerson = new PersonBuilder(personToReject)
                .withRejectionReasonsList(expectedReasons)
                .build();

        String expectedMessage = String.format(AddRejectCommand.MESSAGE_SUCCESS,
                VALID_REASON, 1);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(personToReject, rejectedPerson);

        assertCommandSuccess(rejectCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        AddRejectCommand rejectCommand = new AddRejectCommand(outOfBoundIndex, VALID_REASON);

        String expectedMessage = String.format(Messages.MESSAGE_INDEX_OUT_OF_RANGE,
                outOfBoundIndex.getOneBased(), model.getFilteredPersonList().size());

        assertCommandFailure(rejectCommand, model, expectedMessage);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person personToReject = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        AddRejectCommand rejectCommand = new AddRejectCommand(INDEX_FIRST_PERSON, VALID_REASON);

        List<RejectionReason> expectedReasons = new ArrayList<>();
        expectedReasons.add(VALID_REASON);

        Person rejectedPerson = new PersonBuilder(personToReject)
                .withRejectionReasonsList(expectedReasons)
                .build();

        String expectedMessage = String.format(AddRejectCommand.MESSAGE_SUCCESS,
                VALID_REASON, 1);

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(personToReject, rejectedPerson);
        showPersonAtIndex(expectedModel, INDEX_FIRST_PERSON);

        assertCommandSuccess(rejectCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        AddRejectCommand rejectCommand = new AddRejectCommand(outOfBoundIndex, VALID_REASON);

        String expectedMessage = String.format(Messages.MESSAGE_INDEX_OUT_OF_RANGE,
                outOfBoundIndex.getOneBased(), model.getFilteredPersonList().size());

        assertCommandFailure(rejectCommand, model, expectedMessage);
    }

    @Test
    public void execute_multipleRejections_appendsReason() {
        Person personToReject = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        List<RejectionReason> firstReasons = new ArrayList<>();
        firstReasons.add(VALID_REASON);

        Person firstRejected = new PersonBuilder(personToReject)
                .withRejectionReasonsList(firstReasons)
                .build();
        model.setPerson(personToReject, firstRejected);

        AddRejectCommand secondReject = new AddRejectCommand(INDEX_FIRST_PERSON, ANOTHER_REASON);

        List<RejectionReason> expectedReasons = new ArrayList<>();
        expectedReasons.add(VALID_REASON);
        expectedReasons.add(ANOTHER_REASON);

        Person secondRejected = new PersonBuilder(firstRejected)
                .withRejectionReasonsList(expectedReasons)
                .build();

        String expectedMessage = String.format(AddRejectCommand.MESSAGE_SUCCESS,
                ANOTHER_REASON, 2);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(firstRejected, secondRejected);

        assertCommandSuccess(secondReject, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_sequentialDuplicateReason_warnsUser() {
        Person personToReject = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        List<RejectionReason> firstReasons = new ArrayList<>();
        firstReasons.add(VALID_REASON);

        Person firstRejected = new PersonBuilder(personToReject)
                .withRejectionReasonsList(firstReasons)
                .build();
        model.setPerson(personToReject, firstRejected);

        AddRejectCommand duplicateReject = new AddRejectCommand(INDEX_FIRST_PERSON, VALID_REASON);

        List<RejectionReason> expectedReasons = new ArrayList<>();
        expectedReasons.add(VALID_REASON);
        expectedReasons.add(VALID_REASON);

        Person secondRejected = new PersonBuilder(firstRejected)
                .withRejectionReasonsList(expectedReasons)
                .build();

        String expectedMessage = String.format(
                AddRejectCommand.MESSAGE_SUCCESS_DUPLICATE_WARNING,
                VALID_REASON, 2);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(firstRejected, secondRejected);

        assertCommandSuccess(duplicateReject, model, expectedMessage, expectedModel);
    }

    @Test
    public void equals() {
        AddRejectCommand rejectFirstCommand = new AddRejectCommand(INDEX_FIRST_PERSON, VALID_REASON);
        AddRejectCommand rejectSecondCommand = new AddRejectCommand(INDEX_SECOND_PERSON, VALID_REASON);
        AddRejectCommand rejectFirstDiffReason = new AddRejectCommand(INDEX_FIRST_PERSON, ANOTHER_REASON);

        assertTrue(rejectFirstCommand.equals(rejectFirstCommand));

        AddRejectCommand rejectFirstCommandCopy = new AddRejectCommand(INDEX_FIRST_PERSON, VALID_REASON);
        assertTrue(rejectFirstCommand.equals(rejectFirstCommandCopy));

        assertFalse(rejectFirstCommand.equals(1));
        assertFalse(rejectFirstCommand.equals(null));
        assertFalse(rejectFirstCommand.equals(rejectSecondCommand));
        assertFalse(rejectFirstCommand.equals(rejectFirstDiffReason));
    }

    @Test
    public void toStringMethod() {
        Index targetIndex = Index.fromOneBased(1);
        AddRejectCommand rejectCommand = new AddRejectCommand(targetIndex, VALID_REASON);
        String expected = AddRejectCommand.class.getCanonicalName()
                + "{targetIndex=" + targetIndex + ", reason=" + VALID_REASON + "}";
        assertEquals(expected, rejectCommand.toString());
    }
}
