// This entire file is part of my masterpiece.
// SARAH ZHOU

package gameengine.controllers;

import java.util.ArrayDeque;		
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.function.Supplier;
import gamedata.ActorData;
import gamedata.BasePlacementData;
import gamedata.EnemyInWaveData;
import gamedata.GameData;
import gamedata.LevelData;
import gamedata.PathData;
import gamedata.WaveData;
import gameengine.actors.management.Actor;
import gameengine.conditionsgen.Condition;
import gameengine.grid.interfaces.Identifiers.Grid2D;
import gameengine.grid.interfaces.controllergrid.ControllableGrid;
import gameengine.handlers.LevelHandler;
import gamestatus.GameStatus;
import util.Delay;
/**
 * Controls information about / behavior of levels (ie. leveling up, dealing with win conditions) based on LevelData's and PreferenceData
 * 
 * @author sarahzhou
 * 
 */

public class GameLevelController {
	private ControllableGrid myGrid;
	
	private GameData myGameData;
	
	private GameStatus myGameStatus;
	
	private LevelHandler myLevelHandler;
	
	private Delay myDelay;
	
	private final int DELAY_CONSTANT = 35;
	
	private Condition myWinCondition;
	
	private int myLevel;
	
	private Queue<Supplier<Boolean>> enemiesInWave;
	
	private int enemiesLeft = 0; 

	public GameLevelController(LevelHandler levelHandler,GameData gameData,GameStatus gameStatus) {
		myLevelHandler = levelHandler;
		myGrid = myLevelHandler.getMyGrid();
		myDelay = new Delay(DELAY_CONSTANT);
		myGameData = gameData;
		enemiesInWave = new ArrayDeque<>();
		myGameStatus = gameStatus;
	}
	
	private void setEnemiesLeft(int numEnemies) {
		enemiesLeft = numEnemies;
		myGameStatus.setMyEnemiesLeft(numEnemies);
	}
	
	/**
	 * update queue of enemies, update number of enemies left, and check win conditions for the current level
	 */
	public void update() {
		if(myDelay.delayAction()&&!enemiesInWave.isEmpty()) enemiesInWave.poll().get();
		updateEnemiesLeft();
		checkCondition(myGameStatus);
	}
	
	private void updateEnemiesLeft(){
		int enemiesLeft = enemiesInWave.size()+myLevelHandler.actorCounts().apply(myLevelHandler.getBasicActorTypeEnemy());
		setEnemiesLeft(enemiesLeft);
	}
	
	private void checkCondition(GameStatus myGameStatus) {
		Optional<Boolean> myWin = myWinCondition.conditionSatisfied(myGameStatus);
		myWin.ifPresent(win -> winCondition(win).run());
	}
	
	/**
	 * Try to level up if level is won and display lose alert if level is lost
	 * 
	 * @param win
	 * @return runnable for win condition 
	 */
	private Runnable winCondition(Boolean win) {
		return win ? () -> levelUp():()->myLevelHandler.displayLoseAlert();
	}
	
	public int getLevel() {
		return myLevel;
	}
	
	/**
	 * Level up if there are more levels in GameData
	 * Otherwise, display win alert to user
	 */
	public void levelUp() {
		if (levelsLeft()) {
			changeLevel(myLevel+1);
			myLevelHandler.levelUp();
		} else {
			myLevelHandler.displayWinAlert();
		}
	}
	
	private boolean levelsLeft() {
		return !(myGameData.getLevels().get(myLevel+1)==null);
	}
	
	public void changeLevel(int level) {
		this.myLevel = level;
		LevelData levelData = myGameData.getLevel(level);
		loadLevel(levelData);
	}
	
	private void loadLevel(LevelData levelData) {
		myDelay = new Delay((int) levelData.getDuration());
		updateWinCondition(levelData);
		addSetPieces(myGameData.getLayers().getMyBaseData());
		addPieces(levelData);
	}
	
	/**
	 * Spawns all bases and adds them to grid
	 * @param myBaseData
	 */
	private void addSetPieces(BasePlacementData myBaseData) {
		myBaseData.getMyActorToLocation().stream().forEach(tuple -> myGrid.controllerSpawnActor(builders.objectgen.ActorGenerator.makeActor(myGameData.getOptionKey(tuple.x), tuple.x),tuple.y.getX(),tuple.y.getY()));
	}

	/**
	 * updates level win condition based on current levelData
	 * @param levelData
	 */
	private void updateWinCondition(LevelData levelData) {
		myWinCondition = levelData.getCondition();
	}
	
	/**
	 * Use factory to add all of the actors to the grid according to current levelData
	 * 
	 * @param curr LevelData from which to collect Actor information
	 * @param grid Grid to modify (add actors)
	 */
	private void addPieces(LevelData curr){
		Grid2D firstPathCoor =getFirstPathCoor(myGameData.getMyPaths());
		curr.getMyWaves().forEach(wave -> processWave(wave,firstPathCoor));
	}
	
	/**
	 * Process a single wave
	 * @param waveData
	 * @param firstPathCoor
	 */
	private void processWave(WaveData waveData,Grid2D firstPathCoor) {
		processEnemyWaves(waveData.getWaveEnemies(),firstPathCoor);
	}
	
	/**
	 * Process Enemy waves by spawning the specified number of enemeies, and adding them to the Queue
	 * 
	 * @param enemyInWaveDatas
	 * @param firstPathCoor
	 */
	private void processEnemyWaves(List<EnemyInWaveData> enemyInWaveDatas,Grid2D firstPathCoor) {
		enemyInWaveDatas.forEach(data -> {
			for(int i = 0; i<data.getOption(); i++) enemiesInWave.add(() -> {
				spawnEnemy(data,firstPathCoor);
				return true;
			});
		});
	}
	
	public int getEnemiesLeft() {
		return enemiesLeft;
	}

	/**
	 * Spawn enemy using factory API and place it in ActorGrid at its first path coordinate
	 * @param enemyData
	 * @param firstPathCoor
	 */
	private void spawnEnemy(EnemyInWaveData enemyData, Grid2D firstPathCoor) {
		ActorData actorData = enemyData.getMyActor();
		Actor actor = builders.objectgen.ActorGenerator.makeActor(myGameData.getOptionKey(actorData), actorData);
		myGrid.controllerSpawnActor(actor, firstPathCoor.getX(),firstPathCoor.getY());
	}
	
	/**
	 * @param pathData
	 * @return Grid2D coordinate representing where to place the newly spawned actor based on the first Grid2D coordinate in its pathData
	 */
	private Grid2D getFirstPathCoor(PathData pathData) {
		int numPaths = pathData.getMyPaths().size();
		int rand = (int) Math.random()*numPaths;
		return pathData.getPathByIndex(rand).get(0);
	}
	

	
}