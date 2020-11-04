package my.extension.point;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class ImgReaderEP {
	private ImgReaderEP() {
	}

	public static Image getImage(Display display, String resourcePath) throws Exception {
		return new Image(display, resourcePath);
	}
}
