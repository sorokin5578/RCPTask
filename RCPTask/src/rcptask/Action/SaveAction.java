package rcptask.Action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;

import rcptask.Activator;
import rcptask.ICommandIds;

public class SaveAction extends Action{
	private final IWorkbenchWindow window;
	private final String viewId;
	
	public SaveAction(IWorkbenchWindow window, String label, String viewId) {
		this.window = window;
		this.viewId = viewId;
        setText(label);
		setId(ICommandIds.CMD_SAVE);
//		setActionDefinitionId(ICommandIds.CMD_OPEN);
		setImageDescriptor(Activator.getImageDescriptor("/icons/disk.png"));
	}
	
	@Override
	public void run() {
		if(window != null) {	
			try {
				MessageDialog.openInformation(window.getShell(), "Confirm", "SAVE");//TODO
			} catch (Exception e) {
				MessageDialog.openError(window.getShell(), "Error", "Error opening view:" + e.getMessage());
			}
		}
	}
	
//	@Execute
//	public void execute(@Named(IServiceConstants.ACTIVE_SHELL) Shell shell, MPart part){
//		if (part==null) {
//			return;
//		}
//		EditorPart editor = (EditorPart) part.getObject();		
//		FileDialog dialog = new FileDialog(shell);		
//		String fileName = dialog.open();
//		if (fileName!= null) {
//			try {
//				FileWriter writer = new FileWriter(fileName);
//				writer.write(editor.styledText.getText());
//				writer.close();
//			}
//			catch(IOException e) {
//	            MessageDialog.openError(shell, "Error saving file", "File " + fileName + " could not be saved." );
//
//			}
//		}
//	}
}
