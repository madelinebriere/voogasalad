package test;

import java.util.ArrayList;

import gamedata.composition.MoveWithSetPathData;
import gameengine.actors.properties.HealthProperty;
import gameengine.actors.properties.IActProperty;
import gameengine.actors.properties.LimitedHealthProperty;
import gameengine.actors.properties.MoveWithSetPathProperty;
import gameengine.actors.Troop;
import gameengine.grid.ActorGrid;
import gameengine.grid.TestGrid;
import gameengine.grid.classes.Coordinates;
import gameengine.grid.interfaces.ActorGrid.MasterGrid;
import gameengine.grid.interfaces.ActorGrid.ReadAndMoveGrid;
import gameengine.grid.interfaces.ActorGrid.ReadableGrid;
import gameengine.grid.interfaces.Identifiers.Grid2D;

public class tester {

	public static void main(String[] args) {
		MoveWithSetPathData data = new MoveWithSetPathData();
		ArrayList<Grid2D> path = new ArrayList<>();
		Grid2D a = new Coordinates(0,0);
		Grid2D b = new Coordinates(1,1);
		Grid2D c = new Coordinates(2,2);
		path.add(a);
		path.add(b);
		path.add(c);
		data.addFullPath(path);
		IActProperty<MasterGrid> prop = new MoveWithSetPathProperty<>(data);
		HealthProperty health = new LimitedHealthProperty(100);
		Troop<MasterGrid> troop = new Troop<>(1, health, prop);
		MasterGrid actor = new ActorGrid(10,10);
		troop.act(actor);
	}
}