// This entire file is part of my masterpiece.
// Moses Wayne
//
// NOTE: I promise the line count abides by the rules, I am just a very verbose commenter
/*
 * This is the abstract class that was extracted from the near and far shoot properties.
 * This is good design because it limits duplicated code and ensures that each property is only
 * taking on a single responsibility at a time. The reduced line count and length reduces clutter
 * and aids readability of the code.
 */
package gameengine.actors.properties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import gamedata.compositiongen.ShootTargetLineData;
import gameengine.grid.interfaces.ActorGrid.ReadAndSpawnGrid;
import gameengine.grid.interfaces.Identifiers.Grid2D;
import util.PathUtil;

/**
 * Abstract class extending the abstract ShootTargetLineProperty that fires
 * toward one target with a specific preference for specific relative distance
 * from the actor
 * 
 * @author Moses Wayne
 *
 * @param <G>
 */
public abstract class ShootTargetDistanceProperty<G extends ReadAndSpawnGrid> extends ShootTargetLineProperty<G> {

	public ShootTargetDistanceProperty(ShootTargetLineData myData) {
		super(myData);
	}

	@Override
	protected Collection<Double> getEnemyToShoot(Collection<Grid2D> points, Grid2D myPos) {
		return (!points.isEmpty() ? Arrays.asList(PathUtil.getAngle(myPos, distancePref(points, myPos)))
				: new ArrayList<>());
	}

	/**
	 * Method to determine the preference of distance
	 * 
	 * @param points
	 *            collection of identified possible targets
	 * @param myPos
	 *            current location of the actor
	 * @return coordinate location to fire toward
	 */
	protected abstract Grid2D distancePref(Collection<Grid2D> points, Grid2D myPos);

}
