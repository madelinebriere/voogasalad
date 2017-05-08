package ui.authoring.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import gamedata.MapLayersData;
import gamedata.PathData;
import gameengine.grid.classes.Coordinates;
import gameengine.grid.interfaces.Identifiers.Grid2D;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import ui.authoring.map.layercomponents.Layer;
import ui.authoring.map.layercomponents.PointType;
import ui.authoring.map.layercomponents.UIPath;
import ui.authoring.map.layercomponents.UIPoint;
import ui.general.CustomColors;
import ui.general.ImageViewPane;
import util.Location;
import util.Tuple;

/**
 * Allows the user view and modify PathData
 * 
 * @author TNK
 *
 */
public class PathLayerView extends Layer {

	private boolean isActive = false;
	private boolean isLoaded = false;
	private PathData myPathData;
	private boolean isFirstPoint = true;
	private List<UIPath> myUIPath;
	private UIPath myCurrentPath;
	private EventHandler<MouseEvent> myEvent = e -> handleMouseRelease(e);
	
	public PathLayerView(PathData data) {
		super();
		myPathData = data;
		myUIPath = new ArrayList<>();
		addMouseEvents();
		widthProperty().addListener(e -> sizeDidChange());
		heightProperty().addListener(e -> sizeDidChange());
	}
	
	@Override
	protected void layoutChildren(){
		super.layoutChildren();
		if(!isLoaded)
			didFinishLoading();

	}

	private void didFinishLoading() {
		isLoaded = true;
		loadPathData(myPathData);
	}

	private void handleMouseRelease(MouseEvent e) {
		e.consume();
		addPointToMap(e);
	}

	// TODO refactor
	private void addPointToMap(MouseEvent e) {
		
		Coordinates coordinate = new Coordinates(e.getX()/this.getWidth(), e.getY()/this.getHeight());
		if (!coordinate.isValid())
			return;
		if(myCurrentPath == null){
			myCurrentPath = new UIPath();
			myPathData.addPath(new ArrayList<>());
			myUIPath.add(myCurrentPath);
		}

		
		UIPoint p = new UIPoint(coordinate, e.getX(), e.getY());
		myCurrentPath.addPointAndMouseDraggableLine(p, this);
		List<Grid2D> listOfPoints = myPathData.poll();
		listOfPoints.add(coordinate);

		// determines if the point is exit, entry, or regular path
		if (!isFirstPoint && e.getButton().equals(MouseButton.SECONDARY)) {// exit																// path
			isFirstPoint = true;
			p.setPointType(PointType.EXIT);
			setOnMouseMoved(i -> {});
			setOnMouseDragged(i -> {});
			myCurrentPath.finishPath(this);
			myCurrentPath = null;
		} 
		else if (isFirstPoint) {
			p.setPointType(PointType.ENTRY);
			isFirstPoint = false;
		} 
		else {
			p.setPointType(PointType.PATH);
		}
		
		/*
		 * printing map data
		 */


	}


	@Override
	public void clear() {
		this.getChildren().clear();
		myPathData.clear();
		myUIPath.clear();
	}

	@Override
	public void undo() {
		UIPath path = this.myUIPath.remove(myUIPath.size() - 1);
		getChildren().removeAll(path.getLines());
		getChildren().removeAll(path.getPoints());
	}

	@Override
	public void setColor(Color c) {
		//nothing
	}

	@Override
	public boolean isActive() {
		return isActive;
	}
	
	
	@Override
	public void sizeDidChange() {
		this.myUIPath.forEach(path -> path.reload(this));
	}

	
	private void loadPathData(PathData pathData){
		pathData.getMyPaths().forEach((i,list) -> {
			addPathToSelf(list);
		});
	}
	
	private void addPathToSelf(List<Grid2D> points){
		this.myUIPath.add(new UIPath(points, this));
	}

	@Override
	public void addMouseEvents() {
		addEventHandler(MouseEvent.MOUSE_RELEASED, myEvent);
		
	}

	@Override
	public void removeMouseEvents() {
		removeEventHandler(MouseEvent.MOUSE_RELEASED, myEvent);
		
	}


}
