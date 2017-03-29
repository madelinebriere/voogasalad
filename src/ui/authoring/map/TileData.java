package ui.authoring.map;

import util.Tuple;

/**
 * used by MapData to encode all the needed information from
 * a tile in the map.
 * 
 * @author TNK
 *
 */
public class TileData {
	
	private String imagePath;
	private TileType tileType;
	private Tuple<Integer,Integer> index; 
	
	public TileData(String imagePath,Tuple<Integer,Integer> index, TileType tileType) {
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

	public Tuple<Integer,Integer> getIndex(){
		return index;
	}
	
	public void setImagePath(String path){
		this.imagePath = path;
	}

	public void setIndex(Tuple<Integer, Integer> index) {
		this.index = index;
	}
	
}
