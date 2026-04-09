package seedu.address.logic;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class MessagesTest {

    @Test
    public void format_personWithAddress_containsAddress() {
        Person person = new PersonBuilder().withAddress("123 Main St").build();
        String result = Messages.format(person);
        assertTrue(result.contains("123 Main St"));
        assertFalse(result.contains("Not provided"));
    }

    @Test
    public void format_personWithEmptyAddress_showsNotProvided() {
        // Address.EMPTY sentinel — address was not supplied at add time
        Person person = new PersonBuilder().withAddress("").build();
        String result = Messages.format(person);
        assertTrue(result.contains("Not provided"));
        assertTrue(person.getAddress().isEmpty());
    }

    @Test
    public void format_personWithTags_containsTags() {
        Person person = new PersonBuilder().withTags("friends").build();
        String result = Messages.format(person);
        assertTrue(result.contains("friends"));
    }

    @Test
    public void format_personWithoutTags_doesNotContainTagsSection() {
        Person person = new PersonBuilder().withTags().build();
        String result = Messages.format(person);
        assertFalse(result.contains("Tags:"));
    }

}
