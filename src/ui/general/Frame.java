// This entire file is part of my masterpiece.
// Talha Koc
/*
 * The Frame class allows me the specify the layout properties
 * that I found most useful when adding GUI components to the
 * the screen. Frame contains the xy-position, width, and height
 * variables which define the respective layout properties of a
 * view. Furthermore, getMaxX and getMaxY provide an easy way to
 * position views next to each other.
 */
package ui.general;

/**
 * Purpose is specify the layout of views with ease.
 * Frame is used by UIView to set the layout, height, and width of itself.
 * @author TNK
 *
 */
public class Frame {
	private double x;
	private double y;
	private double height;
	private double width;
	private Frame bounds;
	
	public Frame(double x, double y, double width, double height){
		this.x = x;
		this.y = y;
		this.height = height;
		this.width = width;
		bounds = new Frame(width, height);
	}
	public Frame(double width, double height){
		this.height = height;
		this.width = width;
		this.x = 0;
		this.y = 0;
		bounds = this;
	}
	
	public double getX(){
		return x;
	}
	public double getY(){
		return y;
	}
	public double getWidth(){
		return width;
	}
	public double getHeight(){
		return height;
	}
	public double getMaxX(){
		return x + width;
	}
	public double getMaxY(){
		return y + height;
	}
	
	/**
	 * purpose is to provide a frame object that defines the local layout properties 
	 * @return same frame but with x and y set to 0
	 */
	public Frame toLocalBounds(){
		return bounds;
	}
	
	public String toString(){
		return "x: " + x + "\ty: " + y + "\tw: " + width + "\th" + height;
	}
}
