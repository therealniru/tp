package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADD_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DELETE_TAG;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.collections.ObservableList;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

/**
 * Manages the master tag pool: bulk-creates and bulk-deletes system tags.
 * Implements a Cascading Sweep to safely strip deleted tags from all candidates
 * before removing them from the master registry.
 */
public class TagPoolCommand extends Command {

    public static final String COMMAND_WORD = "tagpool";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Creates and/or deletes tags in the master tag pool.\n"
            + "To list all tags: " + COMMAND_WORD + "\n"
            + "To create/delete: " + COMMAND_WORD + " [" + PREFIX_ADD_TAG + "TAG_TO_CREATE]... ["
            + PREFIX_DELETE_TAG + "TAG_TO_DELETE]...\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_ADD_TAG + "Frontend "
            + PREFIX_ADD_TAG + "Backend " + PREFIX_DELETE_TAG + "Intern";

    public static final String MESSAGE_SUCCESS = "Tag pool updated. Created: %d tag(s). Deleted: %d tag(s).";
    public static final String MESSAGE_POOL_EMPTY = "Tag pool is empty. Use `tagpool a/TAG` to create tags.";
    public static final String MESSAGE_POOL_LISTING = "Tag pool (%d tag%s): %s";
    public static final String MESSAGE_CONFLICT =
            "Error: Cannot create and delete the same tag ('%s') in one command.";
    public static final String MESSAGE_DUPLICATE_ADD =
            "Error: The tag '%s' already exists in the tag pool.";
    public static final String MESSAGE_MISSING_DELETE =
            "Error: Cannot delete tag '%s' because it does not exist in the tag pool.";

    private final List<Tag> toAdd;
    private final List<Tag> toDelete;
    private final boolean isListMode;

    /**
     * Creates a TagPoolCommand to add and/or delete the specified tags.
     */
    public TagPoolCommand(List<Tag> toAdd, List<Tag> toDelete) {
        requireNonNull(toAdd);
        requireNonNull(toDelete);
        this.toAdd = List.copyOf(toAdd);
        this.toDelete = List.copyOf(toDelete);
        this.isListMode = false;
    }

    /**
     * Creates a TagPoolCommand in list mode that displays all tags in the pool.
     */
    public TagPoolCommand() {
        this.toAdd = List.of();
        this.toDelete = List.of();
        this.isListMode = true;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (isListMode) {
            ObservableList<Tag> allTags = model.getAddressBook().getTagList();
            if (allTags.isEmpty()) {
                return new CommandResult(MESSAGE_POOL_EMPTY);
            }
            String tagNames = allTags.stream()
                    .map(tag -> tag.tagName)
                    .sorted(String.CASE_INSENSITIVE_ORDER)
                    .collect(Collectors.joining(", "));
            return new CommandResult(String.format(MESSAGE_POOL_LISTING,
                    allTags.size(), allTags.size() == 1 ? "" : "s", tagNames));
        }

        // ── Phase 1: Pre-Execution Validation (zero mutations) ──

        // 1a. Conflict check
        for (Tag addTag : toAdd) {
            for (Tag delTag : toDelete) {
                if (addTag.equals(delTag)) {
                    throw new CommandException(String.format(MESSAGE_CONFLICT, addTag.tagName));
                }
            }
        }

        // 1b. Duplicate check within toAdd list
        for (int i = 0; i < toAdd.size(); i++) {
            for (int j = i + 1; j < toAdd.size(); j++) {
                if (toAdd.get(i).equals(toAdd.get(j))) {
                    throw new CommandException(String.format(MESSAGE_DUPLICATE_ADD, toAdd.get(j).tagName));
                }
            }
        }

        // 1c. Duplicate check within toDelete list
        for (int i = 0; i < toDelete.size(); i++) {
            for (int j = i + 1; j < toDelete.size(); j++) {
                if (toDelete.get(i).equals(toDelete.get(j))) {
                    throw new CommandException(
                            String.format("Error: Duplicate tag '%s' in delete list.", toDelete.get(j).tagName));
                }
            }
        }

        // 1d. Additions check against existing pool
        for (Tag tag : toAdd) {
            if (model.hasTag(tag)) {
                throw new CommandException(String.format(MESSAGE_DUPLICATE_ADD, tag.tagName));
            }
        }

        // 1e. Deletions check against existing pool
        for (Tag tag : toDelete) {
            if (!model.hasTag(tag)) {
                throw new CommandException(String.format(MESSAGE_MISSING_DELETE, tag.tagName));
            }
        }

        // ── Phase 2: Pool Additions ──
        for (Tag tag : toAdd) {
            model.addTag(tag);
        }

        // ── Phase 3: Cascading Sweep ──
        List<Person> allCandidates = model.getAddressBook().getPersonList();
        for (Tag targetTagToDelete : toDelete) {
            // Iterate over a snapshot to avoid ConcurrentModificationException
            List<Person> snapshot = List.copyOf(allCandidates);
            for (Person person : snapshot) {
                if (person.getTags().contains(targetTagToDelete)) {
                    Set<Tag> updatedTags = new HashSet<>(person.getTags());
                    updatedTags.remove(targetTagToDelete);
                    Person editedPerson = new Person(
                            person.getName(), person.getPhone(), person.getEmail(),
                            person.getAddress(), updatedTags, person.getStatus(),
                            person.getRejectionReasons(), person.getDateAdded(), person.getPriority(),
                            person.getNotes());
                    model.setPerson(person, editedPerson);
                }
            }
        }

        // ── Phase 4: Pool Deletions ──
        for (Tag tag : toDelete) {
            model.deleteTag(tag);
        }

        // ── Phase 5: UI Feedback ──
        return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd.size(), toDelete.size()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof TagPoolCommand)) {
            return false;
        }
        TagPoolCommand otherCommand = (TagPoolCommand) other;
        return isListMode == otherCommand.isListMode
                && toAdd.equals(otherCommand.toAdd) && toDelete.equals(otherCommand.toDelete);
    }
}
