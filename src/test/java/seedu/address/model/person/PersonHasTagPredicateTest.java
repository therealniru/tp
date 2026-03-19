package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.model.tag.Tag;
import seedu.address.testutil.PersonBuilder;

public class PersonHasTagPredicateTest {

    @Test
    public void equals() {
        Tag firstTag = new Tag("friends");
        Tag secondTag = new Tag("owesMoney");

        PersonHasTagPredicate firstPredicate = new PersonHasTagPredicate(firstTag);
        PersonHasTagPredicate secondPredicate = new PersonHasTagPredicate(secondTag);

        assertTrue(firstPredicate.equals(firstPredicate));

        PersonHasTagPredicate firstPredicateCopy = new PersonHasTagPredicate(firstTag);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        assertFalse(firstPredicate.equals(1));
        assertFalse(firstPredicate.equals(null));
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_personHasExactTag_returnsTrue() {
        PersonHasTagPredicate predicate = new PersonHasTagPredicate(new Tag("friends"));
        assertTrue(predicate.test(new PersonBuilder().withTags("friends").build()));

        predicate = new PersonHasTagPredicate(new Tag("FRIENDS"));
        assertTrue(predicate.test(new PersonBuilder().withTags("friends").build()));

        predicate = new PersonHasTagPredicate(new Tag("friends"));
        assertTrue(predicate.test(new PersonBuilder().withTags("owesMoney", "friends").build()));
    }

    @Test
    public void test_personDoesNotHaveExactTag_returnsFalse() {
        PersonHasTagPredicate predicate = new PersonHasTagPredicate(new Tag("friends"));
        assertFalse(predicate.test(new PersonBuilder().build()));

        predicate = new PersonHasTagPredicate(new Tag("friends"));
        assertFalse(predicate.test(new PersonBuilder().withTags("owesMoney").build()));

        predicate = new PersonHasTagPredicate(new Tag("Java"));
        assertFalse(predicate.test(new PersonBuilder().withTags("JavaScript").build()));
    }

    @Test
    public void toStringMethod() {
        Tag tag = new Tag("friends");
        PersonHasTagPredicate predicate = new PersonHasTagPredicate(tag);
        String expected = PersonHasTagPredicate.class.getCanonicalName() + "{tag=" + tag + "}";
        assertEquals(expected, predicate.toString());
    }
}
