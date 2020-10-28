package rcptask.command;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import rcptask.viewpac.NavigationView;

public class DeleteCommand extends AbstractHandler {
	public static final String ID = "rcptask.command.deleteCommand";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
//		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
//		ISelection selection = HandlerUtil.getCurrentSelection(event);
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		IViewPart part = window.getActivePage().findView(NavigationView.ID);
		IViewSite viewSite = part.getViewSite();
		ISelectionProvider provider = viewSite.getSelectionProvider();
		ISelection selection = provider.getSelection();
		
		Object selectObj = null;

		if (selection != null && !selection.isEmpty() && selection instanceof IStructuredSelection) {
			selectObj = ((IStructuredSelection) selection).getFirstElement();
			File file = (File) selectObj;
			String msg = String.format("Confirm to delete the %s %s", file.getName().replaceFirst("[.][^.]+$", ""), "file");
			if(!file.isDirectory()) {
				if (MessageDialog.openConfirm(window.getShell(), "Deletion", msg)) {
					try {
						if (Files.deleteIfExists(file.toPath())) {
							NavigationView navigationView = (NavigationView) window.getActivePage()
									.findView(NavigationView.ID);
							navigationView.refreshTree();
							MessageDialog.openInformation(window.getShell(), "Info", "Deleted successfully");
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}else {
				MessageDialog.openInformation(window.getShell(), "Info", "You can not delete the folder!\nTry to select the Student to delete.");
			}
		}
		return null;
	}

}
