package seedu.address.ui;

import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import seedu.address.model.person.Note;
import seedu.address.model.person.Person;
import seedu.address.model.person.RejectionReason;
import seedu.address.model.person.Status;

/**
 * Panel that displays the full details of a selected candidate.
 */
public class CandidateDetailPanel extends UiPart<Region> {

    private static final String FXML = "CandidateDetailPanel.fxml";

    @FXML
    private ScrollPane detailScrollPane;
    @FXML
    private Label placeholderLabel;
    @FXML
    private VBox detailContent;
    @FXML
    private Label detailName;
    @FXML
    private Label detailPhone;
    @FXML
    private Label detailEmail;
    @FXML
    private Label detailAddress;
    @FXML
    private Label detailStatus;
    @FXML
    private Label detailPriority;
    @FXML
    private Label detailDateAdded;
    @FXML
    private FlowPane detailTags;
    @FXML
    private VBox detailNotes;
    @FXML
    private VBox detailRejections;

    public CandidateDetailPanel() {
        super(FXML);
    }

    /**
     * Displays the full details of the given {@code person}.
     */
    public void showPerson(Person person) {
        placeholderLabel.setVisible(false);
        placeholderLabel.setManaged(false);
        detailContent.setVisible(true);
        detailContent.setManaged(true);

        renderBasicFields(person);
        renderTags(person);
        renderNotes(person.getNotes());
        renderRejections(person.getRejectionReasons());
    }

    private void renderBasicFields(Person person) {
        detailName.setText(person.getName().fullName);
        detailPhone.setText("Phone: " + person.getPhone().value);
        detailEmail.setText("Email: " + person.getEmail().value);
        detailAddress.setText("Address: " + person.getAddress().value);
        detailStatus.setText("Status: " + formatStatus(person.getStatus()));
        detailPriority.setText("Priority: " + (person.getPriority().isPriority ? "⭐ High" : "Normal"));
        detailDateAdded.setText("Added: " + person.getDateAdded().getDisplayFormat());
    }

    private void renderTags(Person person) {
        detailTags.getChildren().clear();
        person.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> {
                    Label tagLabel = new Label(tag.tagName);
                    tagLabel.getStyleClass().add("detail-tag");
                    detailTags.getChildren().add(tagLabel);
                });
    }

    private void renderNotes(List<Note> notes) {
        detailNotes.getChildren().clear();
        if (notes.isEmpty()) {
            detailNotes.getChildren().add(makeFieldLabel("No notes recorded."));
            return;
        }
        IntStream.range(0, notes.size()).forEach(i -> {
            Note note = notes.get(i);
            VBox noteBox = new VBox(2);
            noteBox.getStyleClass().add("detail-note-box");
            Label heading = new Label((i + 1) + ". " + note.heading);
            heading.getStyleClass().add("detail-note-heading");
            heading.setWrapText(true);
            Label content = new Label(note.content);
            content.getStyleClass().add("detail-field");
            content.setWrapText(true);
            noteBox.getChildren().addAll(heading, content);
            detailNotes.getChildren().add(noteBox);
        });
    }

    private void renderRejections(List<RejectionReason> reasons) {
        detailRejections.getChildren().clear();
        if (reasons.isEmpty()) {
            detailRejections.getChildren().add(makeFieldLabel("No rejections recorded."));
            return;
        }
        IntStream.range(0, reasons.size()).forEach(i -> {
            Label reasonLabel = makeFieldLabel((i + 1) + ". " + reasons.get(i).reason);
            reasonLabel.setWrapText(true);
            detailRejections.getChildren().add(reasonLabel);
        });
    }

    private Label makeFieldLabel(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("detail-field");
        label.setWrapText(true);
        return label;
    }

    private String formatStatus(Status status) {
        switch (status) {
        case NONE:
            return "Active";
        case REJECTED:
            return "Rejected";
        case ARCHIVED:
            return "Archived";
        default:
            throw new IllegalArgumentException("Unknown status: " + status);
        }
    }
}
