package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.RejectionReason;

public class PersonCardTest {

    @Test
    public void formatRejectionCountText_singleRejection_returnsSingularForm() {
        assertEquals("Rejected 1 time", PersonCard.formatRejectionCountText(1));
    }

    @Test
    public void formatRejectionCountText_multipleRejections_returnsPluralForm() {
        assertEquals("Rejected 2 times", PersonCard.formatRejectionCountText(2));
        assertEquals("Rejected 5 times", PersonCard.formatRejectionCountText(5));
    }

    @Test
    public void formatRejectionCountText_zeroRejections_returnsPluralForm() {
        assertEquals("Rejected 0 times", PersonCard.formatRejectionCountText(0));
    }

    @Test
    public void formatRejectionReasonsText_singleReason_formatsCorrectly() {
        List<RejectionReason> reasons = Collections.singletonList(new RejectionReason("Failed interview"));
        String expected = "Rejection reasons:\n1. Failed interview";
        assertEquals(expected, PersonCard.formatRejectionReasonsText(reasons));
    }

    @Test
    public void formatRejectionReasonsText_multipleReasons_formatsCorrectly() {
        List<RejectionReason> reasons = Arrays.asList(
                new RejectionReason("Failed interview"),
                new RejectionReason("Insufficient experience")
        );
        String expected = "Rejection reasons:\n1. Failed interview\n2. Insufficient experience";
        assertEquals(expected, PersonCard.formatRejectionReasonsText(reasons));
    }

    @Test
    public void formatRejectionReasonsText_reasonWithPunctuation_formatsCorrectly() {
        List<RejectionReason> reasons = Collections.singletonList(
                new RejectionReason("Poor cultural fit, lacks leadership")
        );
        String expected = "Rejection reasons:\n1. Poor cultural fit, lacks leadership";
        assertEquals(expected, PersonCard.formatRejectionReasonsText(reasons));
    }
}
