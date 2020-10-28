package rcptask.command;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import rcptask.viewpac.NavigationView;

public class OpenCommand extends AbstractHandler {

	public static final String ID = "rcptask.command.openCommand";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();

		DirectoryDialog directoryDialog = new DirectoryDialog(new Shell(), SWT.OPEN);
		String path = directoryDialog.open();
		if (path != null) {
			NavigationView navigationView = (NavigationView) window.getActivePage().findView(NavigationView.ID);
			navigationView.setPath(path);
			navigationView.refreshTree();
		}
		return null;
	}

}
