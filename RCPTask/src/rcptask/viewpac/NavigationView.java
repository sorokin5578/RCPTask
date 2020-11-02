package rcptask.viewpac;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import rcptask.Activator;
import rcptask.action.AddAction;
import rcptask.action.DeleteAction;
import rcptask.action.OptionAction;

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
		Object[] elements = viewer.getExpandedElements();
		File file = new File(path);
		viewer.setInput(new File(file.getParent()).listFiles());
		viewer.setExpandedElements(elements);
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public TreeViewer getViewer() {
		return viewer;
	}

	public void setViewer(TreeViewer viewer) {
		this.viewer = viewer;
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