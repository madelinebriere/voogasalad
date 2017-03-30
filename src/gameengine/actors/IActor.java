package gameengine.actors;

import gameengine.ActorResult;

public interface IActor {

	public ActorResult act();
	
	public boolean isActive();
	
	public double getHealth();
	
	public void setHealth(double health);
	
	public void dies();
	
	public boolean hasDied();
}
