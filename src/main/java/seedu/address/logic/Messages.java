package seedu.address.logic;



import seedu.address.logic.parser.Prefix;
import seedu.address.model.person.Person;

/**
 * Container for user visible messages.
 */
public class Messages {

    public static final String MESSAGE_UNKNOWN_COMMAND = "Error: Unknown command";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT = "Error: Invalid command format! \n%1$s";
    public static final String MESSAGE_INDEX_OUT_OF_RANGE =
            "Error: Index %1$d is out of range. The current list has %2$d candidate(s). "
            + "Please provide an index between 1 and %2$d.";
    public static final String MESSAGE_EMPTY_LIST =
            "Error: The candidate list is currently empty. "
            + "There are no candidates to perform this action on.";
    public static final String MESSAGE_DUPLICATE_FIELDS =
                "Error: Duplicate parameter detected \u2014 multiple values for '%s' provided.";

    public static final String MESSAGE_REJECT_INVALID_INDEX =
            "Error: Invalid index. Please provide a valid positive integer. Usage: addreject INDEX REASON";
    public static final String MESSAGE_REJECT_INVALID_FORMAT =
            "Error: Invalid command format. Usage: addreject INDEX REASON";

    /**
     * Returns an error message indicating the duplicate prefixes.
     */
    public static String getErrorMessageForDuplicatePrefixes(Prefix... duplicatePrefixes) {
        assert duplicatePrefixes.length > 0;

        String duplicateField = duplicatePrefixes[0].toString();

        return String.format(MESSAGE_DUPLICATE_FIELDS, duplicateField);
    }

    /**
     * Formats the {@code person} for display to the user.
     */
    public static String format(Person person) {
        final StringBuilder builder = new StringBuilder();
        builder.append(person.getName())
                .append(" | Phone: ")
                .append(person.getPhone())
                .append(" | Email: ")
                .append(person.getEmail());
        builder.append(" | Address: ").append(person.getAddress().value);
        if (!person.getTags().isEmpty()) {
            builder.append(" | Tags: ");
            person.getTags().forEach(builder::append);
        }
        return builder.toString();
    }

}
