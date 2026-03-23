package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TypicalPersons.ALICE;

import org.junit.jupiter.api.Test;

public class VersionedAddressBookTest {

    @Test
    public void canUndo_noCommit_returnsFalse() {
        VersionedAddressBook versionedAddressBook = new VersionedAddressBook(new AddressBook());

        assertFalse(versionedAddressBook.canUndo());
    }

    @Test
    public void commitAndUndo_restoresPreviousState() {
        VersionedAddressBook versionedAddressBook = new VersionedAddressBook(new AddressBook());
        versionedAddressBook.addPerson(ALICE);
        versionedAddressBook.commit();

        assertTrue(versionedAddressBook.hasPerson(ALICE));
        assertTrue(versionedAddressBook.canUndo());

        versionedAddressBook.undo();

        assertFalse(versionedAddressBook.hasPerson(ALICE));
    }

    @Test
    public void canRedo_noUndo_returnsFalse() {
        VersionedAddressBook versionedAddressBook = new VersionedAddressBook(new AddressBook());

        assertFalse(versionedAddressBook.canRedo());
    }

    @Test
    public void commitUndoAndRedo_restoresState() {
        VersionedAddressBook versionedAddressBook = new VersionedAddressBook(new AddressBook());
        versionedAddressBook.addPerson(ALICE);
        versionedAddressBook.commit();

        assertTrue(versionedAddressBook.hasPerson(ALICE));

        versionedAddressBook.undo();

        assertFalse(versionedAddressBook.hasPerson(ALICE));
        assertTrue(versionedAddressBook.canRedo());

        versionedAddressBook.redo();

        assertTrue(versionedAddressBook.hasPerson(ALICE));
    }
}
