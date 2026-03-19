package seedu.address.model.person;

import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.tag.Tag;

/**
 * Tests that a {@code Person} has a specific {@code Tag}.
 * Tag matching uses {@code Tag.equals}, which is case-insensitive.
 */
public class PersonHasTagPredicate implements Predicate<Person> {

    private final Tag tag;

    public PersonHasTagPredicate(Tag tag) {
        this.tag = tag;
    }

    @Override
    public boolean test(Person person) {
        return person.getTags().contains(tag);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof PersonHasTagPredicate)) {
            return false;
        }

        PersonHasTagPredicate otherPredicate = (PersonHasTagPredicate) other;
        return tag.equals(otherPredicate.tag);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("tag", tag)
                .toString();
    }
}
