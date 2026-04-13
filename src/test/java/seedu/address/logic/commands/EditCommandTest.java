package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.person.Priority;
import seedu.address.testutil.EditPersonDescriptorBuilder;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for EditCommand.
 */
public class EditCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() {
        Person originalPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(originalPerson).withName("Amy Bee")
                .withPhone("85355255").withEmail("amy@gmail.com").build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(editedPerson).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(originalPerson, editedPerson);

        assertCommandSuccess(editCommand, model, new CommandResult(expectedMessage, editedPerson), expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() {
        Index indexLastPerson = Index.fromOneBased(model.getFilteredPersonList().size());
        Person lastPerson = model.getFilteredPersonList().get(indexLastPerson.getZeroBased());

        PersonBuilder personInList = new PersonBuilder(lastPerson);
        Person editedPerson = personInList.withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB).build();

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB).build();
        EditCommand editCommand = new EditCommand(indexLastPerson, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(lastPerson, editedPerson);

        assertCommandSuccess(editCommand, model, new CommandResult(expectedMessage, editedPerson), expectedModel);
    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_noChangeDetected() {
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, new EditPersonDescriptor());
        // No fields edited -> returns no-change message
        String expectedMessage = "Note: No changes detected; candidate details remain the same.";

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person personInFilteredList = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(personInFilteredList).withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON,
                new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(model.getFilteredPersonList().get(0), editedPerson);

        assertCommandSuccess(editCommand, model, new CommandResult(expectedMessage, editedPerson), expectedModel);
    }

    @Test
    public void execute_priorityFieldSpecifiedUnfilteredList_success() {
        // Edit only the priority field to "yes"
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(firstPerson).withPriority("yes").build();

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withPriority("yes").build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(firstPerson, editedPerson);

        assertCommandSuccess(editCommand, model, new CommandResult(expectedMessage, editedPerson), expectedModel);
    }

    @Test
    public void execute_priorityFieldSetToNo_success() {
        // Edit only the priority field to "no"
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        // First set to "yes" so the edit from "no" to "no" is a real change
        Person personWithPriority = new PersonBuilder(firstPerson).withPriority("yes").build();

        Model freshModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        freshModel.setPerson(firstPerson, personWithPriority);

        Person editedPerson = new PersonBuilder(personWithPriority).withPriority("no").build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withPriority("no").build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(freshModel.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(personWithPriority, editedPerson);

        assertCommandSuccess(editCommand, freshModel, new CommandResult(expectedMessage, editedPerson), expectedModel);
    }

    @Test
    public void execute_priorityUnchanged_noChangeDetected() {
        // Person already has priority "no" (default); editing with "no" again => no change
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withPriority("no").build();
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        // Only detect no-change if all resulting fields are identical
        Person editedPerson = new PersonBuilder(firstPerson).withPriority("no").build();
        if (firstPerson.equals(editedPerson)) {
            EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, descriptor);
            String expectedMessage = "Note: No changes detected; candidate details remain the same.";
            Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
            assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
        }
    }

    @Test
    public void execute_duplicatePersonUnfilteredList_failure() {
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(firstPerson).build();
        EditCommand editCommand = new EditCommand(INDEX_SECOND_PERSON, descriptor);

        String expectedMessage = String.format("Error: This edit would duplicate an existing candidate. "
                        + "Phone %s and Email %s are already assigned to %s.",
                firstPerson.getPhone().value, firstPerson.getEmail().value, firstPerson.getName().fullName);

        assertCommandFailure(editCommand, model, expectedMessage);
    }

