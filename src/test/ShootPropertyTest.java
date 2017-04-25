package test;

import static org.junit.Assert.*;

import org.junit.Test;

import gamedata.composition.LimitedHealthData;
import gamedata.compositiongen.HealthData;
import gamedata.compositiongen.ShootData;
import gameengine.actors.MainActor;
import gameengine.actors.management.Actor;
import gameengine.actors.properties.health.LimitedHealthProperty;
import gameengine.actors.properties.shoot.ShootTargetNearProperty;
import gameengine.grid.ActorGrid;
import gameengine.grid.classes.Coordinates;
import gameengine.grid.interfaces.ActorGrid.MasterGrid;
import types.BasicActorType;

public class ShootPropertyTest {

	public ActorGrid setUp() {
		ActorGrid grid = new ActorGrid(10,10);
		LimitedHealthData data = new LimitedHealthData(100);
		Actor actor = new MainActor(BasicActorType.Troop, 1, new LimitedHealthProperty(data));
		Actor actor2 = new MainActor(BasicActorType.Troop, 2, new LimitedHealthProperty(data));
		grid.spawn(actor, 3, 3);
		grid.spawn(actor2, 2.5, 2.5);
		return grid;
	}
	
	@Test
	public void testGetEnemy() {
		ActorGrid grid = new ActorGrid(10,10);
		LimitedHealthData data = new LimitedHealthData(100);
		Actor actor = new MainActor(BasicActorType.Troop, 1, new LimitedHealthProperty(data));
		Actor actor2 = new MainActor(BasicActorType.Troop, 2, new LimitedHealthProperty(data));
		grid.spawn(actor, 3, 3);
		grid.spawn(actor2, 2.5, 2.5);
		MasterGrid myGrid = grid;
		ShootData shootdata = new ShootData(10, 10);
		ShootTargetNearProperty<MasterGrid> prop = new ShootTargetNearProperty<>(shootdata);
		assertEquals("BoolTest",false,prop.getEnemyToShoot(myGrid.getActorLocationsInRadius(1, 1, 10, BasicActorType.Troop), new Coordinates(1,1)).isEmpty());
	}
	
	@Test
	public void testSomething() {
		assertEquals("test","","");
	}

}
