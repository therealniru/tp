package seedu.address.ui;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.person.Person;
import seedu.address.model.person.RejectionReason;

/**
 * An UI component that displays information of a {@code Person}.
 */
public class PersonCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Person person;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label phone;
    @FXML
    private Label address;
    @FXML
    private Label email;
    @FXML
    private FlowPane tags;
    @FXML
    private Label rejectionCountTag;
    @FXML
    private Label dateAdded;
    @FXML
    private Label priorityTag;

    /**
     * Creates a {@code PersonCode} with the given {@code Person} and index to display.
     */
    public PersonCard(Person person, int displayedIndex) {
        super(FXML);
        this.person = person;
        id.setText(displayedIndex + ". ");
        name.setText(person.getName().fullName);
        phone.setText(person.getPhone().value);
        address.setText(person.getAddress().value);
        email.setText(person.getEmail().value);
        person.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));

        if (!person.getRejectionReasons().isEmpty()) {
            List<RejectionReason> reasons = person.getRejectionReasons();
            rejectionCountTag.setText(formatRejectionCountText(reasons.size()));
            rejectionCountTag.setStyle("-fx-background-color: #cc0000; -fx-text-fill: white; "
                    + "-fx-font-family: 'Segoe UI Semibold'; -fx-font-size: 16px; "
                    + "-fx-padding: 0 5 0 5; -fx-background-radius: 3;");
            rejectionCountTag.setVisible(true);
            rejectionCountTag.setManaged(true);
        }
        if (person.getPriority().isPriority) {
            priorityTag.setText("⭐ PRIORITY");
            priorityTag.setStyle("-fx-background-color: #f0ad4e; -fx-text-fill: black; "
                    + "-fx-font-family: 'Segoe UI Semibold'; -fx-font-size: 16px; "
                    + "-fx-padding: 0 5 0 5; -fx-background-radius: 3;");
            priorityTag.setVisible(true);
            priorityTag.setManaged(true);
        }
        dateAdded.setText("added on: " + person.getDateAdded().getDisplayFormat());
    }

    /**
     * Returns the rejection count badge text for the given {@code count}.
     */
    static String formatRejectionCountText(int count) {
        return "Rejected " + count + (count == 1 ? " time" : " times");
    }

    /**
     * Returns the formatted rejection reasons text for the given {@code reasons}.
     */
    static String formatRejectionReasonsText(List<RejectionReason> reasons) {
        String reasonsText = IntStream.range(0, reasons.size())
                .mapToObj(i -> (i + 1) + ". " + reasons.get(i).reason)
                .collect(Collectors.joining("\n"));
        return "Rejection reasons:\n" + reasonsText;
    }
}
