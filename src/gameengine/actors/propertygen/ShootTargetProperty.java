// This entire file is part of my masterpiece.
// Moses Wayne
//
// NOTE: I promise the line count abides by the rules, I am just a very verbose commenter

/* The entire shoot property hierarchy is my masterpiece for VOOGASalad. I believe 
 * the class was already well designed, and the additions I made improved the 
 * inheritance structure, readability, and compactness of the class hierarchy. In this
 * class, I added a protected helper method to provide the position of the actor 
 * holding this property to reduce clutter when calling the method in the grid. I
 * also refactored parameter lines to reduce the number of parameters needed by passing 
 * objects that encompass multiple objects.
 * 
 */

package gameengine.actors.propertygen;

import java.util.Collection;
import java.util.function.Consumer;

import gamedata.compositiongen.ShootData;
import gameengine.grid.interfaces.ActorGrid.ReadAndSpawnGrid;
import gameengine.grid.interfaces.ActorGrid.MasterGrid;
import gameengine.grid.interfaces.Identifiers.Grid2D;
import types.BasicActorType;

/**
 * Abstract class extending the CycleProperty class that outlines the basic
 * functionality for a property that "shoots" or spawns any kind of actor. Due
 * to the extension of the CycleProperty abstract class, this class is designed
 * to operate on intervals or cycles. The ShotoProperty abstract class handles
 * all main action and spawning of additional actors, relying on subclasses of
 * this class to dictate where to fire, what to fire, and how to fire the actor
 * that is spawned.
 * 
 * @author Moses Wayne
 *
 * @param <G>
 *            generic grid that is used by this class and subclasses depending
 *            on access rights required. Since this abstract class makes use of
 *            spawning actors, it requires the functionality of the
 *            ReadAndSpawnGrid
 */
public abstract class ShootTargetProperty<G extends ReadAndSpawnGrid> extends CycleProperty<G> {

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

	/**
	 * Performs the action of spawning projectiles based on the determined
	 * enemy(ies) to shoot
	 * 
	 * @see IActProperty#action(gameengine.grid.interfaces.ActorGrid.ReadableGrid,
	 *      Integer)
	 */
	@Override
	public void action(G grid, Integer actorID) {
		spawnProjectiles(grid, getEnemyToShoot(grid.getActorLocationsInRadius(getPos(grid, actorID), myRange, myTarget),
				getPos(grid, actorID)), actorID);
	}

	/**
	 * Abstract method that is used to determine where this property should fire
	 * 
	 * @param points
	 *            locations of enemy actors based on the grid's locations in
	 *            radius function
	 * @param myPos
	 *            Grid2D location of the actor holding this property
	 * @return collection of targets to fire at
	 */
	protected abstract Collection<Double> getEnemyToShoot(Collection<Grid2D> points, Grid2D pos);

	/**
	 * Method to spawn actors (typically projectiles, but functionally
	 * unlimited) onto the screen with added properties based on subclasses.
	 * 
	 * @param grid
	 *            grid object used to interface with the rest of the gameengine
	 * @param targets
	 *            angles represented as doubles of targets
	 * @param id
	 *            unique Integer representation of the actor
	 */
	protected void spawnProjectiles(G grid, Collection<Double> targets, Integer id) {
		targets.stream().forEach(target -> {
			grid.actorSpawnActor(myProjectile, getPos(grid, id), projectileProperty(target, myRange, mySpeed));
		});
	}

	/**
	 * Consumer used to alter the properties of the projectile that is spawned.
	 * In this case, it is used to add a particular property determined by
	 * extending subclasses
	 * 
	 * @param target
	 *            double representation of the angle from the actor of the
	 *            location of the enemy target
	 * @param range
	 *            double representation of the maximum distance to which this
	 *            property can fire
	 * @param speed
	 *            double representing the speed that this property may give to
	 *            its spawned actors
	 * @return a Consumer to modify the properties of the actor to be spawned in
	 *         the game grid
	 */
	protected abstract Consumer<Collection<IActProperty<MasterGrid>>> projectileProperty(Double target, double range,
			double speed);

	/**
	 * Method used to determine the target BasicActorType of this property. Used
	 * by extending subclasses
	 * 
	 * @return BasicActorType of the target for this ShootProperty
	 */
	protected BasicActorType getMyTarget() {
		return myTarget;
	}

	/**
	 * Helper method to reduce clutter from constant calls to the grid
	 * 
	 * @param grid
	 *            grid used to determine Grid2D location
	 * @param actorID
	 *            unique Integer identifier
	 * @return current position of MainActor holding this property
	 */
	protected Grid2D getPos(G grid, Integer actorID) {
		return grid.getLocationOf(actorID);
	}
}
