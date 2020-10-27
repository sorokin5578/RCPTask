package rcptask.editor;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import rcptask.Student;

public class StudentEditorInput implements IEditorInput {

	private Student student;

	public StudentEditorInput(Student student) {
		this.student = student;
	}

	public Student getStudent() {
		return student;
	}

	@Override
	public <T> T getAdapter(Class<T> adapter) {
		return null;
	}

	@Override
	public boolean exists() {
		return false;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	@Override
	public String getName() {
		return student != null ? student.getName() : "Student";
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public String getToolTipText() {
		return "Student";
	}

}
