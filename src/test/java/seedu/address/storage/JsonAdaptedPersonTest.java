package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.storage.JsonAdaptedPerson.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.BENSON;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.AddressBook;
import seedu.address.model.person.Note;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;

public class JsonAdaptedPersonTest {
    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_PHONE = "911a";
    private static final String INVALID_ADDRESS = " ";
    private static final String INVALID_EMAIL = "example.com";
    private static final String INVALID_TAG = "#friend";

    private static final String VALID_NAME = BENSON.getName().toString();
    private static final String VALID_PHONE = BENSON.getPhone().toString();
    private static final String VALID_EMAIL = BENSON.getEmail().toString();
    private static final String VALID_ADDRESS = BENSON.getAddress().toString();
    private static final List<JsonAdaptedTag> VALID_TAGS = BENSON.getTags().stream()
            .map(JsonAdaptedTag::new)
            .collect(Collectors.toList());
    private static final String VALID_STATUS = BENSON.getStatus().name();
    private static final List<JsonAdaptedRejectionReason> VALID_REJECTION_REASONS =
            BENSON.getRejectionReasons().stream()
                    .map(JsonAdaptedRejectionReason::new)
                    .collect(Collectors.toList());
    private static final String VALID_DATE_ADDED = BENSON.getDateAdded().value;
    private static final String VALID_PRIORITY = BENSON.getPriority().value;

    @Test
    public void toModelType_validPersonDetails_returnsPerson() throws Exception {
        JsonAdaptedPerson person = new JsonAdaptedPerson(BENSON);
        AddressBook ab = new AddressBook();
        BENSON.getTags().forEach(ab::addTag);
        assertEquals(BENSON, person.toModelType(ab));
    }

