// This entire file is part of my masterpiece.
// HARI RAJAN
//
// This class allows an actor to be shoot the target nerest to it within its hit
// radius. I think the use of generics here is especially noteworthy. I spent a 
// lot of time explaining this in my ANALYSIS.md, but the use of generics here 
// allows this property class to gain access to exactly the methods it needs. 
// Because the ShootTargetNearProperty needs to spawn projectiles,it needs access
// to the methods from the ReadAndSpawnGrid.  This class also showcases how duplicate
// code can easily be eliminated using abstract super classes.

package gameengine.actors.propertygen;

import java.util.Collection;
import java.util.function.Consumer;

import gamedata.compositiongen.ShootData;
import gameengine.grid.interfaces.ActorGrid.ReadAndSpawnGrid;
import gameengine.grid.interfaces.ActorGrid.MasterGrid;
import gameengine.grid.interfaces.Identifiers.Grid2D;
import types.BasicActorType;

public abstract class ShootTargetProperty<G extends ReadAndSpawnGrid> extends CycleProperty<G>{
	
	private double myRange;
	private BasicActorType myTarget;
	private Integer myProjectile;
	private double mySpeed;
	
	public ShootTargetProperty(ShootData myData) {
		super(myData.getFireRate());
		myRange = myData.getRange();
		myTarget = myData.getTarget();
		myProjectile = myData.getProjectile();
		mySpeed = myData.getSpeed();
	}
	
	@Override
	public void action(G grid, Integer actorID) {
		Collection<Double> dirCoordinates = getEnemyToShoot(grid.getActorLocationsInRadius(grid.getLocationOf(actorID).getX(), grid.getLocationOf(actorID).getY(), myRange, myTarget), grid.getLocationOf(actorID));
		spawnProjectiles(grid, dirCoordinates,grid.getLocationOf(actorID));
	}
	
	protected abstract Collection<Double> getEnemyToShoot(Collection<Grid2D> points, Grid2D myPos);
	
	protected void spawnProjectiles(G grid, Collection<Double> targets, Grid2D myLoc) {
		targets.stream().forEach(target -> {
			grid.actorSpawnActor(myProjectile, myLoc.getX(), myLoc.getY(), projectileProperty(target,myRange,mySpeed));
		});
	}
	
	protected abstract Consumer<Collection<IActProperty<MasterGrid>>> projectileProperty(Double target, double range, double speed);
	
	protected BasicActorType getMyTarget() {
		return myTarget;
	}
}
