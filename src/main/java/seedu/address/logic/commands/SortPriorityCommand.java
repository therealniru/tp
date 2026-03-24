package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ORDER;

import java.util.Comparator;

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

    public static final String MESSAGE_SUCCESS = "Sorted all candidates by priority status.";
    public static final String MESSAGE_EMPTY_ADDRESS_BOOK = "The address book is currently empty. Nothing to sort.";

    private final boolean isAscending;

    /**
     * @param isAscending whether to sort high priority to the top (true) or bottom (false).
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

        Comparator<Person> comparator = Comparator.comparing((Person p) -> p.getPriority().isPriority ? 0 : 1)
                .thenComparing(Person::getDateAdded, Comparator.reverseOrder())
                .thenComparing(p -> p.getName().fullName);

        if (!isAscending) {
            comparator = Comparator.comparing((Person p) -> p.getPriority().isPriority ? 1 : 0)
                    .thenComparing(Person::getDateAdded, Comparator.reverseOrder())
                    .thenComparing(p -> p.getName().fullName);
        }

        model.sortFilteredPersonList(comparator);

        return new CommandResult(MESSAGE_SUCCESS);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof SortPriorityCommand // instanceof handles nulls
                && isAscending == ((SortPriorityCommand) other).isAscending); // state check
    }
}
