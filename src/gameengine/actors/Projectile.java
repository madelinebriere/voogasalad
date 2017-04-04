package gameengine.actors;

import gameengine.actors.properties.Health;
import gameengine.actors.properties.IActProperty;
import gameengine.grid.interfaces.ActorGrid.ReadAndMoveGrid;

import java.util.List;

import gamedata.ProjectileData;

public class Projectile extends Shot<ReadAndMoveGrid>{

	private double mySpeed;
	private double finalX;
	private double finalY;
	//private Path myPath;
	
	public Projectile(Health h, List<IActProperty> properties){	
	}
	
	public Projectile(double endX, double endY) {
		finalX = endX;
		finalY = endY;
	}
	
	/* (non-Javadoc)
	 * @see gameengine.actors.management.Actor#act(java.lang.Object)
	 */
	@Override
	public void act(ReadAndMoveGrid grid) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see gameengine.actors.management.Actor#isActive()
	 */
	@Override
	public boolean isActive() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void applyDamage(double health) {
		// TODO Auto-generated method stub
		
	}
}
