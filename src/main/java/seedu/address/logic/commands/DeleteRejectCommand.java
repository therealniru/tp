package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.RejectionReason;

/**
 * Deletes a rejection reason from a candidate in the address book.
 */
public class DeleteRejectCommand extends Command {

    public static final String COMMAND_WORD = "deletereject";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes a rejection reason from the candidate identified by the index number.\n"
            + "Parameters: INDEX REJECT_INDEX\n"
            + "Example: " + COMMAND_WORD + " 1 2";

    public static final String MESSAGE_SUCCESS =
            "Successfully deleted rejection reason from candidate: %1$s";

    public static final String MESSAGE_NO_REJECTIONS =
            "Error: Candidate %1$s has no rejection reasons.";

    public static final String MESSAGE_INVALID_REJECT_INDEX =
            "Error: Rejection index %1$d is out of range. Candidate %2$s has %3$d rejection(s). "
            + "Please provide a rejection index between 1 and %3$d.";

    private static final Logger logger = LogsCenter.getLogger(DeleteRejectCommand.class);

    private final Index targetIndex;
    private final Index rejectIndex;

    /**
     * Creates a DeleteRejectCommand to delete the rejection at {@code rejectIndex}
     * from the person at {@code targetIndex}.
     */
    public DeleteRejectCommand(Index targetIndex, Index rejectIndex) {
        requireNonNull(targetIndex);
        requireNonNull(rejectIndex);
        this.targetIndex = targetIndex;
        this.rejectIndex = rejectIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (lastShownList.isEmpty()) {
            throw new CommandException(Messages.MESSAGE_EMPTY_LIST);
        }

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(String.format(Messages.MESSAGE_INDEX_OUT_OF_RANGE,
                    targetIndex.getOneBased(), lastShownList.size(), lastShownList.size()));
        }

        Person personToEdit = lastShownList.get(targetIndex.getZeroBased());
        List<RejectionReason> currentReasons = personToEdit.getRejectionReasons();

        if (currentReasons.isEmpty()) {
            throw new CommandException(String.format(MESSAGE_NO_REJECTIONS, personToEdit.getName()));
        }

        if (rejectIndex.getZeroBased() >= currentReasons.size()) {
            throw new CommandException(String.format(MESSAGE_INVALID_REJECT_INDEX,
                    rejectIndex.getOneBased(), personToEdit.getName(), currentReasons.size()));
        }

        List<RejectionReason> updatedReasons = new ArrayList<>(currentReasons);
        updatedReasons.remove(rejectIndex.getZeroBased());

        Person editedPerson = new Person(
                personToEdit.getName(),
                personToEdit.getPhone(),
                personToEdit.getEmail(),
                personToEdit.getAddress(),
                personToEdit.getTags(),
                updatedReasons,
                personToEdit.getDateAdded(),
                personToEdit.getPriority(),
                personToEdit.getNotes()
        );

        model.setPerson(personToEdit, editedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        model.sortFilteredPersonList(ListCommand.DEFAULT_SORT);
        logger.info("Deleted rejection " + rejectIndex.getOneBased()
                + " from candidate: " + personToEdit.getName());
        return new CommandResult(String.format(MESSAGE_SUCCESS, personToEdit.getName()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof DeleteRejectCommand)) {
            return false;
        }
        DeleteRejectCommand otherCmd = (DeleteRejectCommand) other;
        return targetIndex.equals(otherCmd.targetIndex)
                && rejectIndex.equals(otherCmd.rejectIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .add("rejectIndex", rejectIndex)
                .toString();
    }
}
