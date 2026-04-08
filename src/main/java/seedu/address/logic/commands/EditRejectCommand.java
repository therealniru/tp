package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

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
 * Edits an existing rejection reason for a candidate in the address book.
 */
public class EditRejectCommand extends Command {

    public static final String COMMAND_WORD = "editreject";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Edits a rejection reason for the candidate identified by the index number.\n"
            + "Parameters: INDEX REJECT_INDEX NEW_REASON\n"
            + "Example: " + COMMAND_WORD + " 1 2 Failed cultural fit interview";

    public static final String MESSAGE_SUCCESS =
            "Successfully edited rejection reason for candidate: %1$s";

    public static final String MESSAGE_NO_REJECTIONS =
            "Error: Candidate %1$s has no rejection reasons.";

    public static final String MESSAGE_INVALID_REJECT_INDEX =
            "Error: Rejection index %1$d is out of range. Candidate %2$s has %3$d rejection(s). "
            + "Please provide a rejection index between 1 and %3$d.";

    private static final Logger logger = LogsCenter.getLogger(EditRejectCommand.class);

    private final Index targetIndex;
    private final Index rejectIndex;
    private final String newReason;

    /**
     * Creates an EditRejectCommand to edit the rejection at {@code rejectIndex}
     * for the person at {@code targetIndex}.
     */
    public EditRejectCommand(Index targetIndex, Index rejectIndex, String newReason) {
        requireNonNull(targetIndex);
        requireNonNull(rejectIndex);
        requireNonNull(newReason);
        this.targetIndex = targetIndex;
        this.rejectIndex = rejectIndex;
        this.newReason = newReason;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(String.format(Messages.MESSAGE_INDEX_OUT_OF_RANGE,
                    targetIndex.getOneBased(), lastShownList.size()));
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

        RejectionReason originalReason = currentReasons.get(rejectIndex.getZeroBased());
        if (originalReason.reason.equals(newReason)) {
            return new CommandResult("Note: No changes detected; rejection reason remains the same.");
        }

        List<RejectionReason> updatedReasons = new ArrayList<>(currentReasons);
        updatedReasons.set(rejectIndex.getZeroBased(), new RejectionReason(newReason));

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
        logger.info("Edited rejection " + rejectIndex.getOneBased()
                + " for candidate: " + personToEdit.getName());
        return new CommandResult(String.format(MESSAGE_SUCCESS, personToEdit.getName()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof EditRejectCommand)) {
            return false;
        }
        EditRejectCommand otherCmd = (EditRejectCommand) other;
        return targetIndex.equals(otherCmd.targetIndex)
                && rejectIndex.equals(otherCmd.rejectIndex)
                && newReason.equals(otherCmd.newReason);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .add("rejectIndex", rejectIndex)
                .add("newReason", newReason)
                .toString();
    }
}
