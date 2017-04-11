package gamedata;

import java.util.List;
import java.util.Map;

import gameengine.grid.interfaces.Identifiers.Grid2D;

// like Path Data, index ProjectileType
/**
 * 
 * @author Anh
 *
 */
public class ProjectileData {
	private int numOptions;
	private Map<Integer,ProjectileType> allProjectileTypes;
	
	public ProjectileData(Map <Integer, ProjectileType> projectiles){
		allProjectileTypes = projectiles;
		numOptions = allProjectileTypes.size();
		
	}
	
	public ProjectileType getProjectileTypeByIndex(Integer index){
		return allProjectileTypes.get(index);
	}
	
	public void addProjectileType(ProjectileType projectile){
		allProjectileTypes.put(numOptions++, projectile);
	}
	public Map<Integer, ProjectileType> getMyProjectileTypes() {
		return allProjectileTypes;
	}
	public void setMyProjectileTypes(Map<Integer, ProjectileType> projectiletypes) {
		allProjectileTypes = projectiletypes;
	}
}
