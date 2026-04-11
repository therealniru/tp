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
 * Contains unit and integration tests for {@code SortPriorityCommand}.
 */
public class SortPriorityCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_sortPriorityAscending_success() {
        SortPriorityCommand sortPriorityCommand = new SortPriorityCommand(true);
        String expectedMessage = SortPriorityCommand.MESSAGE_SUCCESS_ASC;

        expectedModel.sortFilteredPersonList(
                java.util.Comparator.comparing((Person p) -> p.getPriority().isPriority() ? 1 : 0)
                        .thenComparing(Person::getDateAdded, java.util.Comparator.reverseOrder())
                        .thenComparing(p -> p.getName().fullName)
        );
        expectedModel.updateFilteredPersonList(seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS);

        assertCommandSuccess(sortPriorityCommand, model, expectedMessage, expectedModel);
        assertEquals(expectedModel.getFilteredPersonList(), model.getFilteredPersonList());
    }

    @Test
    public void execute_sortPriorityDescending_success() {
        SortPriorityCommand sortPriorityCommand = new SortPriorityCommand(false);
        String expectedMessage = SortPriorityCommand.MESSAGE_SUCCESS_DESC;

        expectedModel.sortFilteredPersonList(
                java.util.Comparator.comparing((Person p) -> p.getPriority().isPriority() ? 0 : 1)
                        .thenComparing(Person::getDateAdded, java.util.Comparator.reverseOrder())
                        .thenComparing(p -> p.getName().fullName)
        );
        expectedModel.updateFilteredPersonList(seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS);

        assertCommandSuccess(sortPriorityCommand, model, expectedMessage, expectedModel);
        assertEquals(expectedModel.getFilteredPersonList(), model.getFilteredPersonList());
    }

    @Test
    public void execute_emptyAddressBook_showsEmptyMessage() {
        Model emptyModel = new ModelManager();
        SortPriorityCommand sortPriorityCommand = new SortPriorityCommand(true);

        CommandResult result = sortPriorityCommand.execute(emptyModel);
        assertEquals(SortPriorityCommand.MESSAGE_EMPTY_ADDRESS_BOOK, result.getFeedbackToUser());
    }

    @Test
    public void execute_emptyAddressBookDescending_showsEmptyMessage() {
        Model emptyModel = new ModelManager();
        SortPriorityCommand sortPriorityCommand = new SortPriorityCommand(false);

        CommandResult result = sortPriorityCommand.execute(emptyModel);
        assertEquals(SortPriorityCommand.MESSAGE_EMPTY_ADDRESS_BOOK, result.getFeedbackToUser());
    }

    @Test
    public void execute_sortAscendingSecondary_priorityThenDateThenAlpha() {
        // Create candidates with identical priority but different dates
        Person aPerson = new PersonBuilder().withName("Alice").withPhone("11111111")
                .withEmail("a@a.com").withDateAdded("01/01/2024 10:00").withPriority("yes").build();
        Person zPerson = new PersonBuilder().withName("Zack").withPhone("22222222")
                .withEmail("z@z.com").withDateAdded("02/01/2024 10:00").withPriority("yes").build();
        Person cPerson = new PersonBuilder().withName("Charlie").withPhone("33333333")
                .withEmail("c@c.com").withDateAdded("01/01/2024 10:00").withPriority("yes").build();
        Person dPerson = new PersonBuilder().withName("Dan").withPhone("44444444")
                .withEmail("d@d.com").withDateAdded("01/01/2024 10:00").withPriority("no").build();

        Model customModel = new ModelManager();
        customModel.addPerson(aPerson);
        customModel.addPerson(cPerson);
        customModel.addPerson(zPerson);
        customModel.addPerson(dPerson);

        SortPriorityCommand sortPriorityCommand = new SortPriorityCommand(true);
        sortPriorityCommand.execute(customModel);

        // asc = low-priority first, so Dan (no priority) should come first
        assertEquals("Dan", customModel.getFilteredPersonList().get(0).getName().fullName);
        // Then high priority sorted by date desc then name
        // Zack has newer date — high priority
        assertEquals("Zack", customModel.getFilteredPersonList().get(1).getName().fullName);
        // Alice and Charlie same date, same priority — alphabetical
        assertEquals("Alice", customModel.getFilteredPersonList().get(2).getName().fullName);
        assertEquals("Charlie", customModel.getFilteredPersonList().get(3).getName().fullName);
    }

    @Test
    public void execute_sortDescendingSecondary_highPriorityFirst() {
        // desc order: high priority first, non-priority last
        Person aPerson = new PersonBuilder().withName("Alice").withPhone("11111111")
                .withEmail("a@a.com").withDateAdded("01/01/2024 10:00").withPriority("yes").build();
        Person bPerson = new PersonBuilder().withName("Bob").withPhone("22222222")
                .withEmail("b@b.com").withDateAdded("02/01/2024 10:00").withPriority("no").build();
        Person cPerson = new PersonBuilder().withName("Carol").withPhone("33333333")
                .withEmail("c@c.com").withDateAdded("01/01/2024 10:00").withPriority("no").build();

        Model customModel = new ModelManager();
        customModel.addPerson(aPerson);
        customModel.addPerson(bPerson);
        customModel.addPerson(cPerson);

        SortPriorityCommand sortPriorityCommand = new SortPriorityCommand(false);
        sortPriorityCommand.execute(customModel);

        // High priority candidate comes first in descending mode
        assertEquals("Alice", customModel.getFilteredPersonList().get(0).getName().fullName);
        // Non-priority candidates come after
        // Bob has newer date among non-priority
        assertEquals("Bob", customModel.getFilteredPersonList().get(1).getName().fullName);
        assertEquals("Carol", customModel.getFilteredPersonList().get(2).getName().fullName);
    }

    @Test
    public void execute_allHighPriority_sortedByDateThenName() {
        Person aPerson = new PersonBuilder().withName("Alpha").withPhone("11111111")
                .withEmail("alpha@test.com").withDateAdded("01/01/2024 10:00").withPriority("yes").build();
        Person bPerson = new PersonBuilder().withName("Beta").withPhone("22222222")
                .withEmail("beta@test.com").withDateAdded("01/01/2024 10:00").withPriority("yes").build();

        Model customModel = new ModelManager();
        customModel.addPerson(bPerson);
        customModel.addPerson(aPerson);

        new SortPriorityCommand(true).execute(customModel);

        // Same date and priority — alphabetical order
        assertEquals("Alpha", customModel.getFilteredPersonList().get(0).getName().fullName);
        assertEquals("Beta", customModel.getFilteredPersonList().get(1).getName().fullName);
    }

    @Test
    public void execute_allNoPriority_sortedByDateDescThenName() {
        Person older = new PersonBuilder().withName("Alpha").withPhone("11111111")
                .withEmail("alpha@test.com").withDateAdded("01/01/2023 10:00").withPriority("no").build();
        Person newer = new PersonBuilder().withName("Beta").withPhone("22222222")
                .withEmail("beta@test.com").withDateAdded("01/01/2024 10:00").withPriority("no").build();

        Model customModel = new ModelManager();
        customModel.addPerson(older);
        customModel.addPerson(newer);

        new SortPriorityCommand(true).execute(customModel);

        // Both non-priority; newer date first (secondary sort is date desc)
        assertEquals("Beta", customModel.getFilteredPersonList().get(0).getName().fullName);
        assertEquals("Alpha", customModel.getFilteredPersonList().get(1).getName().fullName);
    }

    @Test
    public void execute_singlePerson_success() {
        Person onlyPerson = new PersonBuilder().withName("Solo").withPhone("99999999")
                .withEmail("solo@solo.com").withPriority("yes").build();

        Model customModel = new ModelManager();
        customModel.addPerson(onlyPerson);

        CommandResult result = new SortPriorityCommand(true).execute(customModel);
        assertEquals(SortPriorityCommand.MESSAGE_SUCCESS_ASC, result.getFeedbackToUser());
        assertEquals(1, customModel.getFilteredPersonList().size());
    }

    @Test
    public void equals() {
        SortPriorityCommand sortAscFirst = new SortPriorityCommand(true);
        SortPriorityCommand sortAscSecond = new SortPriorityCommand(true);
        SortPriorityCommand sortDesc = new SortPriorityCommand(false);

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
        SortPriorityCommand sortAscFirst = new SortPriorityCommand(true);
        // Ensure toString does not throw and returns non-null
        assertNotNull(sortAscFirst.toString());
    }
}
