package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ORDER;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.Comparator;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Sorts all candidates in the address book by priority.
 */
public class SortPriorityCommand extends Command {

    public static final String COMMAND_WORD = "sort"; // Preamble is pr

    public static final String MESSAGE_USAGE = COMMAND_WORD + " pr: Sorts candidates by priority.\n"
            + "Secondary sorts by Date Added (descending) and Name (alphabetically).\n"
            + "Parameters: " + PREFIX_ORDER + "ORDER\n"
            + "Example: " + COMMAND_WORD + " pr " + PREFIX_ORDER + "desc";

    public static final String MESSAGE_SUCCESS_ASC =
            "Sorted all candidates by priority status (low-priority first).";
    public static final String MESSAGE_SUCCESS_DESC =
            "Sorted all candidates by priority status (high-priority first).";
    public static final String MESSAGE_EMPTY_ADDRESS_BOOK = "The address book is currently empty. Nothing to sort.";

    private static final Logger logger = LogsCenter.getLogger(SortPriorityCommand.class);

    private final boolean isAscending;

    /**
     * Creates a SortPriorityCommand.
     *
     * @param isAscending if true, low-priority candidates appear first (asc);
     *                    if false, high-priority candidates appear first (desc).
     */
    public SortPriorityCommand(boolean isAscending) {
        this.isAscending = isAscending;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        if (model.getAddressBook().getPersonList().isEmpty()) {
            return new CommandResult(MESSAGE_EMPTY_ADDRESS_BOOK);
        }

        Comparator<Person> comparator = buildComparator(isAscending);

        model.sortFilteredPersonList(comparator);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        String resultMessage = isAscending ? MESSAGE_SUCCESS_ASC : MESSAGE_SUCCESS_DESC;
        logger.fine("SortPriorityCommand executed: " + resultMessage);
        return new CommandResult(resultMessage);
    }

    /**
     * Builds the sort comparator for the given order direction.
     * In ascending order, low-priority candidates (isPriority=false) come first.
     * In descending order, high-priority candidates (isPriority=true) come first.
     * Secondary sort is by Date Added descending, then by Name ascending.
     *
     * @param ascending true for low-priority first, false for high-priority first.
     * @return the composed {@code Comparator<Person>}.
     */
    private static Comparator<Person> buildComparator(boolean ascending) {
        if (ascending) {
            return Comparator.comparing((Person p) -> p.getPriority().isPriority() ? 1 : 0)
                    .thenComparing(Person::getDateAdded, Comparator.reverseOrder())
                    .thenComparing(p -> p.getName().fullName.toLowerCase());
        }
        return Comparator.comparing((Person p) -> p.getPriority().isPriority() ? 0 : 1)
                .thenComparing(Person::getDateAdded, Comparator.reverseOrder())
                .thenComparing(p -> p.getName().fullName.toLowerCase());
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof SortPriorityCommand
                && isAscending == ((SortPriorityCommand) other).isAscending);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("isAscending", isAscending)
                .toString();
    }
}
