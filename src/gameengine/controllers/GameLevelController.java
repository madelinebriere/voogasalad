package gameengine.controllers;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.function.Supplier;

import gamedata.ActorData;
import gamedata.EnemyInWaveData;
import gamedata.GameData;
import gamedata.LevelData;
import gamedata.PathData;
import gamedata.PreferencesData;
import gamedata.WaveData;
import gameengine.actors.management.Actor;
import gameengine.grid.interfaces.Identifiers.Grid2D;
import gameengine.grid.interfaces.controllergrid.ControllableGrid;
import gameengine.handlers.LevelHandler;
import util.Delay;
import util.VoogaException;

/**
 * Controls information about/behavior of a single level
 * 
 * @author sarahzhou
 * 
 */

public class GameLevelController {
	private ControllableGrid myGrid;
	
	private GameData myGameData;
	
	private PreferencesData myPreferences;
	
	private LevelHandler myLevelHandler;
	
	private Delay delay;
	
	private final int DELAY_CONSTANT = 2;
	
	private int level;
	
	private Queue<Supplier<Boolean>> enemiesInWave;
	
	public GameLevelController(LevelHandler levelHandler,GameData gameData) {
		myLevelHandler = levelHandler;
		myGrid = myLevelHandler.getMyGrid();
		delay = new Delay(DELAY_CONSTANT);
		myGameData = gameData;
		myPreferences = myGameData.getPreferences();
		enemiesInWave = new ArrayDeque<>();
	}
	
	public void update() {
		if(delay.delayAction()&&!enemiesInWave.isEmpty()) {
			enemiesInWave.poll().get();
		}
	}
	
	public int getLevel() {
		return level;
	}
	
	public void levelUp() throws VoogaException {
		if (!(myGameData.getLevels().get(level+1)==null)) {
			changeLevel(level+1);
			myLevelHandler.levelUp();
		} else {
			myLevelHandler.displayWinAlert();
		}
	}
	
	public void changeLevel(int level) throws VoogaException{
		this.level = level;
		LevelData levelData = myGameData.getLevel(level);
		if (levelData!=null) loadLevel(levelData);
		else throw new VoogaException(VoogaException.NONEXISTANT_LEVEL);
	}
	
	private void loadLevel(LevelData levelData) {
		addPieces(levelData);
	}
	
	/**
	 * Use Actor factory to add all of the actors
	 * to the grid
	 * 
	 * @param curr LevelData from which to collect Actor information
	 * @param grid Grid to modify (add actors)
	 */
	private void addPieces(LevelData curr){
		Grid2D firstPathCoor =getFirstPathCoor(myGameData.getMyPaths());
		curr.getMyWaves().forEach(wave -> processWave(wave,firstPathCoor));
	}
	
	private void processWave(WaveData waveData,Grid2D firstPathCoor) {
		processEnemyWaves(waveData.getWaveEnemies(),firstPathCoor);
	}
	
	private void processEnemyWaves(List<EnemyInWaveData> enemyInWaveDatas,Grid2D firstPathCoor) {
		enemyInWaveDatas.forEach(data -> {
			for(int i = 0; i<data.getOption(); i++) enemiesInWave.add(() -> {
				spawnEnemy(data,firstPathCoor);
				return true;
			});
		});
	}

	private void spawnEnemy(EnemyInWaveData enemyData, Grid2D firstPathCoor) {
		ActorData actorData = enemyData.getMyActor();
		Actor actor = builders.ActorGenerator.makeActor(myGameData.getOptionKey(actorData), actorData);
		myGrid.controllerSpawnActor(actor, firstPathCoor.getX(),firstPathCoor.getY());
	}
	
	private Grid2D getFirstPathCoor(PathData pathData) {
		int numPaths = pathData.getMyPaths().size();
		int rand = (int) Math.random()*numPaths;
		return pathData.getPathByIndex(rand).get(0);
	}
	

	
}