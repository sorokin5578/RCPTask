package rcptask.command;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import rcptask.editor.StudentEditor;
import rcptask.editor.StudentEditorInput;
import rcptask.entity.Student;

public class StudentCommand extends AbstractHandler {

	public static final String ID = "rcptask.command.studentCommand";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
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