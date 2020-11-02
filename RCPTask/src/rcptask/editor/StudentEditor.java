package rcptask.editor;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IReusableEditor;
import org.eclipse.ui.IWorkbenchPartConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import rcptask.dirty.MyConstants;
import rcptask.entity.Student;
import rcptask.utils.CSVWriter;
import rcptask.utils.ImgReader2;
import rcptask.viewpac.NavigationView;

public class StudentEditor extends AbstractBaseEditor implements IReusableEditor {

	public static final String ID = "rcptask.editor.studentEditor";
	private static final String TOOL_TIP_TXT = "Click here to load your image";

	private Text nameText;
	private Text groupText;
	private Text adressText;
	private Text cityText;
	private Text resulText;
	private Text pathText;
	private Label imgLabel;

	@Override
	public void createPartControl2(Composite parent) {
		Composite top = new Composite(parent, SWT.NONE);
		top.setLayout(new GridLayout(2, true));
		top.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		//
		// Text part
		//
		Composite textComposite = new Composite(top, SWT.NONE);
		textComposite.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		textComposite.setLayout(new GridLayout(3, false));

		initNewLabel(textComposite, "Name");
		nameText = initNewText(textComposite, "Name", "Insert name", "Enter one or more words");

		initNewLabel(textComposite, "Group");
		groupText = initNewText(textComposite, "Group", "Insert group", "Enter one number");

		initNewLabel(textComposite, "Adress");
		adressText = initNewText(textComposite, "Adress", "Insert adress", "Enter the address using words and numbers");

		initNewLabel(textComposite, "City");
		cityText = initNewText(textComposite, "City", "Insert city", "Enter one or more words");

		initNewLabel(textComposite, "Result");
		resulText = initNewText(textComposite, "Result", "Insert result", "Enter one number from 1 to 5");

		//
		// End Text part
		//

		//
		// Image part
		//

		Composite imgComposite = new Composite(top, SWT.NONE);
		imgComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		imgComposite.setLayout(new GridLayout(1, true));
		imgComposite.setToolTipText(TOOL_TIP_TXT);
		imgComposite.addMouseListener(new CustomMouseListener());

		imgLabel = new Label(imgComposite, SWT.NONE);
		imgLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1));
		imgLabel.setText(TOOL_TIP_TXT);
		imgLabel.setToolTipText(TOOL_TIP_TXT);

		pathText = new Text(imgComposite, SWT.NONE);
		pathText.setVisible(false);

		imgLabel.addMouseListener(new CustomMouseListener());
		//
		// End Image part
		//
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		NavigationView navigationView = (NavigationView) window.getActivePage().findView(NavigationView.ID);
		if (navigationView.getPath() != null) {
			if (checkDataValid()) {
				Student newStudent = new Student(nameText.getText(), Integer.parseInt(groupText.getText()),
						adressText.getText(), cityText.getText(), Integer.parseInt(resulText.getText()),
						pathText.getText());
				Student oldStudent = ((StudentEditorInput) getEditorInput()).getStudent();
				if (CSVWriter.writeCSVInFile(newStudent, oldStudent, navigationView.getPath())) {
					navigationView.refreshTree();
					this.setInput(new StudentEditorInput(newStudent));
					this.setDirty(false);
					this.firePropertyChange(MyConstants.EDITOR_DATA_CHANGED);
					setPartName(newStudent.getName());
				}
			} else {
				String msg = "All fields must be filled. Changes will not be saved!\nСontinue?";
				if (!MessageDialog.openConfirm(window.getShell(), "Warning", msg)) {
					monitor.setCanceled(true);
				}
			}

		} else {
			String msg = "Groups folder is not open. Changes will not be saved!\nСontinue?";
			if (!MessageDialog.openConfirm(window.getShell(), "Warning", msg)) {
				monitor.setCanceled(true);
			}
		}
	}

	@Override
	protected Control[] registryDirtyControls() {
		return new Control[] { this.nameText, this.groupText, this.adressText, this.cityText, this.resulText,
				this.pathText };
	}

	@Override
	public void showData() {
		StudentEditorInput input = (StudentEditorInput) getEditorInput();
		Student student = input.getStudent();
		String name = student.getName();
		setPartName(name == null ? "New Student" : name);

		nameText.setText(name == null ? "" : name);
		groupText.setText(student.getName() == null ? "" : String.valueOf(student.getGroup()));
		adressText.setText(student.getAdress() == null ? "" : student.getAdress());
		cityText.setText(student.getCity() == null ? "" : student.getCity());
		resulText.setText(student.getName() == null ? "" : String.valueOf(student.getResult()));
		pathText.setText(student.getImgPath() == null ? "" : student.getImgPath());
		if (student.getImgPath() != null) {
			try {
				imgLabel.setImage(ImgReader2.getImg(null, student.getImgPath()));
				pathText.setText(student.getImgPath());
				setFocus();
			} catch (Exception e) {
				imgLabel.setText("Can not find your image");
				pathText.setText("");
			}
		}
		this.setDirty(false);
	}

	@Override
	public void setInput(IEditorInput input) {
		super.setInput(input);
		firePropertyChange(IWorkbenchPartConstants.PROP_INPUT);
	}

	private Label initNewLabel(Composite parent, String text) {
		Label label = new Label(parent, SWT.NONE);
		label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, true, 1, 1));
		label.setText(text);
		return label;
	}

	private Text initNewText(Composite parent, String msg, String toolTip, String descriptionTipText) {
		Text text = new Text(parent, SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 2, 1));
		createControlDecorationForText(text, descriptionTipText);
		text.setMessage(msg);
		text.setToolTipText(toolTip);
		return text;
	}

	private ControlDecoration createControlDecorationForText(Text text, String descriptionText) {
		ControlDecoration deco = new ControlDecoration(text, SWT.TOP | SWT.LEFT);
		Image image = FieldDecorationRegistry.getDefault().getFieldDecoration(FieldDecorationRegistry.DEC_INFORMATION)
				.getImage();
		deco.setDescriptionText(descriptionText);
		deco.setImage(image);
		deco.setShowOnlyOnFocus(false);
		return deco;
	}

	private boolean checkDataValid() {
		return nameText != null && nameText.getText().length() > 0 && groupText != null
				&& groupText.getText().length() > 0 && adressText != null && adressText.getText().length() > 0
				&& cityText != null && cityText.getText().length() > 0 && resulText != null
				&& resulText.getText().length() > 0;

	}

	class CustomMouseListener extends MouseAdapter {
		@Override
		public void mouseDown(MouseEvent e) {
			FileDialog imgDialog = new FileDialog(new Shell(), SWT.OPEN);
			setFilters(imgDialog);
			String path = imgDialog.open();
			if (path != null) {
				try {
					imgLabel.setImage(ImgReader2.getImg(null, path));
					pathText.setText(path);
					setFocus();
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
	}

}
