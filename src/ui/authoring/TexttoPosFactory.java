//This entire file is part of my masterpiece.
//Alex Blumenstock
//This factory is used specifically by BorderPanes, like the DisplayViewEditor.  It allows them to have a comboBoc with the four
//cardinal directions (Top, Buttom, Right, Left), and using reflection, dynamically change the position of a node using only the String
//representaion.  This util class also has a resetCardinalDirections method to clear the borderpane first, to avoid duplication.
//For an invalid input, an error appears telling the user that their string was not defined, although this should never happen for my
//ComboBox implementation
//This class works closely with the DisplayViewmENU in this program, as each are called by the DisplayViewEditor,
//and this factor serves to update the Menu
package ui.authoring;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import javafx.geometry.Orientation;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import ui.authoring.display.DisplayMenu;

public class TexttoPosFactory {
	public static final Map<String, Orientation> ORIENTATION_MAP = getOrientationMap();

	static Map<String, Orientation> getOrientationMap() {
		HashMap<String, Orientation> map = new HashMap<String, Orientation>();
		map.put("Top", Orientation.HORIZONTAL);
		map.put("Bottom", Orientation.HORIZONTAL);
		map.put("Right", Orientation.VERTICAL);
		map.put("Left", Orientation.VERTICAL);
		return map;
	}

	public static void updateMenuPosition(BorderPane pane, String s, DisplayMenu menu) {
		Method[] methods = pane.getClass().getMethods();
		for (Method method : methods) {
			if (method.getName().equals(getString(s))) {
				method.setAccessible(true);
				try {
					method.invoke(pane, menu.getNode());
					menu.getNode().setOrientation(ORIENTATION_MAP.get(s));
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					Alert a=new Alert(AlertType.ERROR);
					a.setContentText("Not a valid Location");
					a.showAndWait();
				}
			}
		}
	}

	public static void resetCardinalDirections(BorderPane pane) {
		pane.setRight(null);
		pane.setLeft(null);
		pane.setTop(null);
		pane.setBottom(null);
	}

	private static String getString(String s) {
		return String.format("set%s", s);
	}
}
