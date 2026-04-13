package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADD_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DELETE_TAG;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.TagCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new TagCommand object.
 */
public class TagCommandParser implements Parser<TagCommand> {

    public static final int MAX_TAGS = 10;
    public static final String MESSAGE_EXCEEDED_LIMIT =
            "Too many tags. A maximum of " + MAX_TAGS + " tags can be processed per command.";
    public static final String MESSAGE_DUPLICATE_INDICES =
            "Duplicate indices are not allowed.";

    /**
     * Parses the given {@code String} of arguments in the context of the TagCommand
     * and returns a TagCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public TagCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_ADD_TAG, PREFIX_DELETE_TAG);

        List<Index> indices;
        try {
            indices = parseIndices(argMultimap.getPreamble());
        } catch (ParseException pe) {
            if (MESSAGE_DUPLICATE_INDICES.equals(pe.getMessage())) {
                throw pe;
            }
            String preamble = argMultimap.getPreamble().trim();
            if (preamble.matches("^[\\d,]+\\s+.+")) {
                throw new ParseException("Invalid command format. Did you forget a prefix? (e.g. at/ or dt/) \n"
                        + TagCommand.MESSAGE_USAGE);
            }
            if (ParserUtil.MESSAGE_INVALID_INDEX.equals(pe.getMessage()) && preamble.matches("^[\\d,]+$")) {
                throw pe;
            }
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagCommand.MESSAGE_USAGE), pe);
        }

        List<String> addValues = argMultimap.getAllValues(PREFIX_ADD_TAG);
        List<String> deleteValues = argMultimap.getAllValues(PREFIX_DELETE_TAG);

        if (addValues.isEmpty() && deleteValues.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagCommand.MESSAGE_USAGE));
        }

        if (addValues.size() + deleteValues.size() > MAX_TAGS) {
            throw new ParseException(MESSAGE_EXCEEDED_LIMIT);
        }

        List<Tag> tagsToAdd = new ArrayList<>();
        for (String tagName : addValues) {
            tagsToAdd.add(ParserUtil.parseTag(tagName));
        }

        List<Tag> tagsToDelete = new ArrayList<>();
        for (String tagName : deleteValues) {
            tagsToDelete.add(ParserUtil.parseTag(tagName));
        }

        return new TagCommand(indices, tagsToAdd, tagsToDelete);
    }

    private List<Index> parseIndices(String indexList) throws ParseException {
        String[] indexTokens = indexList.split(",");
        List<Index> parsedIndices = new ArrayList<>();
        Set<Integer> seenIndices = new HashSet<>();

        for (String indexToken : indexTokens) {
            String trimmedIndexToken = indexToken.trim();
            if (trimmedIndexToken.isEmpty()) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagCommand.MESSAGE_USAGE));
            }

            Index index = ParserUtil.parseIndex(trimmedIndexToken);
            if (!seenIndices.add(index.getOneBased())) {
                throw new ParseException(MESSAGE_DUPLICATE_INDICES);
            }
            parsedIndices.add(index);
        }

        return parsedIndices;
    }
}
