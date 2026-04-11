package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PRIORITY;

import java.util.ArrayList;
import java.util.Collections;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Address;
import seedu.address.model.person.DateAdded;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Priority;

/**
 * Parses input arguments and creates a new AddCommand object
 */
public class AddCommandParser implements Parser<AddCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddCommand
     * and returns an AddCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS,
                        PREFIX_PRIORITY);

        if (!argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
        }

        checkRequiredPrefixes(argMultimap);

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS,
                PREFIX_PRIORITY);
        Name name = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get());
        Phone phone = ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).get());
        Email email = ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL).get());
        Address address = ParserUtil.parseAddress(argMultimap.getValue(PREFIX_ADDRESS).get());
        Priority priority = argMultimap.getValue(PREFIX_PRIORITY).isPresent()
                ? ParserUtil.parsePriority(argMultimap.getValue(PREFIX_PRIORITY).get())
                : new Priority("no");

        Person person = new Person(name, phone, email, address, Collections.emptySet(),
                new ArrayList<>(), new DateAdded(), priority);

        return new AddCommand(person);
    }

    /**
     * Checks that all required prefixes (name, phone, email, address) are present
     * and throws a ParseException with a specific error message if any are missing.
     */
    private static void checkRequiredPrefixes(ArgumentMultimap argMultimap) throws ParseException {
        boolean hasName = argMultimap.getValue(PREFIX_NAME).isPresent();
        boolean hasPhone = argMultimap.getValue(PREFIX_PHONE).isPresent();
        boolean hasEmail = argMultimap.getValue(PREFIX_EMAIL).isPresent();
        boolean hasAddress = argMultimap.getValue(PREFIX_ADDRESS).isPresent();

        if (!hasName && !hasPhone && !hasEmail && !hasAddress) {
            throw new ParseException(AddCommand.MESSAGE_MISSING_ALL);
        }
        if (!hasName) {
            throw new ParseException(AddCommand.MESSAGE_MISSING_NAME);
        }
        if (!hasPhone) {
            throw new ParseException(AddCommand.MESSAGE_MISSING_PHONE);
        }
        if (!hasEmail) {
            throw new ParseException(AddCommand.MESSAGE_MISSING_EMAIL);
        }
        if (!hasAddress) {
            throw new ParseException(AddCommand.MESSAGE_MISSING_ADDRESS);
        }
    }

}
