package rcptask;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import rcptask.editor.StudentEditor;
import rcptask.viewpac.NavigationView;
import rcptask.viewpac.StudentView;

public class Perspective implements IPerspectiveFactory {

	@Override	
	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);
		
		layout.addStandaloneView(NavigationView.ID,  false, IPageLayout.LEFT, 0.25f, editorArea);
		IFolderLayout folder = layout.createFolder("students", IPageLayout.TOP, 0.5f, editorArea);
		folder.addPlaceholder(StudentEditor.ID + ":*");
		
		layout.getViewLayout(NavigationView.ID).setCloseable(false);
	}
}
