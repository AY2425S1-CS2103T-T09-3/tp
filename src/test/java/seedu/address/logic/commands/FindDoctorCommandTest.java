package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_DOCTORS_LISTED_OVERVIEW;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.KAREN;
import static seedu.address.testutil.TypicalPersons.KENNEDY;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBookWithDoctors;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.doctor.FindDoctorPredicate;

/**
 * Contains integration tests (interaction with the Model) for {@code FindDoctorCommand}. (in the future)
 */
public class FindDoctorCommandTest {
    private Model model = new ModelManager(getTypicalAddressBookWithDoctors(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalAddressBookWithDoctors(), new UserPrefs());

    @Test
    public void equals() {
        FindDoctorPredicate firstPredicate =
                new FindDoctorPredicate(Collections.singletonList("first"));
        FindDoctorPredicate secondPredicate =
                new FindDoctorPredicate(Collections.singletonList("second"));

        FindDoctorCommand findFirstCommand = new FindDoctorCommand(firstPredicate);
        FindDoctorCommand findSecondCommand = new FindDoctorCommand(secondPredicate);

        // same object -> returns true
        assertTrue(findFirstCommand.equals(findFirstCommand));

        // same values -> returns true
        FindDoctorCommand findFirstCommandCopy = new FindDoctorCommand(firstPredicate);
        assertTrue(findFirstCommand.equals(findFirstCommandCopy));

        // different types -> returns false
        assertFalse(findFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(findFirstCommand.equals(findSecondCommand));
    }

    @Test
    public void execute_zeroKeyword_noPersonFound() {
        String expectedMessage = String.format(MESSAGE_DOCTORS_LISTED_OVERVIEW, 0);
        FindDoctorPredicate predicate = preparePredicate(" ");
        FindDoctorCommand command = new FindDoctorCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(), model.getFilteredPersonList());
    }

    // TODO: Add test that only finds one doctor
    @Test
    public void execute_oneKeyword_onePersonFound() {
        String expectedMessage = String.format(MESSAGE_DOCTORS_LISTED_OVERVIEW, 1);
        FindDoctorPredicate predicate = preparePredicate("Karen");
        FindDoctorCommand command = new FindDoctorCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(KAREN), model.getFilteredPersonList());
    }

    @Test
    public void execute_multipleKeywords_multiplePersonFound() {
        String expectedMessage = String.format(MESSAGE_DOCTORS_LISTED_OVERVIEW, 2);
        FindDoctorPredicate predicate = preparePredicate("Karen KENNEDY");
        FindDoctorCommand command = new FindDoctorCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(KENNEDY, KAREN), model.getFilteredPersonList());
    }

    private FindDoctorPredicate preparePredicate(String userInput) {
        return new FindDoctorPredicate(Arrays.asList(userInput.split("\\s+")));
    }
}
