package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.Model;
import seedu.address.model.person.PersonHasTagPredicate;

/**
 * Filters and lists all persons whose tags exactly match the given tag.
 */
public class FilterCommand extends Command {

    public static final String COMMAND_WORD = "filter";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Filters all persons whose tags exactly match the specified tag (case-insensitive) "
            + "and displays them as a list with index numbers.\n"
            + "Parameters: TAG\n"
            + "Example: " + COMMAND_WORD + " friends";

    public static final String MESSAGE_NO_MATCH = "No matching candidates found.";

    private final PersonHasTagPredicate predicate;

    public FilterCommand(PersonHasTagPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(predicate);

        int listSize = model.getFilteredPersonList().size();
        if (listSize == 0) {
            return new CommandResult(MESSAGE_NO_MATCH);
        }
        return new CommandResult(listSize + " candidates listed.");
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof FilterCommand)) {
            return false;
        }

        FilterCommand otherFilterCommand = (FilterCommand) other;
        return predicate.equals(otherFilterCommand.predicate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .toString();
    }
}
