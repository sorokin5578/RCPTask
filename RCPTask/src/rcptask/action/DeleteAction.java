package rcptask.action;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.action.Action;

import rcptask.Activator;
import rcptask.command.DeleteCommand;

public class DeleteAction extends Action {

	public DeleteAction() {
		super("Delete@Ctrl+D", AS_PUSH_BUTTON);
		setImageDescriptor(Activator.getImageDescriptor("/icons/bin_closed.png"));
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
