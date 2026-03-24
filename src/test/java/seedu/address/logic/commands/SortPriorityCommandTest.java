package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
        String expectedMessage = SortPriorityCommand.MESSAGE_SUCCESS;

        expectedModel.sortFilteredPersonList(
                java.util.Comparator.comparing((Person p) -> p.getPriority().isPriority ? 0 : 1)
                                    .thenComparing(Person::getDateAdded, java.util.Comparator.reverseOrder())
                                    .thenComparing(p -> p.getName().fullName)
        );

        assertCommandSuccess(sortPriorityCommand, model, expectedMessage, expectedModel);
        assertEquals(expectedModel.getFilteredPersonList(), model.getFilteredPersonList());
    }

    @Test
    public void execute_sortPriorityDescending_success() {
        SortPriorityCommand sortPriorityCommand = new SortPriorityCommand(false);
        String expectedMessage = SortPriorityCommand.MESSAGE_SUCCESS;

        expectedModel.sortFilteredPersonList(
                java.util.Comparator.comparing((Person p) -> p.getPriority().isPriority ? 1 : 0)
                                    .thenComparing(Person::getDateAdded, java.util.Comparator.reverseOrder())
                                    .thenComparing(p -> p.getName().fullName)
        );

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
    public void execute_sortSecondary_success() {
        // Create candidates with identical priority but different dates
        Person aPerson = new PersonBuilder().withName("Alice").withPhone("11111111")
                .withEmail("a@a.com").withDateAdded("01/01/2024 10:00 +0800").withPriority("yes").build();
        Person zPerson = new PersonBuilder().withName("Zack").withPhone("22222222")
                .withEmail("z@z.com").withDateAdded("02/01/2024 10:00 +0800").withPriority("yes").build();
        Person cPerson = new PersonBuilder().withName("Charlie").withPhone("33333333")
                .withEmail("c@c.com").withDateAdded("01/01/2024 10:00 +0800").withPriority("yes").build();
        Person dPerson = new PersonBuilder().withName("Dan").withPhone("44444444")
                .withEmail("d@d.com").withDateAdded("01/01/2024 10:00 +0800").withPriority("no").build();

        Model customModel = new ModelManager();
        customModel.addPerson(aPerson);
        customModel.addPerson(cPerson);
        customModel.addPerson(zPerson);
        customModel.addPerson(dPerson);

        SortPriorityCommand sortPriorityCommand = new SortPriorityCommand(true);
        sortPriorityCommand.execute(customModel);

        assertEquals("Zack", customModel.getFilteredPersonList().get(0).getName().fullName); // new date first
        assertEquals("Alice", customModel.getFilteredPersonList().get(1).getName().fullName); // same date, alphabetical
        assertEquals("Charlie", customModel.getFilteredPersonList().get(2).getName().fullName);
        assertEquals("Dan", customModel.getFilteredPersonList().get(3).getName().fullName); // no priority
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
}
