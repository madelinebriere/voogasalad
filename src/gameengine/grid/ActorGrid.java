package gameengine.grid;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import gameengine.actors.Base;
import gameengine.actors.Projectile;
import gameengine.actors.Shot;
import gameengine.actors.Troop;
import gameengine.actors.management.Actor;
import gameengine.actors.towers.ATower;
import gameengine.grid.interfaces.ActorGrid.ReadAndMoveGrid;
import gameengine.grid.interfaces.ActorGrid.ReadAndShootGrid;
import gameengine.grid.interfaces.ActorGrid.ReadShootMoveGrid;
import gameengine.grid.interfaces.ActorGrid.ReadableGrid;
import gameengine.grid.interfaces.Identifiers.Grid2D;
import gameengine.grid.interfaces.Identifiers.MovableActor;

public class ActorGrid implements ReadableGrid, ReadAndMoveGrid, ReadAndShootGrid, ReadShootMoveGrid{
	
	private Map<Integer, MovableActor<Shot<ReadableGrid>>> projectileMap;
	private Map<Integer, MovableActor<Troop<ReadableGrid>>> enemyMap;
	private Map<Integer, MovableActor<Base<ReadableGrid>>> baseMap;
	private Map<Integer, MovableActor<ATower<ReadableGrid>>> towerMap;
	private List<Map<Integer, ? extends MovableActor<? extends Actor<ReadableGrid>>>> actorList;
	private Dimensions limits;
	
	public ActorGrid(double maxX, double maxY){
		limits = new Dimensions(maxX, maxY);
		initializeInstances();
	}
	
	private void initializeInstances(){
		projectileMap = new HashMap<>();
		enemyMap = new HashMap<>();
		baseMap = new HashMap<>();
		towerMap = new HashMap<>();
		initializeActorList();
	}
	
	private void initializeActorList(){
		actorList = new ArrayList<>();
		actorList.add(projectileMap);
		actorList.add(enemyMap);
		actorList.add(baseMap);
		actorList.add(towerMap);
	}

	@Override
	public boolean addProjectile(Projectile projectile, double startX, double startY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Collection<Grid2D> getEnemiesInRadius(double x, double y, double radius) {
		Collection<Grid2D> allLocations = getLocationsFromMap(enemyMap);
		List<Grid2D> enemiesInRadius = allLocations.stream()
				.filter(a -> getDistance(a.getX(), x, a.getY(), y) <= radius)
				.collect(Collectors.toCollection(ArrayList::new));
		return Collections.unmodifiableCollection(enemiesInRadius);
	}
	
	private double getDistance(double x1, double x2, double y1, double y2){
		double squaredXDif = Math.pow(x2, 2) - Math.pow(x1, 2);
		double squaredYDif = Math.pow(y2, 2) - Math.pow(y1, 2);
		return Math.pow(squaredXDif + squaredYDif, 0.5);
	}

	@Override
	public Grid2D getLocationOf(double id) {
		Map<Integer, ? extends MovableActor<? extends Actor<ReadableGrid>>> map = findRelevantMap(id);
		return map.get(id).getLocation();
	}
	
	private Map<Integer, ? extends MovableActor<? extends Actor<ReadableGrid>>> findRelevantMap(double id){
		
		for(Map<Integer, ? extends MovableActor<? extends Actor<ReadableGrid>>> map : actorList){
			if(map.keySet().contains(id)){
				return map;
			}
		}
		throw new IllegalArgumentException("Invalid id was called");
	}

	@Override
	public boolean isValidLoc(double x, double y) {
		if(x < 0 || y < 0){
			return false;
		}
		return (x <= limits.getX() && y < limits.getY());
	}

	@Override
	public double getMaxX() {
		return limits.getX();
	}

	@Override
	public double getMaxY() {
		return limits.getY();
	}

	@Override
	public void move(double ID, double newX, double newY) {
		Map<Integer, ? extends MovableActor<? extends Actor<ReadableGrid>>> map = findRelevantMap(ID);
		map.get(ID).setLocation(newX, newY);
	}

	private Collection<Grid2D> getLocationsFromMap(Map<Integer, 
			? extends MovableActor<? extends Actor<ReadableGrid>>> map){
		
		return map.values().stream()
				.map(a -> a.getLocation())
				.collect(Collectors.toCollection(ArrayList::new));
	}

	@Override
	public Collection<Grid2D> getEnemyLocations() {
		return Collections.unmodifiableCollection(getLocationsFromMap(enemyMap));
	}

	@Override
	public Collection<Grid2D> getTowerLocations() {
		return Collections.unmodifiableCollection(getLocationsFromMap(towerMap));
	}

	@Override
	public Collection<Grid2D> getBaseLocations() {
		return Collections.unmodifiableCollection(getLocationsFromMap(baseMap));
	}

	@Override
	public Collection<Grid2D> getProjectileLocations() {
		return Collections.unmodifiableCollection(getLocationsFromMap(projectileMap));
	}

}
