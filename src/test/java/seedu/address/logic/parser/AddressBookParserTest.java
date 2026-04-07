package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.AddRejectCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.DeleteNoteCommand;
import seedu.address.logic.commands.DeleteRejectCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.logic.commands.EditNoteCommand;
import seedu.address.logic.commands.EditRejectCommand;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.FilterCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.NoteCommand;
import seedu.address.logic.commands.RemoveCommand;
import seedu.address.logic.commands.SortDateCommand;
import seedu.address.logic.commands.TagCommand;
import seedu.address.logic.commands.TagPoolCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.NamePhoneEmailContainsKeywordsPredicate;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonHasTagPredicate;
import seedu.address.model.person.RejectionReason;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.EditPersonDescriptorBuilder;
import seedu.address.testutil.PersonBuilder;
import seedu.address.testutil.PersonUtil;

public class AddressBookParserTest {

    private final AddressBookParser parser = new AddressBookParser();

    @Test
    public void parseCommand_add() throws Exception {
        Person person = new PersonBuilder().build();
        AddCommand command = (AddCommand) parser.parseCommand(PersonUtil.getAddCommand(person));
        assertEquals(new AddCommand(person), command);
    }

    @Test
    public void parseCommand_clear() throws Exception {
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD) instanceof ClearCommand);
    }

    @Test
    public void parseCommand_clearWithExtraArgs_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parseCommand(ClearCommand.COMMAND_WORD + " 3"));
    }

    @Test
    public void parseCommand_remove() throws Exception {
        RemoveCommand command = (RemoveCommand) parser.parseCommand(
                RemoveCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new RemoveCommand(INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_edit() throws Exception {
        Person person = new PersonBuilder().build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(person).build();
        EditCommand command = (EditCommand) parser.parseCommand(EditCommand.COMMAND_WORD + " "
                + INDEX_FIRST_PERSON.getOneBased() + " " + PersonUtil.getEditPersonDescriptorDetails(descriptor));
        assertEquals(new EditCommand(INDEX_FIRST_PERSON, descriptor), command);
    }

    @Test
    public void parseCommand_exit() throws Exception {
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD) instanceof ExitCommand);
    }

    @Test
    public void parseCommand_exitWithExtraArgs_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parseCommand(ExitCommand.COMMAND_WORD + " 3"));
    }

    @Test
    public void parseCommand_find() throws Exception {
        List<String> keywords = Arrays.asList("foo", "bar", "baz");
        FindCommand command = (FindCommand) parser.parseCommand(
                FindCommand.COMMAND_WORD + " " + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FindCommand(new NamePhoneEmailContainsKeywordsPredicate(keywords)), command);
    }

    @Test
    public void parseCommand_filter() throws Exception {
        FilterCommand command = (FilterCommand) parser.parseCommand(FilterCommand.COMMAND_WORD + " friends");
        assertEquals(new FilterCommand(new PersonHasTagPredicate(new Tag("friends"))), command);
    }

    @Test
    public void parseCommand_help() throws Exception {
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD) instanceof HelpCommand);
    }

    @Test
    public void parseCommand_helpWithExtraArgs_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parseCommand(HelpCommand.COMMAND_WORD + " 3"));
    }

    @Test
    public void parseCommand_list() throws Exception {
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD) instanceof ListCommand);
    }

    @Test
    public void parseCommand_listWithExtraArgs_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parseCommand(ListCommand.COMMAND_WORD + " 3"));
    }

    @Test
    public void parseCommand_addreject() throws Exception {
        RejectionReason reason = new RejectionReason("Failed interview");
        AddRejectCommand command = (AddRejectCommand) parser.parseCommand(
                AddRejectCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased() + " Failed interview");
        assertEquals(new AddRejectCommand(INDEX_FIRST_PERSON, reason), command);
    }

    @Test
    public void parseCommand_tagpool() throws Exception {
        TagPoolCommand command = (TagPoolCommand) parser.parseCommand(
                TagPoolCommand.COMMAND_WORD + " a/Frontend");
        assertEquals(new TagPoolCommand(
                java.util.List.of(new seedu.address.model.tag.Tag("Frontend")),
                java.util.Collections.emptyList()), command);
    }

    @Test
    public void parseCommand_tag() throws Exception {
        TagCommand command = (TagCommand) parser.parseCommand(
                TagCommand.COMMAND_WORD + " 1 a/Java");
        assertEquals(new TagCommand(INDEX_FIRST_PERSON,
                java.util.List.of(new seedu.address.model.tag.Tag("Java")),
                java.util.Collections.emptyList()), command);
    }

    @Test
    public void parseCommand_editreject() throws Exception {
        assertTrue(parser.parseCommand(
                EditRejectCommand.COMMAND_WORD + " 1 1 Failed cultural fit") instanceof EditRejectCommand);
    }

    @Test
    public void parseCommand_deletereject() throws Exception {
        assertTrue(parser.parseCommand(
                DeleteRejectCommand.COMMAND_WORD + " 1 1") instanceof DeleteRejectCommand);
    }

    @Test
    public void parseCommand_unrecognisedInput_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE), ()
            -> parser.parseCommand(""));
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_UNKNOWN_COMMAND, () -> parser.parseCommand("unknownCommand"));
    }

    @Test
    public void parseCommand_sort() throws Exception {
        assertTrue(parser.parseCommand(SortCommandParser.COMMAND_WORD + " date o/asc") instanceof SortDateCommand);
        assertTrue(parser.parseCommand(SortCommandParser.COMMAND_WORD + " date o/desc") instanceof SortDateCommand);
    }

    @Test
    public void parseCommand_note() throws Exception {
        NoteCommand command = (NoteCommand) parser.parseCommand(
                NoteCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased() + " n/Some content");
        assertEquals(INDEX_FIRST_PERSON, command.getTargetIndex());
        assertEquals("General Note", command.getNote().heading);
        assertEquals("Some content", command.getNote().content);
    }

    @Test
    public void parseCommand_noteWithHeading() throws Exception {
        NoteCommand command = (NoteCommand) parser.parseCommand(
                NoteCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased()
                        + " n/Passed interview h/Tech Round");
        assertEquals(INDEX_FIRST_PERSON, command.getTargetIndex());
        assertEquals("Tech Round", command.getNote().heading);
        assertEquals("Passed interview", command.getNote().content);
    }

    @Test
    public void parseCommand_deletenote() throws Exception {
        DeleteNoteCommand command = (DeleteNoteCommand) parser.parseCommand(
                DeleteNoteCommand.COMMAND_WORD + " 1 2");
        assertEquals(INDEX_FIRST_PERSON, command.getTargetIndex());
        assertEquals(Index.fromOneBased(2), command.getNoteIndex());
    }

    @Test
    public void parseCommand_editnote() throws Exception {
        EditNoteCommand command = (EditNoteCommand) parser.parseCommand(
                EditNoteCommand.COMMAND_WORD + " 1 2 n/Updated content");
        assertEquals(INDEX_FIRST_PERSON, command.getTargetIndex());
        assertEquals(Index.fromOneBased(2), command.getNoteIndex());
        assertEquals("Updated content", command.getNewContent());
    }

    @Test
    public void parseCommand_undo() throws Exception {
        assertTrue(parser.parseCommand(UndoCommand.COMMAND_WORD) instanceof UndoCommand);
    }

    @Test
    public void parseCommand_undoWithExtraArgs_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parseCommand(UndoCommand.COMMAND_WORD + " now"));
    }
}
