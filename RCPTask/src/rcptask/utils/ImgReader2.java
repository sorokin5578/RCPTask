package rcptask.utils;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import my.extension.point.ImgReaderEP;

public class ImgReader2 extends ImgReaderEP {

	public static Image getImg(Display display, String resourcePath) throws Exception {
		try {
			return getImage(display, resourcePath);
		} catch (Exception e) {
			throw e;
		}
	}
}
