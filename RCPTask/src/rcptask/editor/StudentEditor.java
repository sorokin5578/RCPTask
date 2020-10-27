package rcptask.editor;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IReusableEditor;
import org.eclipse.ui.IWorkbenchPartConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import rcptask.Student;
import rcptask.dirty.DirtyUtils;
import rcptask.dirty.MyConstants;
import rcptask.utils.CSVWriter;
import rcptask.utils.ImgUtil;
import rcptask.viewpac.NavigationView;
import rcptask.viewpac.regexp.RegExp;

public class StudentEditor extends AbstractBaseEditor implements IReusableEditor {

	public static final String ID = "rcptask.editor.studentEditor";

	private Text nameText;
	private Text groupText;
	private Text adressText;
	private Text cityText;
	private Text resulText;
	private Text pathText;
	private Label imgLabel;

	public StudentEditor() {

	}

	@Override
	public void createPartControl2(Composite parent) {
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

		imgLabel = new Label(imgComposite, SWT.NONE);
		imgLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1));
		imgLabel.setText("Click here to load your image");

		pathText = new Text(imgComposite, SWT.NONE);
		pathText.setVisible(false);

		imgLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				FileDialog imgDialog = new FileDialog(new Shell(), SWT.OPEN);
				setFilters(imgDialog);
				String path = imgDialog.open();
				if (path != null) {
					try {
						imgLabel.setImage(ImgUtil.getImage(null, path));
						pathText.setText(path);
					} catch (Exception e1) {
						imgLabel.setText("Can not find your image");
						pathText.setText("");
					}
				}
			}

			public void setFilters(FileDialog dialog) {
				String[] name = { "Файлы PNG (*.png)", "Файлы JPG (*.jpg)" };
				String[] extension = { "*.png", "*.jpg" };
				dialog.setFilterNames(name);
				dialog.setFilterExtensions(extension);
			}
		});

		DirtyListenerImpl dirtyListener = new DirtyListenerImpl();
		DirtyUtils.registryDirty(dirtyListener, this.nameText, this.groupText, this.adressText, this.cityText,
				this.resulText, this.pathText);
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (checkDataValid()) {
			Student student = new Student(nameText.getText(), Integer.parseInt(groupText.getText()),
					adressText.getText(), cityText.getText(), Integer.parseInt(resulText.getText()),
					pathText.getText());

			if (CSVWriter.writeCSVInFile(student)) {
				NavigationView navigationView = (NavigationView) window.getActivePage().findView(NavigationView.ID);
				navigationView.refreshTree();
				this.setInput(new StudentEditorInput(student));
				this.setDirty(false);
				this.firePropertyChange(MyConstants.EDITOR_DATA_CHANGED);
			}
		} else {
			MessageDialog.openInformation(window.getShell(), "Info", "Wrong input!");
		}

	}

	private boolean checkDataValid() {
		return nameText != null && nameText.getText().length() > 0 
				&& groupText != null && groupText.getText().length() > 0 
				&& adressText != null && adressText.getText().length() > 0
				&& cityText != null && cityText.getText().length() > 0 
				&& resulText != null && resulText.getText().length() > 0;

	}

	@Override
	protected Control[] registryDirtyControls() {
		return new Control[] { this.nameText, this.groupText, this.adressText, this.cityText, this.resulText,
				this.pathText };
	}

	@Override
	public void showData() {
		StudentEditorInput input = (StudentEditorInput) this.getEditorInput();
		Student student = input.getStudent();

		nameText.setText(student.getName() == null ? "" : student.getName());
		groupText.setText(student.getName() == null ? "" : String.valueOf(student.getGroup()));
		adressText.setText(student.getAdress() == null ? "" : student.getAdress());
		cityText.setText(student.getCity() == null ? "" : student.getCity());
		resulText.setText(student.getName() == null ? "" : String.valueOf(student.getResult()));
		pathText.setText(student.getImgPath() == null ? "" : student.getImgPath());
		if (student.getImgPath() != null) {
			try {
				imgLabel.setImage(ImgUtil.getImage(null, student.getImgPath()));
				pathText.setText(student.getImgPath());
			} catch (Exception e) {
				imgLabel.setText("Can not find your image");
				pathText.setText("");
			}
		}
		// Clear dirty.
		this.setDirty(false);
	}

	// Override setInput(..) with public (IReusableEditor)
	@Override
	public void setInput(IEditorInput input) {
		super.setInput(input);
		firePropertyChange(IWorkbenchPartConstants.PROP_INPUT);
	}

	public String getDeptInfo() {
		StudentEditorInput input = (StudentEditorInput) this.getEditorInput();
		Student student = input.getStudent();
		if (student == null) {
			return "";
		}
		String info = "information";
		return info;
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
//		text.addModifyListener(new CustomModifyListenerForText());
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
