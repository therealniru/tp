package seedu.address.ui;

import java.util.logging.Logger;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.Stage;
import seedu.address.commons.core.LogsCenter;

/**
 * Controller for a help page
 */
public class HelpWindow extends UiPart<Stage> {

    public static final String USERGUIDE_URL = "https://ay2526s2-cs2103t-t17-4.github.io/tp/UserGuide.html";
    public static final String HELP_MESSAGE = "Refer to the user guide for full details:";

    private static final int MIN_WIDTH = 700;
    private static final int MIN_HEIGHT = 500;

    private static final Logger logger = LogsCenter.getLogger(HelpWindow.class);
    private static final String FXML = "HelpWindow.fxml";

    @FXML
    private Button copyButton;

    @FXML
    private Label helpMessage;

    @FXML
    private Label urlLabel;

    @FXML
    private TableView<CommandEntry> commandTable;

    @FXML
    private TableColumn<CommandEntry, String> actionColumn;

    @FXML
    private TableColumn<CommandEntry, String> formatColumn;

    /**
     * Creates a new HelpWindow.
     *
     * @param root Stage to use as the root of the HelpWindow.
     */
    public HelpWindow(Stage root) {
        super(FXML, root);
        helpMessage.setText(HELP_MESSAGE);
        urlLabel.setText(USERGUIDE_URL);

        // Enforce minimum window size so commands remain visible on resize.
        root.setMinWidth(MIN_WIDTH);
        root.setMinHeight(MIN_HEIGHT);

        actionColumn.setCellValueFactory(new PropertyValueFactory<>("action"));
        formatColumn.setCellValueFactory(new PropertyValueFactory<>("format"));
        actionColumn.setReorderable(false);
        formatColumn.setReorderable(false);
        commandTable.setItems(buildCommandEntries());
        commandTable.setPlaceholder(new Label(""));
    }

    /**
     * Creates a new HelpWindow.
     */
    public HelpWindow() {
        this(new Stage());
    }

    private ObservableList<CommandEntry> buildCommandEntries() {
        ObservableList<CommandEntry> entries = FXCollections.observableArrayList();
        entries.add(new CommandEntry("Add", "add n/NAME p/PHONE e/EMAIL a/ADDRESS [pr/PRIORITY]"));
        entries.add(new CommandEntry("Clear", "clear"));
        entries.add(new CommandEntry("Edit", "edit INDEX [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS] [pr/PRIORITY]"));
        entries.add(new CommandEntry("Exit", "exit"));
        entries.add(new CommandEntry("Filter", "filter TAG"));
        entries.add(new CommandEntry("Find", "find KEYWORD [MORE_KEYWORDS]"));
        entries.add(new CommandEntry("Help", "help"));
        entries.add(new CommandEntry("List", "list"));
        entries.add(new CommandEntry("Add Note", "addnote INDEX c/CONTENT [h/HEADING]"));
        entries.add(new CommandEntry("Delete Note", "deletenote INDEX NOTE_INDEX"));
        entries.add(new CommandEntry("Edit Note", "editnote INDEX NOTE_INDEX [c/CONTENT] [h/HEADING]"));
        entries.add(new CommandEntry("Redo", "redo"));
        entries.add(new CommandEntry("Add Reject", "addreject INDEX REASON"));
        entries.add(new CommandEntry("Delete Reject", "deletereject INDEX REJECT_INDEX"));
        entries.add(new CommandEntry("Edit Reject", "editreject INDEX REJECT_INDEX NEW_REASON"));
        entries.add(new CommandEntry("Remove", "remove INDEX"));
        entries.add(new CommandEntry("Show", "show INDEX"));
        entries.add(new CommandEntry("Sort (date)", "sort date o/ORDER"));
        entries.add(new CommandEntry("Sort (priority)", "sort pr o/ORDER"));
        entries.add(new CommandEntry("Tag", "tag INDEX[,INDEX]... [at/TAG]... [dt/TAG]..."));
        entries.add(new CommandEntry("Tag Pool", "tagpool [at/TAG]... [dt/TAG]..."));
        entries.add(new CommandEntry("Undo", "undo"));
        return entries;
    }

    /**
     * Shows the help window.
     * @throws IllegalStateException
     *     <ul>
     *         <li>
     *             if this method is called on a thread other than the JavaFX Application Thread.
     *         </li>
     *         <li>
     *             if this method is called during animation or layout processing.
     *         </li>
     *         <li>
     *             if this method is called on the primary stage.
     *         </li>
     *         <li>
     *             if {@code dialogStage} is already showing.
     *         </li>
     *     </ul>
     */
    public void show() {
        logger.fine("Showing help page about the application.");
        getRoot().show();
        getRoot().centerOnScreen();
    }

    /**
     * Returns true if the help window is currently being shown.
     */
    public boolean isShowing() {
        return getRoot().isShowing();
    }

    /**
     * Hides the help window.
     */
    public void hide() {
        getRoot().hide();
    }

    /**
     * Focuses on the help window, restoring it if minimized.
     */
    public void focus() {
        getRoot().setIconified(false);
        getRoot().toFront();
        getRoot().requestFocus();
    }

    /**
     * Copies the URL to the user guide to the clipboard.
     */
    @FXML
    private void copyUrl() {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent url = new ClipboardContent();
        url.putString(USERGUIDE_URL);
        clipboard.setContent(url);
    }

    /**
     * A row in the help window's command summary table.
     */
    public static class CommandEntry {
        private final StringProperty action;
        private final StringProperty format;

        /**
         * Constructs a {@code CommandEntry} for display in the help command summary table.
         */
        public CommandEntry(String action, String format) {
            this.action = new SimpleStringProperty(action);
            this.format = new SimpleStringProperty(format);
        }

        public String getAction() {
            return action.get();
        }

        public String getFormat() {
            return format.get();
        }

        public StringProperty actionProperty() {
            return action;
        }

        public StringProperty formatProperty() {
            return format;
        }
    }
}
