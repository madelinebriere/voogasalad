package ui.authoring.map;

import util.Tuple;

/**
 * holds all the information about the map
 * it contains a 2d-array of the size of the map dimensions
 * where each object encodes the tile type and tile image
 * @author TNK
 *
 */
public class MapData {
	private TileData[][] data;
	
	public MapData(int xDim, int yDim){
		data = new TileData[xDim][yDim];
	}
	
	public void addTileDataAtIndex(TileData tile, int col, int row){
		checkDimensions(col,row);
		data[col][row] = tile;
	}
	
	public TileData getTileAtIndex(int col, int row){
		checkDimensions(col,row);
		return data[col][row];
	}
	
	private void checkDimensions(int col, int row){
		//TODO
	}
}
