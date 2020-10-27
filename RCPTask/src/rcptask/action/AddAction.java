package rcptask.action;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.action.Action;

import rcptask.Activator;
import rcptask.command.StudentCommand;

public class AddAction extends Action {

	public AddAction(String label, String iconPath) {
		setText(label);
		setImageDescriptor(Activator.getImageDescriptor(iconPath));
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
