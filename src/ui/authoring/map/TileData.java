package ui.authoring.map;

/**
 * used by MapData to encode all the needed information from
 * a tile in the map.
 * 
 * @author TNK
 *
 */
public class TileData {
	
	private String imagePath;
	private String pathTileClassName;
	
	public TileData(PathTile tile) {
		check();
		imagePath = tile.getImage();
		pathTileClassName = tile.getClass().getName();
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
	
	public String getClassName(){
		return pathTileClassName;
	}

	
}
