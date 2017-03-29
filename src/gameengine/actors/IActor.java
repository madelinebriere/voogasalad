package gameengine.actors;

import gameengine.ActorResult;

public interface IActor {

	public ActorResult act();
	
	public boolean isActive();
}
