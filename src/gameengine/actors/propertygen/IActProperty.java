// This entire file is part of my masterpiece.
// HARI RAJAN
//
// This is simply the interface used by many of the properties. I believe this is
// good code design because it shows how to extend the code to add new properties
// and eliminates some duplicate code.

package gameengine.actors.propertygen;

import gameengine.grid.interfaces.ActorGrid.ReadableGrid;

public interface IActProperty<G extends ReadableGrid> extends Property{

	public void action(G grid, Integer actorID);
	
	public boolean isOn();
	
}
