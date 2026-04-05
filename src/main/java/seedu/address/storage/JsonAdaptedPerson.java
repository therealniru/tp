package seedu.address.storage;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.AddressBook;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Note;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.RejectionReason;
import seedu.address.model.tag.Tag;

/**
 * Jackson-friendly version of {@link Person}.
 * Unknown JSON properties (e.g. legacy "status" field) are silently ignored for backwards compatibility.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
class JsonAdaptedPerson {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Person's %s field is missing!";

    private static final Logger logger = LogsCenter.getLogger(JsonAdaptedPerson.class);

    private final String name;
    private final String phone;
    private final String email;
    private final String address;
    private final List<JsonAdaptedTag> tags = new ArrayList<>();
    private final List<JsonAdaptedRejectionReason> rejectionReasons = new ArrayList<>();
    private final String dateAdded;
    private final String priority;
    private final List<JsonAdaptedNote> notes = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedPerson} with the given person details.
     */
    @JsonCreator
    public JsonAdaptedPerson(@JsonProperty("name") String name, @JsonProperty("phone") String phone,
            @JsonProperty("email") String email, @JsonProperty("address") String address,
            @JsonProperty("tags") List<JsonAdaptedTag> tags,
            @JsonProperty("rejectionReasons") List<JsonAdaptedRejectionReason> rejectionReasons,
            @JsonProperty("dateAdded") String dateAdded,
            @JsonProperty("priority") String priority,
            @JsonProperty("notes") List<JsonAdaptedNote> notes) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        if (tags != null) {
            this.tags.addAll(tags);
        }
        if (rejectionReasons != null) {
            this.rejectionReasons.addAll(rejectionReasons);
        }
        this.dateAdded = dateAdded;
        this.priority = priority;
        if (notes != null) {
            this.notes.addAll(notes);
        }
    }

    /**
     * Converts a given {@code Person} into this class for Jackson use.
     */
    public JsonAdaptedPerson(Person source) {
        name = source.getName().fullName;
        phone = source.getPhone().value;
        email = source.getEmail().value;
        address = source.getAddress().value;
        tags.addAll(source.getTags().stream()
                .map(JsonAdaptedTag::new)
                .collect(Collectors.toList()));
        rejectionReasons.addAll(source.getRejectionReasons().stream()
                .map(JsonAdaptedRejectionReason::new)
                .collect(Collectors.toList()));
        dateAdded = source.getDateAdded().value;
        priority = source.getPriority().value;
        notes.addAll(source.getNotes().stream()
                .map(JsonAdaptedNote::new)
                .collect(Collectors.toList()));
    }

    /**
     * Converts this Jackson-friendly adapted person object into the model's {@code Person} object.
     *
     * @param masterAddressBook the master address book to verify tags against
     * @throws IllegalValueException if there were any data constraints violated in the adapted person.
     */
    public Person toModelType(AddressBook masterAddressBook) throws IllegalValueException {
        final List<Tag> personTags = new ArrayList<>();
        for (JsonAdaptedTag tag : tags) {
            Tag parsedTag = tag.toModelType();
            if (!masterAddressBook.hasTag(parsedTag)) {
                throw new IllegalValueException("Candidate contains a tag that does not exist in the master tag list.");
            }
            Tag canonicalTag = masterAddressBook.getTagList().stream()
                    .filter(t -> t.equals(parsedTag))
                    .findFirst()
                    .get();
            personTags.add(canonicalTag);
        }

        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        final Name modelName = new Name(name);

        if (phone == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName()));
        }
        if (!Phone.isValidPhone(phone)) {
            throw new IllegalValueException(Phone.MESSAGE_CONSTRAINTS);
        }
        final Phone modelPhone = new Phone(phone);

        if (email == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName()));
        }
        if (!Email.isValidEmail(email)) {
            throw new IllegalValueException(Email.MESSAGE_CONSTRAINTS);
        }
        final Email modelEmail = new Email(email);

        if (address == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName()));
        }
        if (!Address.isValidAddress(address)) {
            throw new IllegalValueException(Address.MESSAGE_CONSTRAINTS);
        }
        final Address modelAddress = new Address(address);

        final Set<Tag> modelTags = new HashSet<>(personTags);

        final List<RejectionReason> modelRejectionReasons = new ArrayList<>();
        for (JsonAdaptedRejectionReason reason : rejectionReasons) {
            modelRejectionReasons.add(reason.toModelType());
        }

        final seedu.address.model.person.DateAdded modelDateAdded;
        if (dateAdded == null) {
            modelDateAdded = new seedu.address.model.person.DateAdded();
        } else if (!seedu.address.model.person.DateAdded.isValidDateAdded(dateAdded)) {
            throw new IllegalValueException(seedu.address.model.person.DateAdded.MESSAGE_CONSTRAINTS);
        } else {
            ZonedDateTime parsedDateAdded = ZonedDateTime.parse(dateAdded,
                    seedu.address.model.person.DateAdded.FORMATTER);
            if (parsedDateAdded.isAfter(ZonedDateTime.now(ZoneId.systemDefault()))) {
                logger.warning("Candidate dateAdded is in the future (" + parsedDateAdded
                        + "). Clamping to current time.");
                modelDateAdded = new seedu.address.model.person.DateAdded();
            } else {
                modelDateAdded = new seedu.address.model.person.DateAdded(dateAdded);
            }
        }

        final seedu.address.model.person.Priority modelPriority;
        if (priority == null) {
            modelPriority = new seedu.address.model.person.Priority("no");
        } else if (!seedu.address.model.person.Priority.isValidPriority(priority)) {
            throw new IllegalValueException(seedu.address.model.person.Priority.MESSAGE_CONSTRAINTS);
        } else {
            modelPriority = new seedu.address.model.person.Priority(priority);
        }

        final List<Note> modelNotes = new ArrayList<>();
        for (JsonAdaptedNote note : notes) {
            modelNotes.add(note.toModelType());
        }

        return new Person(modelName, modelPhone, modelEmail, modelAddress, modelTags,
                modelRejectionReasons, modelDateAdded, modelPriority, modelNotes);
    }

}
