package rcptask.dirty;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;

import rcptask.viewpac.regexp.RegExp;

public class DirtyUtils {

	public static void registryDirty(DirtyListener listener, Control... controls) {
		if (controls == null) {
			return;
		}

		for (Control control : controls) {

			if (control instanceof Text) {
				Text text = (Text) control;
				text.addModifyListener(new CustomModifyListenerForText(listener));
			}
			else {
				throw new UnsupportedOperationException("Not support for " + control.getClass().getSimpleName());
			}
		}
	}

	private static class CustomModifyListenerForText implements ModifyListener {

		private DirtyListener listener;
		private boolean isMsgWasShown;

		public CustomModifyListenerForText(DirtyListener listener) {
			this.listener = listener;
		}

		@Override
		public void modifyText(ModifyEvent e) {
			Text text = (Text) e.getSource();
			if (isInputDataValid(text.getText(), text.getMessage()) || text.getText().length() == 0) {
				text.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
				isMsgWasShown=false;
			} else {
				text.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
				if (!isMsgWasShown) {
					MessageDialog.openWarning(null, "Warning", "Wrong input!\nСheck the field hint");
					isMsgWasShown = true;
				}
			}
			listener.fireDirty();

		}

		private boolean isInputDataValid(String input, String nameOfTextField) {
			switch (nameOfTextField) {
			case "Name":
				return RegExp.isNameValid(input);
			case "Group":
				return RegExp.isGroupValid(input);
			case "Adress":
				return RegExp.isAdressValid(input);
			case "City":
				return RegExp.isCityValid(input);
			case "Result":
				return RegExp.isResultValid(input);
			default:
				return false;
			}
		}
	}

}