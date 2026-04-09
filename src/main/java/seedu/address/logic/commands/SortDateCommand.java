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
 * Sorts all candidates in the address book by date added.
 */
public class SortDateCommand extends Command {

    public static final String COMMAND_WORD = "sort";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Sorts candidates chronologically based on their creation datetime.\n"
            + "Parameters: " + COMMAND_WORD + " date " + PREFIX_ORDER + "ORDER\n"
            + "ORDER must be 'asc' or 'desc' (case-insensitive).\n"
            + "Example: " + COMMAND_WORD + " date " + PREFIX_ORDER + "desc";

    public static final String MESSAGE_SUCCESS_ASC = "Sorted all candidates by date added in ascending order.";
    public static final String MESSAGE_SUCCESS_DESC = "Sorted all candidates by date added in descending order.";
    public static final String MESSAGE_EMPTY_ADDRESS_BOOK = "The address book is currently empty. Nothing to sort.";

    private static final Logger logger = LogsCenter.getLogger(SortDateCommand.class);

    private final boolean isAscending;

    public SortDateCommand(boolean isAscending) {
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
        logger.fine("SortDateCommand executed: " + resultMessage);
        return new CommandResult(resultMessage);
    }

    /**
     * Builds the sort comparator for the given order direction.
     * Secondary sort is by Name ascending for determinism.
     *
     * @param ascending true for oldest-first, false for newest-first.
     * @return the composed {@code Comparator<Person>}.
     */
    private static Comparator<Person> buildComparator(boolean ascending) {
        if (ascending) {
            return Comparator.comparing(Person::getDateAdded)
                             .thenComparing(p -> p.getName().fullName.toLowerCase());
        }
        return Comparator.comparing(Person::getDateAdded, Comparator.reverseOrder())
                         .thenComparing(p -> p.getName().fullName.toLowerCase());
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof SortDateCommand
                && isAscending == ((SortDateCommand) other).isAscending);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("isAscending", isAscending)
                .toString();
    }
}