    @Test
    public void toModelType_invalidName_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(INVALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS, VALID_TAGS,
                        VALID_STATUS, VALID_REJECTION_REASONS, VALID_DATE_ADDED, VALID_PRIORITY, null);
        String expectedMessage = Name.MESSAGE_CONSTRAINTS;
        AddressBook ab = new AddressBook();
        BENSON.getTags().forEach(ab::addTag);
        assertThrows(IllegalValueException.class, expectedMessage, () -> person.toModelType(ab));
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(null, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS, VALID_TAGS,
                        VALID_STATUS, VALID_REJECTION_REASONS, VALID_DATE_ADDED, VALID_PRIORITY, null);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName());
        AddressBook ab = new AddressBook();
        BENSON.getTags().forEach(ab::addTag);
        assertThrows(IllegalValueException.class, expectedMessage, () -> person.toModelType(ab));
    }

    @Test
    public void toModelType_invalidPhone_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, INVALID_PHONE, VALID_EMAIL, VALID_ADDRESS, VALID_TAGS,
                        VALID_STATUS, VALID_REJECTION_REASONS, VALID_DATE_ADDED, VALID_PRIORITY, null);
        String expectedMessage = Phone.MESSAGE_CONSTRAINTS;
        AddressBook ab = new AddressBook();
        BENSON.getTags().forEach(ab::addTag);
        assertThrows(IllegalValueException.class, expectedMessage, () -> person.toModelType(ab));
    }

    @Test
    public void toModelType_nullPhone_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_NAME, null, VALID_EMAIL, VALID_ADDRESS, VALID_TAGS,
                        VALID_STATUS, VALID_REJECTION_REASONS, VALID_DATE_ADDED, VALID_PRIORITY, null);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName());
        AddressBook ab = new AddressBook();
        BENSON.getTags().forEach(ab::addTag);
        assertThrows(IllegalValueException.class, expectedMessage, () -> person.toModelType(ab));
    }

    @Test
    public void toModelType_invalidEmail_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, INVALID_EMAIL, VALID_ADDRESS, VALID_TAGS,
                        VALID_STATUS, VALID_REJECTION_REASONS, VALID_DATE_ADDED, VALID_PRIORITY, null);
        String expectedMessage = Email.MESSAGE_CONSTRAINTS;
        AddressBook ab = new AddressBook();
        BENSON.getTags().forEach(ab::addTag);
        assertThrows(IllegalValueException.class, expectedMessage, () -> person.toModelType(ab));
    }

    @Test
    public void toModelType_nullEmail_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, null, VALID_ADDRESS, VALID_TAGS,
                        VALID_STATUS, VALID_REJECTION_REASONS, VALID_DATE_ADDED, VALID_PRIORITY, null);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName());
        AddressBook ab = new AddressBook();
        BENSON.getTags().forEach(ab::addTag);
        assertThrows(IllegalValueException.class, expectedMessage, () -> person.toModelType(ab));
    }

    @Test
    public void toModelType_invalidAddress_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, INVALID_ADDRESS, VALID_TAGS,
                        VALID_STATUS, VALID_REJECTION_REASONS, VALID_DATE_ADDED, VALID_PRIORITY, null);
        String expectedMessage = Address.MESSAGE_CONSTRAINTS;
        AddressBook ab = new AddressBook();
        BENSON.getTags().forEach(ab::addTag);
        assertThrows(IllegalValueException.class, expectedMessage, () -> person.toModelType(ab));
    }

    @Test
    public void toModelType_nullAddress_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, null, VALID_TAGS,
                        VALID_STATUS, VALID_REJECTION_REASONS, VALID_DATE_ADDED, VALID_PRIORITY, null);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName());
        AddressBook ab = new AddressBook();
        BENSON.getTags().forEach(ab::addTag);
        assertThrows(IllegalValueException.class, expectedMessage, () -> person.toModelType(ab));
    }

    @Test
    public void toModelType_personWithNotes_roundTripPreservesNotes() throws Exception {
        LocalDateTime fixedDate = LocalDateTime.of(2026, 3, 23, 14, 30, 0);
        Note note = new Note("Tech Round", "Passed interview", fixedDate);
        Person personWithNote = new PersonBuilder(BENSON).withNotes(List.of(note)).build();

        JsonAdaptedPerson adapted = new JsonAdaptedPerson(personWithNote);
        AddressBook ab = new AddressBook();
        BENSON.getTags().forEach(ab::addTag);

        Person restored = adapted.toModelType(ab);
        assertEquals(1, restored.getNotes().size());
        assertEquals(note, restored.getNotes().get(0));
    }

    @Test
    public void toModelType_personWithMultipleNotes_preservesOrder() throws Exception {
        LocalDateTime date1 = LocalDateTime.of(2026, 1, 1, 9, 0, 0);
        LocalDateTime date2 = LocalDateTime.of(2026, 2, 1, 10, 0, 0);
        Note note1 = new Note("First", "content one", date1);
        Note note2 = new Note("Second", "content two", date2);
        Person personWithNotes = new PersonBuilder(BENSON).withNotes(List.of(note1, note2)).build();

        JsonAdaptedPerson adapted = new JsonAdaptedPerson(personWithNotes);
        AddressBook ab = new AddressBook();
        BENSON.getTags().forEach(ab::addTag);

        Person restored = adapted.toModelType(ab);
        assertEquals(2, restored.getNotes().size());
        assertEquals(note1, restored.getNotes().get(0));
        assertEquals(note2, restored.getNotes().get(1));
    }

    @Test
    public void toModelType_invalidTags_throwsIllegalValueException() {
        List<JsonAdaptedTag> invalidTags = new ArrayList<>(VALID_TAGS);
        invalidTags.add(new JsonAdaptedTag(INVALID_TAG));
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS, invalidTags,
                        VALID_STATUS, VALID_REJECTION_REASONS, VALID_DATE_ADDED, VALID_PRIORITY, null);

        AddressBook ab = new AddressBook();
        BENSON.getTags().forEach(ab::addTag);

        assertThrows(IllegalValueException.class, () -> person.toModelType(ab));
    }

}
