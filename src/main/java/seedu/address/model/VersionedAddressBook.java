package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores current address book state and a history of past states for undo operations.
 */
public class VersionedAddressBook extends AddressBook {

    private final List<ReadOnlyAddressBook> addressBookStateList;
    private int currentStatePointer;

    /**
     * Initializes with the given initial state and starts history with that snapshot.
     */
    public VersionedAddressBook(ReadOnlyAddressBook initialState) {
        super(initialState);
        addressBookStateList = new ArrayList<>();
        addressBookStateList.add(new AddressBook(initialState));
        currentStatePointer = 0;
    }

    /**
     * Saves a snapshot of the current address book state.
     */
    public void commit() {
        removeStatesAfterCurrentPointer();
        addressBookStateList.add(new AddressBook(this));
        currentStatePointer++;
    }

    /**
     * Returns {@code true} if undo can be performed.
     */
    public boolean canUndo() {
        return currentStatePointer > 0;
    }

    /**
     * Restores the previous address book state.
     */
    public void undo() {
        if (!canUndo()) {
            throw new IllegalStateException("No undoable state available.");
        }

        currentStatePointer--;
        resetData(addressBookStateList.get(currentStatePointer));
    }

    /**
     * Returns {@code true} if redo can be performed.
     */
    public boolean canRedo() {
        return currentStatePointer < addressBookStateList.size() - 1;
    }

    /**
     * Restores the next address book state.
     */
    public void redo() {
        if (!canRedo()) {
            throw new IllegalStateException("No redoable state available.");
        }

        currentStatePointer++;
        resetData(addressBookStateList.get(currentStatePointer));
    }

    private void removeStatesAfterCurrentPointer() {
        addressBookStateList.subList(currentStatePointer + 1, addressBookStateList.size()).clear();
    }

    @Override
    public void resetData(ReadOnlyAddressBook newData) {
        requireNonNull(newData);
        super.resetData(newData);
    }
}
