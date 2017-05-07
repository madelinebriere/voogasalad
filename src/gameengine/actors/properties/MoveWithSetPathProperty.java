/**
 * This entire file is part of my masterpiece.
 * Anh Trinh
 * 
 * This is a move property with a predefined path for an actor. If there are multiple path options assigned, one will 
 * be chosen randomly and converted into a full path having taken into account the speed for every step.  
 * 
 * Every property implements the IActProperty<G> interface that provides the action method that defines how the property 
 * would act every game step. The property interface is restricted to an interface of the gameengine.grid.ActorGrid-an object
 * that holds info of all actors in the game and can affect them in different ways such as moving, removing actors and so on. 
 * The grid `G` here can be any generic grid that extends the most basic ReadableGrid, which only has getters for 
 * actors' locations. For this specific property, `G` extends the `ReadAndMoveGrid` as it needs access to method that moves the 
 * actor to a new coordinate by every step but nothing else. This design of grid interfaces allows for encapsulation of methods 
 * as it restricts what a property can do depending on its need. 
 * 
 * In addition, this design of property classes supports composition as actor is just a shell of any combination of properties such as this
 * one. It prevents duplicate codes that might result from writing concrete classes for a set of actors in the game as some of them would
 * have similar behavior such as move. We just need to write a property once. At the same time, the composition design of property gives the 
 * user more freedom in building up their characters in their game. They can go as far as making an actor shoot projectiles that shoot projectiles 
 * themselves or they can make actors of the same type able to damage each other. For this, our program can make a variety of
 * games from PvZ, Bloons, Base Defense, to Space Invaders, Angry Birds, and Pong. 
 * 
 */


package gameengine.actors.properties;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import gamedata.composition.MoveWithSetPathData;
import gameengine.actors.propertygen.IActProperty;
import gameengine.grid.classes.Coordinates;
import gameengine.grid.interfaces.ActorGrid.ReadAndMoveGrid;
import gameengine.grid.interfaces.Identifiers.Grid2D;
import util.PathUtil;
/**
 * @author Anh
 * @author Maddie
 */
public class MoveWithSetPathProperty<G extends ReadAndMoveGrid> implements IActProperty<G>{
	
	private Queue<Grid2D> myPathCoordinates;

	public MoveWithSetPathProperty(MoveWithSetPathData data){
		//Apply random path to current actor
		myPathCoordinates = new LinkedList<>(getRandomSteps(data.getMyPaths(), data.getMySpeed()));
	}
	
	/**
	 * Get a random path from the available options, taking
	 * into account how speed will affect step size.
	 * 
	 * @param paths list of assigned paths 
	 * @return Random path, accommodating speed
	 */
	public List<Grid2D> getRandomSteps(List<List<Grid2D>> paths, double speed){
		if (paths.size() == 0){ // default path if none was chosen by the user
			List<Grid2D> path = new ArrayList<Grid2D>();
			path.add(new Coordinates(0,0));
			path.add(new Coordinates(0.5,0.5));
			path.add(new Coordinates(1,1));
			paths.add(path);
		}
		int rand = (new Random()).nextInt(paths.size());
		return PathUtil.getPathCoordinates(paths.get(rand), speed);
	}
	
	/**
	 * this method is called by the game step function to call the property to act
	 * @param G an interface for grid with access to methods restricted to what the property needs
	 * @param actorID the id of the actor having this behavior
	 */
	@Override
	public void action(G grid, Integer actorID) {
		// TODO Auto-generated method stub
		if (!myPathCoordinates.isEmpty()){
			// poll a coordinate from myPathCoordinates to set the enemy location to
			Grid2D newLoc = myPathCoordinates.poll();
			grid.move(actorID, newLoc.getX(), newLoc.getY()); 
		}
	}
	
	/**
	 * whether this property is active or not 
	 */
	@Override
	public boolean isOn() {
		// TODO Auto-generated method stub
		return true;
	}

}
