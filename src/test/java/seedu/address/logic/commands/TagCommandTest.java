package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
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

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    // ── Parser integration ────────────────────────────────────────────────────

    /**
     * Parser must reject a preamble containing garbage text after the index.
     */
    @Test
    public void parse_preambleWithGarbage_throwsParseException() {
        assertThrows(ParseException.class, () ->
                new TagCommandParser().parse("1 randomtext a/friends"));
    }

    // ── Out-of-range index ────────────────────────────────────────────────────

    @Test
    public void execute_outOfRangeIndex_throwsCommandException() {
        Index outOfBounds = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        TagCommand command = new TagCommand(outOfBounds, List.of(), List.of());
        assertCommandFailure(command, model,
                String.format("Error: Index %d is out of range. The current list has %d candidate(s). "
                        + "Please provide an index between 1 and %d.",
                        outOfBounds.getOneBased(),
                        model.getFilteredPersonList().size(),
                        model.getFilteredPersonList().size()));
    }

    @Test
    public void execute_outOfRangeIndexOnFilteredList_throwsCommandException() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        // Filtered list now has 1 person; index 2 is out of range
        TagCommand command = new TagCommand(INDEX_SECOND_PERSON, List.of(), List.of());
        assertCommandFailure(command, model,
                String.format("Error: Index %d is out of range. The current list has %d candidate(s). "
                        + "Please provide an index between 1 and %d.",
                        INDEX_SECOND_PERSON.getOneBased(), 1, 1));
    }

    // ── Conflict check ────────────────────────────────────────────────────────

    @Test
    public void execute_conflictAddAndDeleteSameTag_throwsCommandException() {
        Tag java = new Tag("Java");
        model.addTag(java);
        TagCommand command = new TagCommand(INDEX_FIRST_PERSON, List.of(java), List.of(java));
        assertCommandFailure(command, model,
                String.format(TagCommand.MESSAGE_CONFLICT, "Java"));
    }

    // ── Tag not in pool ───────────────────────────────────────────────────────

    @Test
    public void execute_addTagNotInPool_throwsCommandException() {
        // "Ghost" is not registered in the pool
        TagCommand command = new TagCommand(INDEX_FIRST_PERSON,
                List.of(new Tag("Ghost")), List.of());
        assertCommandFailure(command, model,
                String.format(TagCommand.MESSAGE_TAG_NOT_IN_POOL, "Ghost"));
    }

    /**
     * Fail-fast atomicity: a valid a/Java combined with an invalid d/FakeTag must
     * fail entirely — "Java" must NOT be added to the candidate.
     */
    @Test
    public void execute_invalidDeleteTagAbortsBeforeMutation() {
        model.addTag(new Tag("Java"));

        Person aliceBefore = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        assertFalse(aliceBefore.getTags().stream()
                .anyMatch(t -> t.tagName.equalsIgnoreCase("Java")));

        TagCommand command = new TagCommand(INDEX_FIRST_PERSON,
                List.of(new Tag("Java")), List.of(new Tag("FakeTag")));

        assertCommandFailure(command, model,
                String.format(TagCommand.MESSAGE_TAG_NOT_IN_POOL, "FakeTag"));

        Person aliceAfter = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        assertFalse(aliceAfter.getTags().stream()
                .anyMatch(t -> t.tagName.equalsIgnoreCase("Java")));
    }

    // ── Candidate state checks ────────────────────────────────────────────────

    @Test
    public void execute_deleteTagNotOnCandidate_throwsCommandException() {
        // "Java" exists in pool but ALICE does not have it
        model.addTag(new Tag("Java"));
        TagCommand command = new TagCommand(INDEX_FIRST_PERSON,
                List.of(), List.of(new Tag("Java")));
        assertCommandFailure(command, model,
                String.format(TagCommand.MESSAGE_TAG_NOT_ON_CANDIDATE, "Java"));
    }

    @Test
    public void execute_addTagAlreadyOnCandidate_throwsCommandException() {
        // ALICE already has "friends" and it is already in the pool (from TypicalPersons)
        TagCommand command = new TagCommand(INDEX_FIRST_PERSON,
                List.of(new Tag("friends")), List.of());
        assertCommandFailure(command, model,
                String.format(TagCommand.MESSAGE_TAG_ALREADY_ON_CANDIDATE, "friends"));
    }

    // ── Successful mutations ──────────────────────────────────────────────────

    /**
     * After a successful add, the Tag stored on the candidate must be the exact
     * same instance (==) as the canonical tag in the master pool.
     */
    @Test
    public void execute_addTag_storesCanonicalReferenceFromPool() throws CommandException {
        Tag canonicalJava = new Tag("Java");
        model.addTag(canonicalJava);

        TagCommand command = new TagCommand(INDEX_FIRST_PERSON,
                List.of(new Tag("Java")), List.of());
        command.execute(model);

        Person alice = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Tag tagOnAlice = alice.getTags().stream()
                .filter(t -> t.tagName.equalsIgnoreCase("Java"))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Expected Java tag on ALICE"));

        Tag tagInPool = model.getAddressBook().getTagList().stream()
                .filter(t -> t.tagName.equalsIgnoreCase("Java"))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Expected Java tag in pool"));

        assertSame(tagInPool, tagOnAlice);
    }

    @Test
    public void execute_deleteTag_removesTagFromCandidate() throws CommandException {
        // BENSON has "owesMoney"; delete it
        TagCommand command = new TagCommand(INDEX_SECOND_PERSON,
                List.of(), List.of(new Tag("owesMoney")));
        command.execute(model);

        Person benson = model.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        assertFalse(benson.getTags().stream()
                .anyMatch(t -> t.tagName.equalsIgnoreCase("owesMoney")));
    }

    @Test
    public void execute_addAndDeleteTag_success() throws CommandException {
        // BENSON has "owesMoney"; add "Java" and delete "owesMoney" atomically
        Tag java = new Tag("Java");
        model.addTag(java);

        TagCommand command = new TagCommand(INDEX_SECOND_PERSON,
                List.of(new Tag("Java")), List.of(new Tag("owesMoney")));
        command.execute(model);

        Person benson = model.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        assertTrue(benson.getTags().stream().anyMatch(t -> t.tagName.equalsIgnoreCase("Java")));
        assertFalse(benson.getTags().stream().anyMatch(t -> t.tagName.equalsIgnoreCase("owesMoney")));
    }

    @Test
    public void execute_multipleIndicesAddTag_success() throws CommandException {
        Tag java = new Tag("Java");
        model.addTag(java);

        TagCommand command = new TagCommand(List.of(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON),
                List.of(new Tag("Java")), List.of());
        command.execute(model);

        Person alice = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person benson = model.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());

        assertTrue(alice.getTags().stream().anyMatch(t -> t.tagName.equalsIgnoreCase("Java")));
        assertTrue(benson.getTags().stream().anyMatch(t -> t.tagName.equalsIgnoreCase("Java")));
    }

    @Test
    public void execute_multipleIndicesValidationFails_noMutation() {
        // ALICE does not have "owesMoney", BENSON does. This should fail before any mutation.
        TagCommand command = new TagCommand(List.of(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON),
                List.of(), List.of(new Tag("owesMoney")));

        assertCommandFailure(command, model,
                String.format(TagCommand.MESSAGE_TAG_NOT_ON_CANDIDATE, "owesMoney"));

        Person benson = model.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        assertTrue(benson.getTags().stream().anyMatch(t -> t.tagName.equalsIgnoreCase("owesMoney")));
    }

    @Test
    public void execute_addTag_returnsSuccessMessage() throws CommandException {
        model.addTag(new Tag("Java"));
        Person alice = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        TagCommand command = new TagCommand(INDEX_FIRST_PERSON,
                List.of(new Tag("Java")), List.of());
        CommandResult result = command.execute(model);

        assertEquals(String.format(TagCommand.MESSAGE_SUCCESS, alice.getName().fullName),
                result.getFeedbackToUser());
    }

    // ── Filtered list ─────────────────────────────────────────────────────────

    /**
     * After filtering so that BENSON is at index 1, the command must modify BENSON, not ALICE.
     */
    @Test
    public void execute_filteredList_modifiesFirstPersonInFilteredList() throws CommandException {
        showPersonAtIndex(model, INDEX_SECOND_PERSON);

        Person bensonInFilteredList =
                model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        assertTrue(bensonInFilteredList.getTags().stream()
                .anyMatch(t -> t.tagName.equalsIgnoreCase("owesMoney")),
                "BENSON must have owesMoney before the command");

        TagCommand command = new TagCommand(INDEX_FIRST_PERSON,
                List.of(), List.of(new Tag("owesMoney")));
        command.execute(model);

        Person bensonAfter = model.getAddressBook().getPersonList().stream()
                .filter(p -> p.getName().fullName.equals("Benson Meier"))
                .findFirst()
                .orElseThrow(() -> new AssertionError("BENSON not found in master list"));
        assertFalse(bensonAfter.getTags().stream()
                .anyMatch(t -> t.tagName.equalsIgnoreCase("owesMoney")),
                "BENSON must not have owesMoney after the command");

        Person aliceAfter = model.getAddressBook().getPersonList().stream()
                .filter(p -> p.getName().fullName.equals("Alice Pauline"))
                .findFirst()
                .orElseThrow(() -> new AssertionError("ALICE not found in master list"));
        assertFalse(aliceAfter.getTags().stream()
                .anyMatch(t -> t.tagName.equalsIgnoreCase("owesMoney")),
                "ALICE must not have owesMoney — she was not the target");
    }

    // ── equals ────────────────────────────────────────────────────────────────

    @Test
    public void equals_sameObject_returnsTrue() {
        TagCommand command = new TagCommand(INDEX_FIRST_PERSON,
                List.of(new Tag("Java")), List.of());
        assertEquals(command, command);
    }

    @Test
    public void equals_equalCommands_returnsTrue() {
        TagCommand a = new TagCommand(INDEX_FIRST_PERSON,
                List.of(new Tag("Java")), List.of(new Tag("Python")));
        TagCommand b = new TagCommand(INDEX_FIRST_PERSON,
                List.of(new Tag("Java")), List.of(new Tag("Python")));
        assertEquals(a, b);
    }

    @Test
    public void equals_differentIndex_returnsFalse() {
        TagCommand a = new TagCommand(INDEX_FIRST_PERSON, List.of(), List.of());
        TagCommand b = new TagCommand(INDEX_SECOND_PERSON, List.of(), List.of());
        assertNotEquals(a, b);
    }

    @Test
    public void equals_differentTagsToAdd_returnsFalse() {
        TagCommand a = new TagCommand(INDEX_FIRST_PERSON, List.of(new Tag("Java")), List.of());
        TagCommand b = new TagCommand(INDEX_FIRST_PERSON, List.of(new Tag("Python")), List.of());
        assertNotEquals(a, b);
    }

    @Test
    public void equals_differentTagsToDelete_returnsFalse() {
        TagCommand a = new TagCommand(INDEX_FIRST_PERSON, List.of(), List.of(new Tag("Java")));
        TagCommand b = new TagCommand(INDEX_FIRST_PERSON, List.of(), List.of(new Tag("Python")));
        assertNotEquals(a, b);
    }

    @Test
    public void equals_notTagCommand_returnsFalse() {
        TagCommand command = new TagCommand(INDEX_FIRST_PERSON, List.of(), List.of());
        assertNotEquals(command, "some string");
    }
}
