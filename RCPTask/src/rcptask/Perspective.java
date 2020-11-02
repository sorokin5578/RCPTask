package rcptask;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import rcptask.editor.StudentEditor;
import rcptask.viewpac.NavigationView;

public class Perspective implements IPerspectiveFactory {
	
	@Override
	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);
		layout.setFixed(true);

		layout.addStandaloneView(NavigationView.ID, false, IPageLayout.LEFT, 0.25f, editorArea);
		IFolderLayout folder = layout.createFolder("Students", IPageLayout.TOP, 0.5f, editorArea);
		folder.addPlaceholder(StudentEditor.ID + ":*");

		layout.getViewLayout(NavigationView.ID).setCloseable(false);
	}

}
