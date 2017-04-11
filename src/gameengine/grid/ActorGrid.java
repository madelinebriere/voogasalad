package gameengine.grid;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import gameengine.actors.management.Actor;
import gameengine.actors.propertygen.IActProperty;
import gameengine.grid.classes.ActorLocator;
import gameengine.grid.classes.Coordinates;
import gameengine.grid.interfaces.ActorGrid.MasterGrid;
import gameengine.grid.interfaces.Identifiers.Grid2D;
import gameengine.grid.interfaces.Identifiers.SettableActorLocator;
import gameengine.grid.interfaces.controllergrid.ControllableGrid;
import types.BasicActorType;
import java.util.Observable;

public class ActorGrid extends Observable implements MasterGrid, ControllableGrid{
	
	private Coordinates limits;
	private Collection<SettableActorLocator> actors;
	private Function<Integer, Actor> actorMaker;
	private Map<Integer, Double> frontEndInfo;
	
	public ActorGrid(double maxX, double maxY, Function<Integer, Actor> actorMaker){
		limits = new Coordinates(maxX, maxY);
		actors = new ArrayList<>();
		frontEndInfo = new HashMap<>();
		this.actorMaker = actorMaker;
	}

	@Override
	public void step() {
		actors.forEach(a -> a.getActor().act(this));
		actors = filter(actors, a -> a.getActor().isActive());
		frontEndInfo = actors.stream()
				.map(a -> a.getActor())
				.collect(Collectors.toMap(Actor::getID, a -> a.getPercentHealth()));
		setChanged();
		notifyObservers(frontEndInfo);
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
	
	private Collection<SettableActorLocator> specificActorTypes(BasicActorType type){
		return filter(actors, a -> a.getActor().getType() == type);
	}
	
	private SettableActorLocator getActorFromID(int ID){
		Collection<SettableActorLocator> foundIDs = filter(actors, a-> a.getActor().getID() == ID);
		if(foundIDs.size() != 1) 
			throw new IllegalStateException("found an invalid number of id's ~ lines 75 ActorGrid");
		return foundIDs.iterator().next();
	}

	@Override
	public void move(int ID, double newX, double newY) {
		SettableActorLocator actor = getActorFromID(ID);
		actor.setLocation(newX, newY);
	}
	
	private Collection<SettableActorLocator> getActorsInRadius(double x, double y, double radius, BasicActorType type){
		Collection<SettableActorLocator> filteredTypes = specificActorTypes(type);
		return filter(filteredTypes, a -> distance(a.getLocation().getX(), x, a.getLocation().getY(), y) <= radius);
	}

	@Override
	public Collection<Grid2D> getActorLocationsInRadius(double x, double y, double radius, BasicActorType type) {
		Collection<SettableActorLocator> actorsinRadius = getActorsInRadius(x, y, radius, type);
		return Collections.unmodifiableCollection(map(actorsinRadius, a -> a.getLocation()));
	}
	
	@Override
	public Map<Double, Consumer<Double>> getActorDamagablesInRadius(double x,
			double y, double radius, BasicActorType type) {
		return Collections.unmodifiableMap(getActorsInRadius(x, y, radius, type).stream()
					.collect(Collectors.toMap(
							e -> e.getActor().getRemainingHealth(), e -> e.getActor().applyDamage())));
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
	public void removeActor(int ID) {
		actors = filter(actors, a -> a.getActor().getID() != ID);
	}
	
	@Override
	public void controllerSpawnActor(Actor actor, double startX, double startY){
		addActor(actor, startX, startY);
	}
	
	private void addActor(Actor newActor, double startX, double startY){
		SettableActorLocator movingActor = new ActorLocator(new Coordinates(startX, startY), newActor);
		actors.add(movingActor);
	}

	@Override
	public Consumer<IActProperty<MasterGrid>> actorSpawnActor(Integer actorType, double startX, double startY) {
		Actor newActor = actorMaker.apply(actorType);
		addActor(newActor, startX, startY);
		return newActor.addProperty();
	}

	@Override
	public Consumer<Double> getMyDamageable(int actorID) {
		return getActorFromID(actorID).getActor().applyDamage();
	}

}
