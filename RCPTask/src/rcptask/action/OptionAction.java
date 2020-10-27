package rcptask.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;

import rcptask.Activator;
import rcptask.ICommandIds;
import rcptask.command.StudentCommand;

public class OptionAction extends Action{
	private final IWorkbenchWindow window;
	
	public OptionAction(IWorkbenchWindow window, String label, String iconPath) {
		this.window = window;
        setText(label);
		setImageDescriptor(Activator.getImageDescriptor(iconPath));
	}
	
	
	@Override
	public void run() {
		if(window != null) {	
			try {
					MessageDialog.openConfirm(window.getShell(), "Info", getText());
			} catch (Exception e) {
				MessageDialog.openError(window.getShell(), "Error", "Error opening view:" + e.getMessage());
			}
		}
	}
}