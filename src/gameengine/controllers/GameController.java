package gameengine.controllers;

import factories.ActorGenerator;
import gamedata.ActorData;
import gamedata.GameData;
import gameengine.actors.management.Actor;
import gameengine.grid.ActorGrid;
import gameengine.grid.interfaces.Identifiers.Grid2D;
import gameengine.player.GameStatus;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import ui.UIMain;
import ui.handlers.UIHandler;
import util.IDGenerator;
import util.VoogaException;

public class GameController {
	private Timeline animation;
	
	private GameStatus myGameStatus;
	private GameData myGameData;
	
	private UIHandler myUIHandler;
	private LevelController myLevelController;
	private IDGenerator myIDGenerator;
	private ActorGrid myActorGrid;
	
	private UIMain myUIMain;
	
	private final int MAX_X = 1000;
	private final int MAX_Y =1000;
	
	private final double MILLISECOND_DELAY=17;

	public GameController() {
		myIDGenerator = new IDGenerator();
		myGameData = new GameData();
		myGameStatus = new GameStatus();
		myActorGrid = new ActorGrid(MAX_X,MAX_Y,
				i -> ActorGenerator.makeActor(myGameData.getOption(i)));
		initializeUIHandler();
	}
	
	public void intitializeTimeline() {
		KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY),
				d -> step());
		animation = new Timeline();
		animation.setCycleCount(Timeline.INDEFINITE);
		animation.getKeyFrames().add(frame);
	}
	
	public void step() {
		myActorGrid.step();
	}
	
	private double getScreenSizeX() {
		//need getMap method in front end
		return myUIMain.getScene().getWidth();
	}
	
	private double getScreenSizeY() {
		//need getMap method in front end
		return myUIMain.getScene().getHeight();
	}
	
	
	
	
	private void initializeUIHandler() {
		myUIHandler = new UIHandler() {

			@Override
			public void deleteGameObject(int id) {
				myActorGrid.removeActor(id);
			}

			@Override
			public void updateGameObjectType(int id, Integer currentOption, Integer newOption) throws VoogaException {
				if (myGameData.getOption(currentOption).getActor().equals(myGameData.getOption(newOption))) {
					Grid2D location = myActorGrid.getActorFromID(id).getLocation();
					addGameObject(newOption,location.getX(),location.getY());
					deleteGameObject(id);
				} else {
					throw new VoogaException(VoogaException.ILLEGAL_UPDATE);
				}
				
			}

			@Override
			public void updateGameObjectLocation(int id, double x, double y) throws VoogaException {
				if (myActorGrid.isValidLoc(x, y)) {
					myActorGrid.getActorFromID(id).setLocation(x, y);
				} else {
					throw new VoogaException(VoogaException.INVALID_LOCATION);
				}
				
			}

			@Override
			public int addGameObject(Integer option, double xRatio, double yRatio) throws VoogaException{
				ActorData actorData = myGameData.getOption(option);
				int ID = myIDGenerator.getNewID();
				Actor actor = ActorGenerator.makeActor(actorData);
				if (myActorGrid.isValidLoc(x, y)) {
					myActorGrid.spawn(actor, x, y);
				} else {
					throw new VoogaException(VoogaException.INVALID_LOCATION);
				}
				
				return ID;
			}

			@Override
			public void pause() {
				animation.pause();
			}

			@Override
			public void play() {
				animation.play();
			}

			@Override
			public void stop() {
				animation.stop();
			}

			@Override
			public void exit() {
				
				
			}
			
		};
	}
	
	public void updateLevel() {
	}
}