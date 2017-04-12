package gamedata.composition;

import gamedata.compositiongen.ShootData;
import types.BasicActorType;

public class ShootTargetFarData extends ShootData{
	
	public ShootTargetFarData (Double myRange, Integer fireRate, BasicActorType type, Integer projectile){
		super(myRange, fireRate, type, projectile);
	}
	
}
