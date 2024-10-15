package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.FindPatientCommand.MESSAGE_NOT_IMPLEMENTED_YET;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Collections;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.patient.FindPatientPredicate;
import seedu.address.model.person.NameContainsKeywordsPredicate;

/**
 * Contains integration tests (interaction with the Model) for {@code FindPatientCommand}. (in the future)
 */
public class FindPatientCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void equals() {
        FindPatientPredicate firstPredicate =
                new FindPatientPredicate(Collections.singletonList("first"));
        FindPatientPredicate secondPredicate =
                new FindPatientPredicate(Collections.singletonList("second"));

        FindPatientCommand findFirstCommand = new FindPatientCommand(firstPredicate);
        FindPatientCommand findSecondCommand = new FindPatientCommand(secondPredicate);

        // same object -> returns true
        assertTrue(findFirstCommand.equals(findFirstCommand));

        // same values -> returns true
        FindPatientCommand findFirstCommandCopy = new FindPatientCommand(firstPredicate);
        assertTrue(findFirstCommand.equals(findFirstCommandCopy));

        // different types -> returns false
        assertFalse(findFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(findFirstCommand.equals(findSecondCommand));
    }

    @Test
    public void execute() {
        FindPatientPredicate firstPredicate =
                new FindPatientPredicate(Collections.singletonList("first"));
        assertCommandFailure(new FindPatientCommand(firstPredicate), model, MESSAGE_NOT_IMPLEMENTED_YET);
    }
}
