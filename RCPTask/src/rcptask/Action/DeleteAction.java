package rcptask.Action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;

import rcptask.Activator;
import rcptask.ICommandIds;

public class DeleteAction extends Action{
	private final IWorkbenchWindow window;
	private final String viewId;
	
	public DeleteAction(IWorkbenchWindow window, String label, String viewId) {
		this.window = window;
		this.viewId = viewId;
        setText(label);
		setId(ICommandIds.CMD_DELETE);
//		setActionDefinitionId(ICommandIds.CMD_OPEN);
		setImageDescriptor(Activator.getImageDescriptor("/icons/bin_closed.png"));
	}
	
	@Override
	public void run() {
		if(window != null) {	
			try {
				MessageDialog.openInformation(window.getShell(), "Confirm", "DELETE");//TODO
			} catch (Exception e) {
				MessageDialog.openError(window.getShell(), "Error", "Error opening view:" + e.getMessage());
			}
		}
	}
}
