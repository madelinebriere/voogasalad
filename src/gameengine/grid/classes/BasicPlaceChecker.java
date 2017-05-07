package gameengine.grid.classes;

import gameengine.grid.interfaces.Identifiers.PlaceChecker;
/**
 * 
 * @author Gideon
 *
 * This is a very basic version of a {@link PlaceChecker}
 * 
 * If no PlaceCheker is provided this will
 * be used in {@link ActorGrid} as the default
 */
public class BasicPlaceChecker implements PlaceChecker{

	private double maxX, maxY;
	
	public BasicPlaceChecker(double maxX, double maxY){
		this.maxX = maxX;
		this.maxY = maxY;
	}
	
	/**
	 * Returns whether or not the position is within the boundary
	 * of 0 and the max X's and Y's
	 */
	@Override
	public boolean canBePlaced(double X, double Y) {
		return X >= 0 && Y >= 0 && X <= maxX && Y <= maxY;
	}

}
