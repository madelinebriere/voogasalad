package gameengine.actors.properties.damage;

import java.util.Collection;

import gamedata.composition.BaseDamageData;
import gameengine.actors.propertygen.IActProperty;
import gameengine.grid.interfaces.ActorGrid.ReadAndDamageGrid;
import types.BasicActorType;

/**
 * Property
 * 
 * @author Moses Wayne
 *
 * @param <G> grid implementation that extends the ReadAndDamageGrid
 */
public class BaseDamageProperty<G extends ReadAndDamageGrid> implements IActProperty<G> {

	private double myRadius;
	private Collection<BasicActorType> myTargets;

	public BaseDamageProperty(BaseDamageData data) {
		myRadius = data.getMyRadius();
		myTargets = data.getMyTarget();
	}

	@Override
	public void action(G grid, Integer actorID) {
		myTargets.stream().forEach(target -> grid.getActorDamagablesInRadius(grid.getLocationOf(actorID).getX(), grid.getLocationOf(actorID).getY(), myRadius, target).forEach((damage,remaining) -> {
			damage.accept(remaining);
			grid.getMyDamageable(actorID).accept(remaining);
				}));
	}

	@Override
	public boolean isOn() {
		return true;
	}

}