    @Test
    public void execute_duplicatePersonFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        // edit person in filtered list into a duplicate in address book
        Person personInList = model.getAddressBook().getPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON,
                new EditPersonDescriptorBuilder(personInList).build());

        String expectedMessage = String.format("Error: This edit would duplicate an existing candidate. "
                        + "Phone %s and Email %s are already assigned to %s.",
                personInList.getPhone().value, personInList.getEmail().value, personInList.getName().fullName);

        assertCommandFailure(editCommand, model, expectedMessage);
    }

    @Test
    public void execute_duplicatePhoneOnly_failure() {
        // Edit second person to have first person's phone but a unique email
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withPhone(firstPerson.getPhone().value)
                .withEmail("unique999@example.com").build();
        EditCommand editCommand = new EditCommand(INDEX_SECOND_PERSON, descriptor);

        String expectedMessage = String.format("Error: This edit would duplicate an existing candidate. "
                        + "Phone %s is already assigned to %s.",
                firstPerson.getPhone().value, firstPerson.getName().fullName);

        assertCommandFailure(editCommand, model, expectedMessage);
    }

    @Test
    public void execute_duplicateEmailOnly_failure() {
        // Edit second person to have first person's email but a unique phone
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withEmail(firstPerson.getEmail().value)
                .withPhone("99999999").build();
        EditCommand editCommand = new EditCommand(INDEX_SECOND_PERSON, descriptor);

        String expectedMessage = String.format("Error: This edit would duplicate an existing candidate. "
                        + "Email %s is already assigned to %s.",
                firstPerson.getEmail().value, firstPerson.getName().fullName);

        assertCommandFailure(editCommand, model, expectedMessage);
    }

    @Test
    public void execute_caseOnlyNameChange_success() {
        // Changing "Alice Pauline" to "alice pauline" should be detected as a real edit
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        String lowerCaseName = firstPerson.getName().fullName.toLowerCase();
        Person editedPerson = new PersonBuilder(firstPerson).withName(lowerCaseName).build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(lowerCaseName).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(firstPerson, editedPerson);

        assertCommandSuccess(editCommand, model, new CommandResult(expectedMessage, editedPerson), expectedModel);
    }

    @Test
    public void execute_emptyList_failure() {
        Model emptyModel = new ModelManager();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, descriptor);

        assertCommandFailure(editCommand, emptyModel, Messages.MESSAGE_EMPTY_LIST);
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(outOfBoundIndex, descriptor);

        String expectedMessage = String.format("Error: Index %d is out of range. "
                        + "The current list has %d candidate(s). Please provide an index from 1 to %d.",
                outOfBoundIndex.getOneBased(), model.getFilteredPersonList().size(),
                model.getFilteredPersonList().size());

        assertCommandFailure(editCommand, model, expectedMessage);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of address book.
     */
    @Test
    public void execute_invalidPersonIndexFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        EditCommand editCommand = new EditCommand(outOfBoundIndex,
                new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build());

        String expectedMessage = String.format("Error: Index %d is out of range. "
                        + "The current list has %d candidate(s). Please provide an index from 1 to %d.",
                outOfBoundIndex.getOneBased(), model.getFilteredPersonList().size(),
                model.getFilteredPersonList().size());

        assertCommandFailure(editCommand, model, expectedMessage);
    }

    @Test
    public void equals() {
        final EditCommand standardCommand = new EditCommand(INDEX_FIRST_PERSON, DESC_AMY);

        // same values -> returns true
        EditPersonDescriptor copyDescriptor = new EditPersonDescriptor(DESC_AMY);
        EditCommand commandWithSameValues = new EditCommand(INDEX_FIRST_PERSON, copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new EditCommand(INDEX_SECOND_PERSON, DESC_AMY)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new EditCommand(INDEX_FIRST_PERSON, DESC_BOB)));
    }

    @Test
    public void toStringMethod() {
        Index index = Index.fromOneBased(1);
        EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor();
        EditCommand editCommand = new EditCommand(index, editPersonDescriptor);
        String expected = EditCommand.class.getCanonicalName() + "{index=" + index + ", editPersonDescriptor="
                + editPersonDescriptor + "}";
        assertEquals(expected, editCommand.toString());
    }

    @Test
    public void editDescriptor_priorityField_success() {
        EditPersonDescriptor descriptor = new EditPersonDescriptor();
        descriptor.setPriority(new Priority("yes"));
        assertTrue(descriptor.getPriority().isPresent());
        assertEquals(new Priority("yes"), descriptor.getPriority().get());
    }

    @Test
    public void editDescriptor_priorityFieldEdited_returnsTrue() {
        EditPersonDescriptor descriptor = new EditPersonDescriptor();
        assertFalse(descriptor.isAnyFieldEdited());
        descriptor.setPriority(new Priority("yes"));
        assertTrue(descriptor.isAnyFieldEdited());
    }

    @Test
    public void execute_addressOnlyChange_success() {
        // Change only the address field to cover the hasIdenticalFields branch for address
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(firstPerson).withAddress("New Address 123").build();

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withAddress("New Address 123").build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(firstPerson, editedPerson);

        assertCommandSuccess(editCommand, model, new CommandResult(expectedMessage, editedPerson), expectedModel);
    }

    @Test
    public void execute_emailOnlyChange_success() {
        // Change only the email field to cover the hasIdenticalFields branch for email
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(firstPerson).withEmail("newemail@example.com").build();

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withEmail("newemail@example.com").build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(firstPerson, editedPerson);

        assertCommandSuccess(editCommand, model, new CommandResult(expectedMessage, editedPerson), expectedModel);
    }

    @Test
    public void execute_phoneOnlyChange_success() {
        // Change only the phone field to cover the hasIdenticalFields branch for phone
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(firstPerson).withPhone("11111111").build();

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withPhone("11111111").build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(firstPerson, editedPerson);

        assertCommandSuccess(editCommand, model, new CommandResult(expectedMessage, editedPerson), expectedModel);
    }

    @Test
    public void execute_sameValuesResubmitted_noChangeDetected() {
        // Re-submit the exact same name, phone, email, address — should detect no change
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withName(firstPerson.getName().fullName)
                .withPhone(firstPerson.getPhone().value)
                .withEmail(firstPerson.getEmail().value)
                .withAddress(firstPerson.getAddress().value)
                .withPriority(firstPerson.getPriority().getValue())
                .build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, descriptor);

        String expectedMessage = "Note: No changes detected; candidate details remain the same.";
        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }
}

