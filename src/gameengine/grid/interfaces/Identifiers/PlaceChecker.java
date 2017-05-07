package gameengine.grid.interfaces.Identifiers;

public interface PlaceChecker {
	
	/**
	 * @param X the x location to check
	 * @param Y the y location to check
	 * @return true when something can be placed there, false otherwise
	 */
	boolean canBePlaced(double X, double Y);

}
