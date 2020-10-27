package rcptask.viewpac;

import java.io.File;
import java.io.IOException;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
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
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.part.ViewPart;

import rcptask.Activator;
import rcptask.Student;
import rcptask.action.AddAction;
import rcptask.action.DeleteAction;
import rcptask.action.OptionAction;
import rcptask.command.OpenCommand;
import rcptask.editor.StudentEditor;
import rcptask.editor.StudentEditorInput;
import rcptask.utils.CSVReader;

public class NavigationView extends ViewPart {
	public static final String ID = "rcptask.viewpac.navigationView";
	private TreeViewer viewer;
	private String path = "C:\\Users\\Illia\\git\\RCPTask\\RCPTask\\Folder";

	public void createPartControl(Composite parent) {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		File file = new File(path);
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new DelegatingStyledCellLabelProvider(new ViewLabelProvider()));

		addContextMenu();

		DragSource ds = new DragSource(viewer.getTree(), DND.DROP_MOVE);
		ds.setTransfer(new Transfer[] { TextTransfer.getInstance() });
		ds.addDragListener(new DragSourceAdapter() {
			@Override
			public void dragSetData(DragSourceEvent event) {
				IStructuredSelection selection = viewer.getStructuredSelection();
				File file = (File) selection.getFirstElement();
				if (file.isDirectory()) {
					event.data = "directory";// TODO
					return;
				}
				event.data = file.getAbsolutePath();
			}
		});

		DropTarget dt = new DropTarget(window.getShell(), DND.DROP_MOVE);
		dt.setTransfer(new Transfer[] { TextTransfer.getInstance() });
		dt.addDropListener(new DropTargetAdapter() {
			@Override
			public void drop(DropTargetEvent event) {
				File fileToRead = new File(event.data.toString());
				Student student = new Student();
				try {
					student = CSVReader.readStudentFromCSV(fileToRead.getAbsolutePath());
				} catch (NumberFormatException | IOException e) {
					// TODO
				} catch (Exception e) {
					e.printStackTrace();
				}
				StudentEditorInput input = new StudentEditorInput(student);
				try {
					window.getActivePage().openEditor(input, StudentEditor.ID);
				} catch (PartInitException e) {
					throw new RuntimeException(e);
				}
			}
		});

		getSite().setSelectionProvider(viewer);
		viewer.setInput(new File(file.getParent()).listFiles());
	}

	private void addContextMenu() {
		MenuManager menuManager = new MenuManager();
		Menu menu = menuManager.createContextMenu(viewer.getTree());
		menuManager.add(new AddAction("Add", "/icons/add.png"));
		menuManager.add(new DeleteAction("Delete", "/icons/bin_closed.png"));
		menuManager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		menuManager.add(new OptionAction(PlatformUI.getWorkbench().getActiveWorkbenchWindow(), "Option 1",
				"/icons/eclipse16.png"));
		menuManager.add(new OptionAction(PlatformUI.getWorkbench().getActiveWorkbenchWindow(), "Option 2",
				"/icons/eclipse16.png"));
		menuManager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));

		MenuManager subManager = new MenuManager("Submenu");
		subManager.add(new OptionAction(PlatformUI.getWorkbench().getActiveWorkbenchWindow(), "Option 3",
				"/icons/eclipse16.png"));
		subManager.add(new OptionAction(PlatformUI.getWorkbench().getActiveWorkbenchWindow(), "Option 4",
				"/icons/eclipse16.png"));
		menuManager.add(subManager);

		viewer.getTree().setMenu(menu);
		getSite().registerContextMenu(menuManager, viewer);
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
			if (file.isDirectory()) {
				return true;
			}
			return false;
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
			// garbage collect system resources
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

}