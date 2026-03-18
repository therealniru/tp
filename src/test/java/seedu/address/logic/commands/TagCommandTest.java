package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.TagCommandParser;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

/**
 * Unit and integration tests for {@code TagCommand}.
 */
public class TagCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    /**
     * Test 1: Parser must reject a preamble containing garbage text after the index.
     * e.g. "tag 1 randomtext a/Java" must throw a ParseException because
     * "1 randomtext" is not a valid index.
     */
    @Test
    public void parse_preambleWithGarbage_throwsParseException() {
        assertThrows(ParseException.class, () ->
                new TagCommandParser().parse("1 randomtext a/friends"));
    }

    /**
     * Test 2: Fail-fast atomicity.
     * A command with a valid a/Java and an invalid d/FakeTag (not in pool) must
     * fail entirely — "Java" must NOT be added to the candidate.
     */
    @Test
    public void execute_invalidDeleteTagAbortsBeforeMutation() {
        // Add "Java" to pool so it passes the pool-existence check for a/
        model.addTag(new Tag("Java"));

        // Confirm ALICE (index 1) does not yet have "Java"
        Person aliceBefore = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        assertFalse(aliceBefore.getTags().stream()
                .anyMatch(t -> t.tagName.equalsIgnoreCase("Java")));

        // "FakeTag" does not exist in the pool, so d/FakeTag must trigger failure
        TagCommand command = new TagCommand(INDEX_FIRST_PERSON,
                List.of(new Tag("Java")), List.of(new Tag("FakeTag")));

        assertCommandFailure(command, model,
                String.format(TagCommand.MESSAGE_TAG_NOT_IN_POOL, "FakeTag"));

        // Atomicity guarantee: ALICE must still not have "Java"
        Person aliceAfter = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        assertFalse(aliceAfter.getTags().stream()
                .anyMatch(t -> t.tagName.equalsIgnoreCase("Java")));
    }

    /**
     * Test 3: Reference integrity.
     * After a successful tag addition, the Tag object stored on the candidate must
     * be the exact same instance (==) as the canonical tag in the master pool.
     */
    @Test
    public void execute_addTag_storesCanonicalReferenceFromPool() throws CommandException {
        // Register "Java" in the pool; this instance becomes the canonical reference
        Tag canonicalJava = new Tag("Java");
        model.addTag(canonicalJava);

        // Run tag 1 a/Java — ALICE (index 1) gets "Java"
        TagCommand command = new TagCommand(INDEX_FIRST_PERSON,
                List.of(new Tag("Java")), List.of());
        command.execute(model);

        // Locate the "Java" tag on ALICE
        Person alice = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Tag tagOnAlice = alice.getTags().stream()
                .filter(t -> t.tagName.equalsIgnoreCase("Java"))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Expected Java tag on ALICE"));

        // Locate the canonical "Java" tag in the master pool
        Tag tagInPool = model.getAddressBook().getTagList().stream()
                .filter(t -> t.tagName.equalsIgnoreCase("Java"))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Expected Java tag in pool"));

        // Must be the exact same object reference — not merely equal
        assertSame(tagInPool, tagOnAlice);
    }

    /**
     * Test 4: Filtered list indexing.
     * After filtering the list so that BENSON (originally index 2) is at index 1,
     * "tag 1 d/owesMoney" must modify BENSON — not ALICE (who is index 1 in the full list).
     */
    @Test
    public void execute_filteredList_modifiesFirstPersonInFilteredList() throws CommandException {
        // Filter to show only BENSON (full-list index 2 → filtered-list index 1)
        showPersonAtIndex(model, INDEX_SECOND_PERSON);

        // Confirm the person at filtered index 1 is BENSON with "owesMoney"
        Person bensonInFilteredList =
                model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        assertTrue(bensonInFilteredList.getTags().stream()
                .anyMatch(t -> t.tagName.equalsIgnoreCase("owesMoney")),
                "BENSON must have owesMoney before the command");

        // Run tag 1 d/owesMoney — must target filtered index 1 (BENSON), not full-list index 1 (ALICE)
        TagCommand command = new TagCommand(INDEX_FIRST_PERSON,
                List.of(), List.of(new Tag("owesMoney")));
        command.execute(model);

        // BENSON in the master list must no longer have "owesMoney"
        Person bensonAfter = model.getAddressBook().getPersonList().stream()
                .filter(p -> p.getName().fullName.equals("Benson Meier"))
                .findFirst()
                .orElseThrow(() -> new AssertionError("BENSON not found in master list"));
        assertFalse(bensonAfter.getTags().stream()
                .anyMatch(t -> t.tagName.equalsIgnoreCase("owesMoney")),
                "BENSON must not have owesMoney after the command");

        // ALICE must be completely unaffected (she never had owesMoney anyway)
        Person aliceAfter = model.getAddressBook().getPersonList().stream()
                .filter(p -> p.getName().fullName.equals("Alice Pauline"))
                .findFirst()
                .orElseThrow(() -> new AssertionError("ALICE not found in master list"));
        assertFalse(aliceAfter.getTags().stream()
                .anyMatch(t -> t.tagName.equalsIgnoreCase("owesMoney")),
                "ALICE must not have owesMoney — she was not the target");
    }
}
