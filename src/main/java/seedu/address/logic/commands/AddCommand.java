package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Adds a candidate to the address book.
 */
public class AddCommand extends Command {

    public static final String COMMAND_WORD = "add";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a candidate. "
            + "Parameters: "
            + PREFIX_NAME + "NAME "
            + PREFIX_PHONE + "PHONE "
            + PREFIX_EMAIL + "EMAIL "
            + PREFIX_ADDRESS + "ADDRESS "
            + "[" + seedu.address.logic.parser.CliSyntax.PREFIX_PRIORITY + "PRIORITY]\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "John Doe "
            + PREFIX_PHONE + "98765432 "
            + PREFIX_EMAIL + "johnd@example.com "
            + PREFIX_ADDRESS + "311, Clementi Ave 2, #02-25 "
            + seedu.address.logic.parser.CliSyntax.PREFIX_PRIORITY + "yes";

    public static final String MESSAGE_SUCCESS = "New candidate added: %1$s";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the address book";

    public static final String MESSAGE_MISSING_NAME =
            "Error: Name is required. Usage: add n/NAME p/PHONE e/EMAIL a/ADDRESS [pr/PRIORITY]";
    public static final String MESSAGE_MISSING_PHONE =
            "Error: Phone number is required. Usage: add n/NAME p/PHONE e/EMAIL a/ADDRESS [pr/PRIORITY]";
    public static final String MESSAGE_MISSING_EMAIL =
            "Error: Email address is required. Usage: add n/NAME p/PHONE e/EMAIL a/ADDRESS [pr/PRIORITY]";
    public static final String MESSAGE_MISSING_ADDRESS =
            "Error: Address is required. Usage: add n/NAME p/PHONE e/EMAIL a/ADDRESS [pr/PRIORITY]";
    public static final String MESSAGE_MISSING_ALL =
            "Error: Missing required parameters. Usage: add n/NAME p/PHONE e/EMAIL a/ADDRESS [pr/PRIORITY]";

    private final Person toAdd;

    /**
     * Creates an AddCommand to add the specified {@code Person}
     */
    public AddCommand(Person person) {
        requireNonNull(person);
        toAdd = person;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasPerson(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        model.addPerson(toAdd);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        model.sortFilteredPersonList(ListCommand.DEFAULT_SORT);
        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(toAdd)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddCommand)) {
            return false;
        }

        AddCommand otherAddCommand = (AddCommand) other;
        // dateAdded is auto-generated at parse time, so we compare using isSamePerson
        // (phone/email identity) plus name, address, tags, priority, and notes.
        return toAdd.isSamePerson(otherAddCommand.toAdd)
                && toAdd.getName().equals(otherAddCommand.toAdd.getName())
                && toAdd.getAddress().equals(otherAddCommand.toAdd.getAddress())
                && toAdd.getTags().equals(otherAddCommand.toAdd.getTags())
                && toAdd.getPriority().equals(otherAddCommand.toAdd.getPriority())
                && toAdd.getNotes().equals(otherAddCommand.toAdd.getNotes());
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("toAdd", toAdd)
                .toString();
    }
}
