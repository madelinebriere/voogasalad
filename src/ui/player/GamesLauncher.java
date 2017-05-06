//This entire file is part of my masterpiece.
//Vishnu Gottiparthy

/**
 * This class was factored out of LoginMain in order to cut down on its length
 * This particular class accomplishes the specific task of building a list of available games.
 */

package ui.player;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import XML.xmlmanager.classes.XStreamSerializer;
import XML.xmlmanager.exceptions.IllegalXStreamCastException;
import XML.xmlmanager.interfaces.serialization.VoogaSerializer;
import gamedata.GameData;
import gameengine.controllers.GameController;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import ui.Preferences;
import ui.handlers.LoginHandler;
import ui.player.login.MiniGame;
import util.FileSelector;

/**
 * This class reads in all of the games in the "games" folder and generates a list of 
 * {@link ui.player.login.MiniGame MiniGame}s for use in the {@link ui.player.GameSelector GameSelector}.
 * @author Vishnu Gottiparthy
 *
 */
public class GamesLauncher {

	private List<MiniGame> list;
	private LoginHandler loginhandler;
	private Stage stage;
	private ResourceBundle resource;
	
	/**
	 * Gets the list of {@code MiniGame}s for use with the {@code GameSelector}
	 * @return
	 */
	public List<MiniGame> getList() {
		return list;
	}
	
	/**
	 * Creates a {@code GamesLauncher}
	 * @param stage Main {@code Stage}
	 * @param loginhandler For returning to the main screen
	 * @param resource Describes all of the dialog text
	 */
	public GamesLauncher(Stage stage, LoginHandler loginhandler, ResourceBundle resource) {
		this.loginhandler = loginhandler;
		this.stage = stage;
		this.resource = resource;
		setupGamesList();
	}
	
	private void setupGamesList() {
		list = new ArrayList<MiniGame>();
		list.add(new MiniGame(resource.getString("customGame"), 
				resource.getString("defaultImage"), e -> promptUserToChooseGame()));
		
		findFileNames().forEach(game -> {
			try {
				GameData gd = readGame(new File(game));
				list.add(new MiniGame(gd.getName(), 
						gd.getDisplayData().getBackgroundImagePath(), 
						e -> goToGameScreen(gd)));
			} catch (Exception e) { 
				//Ignore game
			}
		});
	}
	
	private List<String> findFileNames() {
		ArrayList<String> fileNames = new ArrayList<String>();
		File folder = new File(resource.getString("gamesDirectory"));
		File[] filesList = folder.listFiles();

		for (int i = 0; i < filesList.length; i++) {
			if (filesList[i].isFile()) {
				fileNames.add(resource.getString("gamesDirectory") + "/" + filesList[i].getName());
			}
		}
		return fileNames;
	}
	
	private void promptUserToChooseGame() {
		FileSelector mySelector = new FileSelector(resource.getString("configExtension"));
		File dataFile = mySelector.open();
		try {
			goToGameScreen(readGame(dataFile));
		} catch (Exception e) {
			new Alert(AlertType.ERROR, resource.getString("dataError")).showAndWait();
		} 
	}
	
	private GameData readGame(File dataFile) throws IOException, IllegalXStreamCastException {	
		if (dataFile != null) {
			String XML = new String(Files.readAllBytes(Paths.get(dataFile.getAbsolutePath())));
			VoogaSerializer serializer = new XStreamSerializer();
			GameData gameData = serializer.makeObjectFromXMLString(XML, GameData.class);
			return gameData;
		}
		return null;
	}
	
	private void goToGameScreen(GameData gameData) {
		GameController gameController = new GameController(gameData, loginhandler);
		stage.setTitle(resource.getString("gameScreen"));
		gameController.start(stage, Preferences.SCREEN_WIDTH, Preferences.SCREEN_HEIGHT, Color.WHITE);
	}
}
