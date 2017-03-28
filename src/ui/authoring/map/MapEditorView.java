package ui.authoring.map;

import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import ui.general.CustomColors;
import ui.general.Frame;
import ui.general.Frameable;
import util.Tuple;

public class MapEditorView extends GridPane implements Frameable{
	
	private Tuple<Integer, Integer> myDimensions;
	private String defaultBlockImage = "grass.png";
	private String defaultWalkImage = "dirt.png";
	private String defaultEntryImage = "default_entry.png";
	private String defaultExitImage = "default_exit.png";
	private double myTileHeight;
	private double myTileWidth;
	private Frame myFrame;
	private MapData myMapData;
	
	public MapEditorView(double width, double height, Frame frame){
		super();
		this.setBackground(new Background(new BackgroundFill[] { new BackgroundFill(CustomColors.GREEN, new CornerRadii(3), Insets.EMPTY)}));
		myDimensions = new Tuple<Integer,Integer>((int)width,(int) height);
		myTileHeight = frame.getHeight() / height;
		myTileWidth = frame.getWidth() / width;
		myMapData = new MapData((int)width, (int)height);
		setFrame(frame);
		setupDefaultMap();
	}

	private void setupDefaultMap() {
		for(int i=0; i < myDimensions.x;i++){
			for(int j=0; j < myDimensions.y; j++){
				PathTile tile;
				if(i==0 && j==myDimensions.y/2)//entry point location
					tile = new PathTileEntry(defaultEntryImage,myTileWidth, myTileHeight);
				else if(i == myDimensions.x - 1 && j==myDimensions.y/2)
					tile = new PathTileExit(defaultExitImage,myTileWidth, myTileHeight);
				else if(j==myDimensions.y/2) //exit point location
					tile = new PathTileWalkable(defaultWalkImage,myTileWidth, myTileHeight);
				else //straight path between entry and exit (horizontal)
					tile = new PathTileBlock(defaultBlockImage, myTileWidth, myTileHeight);
				addTileToMap(tile, i, j);
			}
		}
		TileData d = myMapData.getTileAtIndex(5, 5); 
		System.out.println(d.getClassName()+"\n"+d.getImagePath());
	}
	
	private void addTileToMap(PathTile tile, int col, int row){
		myMapData.addTileDataAtIndex(new TileData(tile), col, row);
		this.add(tile, col, row, 1, 1);
	}

	public void setFrame(Frame frame){
		myFrame = frame;
		this.setLayoutX(frame.getX());
		this.setLayoutY(frame.getY());
		this.setPrefWidth(frame.getWidth());
		this.setPrefHeight(frame.getHeight());
	}

	public Frame getFrame() {
		return myFrame;
	}
	public Frame getBounds(){
		return myFrame;
	}
}
