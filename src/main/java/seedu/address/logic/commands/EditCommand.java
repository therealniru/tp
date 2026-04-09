package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PRIORITY;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Priority;

/**
 * Edits the details of an existing person in the address book.
 */
public class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the person identified "
            + "by the index number used in the displayed person list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_PHONE + "PHONE] "
            + "[" + PREFIX_EMAIL + "EMAIL] "
            + "[" + PREFIX_ADDRESS + "ADDRESS] "
            + "[" + PREFIX_PRIORITY + "PRIORITY]\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_PHONE + "91234567 "
            + PREFIX_EMAIL + "johndoe@example.com "
            + PREFIX_ADDRESS + "Clementi Ave 2 "
            + PREFIX_PRIORITY + "yes";

    public static final String MESSAGE_EDIT_PERSON_SUCCESS = "Edited Candidate: %1$s";
    public static final String MESSAGE_NOT_EDITED = "Error: At least one field to edit must be "
            + "provided (n/, p/, e/, a/ or pr/).";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the address book.";

    private static final Logger logger = LogsCenter.getLogger(EditCommand.class);

    private final Index index;
    private final EditPersonDescriptor editPersonDescriptor;

    /**
     * @param index of the person in the filtered person list to edit
     * @param editPersonDescriptor details to edit the person with
     */
    public EditCommand(Index index, EditPersonDescriptor editPersonDescriptor) {
        requireNonNull(index);
        requireNonNull(editPersonDescriptor);

        this.index = index;
        this.editPersonDescriptor = new EditPersonDescriptor(editPersonDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (lastShownList.isEmpty()) {
            throw new CommandException(Messages.MESSAGE_EMPTY_LIST);
        }

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(String.format(Messages.MESSAGE_INDEX_OUT_OF_RANGE,
                    index.getOneBased(), lastShownList.size(), lastShownList.size()));
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());

        Person editedPerson = createEditedPerson(personToEdit, editPersonDescriptor);

        if (hasIdenticalFields(personToEdit, editedPerson)) {
            return new CommandResult("Note: No changes detected; candidate details remain the same.");
        }

        // Duplicate exclusion validation: check if any OTHER person conflicts with the edited identity
        for (Person p : model.getAddressBook().getPersonList()) {
            if (!p.equals(personToEdit) && p.isSamePerson(editedPerson)) {
                boolean phoneMatch = p.getPhone().equals(editedPerson.getPhone());
                boolean emailMatch = p.getEmail().equals(editedPerson.getEmail());
                String field;
                if (phoneMatch && emailMatch) {
                    field = String.format("Phone %s and Email %s are",
                            editedPerson.getPhone().value, editedPerson.getEmail().value);
                } else if (phoneMatch) {
                    field = String.format("Phone %s is", editedPerson.getPhone().value);
                } else {
                    field = String.format("Email %s is", editedPerson.getEmail().value);
                }
                throw new CommandException(String.format(
                        "Error: This edit would duplicate an existing candidate. "
                        + "%s already assigned to %s.", field, p.getName().fullName));
            }
        }

        model.setPerson(personToEdit, editedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        model.sortFilteredPersonList(ListCommand.DEFAULT_SORT);
        logger.info("Edited candidate: " + editedPerson.getName());
        return new CommandResult(String.format(MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson)));
    }

    /**
     * Returns true if both persons have identical field values, including case-sensitive string comparison.
     * This is stricter than {@code Person.equals()} which uses case-insensitive comparison for names,
     * ensuring that case-only changes (e.g., "alice" → "Alice") are detected as actual edits.
     */
    private static boolean hasIdenticalFields(Person a, Person b) {
        return a.getName().fullName.equals(b.getName().fullName)
                && a.getPhone().value.equals(b.getPhone().value)
                && a.getEmail().value.equals(b.getEmail().value)
                && a.getAddress().value.equals(b.getAddress().value)
                && a.getTags().equals(b.getTags())
                && a.getRejectionReasons().equals(b.getRejectionReasons())
                && a.getDateAdded().equals(b.getDateAdded())
                && a.getPriority().equals(b.getPriority())
                && a.getNotes().equals(b.getNotes());
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code personToEdit}
     * edited with {@code editPersonDescriptor}.
     */
    private static Person createEditedPerson(Person personToEdit, EditPersonDescriptor editPersonDescriptor) {
        assert personToEdit != null;
        assert editPersonDescriptor != null;

        Name updatedName = editPersonDescriptor.getName().orElse(personToEdit.getName());
        Phone updatedPhone = editPersonDescriptor.getPhone().orElse(personToEdit.getPhone());
        Email updatedEmail = editPersonDescriptor.getEmail().orElse(personToEdit.getEmail());
        Address updatedAddress = editPersonDescriptor.getAddress().orElse(personToEdit.getAddress());
        Priority updatedPriority = editPersonDescriptor.getPriority().orElse(personToEdit.getPriority());

        return new Person(updatedName, updatedPhone, updatedEmail, updatedAddress,
                personToEdit.getTags(), personToEdit.getRejectionReasons(),
                personToEdit.getDateAdded(), updatedPriority, personToEdit.getNotes());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditCommand)) {
            return false;
        }

        EditCommand otherEditCommand = (EditCommand) other;
        return index.equals(otherEditCommand.index)
                && editPersonDescriptor.equals(otherEditCommand.editPersonDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("editPersonDescriptor", editPersonDescriptor)
                .toString();
    }

    /**
     * Stores the details to edit the person with. Each non-empty field value will replace the
     * corresponding field value of the person.
     */
    public static class EditPersonDescriptor {
        private Name name;
        private Phone phone;
        private Email email;
        private Address address;
        private Priority priority;

        public EditPersonDescriptor() {}

        /**
         * Copy constructor.
         */
        public EditPersonDescriptor(EditPersonDescriptor toCopy) {
            setName(toCopy.name);
            setPhone(toCopy.phone);
            setEmail(toCopy.email);
            setAddress(toCopy.address);
            setPriority(toCopy.priority);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(name, phone, email, address, priority);
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public void setPhone(Phone phone) {
            this.phone = phone;
        }

        public Optional<Phone> getPhone() {
            return Optional.ofNullable(phone);
        }

        public void setEmail(Email email) {
            this.email = email;
        }

        public Optional<Email> getEmail() {
            return Optional.ofNullable(email);
        }

        public void setAddress(Address address) {
            this.address = address;
        }

        public Optional<Address> getAddress() {
            return Optional.ofNullable(address);
        }

        public void setPriority(Priority priority) {
            this.priority = priority;
        }

        public Optional<Priority> getPriority() {
            return Optional.ofNullable(priority);
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditPersonDescriptor)) {
                return false;
            }

            EditPersonDescriptor otherEditPersonDescriptor = (EditPersonDescriptor) other;
            return Objects.equals(name, otherEditPersonDescriptor.name)
                    && Objects.equals(phone, otherEditPersonDescriptor.phone)
                    && Objects.equals(email, otherEditPersonDescriptor.email)
                    && Objects.equals(address, otherEditPersonDescriptor.address)
                    && Objects.equals(priority, otherEditPersonDescriptor.priority);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .add("name", name)
                    .add("phone", phone)
                    .add("email", email)
                    .add("address", address)
                    .add("priority", priority)
                    .toString();
        }
    }
}
