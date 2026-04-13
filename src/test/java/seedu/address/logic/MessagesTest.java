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
        assertFalse(result.contains("No address recorded"));
    }



    @Test
    public void format_personWithTags_doesNotContainTags() {
        Person person = new PersonBuilder().withTags("friends").build();
        String result = Messages.format(person);
        assertFalse(result.contains("friends"));
        assertFalse(result.contains("Tags:"));
    }

    @Test
    public void format_personWithoutTags_doesNotContainTagsSection() {
        Person person = new PersonBuilder().withTags().build();
        String result = Messages.format(person);
        assertFalse(result.contains("Tags:"));
    }

}
