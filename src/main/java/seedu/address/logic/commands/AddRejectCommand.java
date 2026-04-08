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
 * Adds a rejection reason to a candidate identified by index.
 */
public class AddRejectCommand extends Command {

    public static final String COMMAND_WORD = "addreject";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Records a rejection reason for the candidate identified by the index number "
            + "in the displayed candidate list.\n"
            + "Parameters: INDEX (must be a positive integer) REASON\n"
            + "Example: " + COMMAND_WORD + " 1 Failed technical interview";

    public static final int MAX_REJECTIONS_PER_CANDIDATE = 20;
    public static final String MESSAGE_REJECTIONS_LIMIT_REACHED =
            "Cannot add rejection: candidate already has the maximum of " + MAX_REJECTIONS_PER_CANDIDATE
            + " rejection records. Delete an old entry with `deletereject` before adding a new one.";

    public static final String MESSAGE_SUCCESS =
            "Rejection reason recorded. New Reason added: %1$s (Total rejections on record: %2$d)";
    public static final String MESSAGE_SUCCESS_DUPLICATE_WARNING =
            "Rejection reason recorded. New Reason added: %1$s (Total rejections on record: %2$d)\n"
            + "Note: This reason is the same as the previous rejection reason.";

    private static final Logger logger = LogsCenter.getLogger(AddRejectCommand.class);

    private final Index targetIndex;
    private final RejectionReason reason;

    /**
     * Creates an AddRejectCommand to add a rejection reason to the person at the specified {@code targetIndex}.
     */
    public AddRejectCommand(Index targetIndex, RejectionReason reason) {
        requireNonNull(targetIndex);
        requireNonNull(reason);
        this.targetIndex = targetIndex;
        this.reason = reason;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(String.format(Messages.MESSAGE_INDEX_OUT_OF_RANGE,
                    targetIndex.getOneBased(), lastShownList.size()));
        }

        Person personToReject = lastShownList.get(targetIndex.getZeroBased());

        if (personToReject.getRejectionReasons().size() >= MAX_REJECTIONS_PER_CANDIDATE) {
            throw new CommandException(MESSAGE_REJECTIONS_LIMIT_REACHED);
        }

        boolean isDuplicateReason = isSequentialDuplicate(personToReject, reason);
        Person rejectedPerson = createRejectedPerson(personToReject, reason);

        model.setPerson(personToReject, rejectedPerson);
        logger.info("Rejected candidate: " + personToReject.getName() + " with reason: " + reason);

        int totalReasons = rejectedPerson.getRejectionReasons().size();
        String resultMessage = isDuplicateReason
                ? String.format(MESSAGE_SUCCESS_DUPLICATE_WARNING, reason, totalReasons)
                : String.format(MESSAGE_SUCCESS, reason, totalReasons);

        return new CommandResult(resultMessage);
    }

    /**
     * Returns true if the given reason matches the last rejection reason of the person.
     */
    private static boolean isSequentialDuplicate(Person person, RejectionReason newReason) {
        List<RejectionReason> existingReasons = person.getRejectionReasons();
        if (existingReasons.isEmpty()) {
            return false;
        }
        RejectionReason lastReason = existingReasons.get(existingReasons.size() - 1);
        return lastReason.reason.equalsIgnoreCase(newReason.reason);
    }

    /**
     * Creates and returns a {@code Person} with the new rejection reason appended.
     */
    private static Person createRejectedPerson(Person personToReject, RejectionReason reason) {
        assert personToReject != null;
        assert reason != null;

        List<RejectionReason> updatedReasons = new ArrayList<>(personToReject.getRejectionReasons());
        updatedReasons.add(reason);

        return new Person(
                personToReject.getName(),
                personToReject.getPhone(),
                personToReject.getEmail(),
                personToReject.getAddress(),
                personToReject.getTags(),
                updatedReasons,
                personToReject.getDateAdded(),
                personToReject.getPriority(),
                personToReject.getNotes()
        );
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof AddRejectCommand)) {
            return false;
        }
        AddRejectCommand otherCmd = (AddRejectCommand) other;
        return targetIndex.equals(otherCmd.targetIndex)
                && reason.equals(otherCmd.reason);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .add("reason", reason)
                .toString();
    }
}
