package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.model.AddressBook;
import seedu.address.model.Model;

/**
 * Clears the address book.
 */
public class ClearCommand extends Command {

    public static final String COMMAND_WORD = "clear";
    public static final String MESSAGE_SUCCESS = "All candidates and tags have been cleared!";

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.setAddressBook(new AddressBook());
        model.updateFilteredPersonList(Model.PREDICATE_SHOW_ALL_PERSONS);
        model.sortFilteredPersonList(ListCommand.DEFAULT_SORT);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
