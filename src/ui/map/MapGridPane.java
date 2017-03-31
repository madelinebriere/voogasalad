package ui.map;

import javafx.scene.layout.GridPane;
import ui.authoring.map.MapData;
import ui.authoring.map.PathTile;
import ui.general.Frame;
import ui.general.Frameable;

public class MapGridPane extends GridPane implements Frameable {
	
	private Frame myFrame;
	
	public MapGridPane(Frame frame, MapData mapData){
		setFrame(frame);
		setMapData(mapData);
	}
	
	public void setTile(PathTile tile){
		this.add(tile, tile.getTileData().getIndex().x, tile.getTileData().getIndex().y, 1, 1);
	}

	//MARK: -Frameable Interface Implementation
	@Override
	public void setFrame(Frame frame) {
		myFrame = frame;
		this.setLayoutX(frame.getX());
		this.setLayoutY(frame.getY());
		this.setPrefWidth(frame.getWidth());
		this.setPrefHeight(frame.getHeight());
	}

	@Override
	public Frame getFrame() {
		return myFrame;
	}
	
	public void setBackgroundColor(){
		//TODO
	}
	
	public void setGridLineVisibility(boolean b){
		//TODO
	}
	
	public void setMapData(MapData data){
		
	}
	
}
