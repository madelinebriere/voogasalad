/**
* This entire file is part of my masterpiece.
* Anh Trinh
* 
* This is the data class for MoveWithSetPathProperty. In the authoring environment, the user chooses a list of paths for 
* the actor that has this property and a speed to move on that path. This class holds such information. As each path is a
* list of coordinates (Grid2D), the list of paths is a list of lists of Grid2D. 
* 
* Each property class has a corresponding data class in its constructor that holds all the necessary information for that 
* property to work. This class extends the MoveData abstract class, which sets up the speed for all variants of move property
* (there are move with angle, with set destination data classes). 
* 
* Having a data wrapper class for each property makes the constructor of all property classes consistent, which serves well for 
* reflection in the factory. 
*  
*/

package gamedata.composition;
import java.util.ArrayList;
import java.util.List;

import gamedata.compositiongen.MoveData;
import gameengine.grid.interfaces.Identifiers.Grid2D;
/**
 * @author maddie
 * @author Anh
 *
 */
public class MoveWithSetPathData extends MoveData {

	List<List<Grid2D>>myPaths;
	
	/**
	 * @param myAssignedPaths the list of path options without taking into account the speed
	 * @param speed
	 */
	public MoveWithSetPathData(List<List<Grid2D>> myAssignedPaths, Double speed){
		super(speed);
		myPaths = myAssignedPaths;
	}
	
	public MoveWithSetPathData(){
		this(new ArrayList<List<Grid2D>>(), 0.0);
	}
	
	public List<List<Grid2D>> getMyPaths() {
		return myPaths;
	}

	public void setMyPaths(List<List<Grid2D>> myPaths) {
		this.myPaths = myPaths;
	}

}
