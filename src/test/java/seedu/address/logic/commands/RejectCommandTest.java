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
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.person.RejectionReason;
import seedu.address.model.person.Status;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code RejectCommand}.
 */
public class RejectCommandTest {

    private static final RejectionReason VALID_REASON = new RejectionReason("Failed technical interview");
    private static final RejectionReason ANOTHER_REASON = new RejectionReason("Insufficient experience");

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Person personToReject = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        RejectCommand rejectCommand = new RejectCommand(INDEX_FIRST_PERSON, VALID_REASON);

        List<RejectionReason> expectedReasons = new ArrayList<>();
        expectedReasons.add(VALID_REASON);

        Person rejectedPerson = new PersonBuilder(personToReject)
                .withStatus(Status.REJECTED)
                .withRejectionReasonsList(expectedReasons)
                .build();

        String expectedMessage = String.format(RejectCommand.MESSAGE_REJECT_PERSON_SUCCESS,
                VALID_REASON, 1);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(personToReject, rejectedPerson);

        assertCommandSuccess(rejectCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        RejectCommand rejectCommand = new RejectCommand(outOfBoundIndex, VALID_REASON);

        String expectedMessage = String.format(Messages.MESSAGE_REJECT_INDEX_OUT_OF_RANGE,
                outOfBoundIndex.getOneBased(), model.getFilteredPersonList().size());

        assertCommandFailure(rejectCommand, model, expectedMessage);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person personToReject = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        RejectCommand rejectCommand = new RejectCommand(INDEX_FIRST_PERSON, VALID_REASON);

        List<RejectionReason> expectedReasons = new ArrayList<>();
        expectedReasons.add(VALID_REASON);

        Person rejectedPerson = new PersonBuilder(personToReject)
                .withStatus(Status.REJECTED)
                .withRejectionReasonsList(expectedReasons)
                .build();

        String expectedMessage = String.format(RejectCommand.MESSAGE_REJECT_PERSON_SUCCESS,
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
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        RejectCommand rejectCommand = new RejectCommand(outOfBoundIndex, VALID_REASON);

        String expectedMessage = String.format(Messages.MESSAGE_REJECT_INDEX_OUT_OF_RANGE,
                outOfBoundIndex.getOneBased(), model.getFilteredPersonList().size());

        assertCommandFailure(rejectCommand, model, expectedMessage);
    }

    @Test
    public void execute_personWithHiredTag_returnsConfirmationResult() throws CommandException {
        Person personWithHiredTag = new PersonBuilder()
                .withName("Tagged Person")
                .withPhone("88888888")
                .withEmail("tagged@example.com")
                .withAddress("Tagged Street")
                .withTags("hired")
                .build();

        Model modelWithTagged = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        modelWithTagged.addPerson(personWithHiredTag);
        Index taggedIndex = Index.fromOneBased(modelWithTagged.getFilteredPersonList().size());

        RejectCommand rejectCommand = new RejectCommand(taggedIndex, VALID_REASON);
        CommandResult result = rejectCommand.execute(modelWithTagged);

        assertTrue(result.isRequiresConfirmation());
        assertTrue(result.getConfirmedAction().isPresent());
        // Model should be unchanged — rejection not yet applied
        assertEquals(personWithHiredTag,
                modelWithTagged.getFilteredPersonList().get(taggedIndex.getZeroBased()));
    }

    @Test
    public void execute_personWithHiredTag_confirmedActionExecutesRejection() throws CommandException {
        Person personWithHiredTag = new PersonBuilder()
                .withName("Tagged Person")
                .withPhone("88888888")
                .withEmail("tagged@example.com")
                .withAddress("Tagged Street")
                .withTags("hired")
                .build();

        Model modelWithTagged = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        modelWithTagged.addPerson(personWithHiredTag);
        Index taggedIndex = Index.fromOneBased(modelWithTagged.getFilteredPersonList().size());

        RejectCommand rejectCommand = new RejectCommand(taggedIndex, VALID_REASON);
        CommandResult confirmationResult = rejectCommand.execute(modelWithTagged);

        // Simulate user clicking Yes
        CommandResult finalResult = confirmationResult.getConfirmedAction().get().execute();

        assertFalse(finalResult.isRequiresConfirmation());
        assertEquals(
                String.format(RejectCommand.MESSAGE_REJECT_PERSON_SUCCESS, VALID_REASON, 1),
                finalResult.getFeedbackToUser());
        assertEquals(Status.REJECTED,
                modelWithTagged.getFilteredPersonList().get(taggedIndex.getZeroBased()).getStatus());
    }

    @Test
    public void execute_blacklistedPerson_throwsCommandException() {
        Person blacklistedPerson = new PersonBuilder()
                .withName("Blacklisted Person")
                .withPhone("99999999")
                .withEmail("blacklisted@example.com")
                .withAddress("Blacklisted Street")
                .withStatus(Status.BLACKLISTED)
                .build();

        Model modelWithBlacklisted = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        modelWithBlacklisted.addPerson(blacklistedPerson);

        Index blacklistedIndex = Index.fromOneBased(modelWithBlacklisted.getFilteredPersonList().size());
        RejectCommand rejectCommand = new RejectCommand(blacklistedIndex, VALID_REASON);

        assertCommandFailure(rejectCommand, modelWithBlacklisted, RejectCommand.MESSAGE_BLACKLISTED_PERSON);
    }

    @Test
    public void execute_multipleRejections_appendsReason() {
        // First rejection
        Person personToReject = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        List<RejectionReason> firstReasons = new ArrayList<>();
        firstReasons.add(VALID_REASON);

        Person firstRejected = new PersonBuilder(personToReject)
                .withStatus(Status.REJECTED)
                .withRejectionReasonsList(firstReasons)
                .build();
        model.setPerson(personToReject, firstRejected);

        // Second rejection with different reason
        RejectCommand secondReject = new RejectCommand(INDEX_FIRST_PERSON, ANOTHER_REASON);

        List<RejectionReason> expectedReasons = new ArrayList<>();
        expectedReasons.add(VALID_REASON);
        expectedReasons.add(ANOTHER_REASON);

        Person secondRejected = new PersonBuilder(firstRejected)
                .withRejectionReasonsList(expectedReasons)
                .build();

        String expectedMessage = String.format(RejectCommand.MESSAGE_REJECT_PERSON_SUCCESS,
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
                .withStatus(Status.REJECTED)
                .withRejectionReasonsList(firstReasons)
                .build();
        model.setPerson(personToReject, firstRejected);

        // Same reason again
        RejectCommand duplicateReject = new RejectCommand(INDEX_FIRST_PERSON, VALID_REASON);

        List<RejectionReason> expectedReasons = new ArrayList<>();
        expectedReasons.add(VALID_REASON);
        expectedReasons.add(VALID_REASON);

        Person secondRejected = new PersonBuilder(firstRejected)
                .withRejectionReasonsList(expectedReasons)
                .build();

        String expectedMessage = String.format(
                RejectCommand.MESSAGE_REJECT_PERSON_SUCCESS_DUPLICATE_WARNING,
                VALID_REASON, 2);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(firstRejected, secondRejected);

        assertCommandSuccess(duplicateReject, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_setsStatusToRejected() {
        Person personToReject = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        assertEquals(Status.ACTIVE, personToReject.getStatus());

        RejectCommand rejectCommand = new RejectCommand(INDEX_FIRST_PERSON, VALID_REASON);
        try {
            rejectCommand.execute(model);
        } catch (Exception e) {
            throw new AssertionError("Execution should not fail.", e);
        }

        Person rejectedPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        assertEquals(Status.REJECTED, rejectedPerson.getStatus());
    }

    @Test
    public void equals() {
        RejectCommand rejectFirstCommand = new RejectCommand(INDEX_FIRST_PERSON, VALID_REASON);
        RejectCommand rejectSecondCommand = new RejectCommand(INDEX_SECOND_PERSON, VALID_REASON);
        RejectCommand rejectFirstDiffReason = new RejectCommand(INDEX_FIRST_PERSON, ANOTHER_REASON);

        // same object -> returns true
        assertTrue(rejectFirstCommand.equals(rejectFirstCommand));

        // same values -> returns true
        RejectCommand rejectFirstCommandCopy = new RejectCommand(INDEX_FIRST_PERSON, VALID_REASON);
        assertTrue(rejectFirstCommand.equals(rejectFirstCommandCopy));

        // different types -> returns false
        assertFalse(rejectFirstCommand.equals(1));

        // null -> returns false
        assertFalse(rejectFirstCommand.equals(null));

        // different index -> returns false
        assertFalse(rejectFirstCommand.equals(rejectSecondCommand));

        // different reason -> returns false
        assertFalse(rejectFirstCommand.equals(rejectFirstDiffReason));
    }

    @Test
    public void toStringMethod() {
        Index targetIndex = Index.fromOneBased(1);
        RejectCommand rejectCommand = new RejectCommand(targetIndex, VALID_REASON);
        String expected = RejectCommand.class.getCanonicalName()
                + "{targetIndex=" + targetIndex + ", reason=" + VALID_REASON + "}";
        assertEquals(expected, rejectCommand.toString());
    }
}
