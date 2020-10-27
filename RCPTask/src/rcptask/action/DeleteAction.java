package rcptask.action;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.action.Action;

import rcptask.Activator;
import rcptask.command.DeleteCommand;

public class DeleteAction extends Action {

	public DeleteAction(String label, String iconPath) {
		setText(label);
		setImageDescriptor(Activator.getImageDescriptor(iconPath));
	}

	@Override
	public void run() {
		try {
			new DeleteCommand().execute(null);
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

	}
}
