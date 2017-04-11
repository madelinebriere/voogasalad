package voogasalad_ilovesingletons;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import ui.player.Login;

public class Main extends Application{

	public static void main(String[] args) {
		System.out.print("launching");
		launch(args);
	}


	@Override
	public void start(Stage s) throws Exception {
		s.getIcons().add(new Image("tower2.jpg"));
		System.out.println("here");
		Login login = new Login(s, "loginScreen.css", "login");
		s.setScene(login.getScene());
		s.setTitle("Login");
		s.show();
	}
}
