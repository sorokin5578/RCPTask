package rcptask.editor;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import rcptask.dirty.DirtyListener;
import rcptask.dirty.DirtyUtils;
import rcptask.dirty.MyConstants;

public abstract class AbstractBaseEditor extends EditorPart {

	private boolean dirty;

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		setSite(site);
		setInput(input);

	}

	@Override
	public boolean isDirty() {
		return this.dirty;
	}

	protected void setDirty(boolean dirty) {
		if (this.dirty != dirty) {
			this.dirty = dirty;

			this.firePropertyChange(IEditorPart.PROP_DIRTY);
		}
	}

	@Override
	public void doSaveAs() {

	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void setFocus() {

	}

	@Override
	public final void createPartControl(Composite parent) {
		this.createPartControl2(parent);

		this.showData();
		this.setDirty(false);
		this.firePropertyChange(MyConstants.EDITOR_DATA_CHANGED);

		Control[] controls = this.registryDirtyControls();
		DirtyListener listener = new DirtyListenerImpl();
		DirtyUtils.registryDirty(listener, controls);
	}

	public abstract void showData();

	protected abstract void createPartControl2(Composite parent);

	protected abstract Control[] registryDirtyControls();

	class DirtyListenerImpl implements DirtyListener {

		@Override
		public void fireDirty() {
			AbstractBaseEditor.this.setDirty(true);
		}

	}
}
