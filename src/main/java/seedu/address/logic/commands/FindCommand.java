package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.Model;
import seedu.address.model.person.NamePhoneEmailContainsKeywordsPredicate;

/**
 * Finds and lists all persons in address book whose name, phone, email, note headings/contents,
 * or rejection reasons contain any of the argument keywords.
 * Keyword matching is case insensitive.
 */
public class FindCommand extends Command {

    public static final String COMMAND_WORD = "find";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all persons whose names, phones, emails, "
            + "notes, or rejection reasons contain any of the specified keywords (case-insensitive) "
            + "and displays them as a list with index numbers.\n"
            + "Parameters: KEYWORD [MORE_KEYWORDS]...\n"
            + "Example: " + COMMAND_WORD + " alice 9876 john@example.com";

    private static final Logger logger = LogsCenter.getLogger(FindCommand.class);

    private final NamePhoneEmailContainsKeywordsPredicate predicate;

    /**
     * Creates a FindCommand to find persons matching the given {@code predicate}.
     */
    public FindCommand(NamePhoneEmailContainsKeywordsPredicate predicate) {
        requireNonNull(predicate);
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(predicate);
        int listSize = model.getFilteredPersonList().size();
        logger.info("Find command executed: " + listSize + " candidate(s) found");
        if (listSize == 0) {
            return new CommandResult("No matching candidates found.");
        } else {
            return new CommandResult(listSize + (listSize == 1 ? " candidate" : " candidates") + " listed.");
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof FindCommand)) {
            return false;
        }

        FindCommand otherFindCommand = (FindCommand) other;
        return predicate.equals(otherFindCommand.predicate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .toString();
    }
}
