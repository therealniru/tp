package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REASON;

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
import seedu.address.model.person.Status;
import seedu.address.model.tag.Tag;

/**
 * Rejects a candidate identified by index and records a rejection reason.
 */
public class RejectCommand extends Command {

    public static final String COMMAND_WORD = "reject";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Rejects the candidate identified by the index number used in the displayed candidate list "
            + "and records a rejection reason.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_REASON + "REASON\n"
            + "Example: " + COMMAND_WORD + " 1 " + PREFIX_REASON + "Failed technical interview";

    public static final String MESSAGE_REJECT_PERSON_SUCCESS =
            "Candidate marked as REJECTED. New Reason added: %1$s (Total rejections on record: %2$d)";
    public static final String MESSAGE_REJECT_PERSON_SUCCESS_DUPLICATE_WARNING =
            "Candidate marked as REJECTED. New Reason added: %1$s (Total rejections on record: %2$d)\n"
            + "Note: This reason is the same as the previous rejection reason.";
    public static final String MESSAGE_BLACKLISTED_PERSON =
            "Error: Cannot reject a blacklisted candidate.";
    public static final String MESSAGE_HIRED_PERSON =
            "Error: Cannot reject a hired candidate. Edit their status first if needed.";
    public static final String MESSAGE_CONFIRM_HIRED_TAG =
            "Warning: This candidate has the 'hired' tag. Confirm rejection? (y/n)";

    private static final Logger logger = LogsCenter.getLogger(RejectCommand.class);

    private final Index targetIndex;
    private final RejectionReason reason;

    /**
     * Creates a RejectCommand to reject the person at the specified {@code targetIndex}
     * with the given {@code reason}.
     */
    public RejectCommand(Index targetIndex, RejectionReason reason) {
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
            throw new CommandException(String.format(Messages.MESSAGE_REJECT_INDEX_OUT_OF_RANGE,
                    targetIndex.getOneBased(), lastShownList.size()));
        }

        Person personToReject = lastShownList.get(targetIndex.getZeroBased());

        if (personToReject.isBlacklisted()) {
            throw new CommandException(MESSAGE_BLACKLISTED_PERSON);
        }

        if (personToReject.getStatus() == Status.HIRED) {
            throw new CommandException(MESSAGE_HIRED_PERSON);
        }

        if (personToReject.getTags().contains(new Tag("hired"))) {
            Person capturedPerson = personToReject;
            CommandResult.ConfirmedAction action = () -> executeRejection(model, capturedPerson);
            return new CommandResult(MESSAGE_CONFIRM_HIRED_TAG, action);
        }

        return executeRejection(model, personToReject);
    }

    /**
     * Performs the rejection of {@code personToReject} and returns the success {@code CommandResult}.
     */
    private CommandResult executeRejection(Model model, Person personToReject) throws CommandException {
        boolean isDuplicateReason = isSequentialDuplicate(personToReject, reason);
        Person rejectedPerson = createRejectedPerson(personToReject, reason);

        model.setPerson(personToReject, rejectedPerson);
        logger.info("Rejected candidate: " + personToReject.getName() + " with reason: " + reason);

        int totalReasons = rejectedPerson.getRejectionReasons().size();
        String resultMessage = isDuplicateReason
                ? String.format(MESSAGE_REJECT_PERSON_SUCCESS_DUPLICATE_WARNING, reason, totalReasons)
                : String.format(MESSAGE_REJECT_PERSON_SUCCESS, reason, totalReasons);

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
     * Creates and returns a {@code Person} with status set to REJECTED
     * and the new rejection reason appended.
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
                Status.REJECTED,
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

        if (!(other instanceof RejectCommand)) {
            return false;
        }

        RejectCommand otherRejectCommand = (RejectCommand) other;
        return targetIndex.equals(otherRejectCommand.targetIndex)
                && reason.equals(otherRejectCommand.reason);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .add("reason", reason)
                .toString();
    }
}
