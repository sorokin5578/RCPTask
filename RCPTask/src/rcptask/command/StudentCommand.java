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
		return null;
	}

}