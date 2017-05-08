package ui.authoring.map;

//This entire file is part of my masterpiece.
//Talha Koc - tnk3

/*
 * I chose this class as a concrete implementation of Layer
 * because it shows how seamlessly it can be integrated into the already 
 * existing MapViewController
 */

import java.util.ArrayList;
import java.util.List;
import gamedata.LayerData;
import gamedata.map.PolygonData;
import gameengine.grid.classes.Coordinates;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import ui.authoring.map.layercomponents.Layer;
import ui.authoring.map.layercomponents.UIPolygon;
import ui.general.CustomColors;
import util.Location;

/**
 * Allows the user view and modify LayerData.
 * In terms of frontend, the user is able to draw polygons 
 * by 
 * 
 * @author TNK
 *
 */
public class PolygonLayerView extends Layer {
	
	private EventHandler<MouseEvent> myMouseEvents = e -> {
		if(e.getButton().equals(MouseButton.SECONDARY)){
			mouseSecondary(e);
		}
		else if(e.getEventType().equals(MouseEvent.MOUSE_RELEASED)){
			mouseReleased(e);
		}
		else if(e.getEventType().equals(MouseEvent.MOUSE_MOVED)){
			mouseMoved(e);
		} 
	};
	
	private LayerData myLayerData;
	private UIPolygon myCurrentPolygon;
	private List<UIPolygon> myUIPolygons;
	private Color myColor = CustomColors.AMBER;
	
	public PolygonLayerView(LayerData layerData){
		super();
		myLayerData = layerData;
		myUIPolygons = new ArrayList<>();
		loadLayerData(myLayerData);
	}
	
	/**
	 * Instantiates a UIPolygon for every PolygonData in layerData
	 * @param layerData 
	 */
	private void loadLayerData(LayerData layerData) {
		layerData.getMyPolygons().forEach(data -> {
			UIPolygon polygon = new UIPolygon(data, this);
			this.setPolygonStyle(polygon);
			this.getChildren().add(polygon);
			this.myUIPolygons.add(polygon);
		});
		
	}
	
	private void mouseReleased(MouseEvent e) {
		updateMyData(new Location(e.getX(), e.getY()));
	}
	
	/**
	 * This method is supposed to signify the end of the polygon
	 * making process and finalize it by setting the currentPolygon
	 * to null and adding the polygonData into the layerData.
	 * @param e
	 */
	private void mouseSecondary(MouseEvent e) {
		mouseReleased(e);
		myCurrentPolygon = null;
	}


	/**
	 * updates the current polygon by removing the last two doubles and adding two more
	 * @param e
	 */
	private void mouseMoved(MouseEvent e) {
		if(myCurrentPolygon!=null){
			int size = myCurrentPolygon.getPoints().size();
			myCurrentPolygon.getPoints().remove(size - 2, size);
			myCurrentPolygon.getPoints().addAll(e.getX(), e.getY());
		}
	}
	
	/**
	 * @param loc Coordinate location of new point that is to be added
	 * to the last polygon
	 */
	private void updateMyData(Location loc){
		if (!checkIfPointInBounds(loc)) 
	        return;
	    //Case where user is making a new polygon
		if(myCurrentPolygon == null){
			PolygonData data = new PolygonData(new ArrayList<>());
			this.myLayerData.getMyPolygons().add(data);
			myCurrentPolygon = new UIPolygon(data, this);
			myCurrentPolygon.addPoint(compressPoint(loc), this);
			myCurrentPolygon.getPoints().addAll(loc.getX(), loc.getY());
			setPolygonStyle(myCurrentPolygon);
			getChildren().add(myCurrentPolygon);
			this.myUIPolygons.add(myCurrentPolygon);
		}
		//Case where user is adding a point to an already existing polygon
		else{
			myCurrentPolygon.addPoint(compressPoint(loc), this);;
		}
	}
	

	@Override
	public void clear() {
		getChildren().clear();
		myLayerData.getMyPolygons().clear();
	}


	@Override
	public void undo() {
		UIPolygon p = this.myUIPolygons.remove(myUIPolygons.size() - 1);
		myLayerData.getMyPolygons().remove(p);
		this.getChildren().remove(p);
	}


	@Override
	public void setColor(Color c) {
		myColor = c;
		
	}

	@Override
	public void sizeDidChange() {		
		this.myUIPolygons.forEach(polygon -> {
			polygon.reload(this);
		});		
	}

	@Override
	public void addMouseEvents() {
		this.addEventHandler(MouseEvent.ANY, myMouseEvents);
	}


	@Override
	public void removeMouseEvents() {
		this.removeEventHandler(MouseEvent.ANY, myMouseEvents);
		
	}

	
	//TRIVIAL STUFF
	
	private Coordinates compressPoint(Location e) {
		return new Coordinates(e.getX()/this.getWidth(), e.getY()/this.getHeight());
	}


	private boolean checkIfPointInBounds(Location e){
		return this.intersects(e.getX(), e.getY(), 1, 1);
	}
	
	private void setPolygonStyle(Polygon polygon) {
		polygon.setFill(myColor);
		polygon.setStroke(myColor.darker());
		polygon.setStrokeWidth(3.0);
	}

}
