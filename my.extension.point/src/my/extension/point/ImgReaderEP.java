package my.extension.point;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class ImgReaderEP {
	public ImgReaderEP() {
	}

	public static Image getImage(Display display, String resourcePath) throws Exception {
		try {
			return new Image(display, resourcePath);
		} catch (Exception e) {
			throw e;
		}
		
	}
}
