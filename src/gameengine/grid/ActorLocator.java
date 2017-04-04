package gameengine.grid;

import gameengine.actors.management.Actor;
import gameengine.grid.interfaces.ActorGrid.ReadableGrid;
import gameengine.grid.interfaces.Identifiers.Grid2D;
import gameengine.grid.interfaces.Identifiers.MovableActor;

public class ActorLocator <T extends Actor<ReadableGrid>> implements MovableActor<T>{
	
	private Grid2D location;
	private T actor;

	public ActorLocator(Grid2D location, T actor){
		this.actor = actor;
		this.location = location;
	}
	

	@Override
	public Grid2D getLocation() {
		return location;
	}

	@Override
	public void setLocation(double x, double y) {
		location = new Dimensions(x, y);
	}


	@Override
	public T getActor() {
		return actor;
	}

}