package ui.authoring.map;

import java.util.Optional;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
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
	private PathTile mySelectedTile; //this is the tile that will be added on mouse click
	
	
	public MapEditorView(int xDim, int yDim, Frame frame){
		super();
		this.setBackground(new Background(new BackgroundFill[] { new BackgroundFill(CustomColors.GREEN, new CornerRadii(3), Insets.EMPTY)}));
		this.setAlignment(Pos.CENTER);
		myDimensions = new Tuple<Integer,Integer>(xDim,yDim);
		myTileHeight = frame.getHeight() / yDim;
		myTileWidth = frame.getWidth() / xDim;
		myMapData = new MapData(xDim, yDim);
		mySelectedTile = new PathTile(
				new TileData(defaultWalkImage, new Tuple<Integer,Integer>(0,0), TileType.WALK), myTileWidth,myTileHeight);
		setFrame(frame);
		setupDefaultMap();
		setupMouseEvents();
	}

	private void setupDefaultMap() {
		for(int i=0; i < myDimensions.x;i++){
			for(int j=0; j < myDimensions.y; j++){
				PathTile tile;
				Tuple<Integer,Integer> index = new Tuple<Integer,Integer>(i,j);
				if(i==0 && j==myDimensions.y/2)//entry point location
					tile = new PathTile(
							new TileData(defaultEntryImage,index,TileType.ENTRY),myTileWidth, myTileHeight);
				
				else if(i == myDimensions.x - 1 && j==myDimensions.y/2) //exit point location
					tile = new PathTile(
							new TileData(defaultExitImage,index, TileType.EXIT),myTileWidth, myTileHeight);
				
				else if(j==myDimensions.y/2)
					tile = new PathTile(
							new TileData(defaultWalkImage,index, TileType.WALK),myTileWidth, myTileHeight);
				
				else //grass
					tile = new PathTile(
							new TileData(defaultBlockImage, index, TileType.BLOCK), myTileWidth, myTileHeight);
				
				setTile(tile);
			}
		}
	}
	
	private void setTile(PathTile tile){
		myMapData.addTileDataAtIndex(tile.getTileData(), tile.getTileData().getIndex().x, tile.getTileData().getIndex().y);
		this.add(tile, tile.getTileData().getIndex().x, tile.getTileData().getIndex().y, 1, 1);
	}
	private void replaceTile(PathTile currentTile, PathTile newTile){
		this.getChildren().remove(currentTile);
		setTile(newTile);
	}

	private void setupMouseEvents(){
		this.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> this.getScene().setCursor(Cursor.CROSSHAIR));
		this.addEventHandler(MouseEvent.MOUSE_EXITED, e -> this.getScene().setCursor(Cursor.DEFAULT));
		this.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
			PathTile tile = findTileAtPoint(e.getX(),e.getY());
			if(tile!= null){
				replaceTile(tile, mySelectedTile.clone(tile.getTileData().getIndex()));
			}
				
		});
	}
	
	private PathTile findTileAtPoint(double x, double y){
		Optional<Node> node = this.getChildren().stream().filter(e -> e.getBoundsInParent().contains(x, y)).findFirst();
        if(node.isPresent()){
        	return (PathTile) node.get();
        }
        System.out.println("not found tile");
        return null;	
	}
	
	public void setBlockToAdd(PathTile tile){
		mySelectedTile = tile;
	}
	
	//MARK: Frameable interface
	
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
