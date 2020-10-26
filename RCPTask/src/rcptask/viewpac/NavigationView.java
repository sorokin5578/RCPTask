package rcptask.viewpac;

import java.io.File;

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
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import rcptask.Activator;
import rcptask.Student;
import rcptask.utils.CSVReader;

public class NavigationView extends ViewPart {
	public static final String ID = "rcptask.viewpac.navigationView";
	private TreeViewer viewer;
	private String path = "C:\\Users\\Illia\\git\\RCPTask\\RCPTask\\Folder";

	public void createPartControl(Composite parent) {

		System.err.println("navigation");
		System.err.println(parent);
		File file = new File(path);
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new DelegatingStyledCellLabelProvider(new ViewLabelProvider()));
		viewer.addDoubleClickListener(d -> {
			IStructuredSelection selection = viewer.getStructuredSelection();
			File fileToRead = (File)selection.getFirstElement();
			try {
				Student student = CSVReader.readStudentListFromCSV(fileToRead.getAbsolutePath());
				System.err.println(student);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		viewer.setInput(new File(file.getParent()).listFiles());
		getSite().setSelectionProvider(viewer);
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
				if(file.getName().equals(fileName)) {
					newArr[0]=file;
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
		System.out.println("refresh");
		File file = new File(path);
		viewer.setInput(new File(file.getParent()).listFiles());
	}

}