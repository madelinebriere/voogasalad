// This entire file is part of my masterpiece.
// HARI RAJAN
//
// This class allows an actor to be damageable. I believe this class showcases many
// of the concepts we have learned in class including implementing interfaces, 
// generics, streams, consumers, and more. I think the use of generics here is 
// especially noteworthy. I spent a lot of time explaining this in my ANALYSIS.md,
// but the use of generics here allows this property class to gain access to exactly
// the methods it needs. Because the ActorDamageableProperty needs to damage actors,
// it needs access to the ReadAndDamageGrid. 

package gameengine.actors.properties;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;

import gamedata.composition.ActorDamageableData;
import gameengine.actors.propertygen.IActProperty;
import gameengine.grid.interfaces.ActorGrid.ReadAndDamageGrid;
import types.BasicActorType;

public class ActorDamageableProperty<G extends ReadAndDamageGrid> implements IActProperty<G> {

	private Collection<BasicActorType> myTypes;
	private double hitBox;

	public ActorDamageableProperty(ActorDamageableData data) {
		hitBox = data.getMyHitRadius();
		myTypes = new ArrayList<>();
		for (BasicActorType t : data.getMyEnemyTypes()) {
			myTypes.add(t);
		}
	}

	public void action(G grid, Integer actorID) {
		myTypes.stream().forEach(type -> grid.getActorDamagablesInRadius(grid.getLocationOf(actorID).getX(),grid.getLocationOf(actorID).getY(), hitBox, type)
							.forEach((damage, health) -> damageActor(grid, actorID, damage, health)));
	}

	protected void damageActor(G grid, Integer actorID, Consumer<Double> damage, Double health) {
		grid.getMyDamageable(actorID).accept(health);
		damage.accept(health);
	}

	@Override
	public boolean isOn() {
		return true;
	}
}