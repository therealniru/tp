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

    public static final int MAX_POOL_SIZE = 50;
    public static final String MESSAGE_POOL_FULL =
            "Tag pool is full. The pool cannot hold more than " + MAX_POOL_SIZE + " tags. "
            + "Delete unused tags with `tagpool dt/TAG` before creating new ones.";

    public static final String MESSAGE_SUCCESS = "Tag pool updated. Created: %d tag(s). Deleted: %d tag(s).";
    public static final String MESSAGE_POOL_EMPTY = "Tag pool is empty. Use `tagpool at/TAG` to create tags.";
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
        assert !toAdd.isEmpty() || !toDelete.isEmpty() : "Mutation mode requires at least one tag operation";
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
            return listTagPool(model);
        }

        // ── Phase 1: Pre-Execution Validation (zero mutations) ──
        validatePreExecution(model);
        assert toDelete.stream().allMatch(model::hasTag) : "All tags to delete must exist after validation";
        assert toAdd.stream().noneMatch(model::hasTag) : "No tags to add should already exist after validation";

        // ── Phases 2–6: Sweep, Mutate, Reset, Feedback ──
        cascadeSweep(model);
        applyPoolMutations(model);
        return new CommandResult(buildResultMessage());
    }

    private CommandResult listTagPool(Model model) {
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

    private void cascadeSweep(Model model) {
        List<Person> allCandidates = model.getAddressBook().getPersonList();
        for (Tag targetTagToDelete : toDelete) {
            List<Person> snapshot = List.copyOf(allCandidates);
            for (Person person : snapshot) {
                if (person.getTags().contains(targetTagToDelete)) {
                    Set<Tag> updatedTags = new HashSet<>(person.getTags());
                    updatedTags.remove(targetTagToDelete);
                    Person editedPerson = new Person(
                            person.getName(), person.getPhone(), person.getEmail(),
                            person.getAddress(), updatedTags, person.getRejectionReasons(),
                            person.getDateAdded(), person.getPriority(), person.getNotes());
                    model.setPerson(person, editedPerson);
                }
            }
        }
    }

    private void applyPoolMutations(Model model) {
        for (Tag tag : toDelete) {
            model.deleteTag(tag);
        }
        for (Tag tag : toAdd) {
            model.addTag(tag);
        }
        if (!toAdd.isEmpty() || !toDelete.isEmpty()) {
            model.updateFilteredPersonList(Model.PREDICATE_SHOW_ALL_PERSONS);
            model.sortFilteredPersonList(ListCommand.DEFAULT_SORT);
        }
    }

    private String buildResultMessage() {
        String result = String.format(MESSAGE_SUCCESS, toAdd.size(), toDelete.size());
        if (!toDelete.isEmpty()) {
            result += "\nWarning: Cascade deletion — candidates assigned to the deleted tag(s)"
                    + " have had those tags removed (if any such candidates exist).";
        }
        return result;
    }
    private void validatePreExecution(Model model) throws CommandException {
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
                    throw new CommandException(String.format("Error: Duplicate tag '%s' in your add list. "
                            + "Each tag can only appear once per command.", toAdd.get(j).tagName));
                }
            }
        }
        // 1c. Duplicate check within toDelete list
        for (int i = 0; i < toDelete.size(); i++) {
            for (int j = i + 1; j < toDelete.size(); j++) {
                if (toDelete.get(i).equals(toDelete.get(j))) {
                    throw new CommandException(String.format("Error: Duplicate tag '%s' in delete list.",
                            toDelete.get(j).tagName));
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
        // 1f. Capacity check (net change: additions minus deletions)
        int currentPoolSize = model.getAddressBook().getTagList().size();
        if (!toAdd.isEmpty() && currentPoolSize - toDelete.size() + toAdd.size() > MAX_POOL_SIZE) {
            throw new CommandException(MESSAGE_POOL_FULL);
        }
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
