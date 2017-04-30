package gameengine.grid.testers;

import java.util.Collection;
import java.util.function.Consumer;

import gameengine.actors.management.Actor;
import gameengine.actors.propertygen.IActProperty;
import gameengine.grid.interfaces.ActorGrid.MasterGrid;
import gameengine.grid.interfaces.Identifiers.Grid2D;
import types.BasicActorType;

public class TestActor implements Actor{
	private boolean alive;
	private int ID;
	private BasicActorType type;
	
	public TestActor(int ID){
		alive = true;
		this.ID = ID;
		type = new BasicActorType("troop");
	}

	@Override
	public void act(MasterGrid grid) {
		Grid2D loc = grid.getLocationOf(ID);
		grid.move(ID, loc.getX() + 2, loc.getY() - 5);
	}

	@Override
	public boolean isActive() {
		return alive;
	}

	@Override
	public Consumer<Double> applyDamage() {
		return null;
	}

	@Override
	public Integer getID() {
		return ID;
	}

	@Override
	public BasicActorType getType() {
		return type;
	}

	@Override
	public Integer getMyOption() {
		return -1;
	}

	@Override
	public double getRemainingHealth() {
		return 2.5;
	}

	@Override
	public double getPercentHealth() {
		return 0.1;
	}

	@Override
	public void exit(MasterGrid grid) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void changeExit(Consumer<Collection<IActProperty<MasterGrid>>> action) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addProperty(Consumer<Collection<IActProperty<MasterGrid>>> function) {
		// TODO Auto-generated method stub
		
	}

}
