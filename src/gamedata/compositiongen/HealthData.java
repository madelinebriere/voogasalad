package gamedata.compositiongen;

import gamedata.ActorData;

public abstract class HealthData implements Data {
	@Override
	public void addData(ActorData actor){
		actor.setHealth(this);
	}
}
