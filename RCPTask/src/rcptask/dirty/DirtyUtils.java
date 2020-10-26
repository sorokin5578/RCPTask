package rcptask.dirty;

import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
 
public class DirtyUtils {
 
  public static void registryDirty(DirtyListener listener,
          Control... controls) {
      if (controls == null) {
          return;
      }
 
      for (Control control : controls) {
 
          if (control instanceof Text) {
              Text text = (Text) control;
              text.addVerifyListener(new VerifyListenerImpl(listener));
          }
          // Not support
          else {
              throw new UnsupportedOperationException("Not support for "
                      + control.getClass().getSimpleName());
          }
      }
  }
 
  static class VerifyListenerImpl implements VerifyListener {
      private DirtyListener listener;
 
      public VerifyListenerImpl(DirtyListener listener) {
          this.listener = listener;
      }
 
      @Override
      public void verifyText(VerifyEvent arg0) {
    	  System.out.println("change");
          listener.fireDirty();
      }
 
  }
 
}