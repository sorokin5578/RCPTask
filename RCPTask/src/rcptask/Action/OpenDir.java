package rcptask.Action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.ui.IWorkbenchWindow;

import rcptask.Activator;
import rcptask.ICommandIds;

public class OpenDir extends Action{
	private final IWorkbenchWindow window;
	
	public OpenDir(IWorkbenchWindow window, String label) {
		this.window = window;
        setText(label);
		setId(ICommandIds.CMD_OPEN_DIR);
//		setActionDefinitionId(ICommandIds.CMD_OPEN);
		setImageDescriptor(Activator.getImageDescriptor("/icons/folder.png"));
	}
	
	@Override
	public void run() {
		if(window != null) {	
			try {
				DirectoryDialog directoryDialog = new DirectoryDialog(window.getShell(), SWT.OPEN);
				directoryDialog.open();//TODO
			} catch (Exception e) {
				MessageDialog.openError(window.getShell(), "Error", "Error opening view:" + e.getMessage());
			}
		}
	}
}
