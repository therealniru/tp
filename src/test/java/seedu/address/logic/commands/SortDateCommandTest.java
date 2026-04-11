package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains unit and integration tests for {@code SortDateCommand}.
 */
public class SortDateCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_sortDateAscending_success() {
        SortDateCommand sortDateCommand = new SortDateCommand(true);
        String expectedMessage = SortDateCommand.MESSAGE_SUCCESS_ASC;

        expectedModel.sortFilteredPersonList(
                java.util.Comparator.comparing(Person::getDateAdded)
                        .thenComparing(p -> p.getName().fullName)
        );
        expectedModel.updateFilteredPersonList(seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS);

        assertCommandSuccess(sortDateCommand, model, expectedMessage, expectedModel);
        assertEquals(expectedModel.getFilteredPersonList(), model.getFilteredPersonList());
    }

    @Test
    public void execute_sortDateDescending_success() {
        SortDateCommand sortDateCommand = new SortDateCommand(false);
        String expectedMessage = SortDateCommand.MESSAGE_SUCCESS_DESC;

        expectedModel.sortFilteredPersonList(
                java.util.Comparator.comparing(Person::getDateAdded, java.util.Comparator.reverseOrder())
                        .thenComparing(p -> p.getName().fullName)
        );
        expectedModel.updateFilteredPersonList(seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS);

        assertCommandSuccess(sortDateCommand, model, expectedMessage, expectedModel);
        assertEquals(expectedModel.getFilteredPersonList(), model.getFilteredPersonList());
    }

    @Test
    public void execute_emptyAddressBook_showsEmptyMessage() {
        Model emptyModel = new ModelManager();
        SortDateCommand sortDateCommand = new SortDateCommand(true);

        CommandResult result = sortDateCommand.execute(emptyModel);
        assertEquals(SortDateCommand.MESSAGE_EMPTY_ADDRESS_BOOK, result.getFeedbackToUser());
    }

    @Test
    public void execute_emptyAddressBookDescending_showsEmptyMessage() {
        Model emptyModel = new ModelManager();
        SortDateCommand sortDateCommand = new SortDateCommand(false);

        CommandResult result = sortDateCommand.execute(emptyModel);
        assertEquals(SortDateCommand.MESSAGE_EMPTY_ADDRESS_BOOK, result.getFeedbackToUser());
    }

    @Test
    public void execute_sortAscendingSecondaryByName_alphabeticalOnSameDate() {
        // Candidates with same date should be sorted alphabetically
        Person aPerson = new PersonBuilder().withName("Alice").withPhone("11111111")
                .withEmail("a@a.com").withDateAdded("01/01/2024 10:00").build();
        Person zPerson = new PersonBuilder().withName("Zack").withPhone("22222222")
                .withEmail("z@z.com").withDateAdded("01/01/2024 10:00").build();

        Model customModel = new ModelManager();
        customModel.addPerson(zPerson);
        customModel.addPerson(aPerson);

        new SortDateCommand(true).execute(customModel);

        assertEquals("Alice", customModel.getFilteredPersonList().get(0).getName().fullName);
        assertEquals("Zack", customModel.getFilteredPersonList().get(1).getName().fullName);
    }

    @Test
    public void execute_sortDescendingSecondaryByName_alphabeticalOnSameDate() {
        // Candidates with same date in descending sort should still be sorted alphabetically
        Person aPerson = new PersonBuilder().withName("Alice").withPhone("11111111")
                .withEmail("a@a.com").withDateAdded("01/01/2024 10:00").build();
        Person zPerson = new PersonBuilder().withName("Zack").withPhone("22222222")
                .withEmail("z@z.com").withDateAdded("01/01/2024 10:00").build();

        Model customModel = new ModelManager();
        customModel.addPerson(zPerson);
        customModel.addPerson(aPerson);

        new SortDateCommand(false).execute(customModel);

        // Same date: alphabetical still applies
        assertEquals("Alice", customModel.getFilteredPersonList().get(0).getName().fullName);
        assertEquals("Zack", customModel.getFilteredPersonList().get(1).getName().fullName);
    }

    @Test
    public void execute_sortDateDescendingOrder_newerFirst() {
        // Candidates with different dates — descending should show newest first
        Person older = new PersonBuilder().withName("Older").withPhone("11111111")
                .withEmail("older@test.com").withDateAdded("01/01/2022 10:00").build();
        Person newer = new PersonBuilder().withName("Newer").withPhone("22222222")
                .withEmail("newer@test.com").withDateAdded("01/01/2024 10:00").build();

        Model customModel = new ModelManager();
        customModel.addPerson(older);
        customModel.addPerson(newer);

        new SortDateCommand(false).execute(customModel);

        assertEquals("Newer", customModel.getFilteredPersonList().get(0).getName().fullName);
        assertEquals("Older", customModel.getFilteredPersonList().get(1).getName().fullName);
    }

    @Test
    public void execute_sortDateAscendingOrder_olderFirst() {
        // Ascending sort should show oldest first
        Person older = new PersonBuilder().withName("Older").withPhone("11111111")
                .withEmail("older@test.com").withDateAdded("01/01/2022 10:00").build();
        Person newer = new PersonBuilder().withName("Newer").withPhone("22222222")
                .withEmail("newer@test.com").withDateAdded("01/01/2024 10:00").build();

        Model customModel = new ModelManager();
        customModel.addPerson(newer);
        customModel.addPerson(older);

        new SortDateCommand(true).execute(customModel);

        assertEquals("Older", customModel.getFilteredPersonList().get(0).getName().fullName);
        assertEquals("Newer", customModel.getFilteredPersonList().get(1).getName().fullName);
    }

    @Test
    public void execute_singlePerson_success() {
        Person onlyPerson = new PersonBuilder().withName("Solo").withPhone("99999999")
                .withEmail("solo@solo.com").build();

        Model customModel = new ModelManager();
        customModel.addPerson(onlyPerson);

        CommandResult result = new SortDateCommand(true).execute(customModel);
        assertEquals(SortDateCommand.MESSAGE_SUCCESS_ASC, result.getFeedbackToUser());
        assertEquals(1, customModel.getFilteredPersonList().size());
    }

    @Test
    public void equals() {
        SortDateCommand sortAscFirst = new SortDateCommand(true);
        SortDateCommand sortAscSecond = new SortDateCommand(true);
        SortDateCommand sortDesc = new SortDateCommand(false);

        // same object -> returns true
        assertTrue(sortAscFirst.equals(sortAscFirst));

        // same values -> returns true
        assertTrue(sortAscFirst.equals(sortAscSecond));

        // different types -> returns false
        assertFalse(sortAscFirst.equals(1));

        // null -> returns false
        assertFalse(sortAscFirst.equals(null));

        // different values -> returns false
        assertFalse(sortAscFirst.equals(sortDesc));
    }

    @Test
    public void toStringMethod() {
        SortDateCommand sortDateCommand = new SortDateCommand(true);
        // Ensure toString does not throw and returns non-null
        assertNotNull(sortDateCommand.toString());
    }
}
