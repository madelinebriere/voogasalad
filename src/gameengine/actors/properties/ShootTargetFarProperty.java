// This entire file is part of my masterpiece.
// Moses Wayne
//
// NOTE: I promise the line count abides by the rules, I am just a very verbose commenter
/*
 * The rationale for this class is the same as the rationale for the ShootTargetNear
 * property. Line count was reduced and the class now only functions to do one thing:
 * determine which in a list of enemies to shoot.
 */

package gameengine.actors.properties;

import java.util.Collection;
import java.util.Comparator;

import gamedata.composition.ShootTargetFarData;
import gameengine.grid.interfaces.ActorGrid.ReadAndSpawnGrid;
import gameengine.grid.interfaces.Identifiers.Grid2D;
import util.PathUtil;

/**
 * Class extension of the ShootTargetLineProperty that fires an actor toward an
 * enemy unit within a certain range that is the furthest away from the actor
 * holding this property
 * 
 * @author Moses Wayne
 *
 * @param <G>
 *            generic grid extending the ReadAndSpawnGrid
 */
public class ShootTargetFarProperty<G extends ReadAndSpawnGrid> extends ShootTargetDistanceProperty<G> {

	public ShootTargetFarProperty(ShootTargetFarData myData) {
		super(myData);
	}
	
	@Override
	protected Grid2D distancePref(Collection<Grid2D> points, Grid2D myPos) {
		return points.stream().max(Comparator.comparingDouble(point -> PathUtil.getDistance(myPos, point))).get();
	}

}
