// This entire file is part of my masterpiece.
// Moses Wayne
//
// NOTE: I promise the line count abides by the rules, I am just a very verbose commenter
/*
 * This class was refactored to extend a new ShootTargetDistanceProperty that swaps
 * out the comparator used to discern a viable shootable target. This reduced repeated code 
 * because previously, the streams were functionally equivalent save the min vs max. 
 * By removing duplicated code, this code stays DRY and stresses the single responsibility
 * rule.
 */

package gameengine.actors.properties;

import java.util.Collection;
import java.util.Comparator;

import gamedata.composition.ShootTargetNearData;
import gameengine.grid.interfaces.ActorGrid.ReadAndSpawnGrid;
import gameengine.grid.interfaces.Identifiers.Grid2D;
import util.PathUtil;

/**
 * Class extension of the ShootTargetLineProperty that fires an actor toward an
 * enemy unit within a certain range that is the closest to the actor holding
 * this property
 * 
 * @author Moses Wayne
 *
 * @param <G> generic grid extending the ReadAndSpawnGrid
 */
public class ShootTargetNearProperty<G extends ReadAndSpawnGrid> extends ShootTargetDistanceProperty<G> {

	public ShootTargetNearProperty(ShootTargetNearData myData) {
		super(myData);
	}

	@Override
	protected Grid2D distancePref(Collection<Grid2D> points, Grid2D myPos) {
		return points.stream().min(Comparator.comparingDouble(point -> PathUtil.getDistance(myPos, point))).get();
	}

}
