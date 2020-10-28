package rcptask.command;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import rcptask.Student;
import rcptask.editor.StudentEditor;
import rcptask.editor.StudentEditorInput;
import rcptask.utils.CSVReader;
import rcptask.viewpac.NavigationView;

public class StudentCommand extends AbstractHandler {

	public static final String ID = "rcptask.command.studentCommand";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
//		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
//		IWorkbenchPage page = window.getActivePage();
//
//		StudentEditorInput input = new StudentEditorInput(new Student());
//		try {
//			page.openEditor(input, StudentEditor.ID);
//			System.err.println("page");
//		} catch (PartInitException e) {
//			System.out.println("Error:" + this.getClass().getName() + ":" + e);
//			e.printStackTrace();
//			throw new ExecutionException("Error open UserEditor");
//		}

		// get the page
//		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
//		IWorkbenchPage page = window.getActivePage();
//		// get the selection
//		ISelection selection = HandlerUtil.getCurrentSelection(event);

//		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
//		IWorkbenchPage page = window.getActivePage();
//		IViewPart part = window.getActivePage().findView(NavigationView.ID);
//		IViewSite viewSite = part.getViewSite();
//		ISelectionProvider provider = viewSite.getSelectionProvider();
//		ISelection selection = provider.getSelection();
//
//		Object selectObj = null;
//		Student student = new Student();
//
//		// Having selected on DeptListView
//		if (selection != null && !selection.isEmpty() && selection instanceof IStructuredSelection) {
//			selectObj = ((IStructuredSelection) selection).getFirstElement();
//			File fileToRead = (File) selectObj;
//			try {
//				student = CSVReader.readStudentFromCSV(fileToRead.getAbsolutePath());
//			} catch (NumberFormatException | IOException e) {
//				// TODO
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//
//		StudentEditorInput input = new StudentEditorInput(student);
//
//		boolean found = false;
//
//		if (student.getName() != null) {
//			IEditorReference[] eRefs = page.getEditorReferences();
//			for (IEditorReference ref : eRefs) {
//				IEditorPart editor = ref.getEditor(false);
//				if (editor != null && editor instanceof StudentEditor) {
//					// Restore
//					StudentEditor studentEditor = (StudentEditor) ref.getEditor(true);
//					if(studentEditor.getPartName().equals(student.getName())) {
//						found = true;
//						break;
//					}
//				}
//			}
//		}
//		if (!found) {
//			try {
//				page.openEditor(input, StudentEditor.ID);
//			} catch (PartInitException e) {
//				throw new RuntimeException(e);
//			}
//		}
		
		
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		IWorkbenchPage page = window.getActivePage();
		StudentEditorInput input = new StudentEditorInput(new Student());
		try {
			page.openEditor(input, StudentEditor.ID);
		} catch (PartInitException e) {
			throw new RuntimeException(e);
		}
		return null;
	}

}