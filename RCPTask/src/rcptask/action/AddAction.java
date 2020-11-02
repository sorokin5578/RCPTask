package rcptask.action;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.action.Action;

import rcptask.Activator;
import rcptask.command.StudentCommand;

public class AddAction extends Action {

	public AddAction() {
		super("Add@Ctrl+A", AS_PUSH_BUTTON);
		setImageDescriptor(Activator.getImageDescriptor("/icons/add.png"));
	}

	@Override
	public void run() {
		try {
			new StudentCommand().execute(null);
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}
}
