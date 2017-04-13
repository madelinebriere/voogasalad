package gamedata.composition;

import gamedata.compositiongen.*;
import types.BasicActorType;


public class DamageableWeakData extends DamageableData{
	public DamageableWeakData(){
		super();
	}

	public DamageableWeakData(Double myPower, Double myHitRadius, BasicActorType... myEnemyTypes) {
		super(myPower, myHitRadius, myEnemyTypes);
	}
	
}
