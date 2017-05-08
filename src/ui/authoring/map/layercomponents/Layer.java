package ui.authoring.map.layercomponents;

//This entire file is part of my masterpiece.
//Talha Koc - tnk3

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 * This component is meant to be used by the MapViewController to 
 * provide a graphical view of the associated GameData object (eg: path)
 * and functionality to modify that data.
 * 
 * @author TNK
 *
 */
public abstract class Layer extends Pane{
	
	private boolean isActive = false;
	
	/**
	 * remove all children nodes as well as
	 * data objects associated with those nodes
	 * 
	 */
	public abstract void clear();
	
	/**
	 * remove the last added child node and 
	 * the data object associated with that node
	 * 
	 */
	public abstract void undo();
	
	/**
	 * 
	 * @param c Theme color
	 */
	public abstract void setColor(Color c);
	
	/**
	 * add the mouse event instance variable to self
	 */
	public abstract void addMouseEvents();
	
	/**
	 * remove the mouse event instance variable from self
	 */
	public abstract void removeMouseEvents();
	
	/**
	 * internally update the layout properties of child views 
	 */
	public abstract void sizeDidChange();
	
	/**
	 * 
	 * @return true if is layer is active
	 */
	public boolean isActive(){
		return isActive;
	};

	
	public void activate(){
		isActive = true;
		addMouseEvents();
	};
	public void deactivate(){
		isActive = false;
		removeMouseEvents();
	};
}
