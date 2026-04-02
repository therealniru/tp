package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Objects;
import java.util.Optional;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.Person;

/**
 * Represents the result of a command execution.
 */
public class CommandResult {

    /**
     * An action to execute after the user confirms a dangerous operation.
     */
    @FunctionalInterface
    public interface ConfirmedAction {
        CommandResult execute() throws CommandException;
    }

    private final String feedbackToUser;

    /** Help information should be shown to the user. */
    private final boolean showHelp;

    /** The application should exit. */
    private final boolean exit;

    /** The candidate to display in the detail panel. Empty if no candidate to show. */
    private final Optional<Person> selectedPerson;

    /** Whether the command requires explicit user confirmation before proceeding. */
    private final boolean requiresConfirmation;

    /** The action to run if the user confirms. Empty when requiresConfirmation is false. */
    private final Optional<ConfirmedAction> confirmedAction;

    /**
     * Constructs a {@code CommandResult} with the specified fields.
     */
    public CommandResult(String feedbackToUser, boolean showHelp, boolean exit) {
        this.feedbackToUser = requireNonNull(feedbackToUser);
        this.showHelp = showHelp;
        this.exit = exit;
        this.selectedPerson = Optional.empty();
        this.requiresConfirmation = false;
        this.confirmedAction = Optional.empty();
    }

    /**
     * Constructs a {@code CommandResult} with the specified {@code feedbackToUser},
     * and other fields set to their default value.
     */
    public CommandResult(String feedbackToUser) {
        this(feedbackToUser, false, false);
    }

    /**
     * Constructs a {@code CommandResult} that triggers the candidate detail panel.
     */
    public CommandResult(String feedbackToUser, Person selectedPerson) {
        this.feedbackToUser = requireNonNull(feedbackToUser);
        this.showHelp = false;
        this.exit = false;
        this.selectedPerson = Optional.of(selectedPerson);
        this.requiresConfirmation = false;
        this.confirmedAction = Optional.empty();
    }

    /**
     * Constructs a {@code CommandResult} that requires explicit user confirmation
     * before executing {@code confirmedAction}.
     */
    public CommandResult(String feedbackToUser, ConfirmedAction confirmedAction) {
        this.feedbackToUser = requireNonNull(feedbackToUser);
        this.showHelp = false;
        this.exit = false;
        this.selectedPerson = Optional.empty();
        this.requiresConfirmation = true;
        this.confirmedAction = Optional.of(confirmedAction);
    }

    public String getFeedbackToUser() {
        return feedbackToUser;
    }

    public boolean isShowHelp() {
        return showHelp;
    }

    public boolean isExit() {
        return exit;
    }

    public boolean isShowPerson() {
        return selectedPerson.isPresent();
    }

    public Optional<Person> getSelectedPerson() {
        return selectedPerson;
    }

    public boolean isRequiresConfirmation() {
        return requiresConfirmation;
    }

    public Optional<ConfirmedAction> getConfirmedAction() {
        return confirmedAction;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof CommandResult)) {
            return false;
        }

        CommandResult otherCommandResult = (CommandResult) other;
        return feedbackToUser.equals(otherCommandResult.feedbackToUser)
                && showHelp == otherCommandResult.showHelp
                && exit == otherCommandResult.exit
                && selectedPerson.equals(otherCommandResult.selectedPerson)
                && requiresConfirmation == otherCommandResult.requiresConfirmation;
    }

    @Override
    public int hashCode() {
        return Objects.hash(feedbackToUser, showHelp, exit, selectedPerson, requiresConfirmation);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("feedbackToUser", feedbackToUser)
                .add("showHelp", showHelp)
                .add("exit", exit)
                .toString();
    }

}
