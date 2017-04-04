package gameengine.actors.properties;

import java.util.LinkedList;
import java.util.Queue;

import gamedata.composition.MoveWithDestinationData;
import gameengine.grid.classes.Coordinates;
import gameengine.grid.interfaces.ActorGrid.ReadAndMoveGrid;
import gameengine.grid.interfaces.Identifiers.Grid2D;
/**
 * Used in projectiles -- for items that move with a set final
 * location and action at that point
 * 
 * @author maddiebriere
 *
 */

public class MoveWithDestinationProperty implements IActProperty<ReadAndMoveGrid>{
	
	private Queue<Grid2D> myPathCoordinates;
	private Coordinates finalLocation;
	
	public MoveWithDestinationProperty(MoveWithDestinationData data){
		myPathCoordinates = new LinkedList<>(data.getStraightPath());
		finalLocation = data.getFinalLocation();
	}
	
	@Override
	public void action(ReadAndMoveGrid grid, Integer actorID) {
		// TODO Auto-generated method stub
	}

	public Queue<Grid2D> getMyPathCoordinates() {
		return myPathCoordinates;
	}

	public void setMyPathCoordinates(Queue<Grid2D> myPathCoordinates) {
		this.myPathCoordinates = myPathCoordinates;
	}

	public Coordinates getFinalLocation() {
		return finalLocation;
	}

	public void setFinalLocation(Coordinates finalLocation) {
		this.finalLocation = finalLocation;
	}
	
	
	
	

}