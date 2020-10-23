package rcptask.viewpac;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

import rcptask.utils.ImgUtil;
import rcptask.viewpac.regexp.RegExp;

public class StudentView extends ViewPart {
	public static final String ID = "rcptask.viewpac.view";

	public void createPartControl(Composite parent) {
		Composite top = new Composite(parent, SWT.NONE);
		top.setLayout(new GridLayout(2, true));
		top.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		Composite textComposite = new Composite(top, SWT.NONE);
		textComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		textComposite.setLayout(new GridLayout(3, false));

		initNewLabel(textComposite, "Name");
		initNewText(textComposite, "Name", "Insert name");

		initNewLabel(textComposite, "Group");
		initNewText(textComposite, "Group", "Insert group");

		initNewLabel(textComposite, "Adress");
		initNewText(textComposite, "Adress", "Insert adress");

		initNewLabel(textComposite, "City");
		initNewText(textComposite, "City", "Insert city");

		initNewLabel(textComposite, "Result");
		initNewText(textComposite, "Result", "Insert result");

		Composite imgComposite = new Composite(top, SWT.BORDER);
		imgComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		imgComposite.setLayout(new GridLayout(1, true));
		Label imgLabel = new Label(imgComposite, SWT.NONE);
//		imgLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1));
//		imgLabel.setText("");
//		imgComposite.addMouseListener(new MouseAdapter() {
//			
//			@Override
//			public void mouseUp(MouseEvent e) {
//				imgLabel.setImage(Activator.getImageDescriptor("/icons/folder.png").createImage());
//			}
//		});

		imgLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		imgLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				FileDialog imgDialog = new FileDialog(new Shell(), SWT.OPEN);
				setFilters(imgDialog);
				String path = imgDialog.open();
				if(path!=null) {
					imgLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1));
					imgLabel.setImage(ImgUtil.getImage(null, path));
				}
			}
			public void setFilters(FileDialog dialog) {
				String[] name = { "Файлы PNG (*.png)", "Файлы JPG (*.jpg)" };
				String[] extension = { "*.png" , "*.jpg"};
				dialog.setFilterNames(name);
				dialog.setFilterExtensions(extension);
			}
		});

	}

	private Label initNewLabel(Composite parent, String text) {
		Label label = new Label(parent, SWT.NONE);
		label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, true, 1, 1));
		label.setText(text);
		return label;
	}

	private Text initNewText(Composite parent, String msg, String toolTip) {
		Text text = new Text(parent, SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		text.setMessage(msg);
		text.setToolTipText(toolTip);
		text.addModifyListener(new CustomModifyListenerForText());
		return text;
	}

	private class CustomModifyListenerForText implements ModifyListener {

		@Override
		public void modifyText(ModifyEvent e) {
			Text text = (Text) e.getSource();
			if (isInputDataValid(text.getText(), text.getMessage()) || text.getText().length() == 0) {
				text.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
			} else {
				text.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
//				addButton.setEnabled(false);
			}

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

	@Override
	public void setFocus() {
		// empty
	}
}
