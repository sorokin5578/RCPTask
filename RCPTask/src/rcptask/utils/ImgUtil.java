package rcptask.utils;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class ImgUtil {

	public static Image getImage(Display display, String resourcePath) {
		try {
			Image image = new Image(display, resourcePath);
			return image;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
