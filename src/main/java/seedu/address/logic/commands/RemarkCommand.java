package seedu.address.logic.commands;

import seedu.address.model.Model;

public class RemarkCommand extends Command{
    public static final String COMMAND_WORD = "remark";

    public static final String MESSAGE_NOT_IMPLEMENTED_YET = "Remark command not implemented yet";

    @Override
    public CommandResult execute(Model model) {
        return new CommandResult("Remark command executed");
    }
}
