package dad.planeador.vuelos.utils;

import javafx.stage.Window;

public class WindowsUtils {
	
	private WindowsUtils(){}
	
	public static void centerOnParent(Window child, Window parent) {
		if (child != null && parent != null) {
			child.setX(parent.getX() + parent.getWidth() / 2 - child.getWidth() / 2);
			child.setY(parent.getY() + parent.getHeight() / 2 - child.getHeight() / 2);
		}
	}
}
