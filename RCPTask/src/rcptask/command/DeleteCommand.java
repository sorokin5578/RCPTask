package rcptask.command;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import rcptask.viewpac.NavigationView;

public class DeleteCommand extends AbstractHandler {
	public static final String ID = "rcptask.command.deleteCommand";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		Composite composite = window.getShell().getParent();

		Object selectObj = null;
		
		if (selection != null && !selection.isEmpty() && selection instanceof IStructuredSelection) {
			selectObj = ((IStructuredSelection) selection).getFirstElement();
			File file = (File) selectObj;
			String msg = String.format("Confirm to delete the %s %s", file.getName(),
					file.isDirectory() ? "folder" : "file");
			if (!file.isDirectory() && MessageDialog.openConfirm(window.getShell(), "Deletion", msg)) {
				try {
					if(Files.deleteIfExists(file.toPath())) {
						MessageDialog.openInformation(window.getShell(), "Info", "Deleted successfully");
						NavigationView navigationView = (NavigationView) window.getActivePage().findView(NavigationView.ID);
						navigationView.refreshTree();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

}
