package seedu.address.ui;

import java.util.Optional;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.Logic;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Person;

/**
 * The Main Window. Provides the basic application layout containing
 * a menu bar and space where other JavaFX elements can be placed.
 */
public class MainWindow extends UiPart<Stage> {

    private static final String FXML = "MainWindow.fxml";

    private final Logger logger = LogsCenter.getLogger(getClass());

    private Stage primaryStage;
    private Logic logic;

    // Independent Ui parts residing in this Ui container
    private PersonListPanel personListPanel;
    private ResultDisplay resultDisplay;
    private HelpWindow helpWindow;
    private CandidateDetailPanel candidateDetailPanel;
    private Person currentlyShownPerson = null;
    private Person lastRemovedPerson = null;

    @FXML
    private StackPane commandBoxPlaceholder;

    @FXML
    private MenuItem helpMenuItem;

    @FXML
    private StackPane personListPanelPlaceholder;

    @FXML
    private StackPane resultDisplayPlaceholder;

    @FXML
    private StackPane candidateDetailPanelPlaceholder;

    @FXML
    private StackPane statusbarPlaceholder;

    /**
     * Creates a {@code MainWindow} with the given {@code Stage} and {@code Logic}.
     */
    public MainWindow(Stage primaryStage, Logic logic) {
        super(FXML, primaryStage);

        // Set dependencies
        this.primaryStage = primaryStage;
        this.logic = logic;

        // Configure the UI
        setWindowDefaultSize(logic.getGuiSettings());

        setAccelerators();

        helpWindow = new HelpWindow();
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    private void setAccelerators() {
        setAccelerator(helpMenuItem, KeyCombination.valueOf("F1"));
    }

    /**
     * Sets the accelerator of a MenuItem.
     * @param keyCombination the KeyCombination value of the accelerator
     */
    private void setAccelerator(MenuItem menuItem, KeyCombination keyCombination) {
        menuItem.setAccelerator(keyCombination);

        /*
         * TODO: the code below can be removed once the bug reported here
         * https://bugs.openjdk.java.net/browse/JDK-8131666
         * is fixed in later version of SDK.
         *
         * According to the bug report, TextInputControl (TextField, TextArea) will
         * consume function-key events. Because CommandBox contains a TextField, and
         * ResultDisplay contains a TextArea, thus some accelerators (e.g F1) will
         * not work when the focus is in them because the key event is consumed by
         * the TextInputControl(s).
         *
         * For now, we add following event filter to capture such key events and open
         * help window purposely so to support accelerators even when focus is
         * in CommandBox or ResultDisplay.
         */
        getRoot().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getTarget() instanceof TextInputControl && keyCombination.match(event)) {
                menuItem.getOnAction().handle(new ActionEvent());
                event.consume();
            }
        });
    }

    /**
     * Fills up all the placeholders of this window.
     */
    void fillInnerParts() {
        personListPanel = new PersonListPanel(logic.getFilteredPersonList());
        personListPanelPlaceholder.getChildren().add(personListPanel.getRoot());

        resultDisplay = new ResultDisplay();
        resultDisplayPlaceholder.getChildren().add(resultDisplay.getRoot());

        StatusBarFooter statusBarFooter = new StatusBarFooter(logic.getAddressBookFilePath());
        statusbarPlaceholder.getChildren().add(statusBarFooter.getRoot());

        candidateDetailPanel = new CandidateDetailPanel();
        candidateDetailPanelPlaceholder.getChildren().add(candidateDetailPanel.getRoot());

        CommandBox commandBox = new CommandBox(this::executeCommand);
        commandBoxPlaceholder.getChildren().add(commandBox.getRoot());
    }

    /**
     * Sets the default size based on {@code guiSettings}.
     */
    private void setWindowDefaultSize(GuiSettings guiSettings) {
        primaryStage.setHeight(guiSettings.getWindowHeight());
        primaryStage.setWidth(guiSettings.getWindowWidth());
        if (guiSettings.getWindowCoordinates() != null) {
            double x = guiSettings.getWindowCoordinates().getX();
            double y = guiSettings.getWindowCoordinates().getY();
            if (isPositionOnScreen(x, y)) {
                primaryStage.setX(x);
                primaryStage.setY(y);
            }
            // If off-screen (e.g. secondary monitor disconnected), skip setting position
            // so JavaFX centres the window on the primary screen instead.
        }
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);
    }

    /**
     * Returns true if the point ({@code x}, {@code y}) falls within the visual bounds of
     * any currently connected screen. Used to guard against opening the window off-screen
     * when a previously-used monitor has been disconnected.
     */
    private boolean isPositionOnScreen(double x, double y) {
        for (Screen screen : Screen.getScreens()) {
            Rectangle2D bounds = screen.getVisualBounds();
            if (bounds.contains(x, y)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Opens the help window, restoring it from minimised state if necessary.
     */
    @FXML
    public void handleHelp() {
        helpWindow.show();
    }

    void show() {
        primaryStage.show();
    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        GuiSettings guiSettings = new GuiSettings(primaryStage.getWidth(), primaryStage.getHeight(),
                (int) primaryStage.getX(), (int) primaryStage.getY());
        logic.setGuiSettings(guiSettings);
        helpWindow.hide();
        primaryStage.hide();
    }

    public PersonListPanel getPersonListPanel() {
        return personListPanel;
    }

    /**
     * Executes the command and returns the result.
     *
     * @see seedu.address.logic.Logic#execute(String)
     */
    private CommandResult executeCommand(String commandText) throws CommandException, ParseException {
        try {
            CommandResult commandResult = logic.execute(commandText);
            logger.info("Result: " + commandResult.getFeedbackToUser());
            resultDisplay.setFeedbackToUser(commandResult.getFeedbackToUser());

            if (commandResult.isShowHelp()) {
                handleHelp();
            }

            if (commandResult.isExit()) {
                handleExit();
            }

            commandResult.getSelectedPerson().ifPresent(person -> {
                currentlyShownPerson = person;
                candidateDetailPanel.showPerson(person);
            });

            // Refresh the detail panel if a profile is open and the list has changed
            if (currentlyShownPerson != null && !commandResult.getSelectedPerson().isPresent()) {
                ObservableList<Person> currentList = logic.getFilteredPersonList();
                Optional<Person> updatedPerson = currentList.stream()
                        .filter(p -> p.isSamePerson(currentlyShownPerson))
                        .findFirst();
                // Fallback: if isSamePerson fails (e.g. both phone and email changed),
                // try matching by name as a secondary identifier.
                if (!updatedPerson.isPresent()) {
                    updatedPerson = currentList.stream()
                            .filter(p -> p.getName().equals(currentlyShownPerson.getName()))
                            .findFirst();
                }
                if (updatedPerson.isPresent()) {
                    currentlyShownPerson = updatedPerson.get();
                    candidateDetailPanel.updatePerson(currentlyShownPerson);
                } else {
                    lastRemovedPerson = currentlyShownPerson;
                    currentlyShownPerson = null;
                    candidateDetailPanel.clear();
                }
            }

            // Restore detail panel if undo brought back the last removed person
            if (currentlyShownPerson == null && lastRemovedPerson != null) {
                ObservableList<Person> currentList = logic.getFilteredPersonList();
                Optional<Person> restoredPerson = currentList.stream()
                        .filter(p -> p.isSamePerson(lastRemovedPerson))
                        .findFirst();
                if (restoredPerson.isPresent()) {
                    currentlyShownPerson = restoredPerson.get();
                    lastRemovedPerson = null;
                    candidateDetailPanel.showPerson(currentlyShownPerson);
                }
            }

            return commandResult;
        } catch (CommandException | ParseException e) {
            logger.info("An error occurred while executing command: " + commandText);
            resultDisplay.setFeedbackToUser(e.getMessage());
            throw e;
        }
    }
}
