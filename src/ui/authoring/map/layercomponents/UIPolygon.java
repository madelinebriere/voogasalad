package ui.authoring.map.layercomponents;


import gamedata.map.PolygonData;
import gameengine.grid.classes.Coordinates;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;

/**
 * used by PolygonLayerView to manage individual PolygonData of MapLayerData
 * @author TNK
 *
 */
public class UIPolygon extends Polygon{
	
	private PolygonData myPolygonData;
	
	public UIPolygon(PolygonData polygon, Pane pane){
		myPolygonData = polygon;
		reload(pane);
	}
	
	public void addPoint(Coordinates coordinate, Pane pane){
		myPolygonData.getMyPoints().add(coordinate);
		this.getPoints().add(coordinate.getX()*pane.getWidth());
		this.getPoints().add(coordinate.getY()*pane.getHeight());
	}
	
	public void reload(Pane pane){
		this.getPoints().clear();
		myPolygonData.getMyPoints().forEach(c -> {
			this.getPoints().add(c.getX()*pane.getWidth());
			this.getPoints().add(c.getY()*pane.getHeight());
		});
	}
}
