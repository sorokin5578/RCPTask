package rcptask.viewpac;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import rcptask.Activator;
import rcptask.action.AddAction;
import rcptask.action.DeleteAction;
import rcptask.action.OptionAction;
import rcptask.editor.StudentEditor;
import rcptask.editor.StudentEditorInput;
import rcptask.entity.Student;
import rcptask.utils.CSVReader;

public class NavigationView extends ViewPart {
	public static final String ID = "rcptask.viewpac.navigationView";
	private TreeViewer viewer;
	private String path;

	@Override
	public void createPartControl(Composite parent) {
		

		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new DelegatingStyledCellLabelProvider(new ViewLabelProvider()));

		addContextMenu();
		initDragAndDrop();


		getSite().setSelectionProvider(viewer);
	}

	private void addContextMenu() {
		String iconPath = "/icons/eclipse16.png";
		MenuManager menuManager = new MenuManager();
		Menu menu = menuManager.createContextMenu(viewer.getTree());
		menuManager.add(new AddAction());
		menuManager.add(new DeleteAction());
		menuManager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		menuManager.add(new OptionAction(PlatformUI.getWorkbench().getActiveWorkbenchWindow(), "Option 1", iconPath));
		menuManager.add(new OptionAction(PlatformUI.getWorkbench().getActiveWorkbenchWindow(), "Option 2", iconPath));
		menuManager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));

		MenuManager subManager = new MenuManager("Submenu");
		subManager.add(new OptionAction(PlatformUI.getWorkbench().getActiveWorkbenchWindow(), "Option 3", iconPath));
		subManager.add(new OptionAction(PlatformUI.getWorkbench().getActiveWorkbenchWindow(), "Option 4", iconPath));
		menuManager.add(subManager);

		viewer.getTree().setMenu(menu);
		getSite().registerContextMenu(menuManager, viewer);
	}
	
	public void initDragAndDrop() {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		DragSource ds = new DragSource(viewer.getTree(), DND.DROP_MOVE);
		ds.setTransfer(new Transfer[] { TextTransfer.getInstance() });
		ds.addDragListener(new DragSourceAdapter() {
			@Override
			public void dragSetData(DragSourceEvent event) {
				IStructuredSelection selection = viewer.getStructuredSelection();
				File file = (File) selection.getFirstElement();
				event.data = file.getAbsolutePath();
			}
		});

		DropTarget dt = new DropTarget(window.getShell(), DND.DROP_MOVE);
		dt.setTransfer(new Transfer[] { TextTransfer.getInstance() });
		dt.addDropListener(new DropTargetAdapter() {
			@Override
			public void drop(DropTargetEvent event) {
				IWorkbenchPage page = window.getActivePage();
				File fileToRead = new File(event.data.toString());
				if (!fileToRead.isDirectory()) {
					Student student = new Student();
					try {
						student = CSVReader.readStudentFromCSV(fileToRead.getAbsolutePath());
					} catch (NumberFormatException | IOException e) {
						createErrorDialog(e, window);
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
								Student studentFromInput = ((StudentEditorInput)studentEditor.getEditorInput()).getStudent();
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

	class ViewContentProvider implements ITreeContentProvider {
		@Override
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
			// empty
		}

		@Override
		public void dispose() {
			// empty
		}

		@Override
		public Object[] getElements(Object inputElement) {
			File[] arr = (File[]) inputElement;
			File[] newArr = new File[1];
			String fileName = new File(path).getName();
			for (File file : arr) {
				if (file.getName().equals(fileName)) {
					newArr[0] = file;
				}
			}
			return newArr;
		}

		@Override
		public Object[] getChildren(Object parentElement) {
			File file = (File) parentElement;
			return file.listFiles();
		}

		@Override
		public Object getParent(Object element) {
			File file = (File) element;
			return file.getParentFile();
		}

		@Override
		public boolean hasChildren(Object element) {
			File file = (File) element;
			return file.isDirectory();
		}

	}

	class ViewLabelProvider extends LabelProvider implements IStyledLabelProvider {

		private ImageDescriptor directoryImage;
		private ImageDescriptor fileImage;
		private ResourceManager resourceManager;

		public ViewLabelProvider() {
			this.directoryImage = Activator.getImageDescriptor("/icons/folder.png");
			this.fileImage = Activator.getImageDescriptor("/icons/page_white.png");
		}

		@Override
		public StyledString getStyledText(Object element) {
			if (element instanceof File) {
				File file = (File) element;
				StyledString styledString = new StyledString(getFileName(file).replaceFirst("[.][^.]+$", ""));
				String[] files = file.list();
				if (files != null) {
					styledString.append(" ( " + files.length + " ) ", StyledString.COUNTER_STYLER);
				}
				return styledString;

			}
			return null;
		}

		@Override
		public Image getImage(Object element) {
			if (element instanceof File) {
				if (((File) element).isDirectory()) {
					return getResourceManager().createImage(directoryImage);
				} else {
					return getResourceManager().createImage(fileImage);
				}
			}

			return super.getImage(element);
		}

		@Override
		public void dispose() {
			if (resourceManager != null) {
				resourceManager.dispose();
				resourceManager = null;
			}
		}

		protected ResourceManager getResourceManager() {
			if (resourceManager == null) {
				resourceManager = new LocalResourceManager(JFaceResources.getResources());
			}
			return resourceManager;
		}

		private String getFileName(File file) {
			String name = file.getName();
			return name.isEmpty() ? file.getPath() : name;
		}
	}

	public void setFocus() {
		viewer.getControl().setFocus();
	}

	public void refreshTree() {
		File file = new File(path);
		viewer.setInput(new File(file.getParent()).listFiles());
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	public void createErrorDialog(Throwable t, IWorkbenchWindow window) {

		List<Status> childStatuses = new ArrayList<>();
		StackTraceElement[] stackTraces = Thread.currentThread().getStackTrace();

		for (StackTraceElement stackTrace : stackTraces) {
			Status status = new Status(IStatus.ERROR, "JFaceTask", stackTrace.toString());
			childStatuses.add(status);
		}
		MultiStatus status = new MultiStatus("JFaceTask", IStatus.ERROR, childStatuses.toArray(new Status[] {}),
				t.toString(), t);
		ErrorDialog.openError(window.getShell(), "Error", "This is an error", status);

	}

}