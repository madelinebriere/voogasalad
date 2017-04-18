package gameengine.actors.properties;

import java.util.ArrayList;
import java.util.Collection;

import gameengine.actors.propertygen.IActProperty;
import gameengine.grid.interfaces.ActorGrid.ReadAndDamageGrid;
import types.BasicActorType;

public class ActorDamageableProperty<G extends ReadAndDamageGrid> implements IActProperty<G> {

	private Collection<BasicActorType> myTypes;
	private double hitBox;

	public ActorDamageableProperty(Double radius, BasicActorType... types) {
		hitBox = radius;
		myTypes = new ArrayList<>();
		for (BasicActorType t : types) {
			myTypes.add(t);
		}
	}

	public void action(G grid, Integer actorID) {
		myTypes.stream()
				.forEach(type -> grid
						.getActorDamagablesInRadius(grid.getLocationOf(actorID).getX(),
								grid.getLocationOf(actorID).getX(), hitBox, type)
						.forEach((health, damage) -> {
							grid.getMyDamageable(actorID).accept(health);
							damage.accept(health);
						}));
	}

	@Override
	public boolean isOn() {
		return true;
	}

}