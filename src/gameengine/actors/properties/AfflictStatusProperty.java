package gameengine.actors.properties;

import gameengine.actors.propertygen.IActProperty;
import gameengine.actors.status.IStatus;
import gameengine.grid.interfaces.ActorGrid.ReadableGrid;

public class AfflictStatusProperty<G extends IStatus> implements IActProperty<ReadableGrid> {

	@Override
	public void action(ReadableGrid grid, Integer actorID) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isOn() {
		// TODO Auto-generated method stub
		return false;
	}

	
	

}
