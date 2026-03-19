package seedu.address.model.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.address.model.ReadOnlyAddressBook;

public class SampleDataUtilTest {

    @Test
    public void getSampleAddressBook_containsAllPersonTagsInTagPool() {
        ReadOnlyAddressBook sampleAddressBook = SampleDataUtil.getSampleAddressBook();

        Set<String> tagsFromPersons = sampleAddressBook.getPersonList().stream()
                .flatMap(person -> person.getTags().stream())
                .map(tag -> tag.tagName.toLowerCase())
                .collect(Collectors.toSet());

        Set<String> tagsFromTagPool = sampleAddressBook.getTagList().stream()
                .map(tag -> tag.tagName.toLowerCase())
                .collect(Collectors.toSet());

        assertTrue(tagsFromTagPool.containsAll(tagsFromPersons));
        assertEquals(tagsFromPersons, tagsFromTagPool);
    }
}
