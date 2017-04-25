package gameengine.controllers;

import javafx.application.Application;
import javafx.stage.Stage;

public class TestGameController extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		GameController controller = new GameController();
		controller.start(primaryStage);
	}
}