package rcptask.editor;

import org.eclipse.core.runtime.IProgressMonitor;
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
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.part.EditorPart;

import rcptask.Student;
import rcptask.utils.CSVWriter;
import rcptask.utils.ImgUtil;
import rcptask.viewpac.regexp.RegExp;

public class StudentEditor extends EditorPart {

	public static final String ID = "rcptask.editor.studentEditor";

	private Text nameText;
	private Text groupText;
	private Text adressText;
	private Text cityText;
	private Text resulText;

	private boolean dirty;

	public StudentEditor() {

	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		Student student = new Student(nameText.getText(), Integer.parseInt(groupText.getText()), adressText.getText(),
				cityText.getText(), Integer.parseInt(resulText.getText()));
		CSVWriter.writeCSVInFile(student);
	}

	@Override
	public void doSaveAs() {

	}

	/**
	 * Important!!!
	 */
	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		if (!(input instanceof StudentEditorInput)) {
			throw new PartInitException("Invalid Input: Must be " + StudentEditorInput.class.getName());
		}
		setSite(site);
		setInput(input);
	}

	@Override
	public boolean isDirty() {
		return true;
	}

	private void setDirty(boolean dirty) {
		System.err.println(dirty);
		this.dirty = dirty;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void createPartControl(Composite parent) {
		Composite top = new Composite(parent, SWT.NONE);
		top.setLayout(new GridLayout(2, true));
		top.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		Composite textComposite = new Composite(top, SWT.NONE);
		textComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		textComposite.setLayout(new GridLayout(3, false));

		initNewLabel(textComposite, "Name");
		nameText = initNewText(textComposite, "Name", "Insert name");

		initNewLabel(textComposite, "Group");
		groupText = initNewText(textComposite, "Group", "Insert group");

		initNewLabel(textComposite, "Adress");
		adressText = initNewText(textComposite, "Adress", "Insert adress");

		initNewLabel(textComposite, "City");
		cityText = initNewText(textComposite, "City", "Insert city");

		initNewLabel(textComposite, "Result");
		resulText = initNewText(textComposite, "Result", "Insert result");

		Composite imgComposite = new Composite(top, SWT.NONE);
		imgComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		imgComposite.setLayout(new GridLayout(1, true));

		Label imgLabel = new Label(imgComposite, SWT.NONE);
		imgLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1));
		imgLabel.setText("Click here to load your image");
		imgLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				FileDialog imgDialog = new FileDialog(new Shell(), SWT.OPEN);
				setFilters(imgDialog);
				String path = imgDialog.open();
				if (path != null) {
					imgLabel.setImage(ImgUtil.getImage(null, path));
				}
			}

			public void setFilters(FileDialog dialog) {
				String[] name = { "Файлы PNG (*.png)", "Файлы JPG (*.jpg)" };
				String[] extension = { "*.png", "*.jpg" };
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
			setDirty(true);
			Text text = (Text) e.getSource();
			if (isInputDataValid(text.getText(), text.getMessage()) || text.getText().length() == 0) {
				text.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
			} else {
				text.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
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

	}

}
