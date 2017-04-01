package ui.authoring.map;

import util.Index;
import util.Tuple;

/**
 * used by MapData to store the information for each individual
 * tile in the map.
 * 
 * @author TNK
 *
 */
public class TileData {
	
	private String imagePath;
	private TileType tileType;
	private Index index; 
	
	public TileData(String imagePath,Index index, TileType tileType) {
		check();
		this.imagePath = imagePath;
		this.tileType = tileType;
		this.index = index;
	}
	
	/**
	 * checks for possible errors in creating the TileData object
	 */
	private void check() {
		// TODO Auto-generated method stub
		
	}
	
	public String getImagePath(){
		return imagePath;
	}
	
	public TileType getTileType(){
		return tileType;
	}

	public Index getIndex(){
		return index;
	}
	
	public void setImagePath(String path){
		this.imagePath = path;
	}
	
	//TODO remove this method if it's unnecessary
	public void setIndex(Index index) {
		this.index = index;
	}
	
}
