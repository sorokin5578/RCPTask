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
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import rcptask.editor.StudentEditor;
import rcptask.viewpac.NavigationView;

public class DeleteCommand extends AbstractHandler {
	public static final String ID = "rcptask.command.deleteCommand";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		IViewPart part = window.getActivePage().findView(NavigationView.ID);
		IViewSite viewSite = part.getViewSite();
		ISelectionProvider provider = viewSite.getSelectionProvider();
		ISelection selection = provider.getSelection();

		Object selectObj = null;
		if (selection != null && !selection.isEmpty() && selection instanceof IStructuredSelection) {
			selectObj = ((IStructuredSelection) selection).getFirstElement();
			File file = (File) selectObj;
			String fileName = file.getName().replaceFirst("[.][^.]+$", "");
			String msg = String.format("Confirm to delete the %s %s", fileName, "file");
			if (!file.isDirectory()) {
				if (MessageDialog.openConfirm(window.getShell(), "Deletion", msg)) {
					try {
						if (Files.deleteIfExists(file.toPath())) {
							closeTabIfopen(window, fileName);
							NavigationView navigationView = (NavigationView) window.getActivePage()
									.findView(NavigationView.ID);
							navigationView.refreshTree();
							MessageDialog.openInformation(window.getShell(), "Info", "Deleted successfully");
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			} else {
				MessageDialog.openInformation(window.getShell(), "Info",
						"You can not delete the folder!\nTry to select the Student to delete.");
			}
		}
		return null;
	}

	private void closeTabIfopen(IWorkbenchWindow window, String fileName) {

		IEditorReference[] eRefs = window.getActivePage().getEditorReferences();
		for (IEditorReference ref : eRefs) {
			IEditorPart editor = ref.getEditor(false);
			if (editor instanceof StudentEditor) {
				StudentEditor studentEditor = (StudentEditor) ref.getEditor(true);
				if (studentEditor.getPartName().equals(fileName)) {
					window.getActivePage().closeEditor(studentEditor, false);
					break;
				}
			}

		}
	}
}
