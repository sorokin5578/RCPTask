package rcptask.command;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;

import rcptask.Student;
import rcptask.editor.StudentEditor;
import rcptask.editor.StudentEditorInput;

public class StudentCommand extends AbstractHandler {

	public static final String ID = "rcptask.command.studentCommand";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		IWorkbenchPage page = window.getActivePage();

		StudentEditorInput input = new StudentEditorInput(new Student());
		try {
			page.openEditor(input, StudentEditor.ID);
			System.err.println("page");
		} catch (PartInitException e) {
			System.out.println("Error:" + this.getClass().getName() + ":" + e);
			e.printStackTrace();
			throw new ExecutionException("Error open UserEditor");
		}

//		// get the page
//		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
//		IWorkbenchPage page = window.getActivePage();
//		// get the selection
//		ISelection selection = HandlerUtil.getCurrentSelection(event);
//
//		Object selectObj = null;
//
//		// Having selected on DeptListView
//		if (selection != null && !selection.isEmpty() && selection instanceof IStructuredSelection) {
//			selectObj = ((IStructuredSelection) selection).getFirstElement();
//		}
//
//		// No Selection on DeptListView
//		// (Create new Department).
//		else {
//			// Create new Department.
//			selectObj = new Department();
//		}
//
//		Department dept = (Department) selectObj;
//		DeptEditorInput input = new DeptEditorInput(dept);
//
//		boolean found = false;
//
//		// Opening Editor references
//		IEditorReference[] eRefs = page.getEditorReferences();
//		for (IEditorReference ref : eRefs) {
//			IEditorPart editor = ref.getEditor(false);
//			if (editor != null && editor instanceof DeptEditor) {
//				// Restore
//				DeptEditor deptEditor = (DeptEditor) ref.getEditor(true);
//				found = true;
//
//				boolean saved = true;
//
//				// If editor is dirty, save it.
//				if (deptEditor.isDirty()) {
//					saved = page.saveEditor(deptEditor, true);
//				}
//				if (saved) {
//
//					// Reset input for DeptEditor.
//					page.reuseEditor(deptEditor, input);
//					deptEditor.showData();
//				}
//			}
//		}
//		if (!found) {
//			try {
//				page.openEditor(input, DeptEditor.ID);
//			} catch (PartInitException e) {
//				throw new RuntimeException(e);
//			}
//		}
		return null;
	}

}