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
 
   // Will be called before createPartControl
   @Override
   public void init(IEditorSite site, IEditorInput input)
           throws PartInitException {
       // Very Important!!
       setSite(site);
       setInput(input);
   }
 
  
   // When PROP_DIRTY change, this method will be called by the Workbench.
   // If the method returns true, the SAVE button will enabled, else disable.
   @Override
   public boolean isDirty() {
       return this.dirty;
   }
 
   // When changing the data editor, call this method to notify to Workbench.
   protected void setDirty(boolean dirty) {
       if (this.dirty != dirty) {
           this.dirty = dirty;
            
           // Notify PROP_DIRTY changes to Workbench.
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
 
   // Write code in createPartControl2(Composite)
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
 
   public abstract void showData( );
 
   // Create controls in this method.
   protected abstract void createPartControl2(Composite parent);
 
   // If data in Control changed, it fire to Workbench.
   protected abstract Control[] registryDirtyControls();
 
   class DirtyListenerImpl implements DirtyListener {
 
       @Override
       public void fireDirty() {
           // If has any change, fire to Editor.
           AbstractBaseEditor.this.setDirty(true);
       }
 
   }
}
