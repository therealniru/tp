package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.DANIEL;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.PersonHasTagPredicate;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) for {@code FilterCommand}.
 */
public class FilterCommandTest {
    private final Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private final Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void equals() {
        PersonHasTagPredicate firstPredicate = new PersonHasTagPredicate(new Tag("friends"));
        PersonHasTagPredicate secondPredicate = new PersonHasTagPredicate(new Tag("owesMoney"));

        FilterCommand firstCommand = new FilterCommand(firstPredicate);
        FilterCommand secondCommand = new FilterCommand(secondPredicate);

        assertTrue(firstCommand.equals(firstCommand));

        FilterCommand firstCommandCopy = new FilterCommand(firstPredicate);
        assertTrue(firstCommand.equals(firstCommandCopy));

        assertFalse(firstCommand.equals(1));
        assertFalse(firstCommand.equals(null));
        assertFalse(firstCommand.equals(secondCommand));
    }

    @Test
    public void execute_tagNotFound_noPersonFound() {
        String expectedMessage = FilterCommand.MESSAGE_NO_MATCH;
        PersonHasTagPredicate predicate = new PersonHasTagPredicate(new Tag("ghost"));
        FilterCommand command = new FilterCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredPersonList());
    }

    @Test
    public void execute_exactTagMatch_multiplePersonsFound() {
        String expectedMessage = "3 candidates listed.";
        PersonHasTagPredicate predicate = new PersonHasTagPredicate(new Tag("friends"));
        FilterCommand command = new FilterCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(ALICE, BENSON, DANIEL), model.getFilteredPersonList());
    }

    @Test
    public void execute_exactTagMatch_noSubstringMatches() {
        Model customModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Model customExpected = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        customModel.addPerson(new PersonBuilder()
                .withName("Java Person")
                .withPhone("81234567")
                .withEmail("java.person@example.com")
                .withAddress("java street")
                .withTags("Java")
                .build());
        customModel.addPerson(new PersonBuilder()
                .withName("JavaScript Person")
                .withPhone("82345678")
                .withEmail("javascript.person@example.com")
                .withAddress("javascript street")
                .withTags("JavaScript")
                .build());

        customExpected.addPerson(new PersonBuilder()
                .withName("Java Person")
                .withPhone("81234567")
                .withEmail("java.person@example.com")
                .withAddress("java street")
                .withTags("Java")
                .build());
        customExpected.addPerson(new PersonBuilder()
                .withName("JavaScript Person")
                .withPhone("82345678")
                .withEmail("javascript.person@example.com")
                .withAddress("javascript street")
                .withTags("JavaScript")
                .build());

        PersonHasTagPredicate predicate = new PersonHasTagPredicate(new Tag("Java"));
        FilterCommand command = new FilterCommand(predicate);
        customExpected.updateFilteredPersonList(predicate);

        assertCommandSuccess(command, customModel, "1 candidate listed.", customExpected);
        assertEquals(1, customModel.getFilteredPersonList().size());
        assertEquals("Java Person", customModel.getFilteredPersonList().get(0).getName().fullName);
    }

    @Test
    public void toStringMethod() {
        PersonHasTagPredicate predicate = new PersonHasTagPredicate(new Tag("friends"));
        FilterCommand filterCommand = new FilterCommand(predicate);
        String expected = FilterCommand.class.getCanonicalName() + "{predicate=" + predicate + "}";
        assertEquals(expected, filterCommand.toString());
    }
}
