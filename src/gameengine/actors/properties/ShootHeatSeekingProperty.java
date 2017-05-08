// This entire file is part of my masterpiece.
// Moses Wayne
//
// NOTE: I promise the line count abides by the rules, I am just a very verbose commenter

/*
 * This class was improved by the use of syntactic sugar from Java, reducing line 
 * count and cluttered up long lines of code and stressing the single 
 * responsibility of this class. Like many of the other refactored classes, this
 * class has only one or two lines of code for its methods. This is useful for 1)
 * readability and 2) compactness to demonstrate the maintenance of single
 * responsibility in the composition.
 */

package gameengine.actors.properties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Consumer;

import gamedata.composition.ShootHeatSeekingData;
import gameengine.actors.propertygen.IActProperty;
import gameengine.actors.propertygen.ShootTargetProperty;
import gameengine.grid.interfaces.ActorGrid.MasterGrid;
import gameengine.grid.interfaces.ActorGrid.ReadAndSpawnGrid;
import gameengine.grid.interfaces.Identifiers.Grid2D;

/**
 * Shoot property class that extends the ShootTargetProperty abstract class to
 * bestow the heat seeking movement property to the actors that are spawned and
 * fired from the actor holding this property
 * 
 * @author Moses Wayne
 *
 * @param <G>
 *            generic grid extending the ReadAndSpawnGrid interface
 */
public class ShootHeatSeekingProperty<G extends ReadAndSpawnGrid> extends ShootTargetProperty<G> {

	public ShootHeatSeekingProperty(ShootHeatSeekingData myData) {
		super(myData);
	}

	/**
	 * Consumer delineating the addition of the heat seeking movement property
	 * to the spawned actor
	 */
	@Override
	protected Consumer<Collection<IActProperty<MasterGrid>>> projectileProperty(Double target, double range,
			double speed) {
		return (list) -> list
				.addAll(Arrays.asList(new MoveWithHeatSeekProperty<MasterGrid>(range, speed, getMyTarget())));
	}

	@Override
	protected Collection<Double> getEnemyToShoot(Collection<Grid2D> points, Grid2D myPos) {
		return (points.size() > 0 ? new ArrayList<>(Arrays.asList(myPos.getX())) : new ArrayList<>());
	}

}
