/** 
 * This entire file is part of my masterpiece.
 * Anh Trinh
 * 
 * This is the utility class that contain static methods for property classes such as move,shoot,
 * and the layer classes to use. Since our game works on the coordinate map instead of the tile map, this utility 
 * class is useful to calculate distance, get a list of points for paths, checking for collision and placing an actor
 * on a layer. The coordinates system  makes our actor movement looks ,uch more smoothly and dynamically ( projectiles 
 * curving and tracing enemy for example). At the same time, the user can define paths and actors'layers in all shapes and
 * sizes
 */

package util;
import java.util.ArrayList;
import java.util.List;

import gameengine.grid.classes.Coordinates;
import gameengine.grid.interfaces.Identifiers.Grid2D;

/**
 * @author Anh
 * @author maddiebriere
 */
public class PathUtil {
	
	/**
	 * 
	 * @param point1
	 * @param point2
	 * @return the distance between two coordinates
	 */
	public static double getDistance(Grid2D point1,Grid2D point2){
		return Math.sqrt(Math.pow(point2.getX() - point1.getX(), 2) 
				+ Math.pow(point2.getY() - point1.getY(), 2));
	}
	
	/**
	 * 
	 * @param point1
	 * @param point2
	 * @return the angle difference in radians between two vectors from origin to these two points
	 */
	public static double getAngle(Grid2D point1,Grid2D point2){
		return Math.atan2(point2.getY() - point1.getY(), point2.getX() - point1.getX());
	}
	
	/**
	 * 
	 * @param start
	 * @param end
	 * @param increment
	 * @return a list of coordinates from start to end, separated by increment distance
	 */
	public static List<Grid2D> getIncrementPoints(Grid2D start,Grid2D end, double increment){
		List<Grid2D> result = new ArrayList<Grid2D>();
		double distance = getDistance(start, end); 
		double angle = getAngle(start,end);
		int numIncrements = (int) (distance % increment == 0 ? (distance/increment)-1: (distance/increment));
	
		for (int i = 0; i <= numIncrements; i++){
			double newX = start.getX() + increment* i * Math.cos(angle);
			double newY = start.getY() + increment* i * Math.sin(angle);
			result.add(new Coordinates(newX,newY));
		}
		result.add(new Coordinates(end));
		return result; 
	}
	
	/**
	 * 
	 * @param pathChosen the path chosen as a list of waypoints, not accounting for speed
	 * @param increment
	 * @return the list of coordinates as a full path, taken into account a speed/increment value
	 */
	public static List<Grid2D> getPathCoordinates(List<Grid2D> pathChosen , double increment){
		List<Grid2D> coordinatesToReturn = new ArrayList<Grid2D>(); 
		for (int i = 0; i< pathChosen.size()-1; i++ ){
			coordinatesToReturn.addAll(getIncrementPoints(pathChosen.get(i),pathChosen.get(i+1),increment));
			coordinatesToReturn.remove(coordinatesToReturn.size()-1);
		}
		coordinatesToReturn.add(new Coordinates(pathChosen.get(pathChosen.size()-1))); // for the ending point
		return coordinatesToReturn;
	}
	
	/**
	 * assuming vector magnitude is 1 from the origin 
	 * @param angle in radian
	 * @return get x coordinate from origin of angle
	 */
	public static double getXByAngle(double angle){
		return Math.cos(angle);
	}
	
	public static double getYByAngle(double angle){
		return Math.sin(angle);
	}
	
	/**
	 * utility for checking if a point is within a polygon area or not - to check whether to place
	 * an actor on a layer 
	 * @param polygon the list of points representing the polygon 
	 * @param point the point to place an actor 
	 * @return true if the point is within the polygon and false otherwise
	 */
	public static boolean isWithinPolygon(List<? extends Grid2D> polygon, double x, double y){
		int i;
	    int j;
	    boolean result = false;
	    for (i = 0, j = polygon.size() - 1; i < polygon.size(); j = i++) {
	       if ((polygon.get(i).getY() > y) != (polygon.get(j).getY() > y) &&
	          (x < (polygon.get(j).getX() - polygon.get(i).getX()) * (y - polygon.get(i).getY()) / (polygon.get(j).getY()-polygon.get(i).getY()) + polygon.get(i).getX())) {
	          result = !result;
	         }
	      }
	    return result;
	}
		
}
