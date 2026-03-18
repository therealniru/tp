package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADD_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DELETE_TAG;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

/**
 * Adds or deletes tags for a specific candidate without overwriting their entire tag set.
 * All tags referenced must already exist in the master tag pool.
 * Implements strict fail-fast atomicity: any validation failure aborts the entire command.
 */
public class TagCommand extends Command {

    public static final String COMMAND_WORD = "tag";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Adds or deletes tags for a specific candidate.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_ADD_TAG + "TAG_TO_ADD]... ["
            + PREFIX_DELETE_TAG + "TAG_TO_DELETE]...\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_ADD_TAG + "Java " + PREFIX_DELETE_TAG + "Intern";

    public static final String MESSAGE_SUCCESS = "Successfully updated tags for candidate: %1$s";
    public static final String MESSAGE_CONFLICT =
            "Error: Cannot add and delete the same tag ('%s') in a single command.";
    public static final String MESSAGE_TAG_NOT_IN_POOL =
            "Error: The tag '%s' does not exist in the system. Please create it first using 'tagpool'.";
    public static final String MESSAGE_TAG_NOT_ON_CANDIDATE =
            "Error: Cannot delete tag '%s' because the candidate does not currently have it.";
    public static final String MESSAGE_TAG_ALREADY_ON_CANDIDATE =
            "Error: The candidate already has the tag '%s'.";

    private final Index index;
    private final List<Tag> tagsToAdd;
    private final List<Tag> tagsToDelete;

    /**
     * Creates a TagCommand to add and/or delete the specified tags on the candidate at {@code index}.
     */
    public TagCommand(Index index, List<Tag> tagsToAdd, List<Tag> tagsToDelete) {
        requireNonNull(index);
        requireNonNull(tagsToAdd);
        requireNonNull(tagsToDelete);
        this.index = index;
        this.tagsToAdd = List.copyOf(tagsToAdd);
        this.tagsToDelete = List.copyOf(tagsToDelete);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        // ── Phase 1: Target Retrieval ──
        List<Person> lastShownList = model.getFilteredPersonList();
        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(String.format(
                    "Error: Index %d is out of range. The current list has %d candidate(s). "
                    + "Please provide an index between 1 and %d.",
                    index.getOneBased(), lastShownList.size(), lastShownList.size()));
        }
        Person personToEdit = lastShownList.get(index.getZeroBased());

        // ── Phase 2a: Conflict Check ──
        for (Tag addTag : tagsToAdd) {
            for (Tag delTag : tagsToDelete) {
                if (addTag.equals(delTag)) {
                    throw new CommandException(String.format(MESSAGE_CONFLICT, addTag.tagName));
                }
            }
        }

        // ── Phase 2b: Master Registry Check (all tags in both lists) ──
        for (Tag tag : tagsToAdd) {
            if (!model.hasTag(tag)) {
                throw new CommandException(String.format(MESSAGE_TAG_NOT_IN_POOL, tag.tagName));
            }
        }
        for (Tag tag : tagsToDelete) {
            if (!model.hasTag(tag)) {
                throw new CommandException(String.format(MESSAGE_TAG_NOT_IN_POOL, tag.tagName));
            }
        }

        // ── Phase 2c: Candidate State Check (Deletions) ──
        for (Tag tag : tagsToDelete) {
            if (!personToEdit.getTags().contains(tag)) {
                throw new CommandException(String.format(MESSAGE_TAG_NOT_ON_CANDIDATE, tag.tagName));
            }
        }

        // ── Phase 2d: Candidate State Check (Additions) ──
        for (Tag tag : tagsToAdd) {
            if (personToEdit.getTags().contains(tag)) {
                throw new CommandException(String.format(MESSAGE_TAG_ALREADY_ON_CANDIDATE, tag.tagName));
            }
        }

        // ── Phase 3: Mutation & Save Phase ──
        Set<Tag> updatedTags = new HashSet<>(personToEdit.getTags());
        for (Tag tagToDelete : tagsToDelete) {
            updatedTags.remove(tagToDelete);
        }
        for (Tag tagToAdd : tagsToAdd) {
            Tag canonicalTag = model.getTag(tagToAdd);
            updatedTags.add(canonicalTag);
        }

        Person editedPerson = new Person(
                personToEdit.getName(), personToEdit.getPhone(), personToEdit.getEmail(),
                personToEdit.getAddress(), updatedTags, personToEdit.getStatus(),
                personToEdit.getRejectionReasons(), personToEdit.getDateAdded());

        model.setPerson(personToEdit, editedPerson);

        // ── Phase 4: UI Feedback ──
        return new CommandResult(String.format(MESSAGE_SUCCESS, personToEdit.getName().fullName));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof TagCommand)) {
            return false;
        }
        TagCommand otherCommand = (TagCommand) other;
        return index.equals(otherCommand.index)
                && tagsToAdd.equals(otherCommand.tagsToAdd)
                && tagsToDelete.equals(otherCommand.tagsToDelete);
    }
}
