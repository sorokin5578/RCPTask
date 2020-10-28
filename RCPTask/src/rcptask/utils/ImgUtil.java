package rcptask.utils;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class ImgUtil {
	private ImgUtil() {
	}

	public static Image getImage(Display display, String resourcePath) {
		try {
			return new Image(display, resourcePath);
		} catch (Exception e) {
			throw e;
		}
	}
}
