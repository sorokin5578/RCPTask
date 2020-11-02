package rcptask;

import java.io.File;
import java.io.IOException;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import rcptask.editor.StudentEditor;
import rcptask.editor.StudentEditorInput;
import rcptask.entity.Student;
import rcptask.utils.CSVReader2;
import rcptask.viewpac.NavigationView;

public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {

	private CTabFolder cTabFolder;

	private static final String PERSPECTIVE_ID = "RCPTask.perspective"; //$NON-NLS-1$

	@Override
	public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
		return new ApplicationWorkbenchWindowAdvisor(configurer);
	}

	@Override
	public String getInitialWindowPerspectiveId() {
		return PERSPECTIVE_ID;
	}

	@Override
	public void postStartup() {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		findCTabFolder(window.getShell().getChildren());
		dnd();
	}

	private void findCTabFolder(Control[] controls) {
		for (Control control : controls) {
			if (control instanceof CTabFolder) {
				cTabFolder = (CTabFolder) control;
				break;
			} else if (control instanceof Composite) {
				findCTabFolder(((Composite) control).getChildren());
			}
		}
	}

	private void dnd() {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		NavigationView navigationView = (NavigationView) window.getActivePage().findView(NavigationView.ID);
		DragSource ds = new DragSource(navigationView.getViewer().getTree(), DND.DROP_MOVE);
		ds.setTransfer(new Transfer[] { TextTransfer.getInstance() });
		ds.addDragListener(new DragSourceAdapter() {
			@Override
			public void dragSetData(DragSourceEvent event) {
				IStructuredSelection selection = navigationView.getViewer().getStructuredSelection();
				File file = (File) selection.getFirstElement();
				event.data = file.getAbsolutePath();
			}
		});

		DropTarget dt = new DropTarget(cTabFolder, DND.DROP_MOVE);
		dt.setTransfer(new Transfer[] { TextTransfer.getInstance() });
		dt.addDropListener(new DropTargetAdapter() {
			@Override
			public void drop(DropTargetEvent event) {
				IWorkbenchPage page = window.getActivePage();
				File fileToRead = new File(event.data.toString());
				if (!fileToRead.isDirectory()) {
					Student student = new Student();
					try {
						student = CSVReader2.readStudent(fileToRead.getAbsolutePath());
					} catch (NumberFormatException | IOException e) {
						navigationView.createErrorDialog(e, window);
					} catch (Exception e) {
						e.printStackTrace();
					}
					StudentEditorInput input = new StudentEditorInput(student);
					boolean found = false;

					if (student.getName() != null) {
						IEditorReference[] eRefs = page.getEditorReferences();
						for (IEditorReference ref : eRefs) {
							IEditorPart editor = ref.getEditor(false);
							if (editor instanceof StudentEditor) {
								StudentEditor studentEditor = (StudentEditor) ref.getEditor(true);
								Student studentFromInput = ((StudentEditorInput) studentEditor.getEditorInput())
										.getStudent();
								if (student.equals(studentFromInput)) {
									found = true;
									MessageDialog.openInformation(window.getShell(), "Info",
											"This Student is already open.");
									break;
								}
							}
						}
					}
					if (!found) {
						try {
							page.openEditor(input, StudentEditor.ID);
						} catch (PartInitException e) {
							throw new RuntimeException(e);
						}
					}
				} else {
					MessageDialog.openInformation(window.getShell(), "Info",
							"You can not open the folder!\nTry open Student.");
				}
			}
		});
	}

}
