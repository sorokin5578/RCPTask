package rcptask.Action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;

import rcptask.Activator;
import rcptask.ICommandIds;

public class AddAction extends Action{
	private final IWorkbenchWindow window;
	private final String viewId;
	private int instanceNum = 0;
	
	public AddAction(IWorkbenchWindow window, String label, String viewId) {
		this.window = window;
		this.viewId = viewId;
        setText(label);
		setId(ICommandIds.CMD_ADD);
//		setId("rcptask.command.studentCommand");
//		setActionDefinitionId(ICommandIds.CMD_OPEN);
//		setId("rcptask.command.studentCommand");
//		setActionDefinitionId("rcptask.command.studentCommand");
		setImageDescriptor(Activator.getImageDescriptor("/icons/add.png"));
	}
	
	
	@Override
	public void run() {
		if(window != null) {	
			try {
				window.getActivePage().showView(viewId, Integer.toString(instanceNum++), IWorkbenchPage.VIEW_ACTIVATE);
				System.out.println(instanceNum);
			} catch (Exception e) {
				MessageDialog.openError(window.getShell(), "Error", "Error opening view:" + e.getMessage());
			}
		}
	}
}
