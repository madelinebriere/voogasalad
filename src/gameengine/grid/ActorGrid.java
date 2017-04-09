package gameengine.grid;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import gameengine.actors.management.Actor;
import gameengine.grid.classes.ActorLocator;
import gameengine.grid.classes.Coordinates;
import gameengine.grid.interfaces.ActorGrid.MasterGrid;
import gameengine.grid.interfaces.ActorGrid.ReadAndMoveGrid;
import gameengine.grid.interfaces.ActorGrid.ReadAndSpawnGrid;
import gameengine.grid.interfaces.ActorGrid.ReadShootMoveGrid;
import gameengine.grid.interfaces.ActorGrid.ReadableGrid;
import gameengine.grid.interfaces.Identifiers.Grid2D;
import gameengine.grid.interfaces.Identifiers.MovableActor;
import gameengine.grid.interfaces.controllergrid.ControllableGrid;
import gameengine.grid.interfaces.controllergrid.SteppableGrid;
import types.BasicActorType;

public class ActorGrid implements ReadableGrid, MasterGrid,
	ReadAndMoveGrid, ReadAndSpawnGrid, ReadShootMoveGrid, ControllableGrid, SteppableGrid{
	
	private Coordinates limits;
	private Collection<MovableActor> actors;
	
	public ActorGrid(double maxX, double maxY){
		limits = new Coordinates(maxX, maxY);
		actors = new ArrayList<>();
	}

	@Override
	public void step() {
		actors.forEach(a -> a.getActor().act(this));
		actors = filter(actors, a -> a.getActor().isActive());
	}

	
	private <T> Collection<T> filter(Collection<T> items, Predicate<T> predicate){
		return items.stream()
				.filter(t -> predicate.test(t))
				.collect(Collectors.toList());
	}
	
	private <I,O> Collection<O> map(Collection<I> items, Function<I,O> function){
		return items.stream()
				.map(i -> function.apply(i))
				.collect(Collectors.toList());
	}
	
	private double distance(double x1, double x2, double y1, double y2){
		double xDifSquared = Math.pow(x2-x1, 2);
		double yDifSquared = Math.pow(y2-y1, 2);
		return Math.pow(xDifSquared + yDifSquared, 0.5);
	}
	
	private Collection<MovableActor> specificActorTypes(BasicActorType type){
		return filter(actors, a -> a.getActor().getType() == type);
	}
	
	private MovableActor getActorFromID(int ID){
		Collection<MovableActor> foundIDs = filter(actors, a-> a.getActor().getID() == ID);
		if(foundIDs.size() != 1) 
			throw new IllegalStateException("found an invalid number of id's ~ lines 75 ActorGrid");
		return foundIDs.iterator().next();
	}

	@Override
	public void move(int ID, double newX, double newY) {
		MovableActor actor = getActorFromID(ID);
		actor.setLocation(newX, newY);
	}

	@Override
	public Collection<Grid2D> getActorLocationsInRadius(double x, double y, double radius, BasicActorType type) {
		Collection<MovableActor> filteredTypes = specificActorTypes(type);
		Collection<MovableActor> filteredLoc = filter(filteredTypes,
				a -> distance(a.getLocation().getX(), x, a.getLocation().getY(), y) <= radius);
		return Collections.unmodifiableCollection(map(filteredLoc, a -> a.getLocation()));
	}

	@Override
	public Grid2D getLocationOf(int ID) {
		return getActorFromID(ID).getLocation();
	}

	@Override
	public Collection<Grid2D> getActorLocations(BasicActorType type) {
		return Collections.unmodifiableCollection(map(specificActorTypes(type), a -> a.getLocation()));
	}

	@Override
	public boolean isValidLoc(double x, double y) {
		return x >= 0 && y >= 0 && x <= getMaxX() && y <= getMaxY();
	}

	@Override
	public double getMaxX() {
		return limits.getY();
	}

	@Override
	public double getMaxY() {
		return limits.getY();
	}

	@Override
	public void spawn(Actor newActor, double startX, double startY) {
		Grid2D location = new Coordinates(startX, startY);
		MovableActor actor = new ActorLocator(location, newActor);
		actors.add(actor);
	}

}
