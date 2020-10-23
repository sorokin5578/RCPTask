package rcptask.command;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Shell;

public class OpenCommand extends AbstractHandler{

	public static final String ID = "rcptask.command.openCommand";
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		DirectoryDialog directoryDialog = new DirectoryDialog(new Shell(), SWT.OPEN);
		directoryDialog.open();//TODO
		return null;
	}

}
