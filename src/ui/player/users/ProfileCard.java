package ui.player.users;
import java.util.ResourceBundle;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import ui.player.inGame.OptionButton;
public class ProfileCard {
	private HBox card;
	private User user;
	private ResourceBundle profileRB;
	private ExperienceGrid expGrid;
	private GameStatsGrid gsGrid;
	private Hyperlink logout;
	
	public User getUser() {
		return user;
	}
	
	public HBox getCard() {
		return card;
	}
	
	public void setLogoutAction(EventHandler<ActionEvent> e) {
		System.out.println("returning");
		logout.setOnAction(e);
	}
	
	public ProfileCard(String rb, User user, String css) {
		card = new HBox(20);
		card.setPadding(new Insets(20, 20, 20, 20));
		card.getStylesheets().add(css);
		card.setId("hbox");
		this.user = user;
		this.profileRB = ResourceBundle.getBundle(rb);
		setup();
	}
	private void setup() {
		imageAndExperince();
		gameStats();
		exit();
	}
	private void exit() {
		VBox exiting = new VBox(20);
		EventHandler<MouseEvent> close = new EventHandler<MouseEvent>()  {
			@Override
			public void handle(MouseEvent event) {
				ScaleTransition st = new ScaleTransition(Duration.millis(1000), card);
				st.setByX(-1f);
				st.setByY(-1f);
				st.play();	
			}
		};
		OptionButton exit = new OptionButton(0, "", "x_icon.png", close);
		exit.getButton().setStyle("-fx-background-color: transparent");
		logout = new Hyperlink("Logout");
		exiting.getChildren().addAll(exit.getButton(), logout);
		card.getChildren().add(exiting);
	}
	private void imageAndExperince() {
		if(user != null) {
			VBox vb = new VBox(20);
			ImageView profilePicture = new ImageView(new Image(user.getProfilePicture(), 150, 150, false, true));
			expGrid = new ExperienceGrid(profileRB);
			setupExpGridValues();
			vb.getChildren().addAll(profilePicture, expGrid.getGrid());
			vb.setAlignment(Pos.CENTER);
			card.getChildren().add(vb);
		} else {
			new Alert(AlertType.ERROR, profileRB.getString("nouser")).showAndWait();
		}
	}
	private void setupExpGridValues() {
		expGrid.getRank().setText(user.getRank());
		expGrid.getExperience().setText(user.getExperience().toString());
		expGrid.getLevel().setText(user.getLevel().toString());
		expGrid.getEntryMap().keySet().forEach(entry -> {
			expGrid.getEntryMap().get(entry).setEditable(false);
			expGrid.getEntryMap().get(entry).setPrefWidth(100);
		});
	}
	private void gameStats() {
		gsGrid = new GameStatsGrid(profileRB);
		setupGSGridValues();
		gsGrid.getGrid().setAlignment(Pos.BOTTOM_CENTER);
		card.getChildren().add(gsGrid.getGrid());
		card.setAlignment(Pos.BOTTOM_CENTER);
	}
	private void setupGSGridValues() {
		gsGrid.getLastPlayed().setText(user.getLastPlayed());
		gsGrid.getMostPlayed().setText(user.getMostPlayed());
		gsGrid.getScore().setText(user.getHighScore().toString());
		gsGrid.getUsername().setText(user.getUsername());
		gsGrid.getEntryMap().keySet().forEach(entry -> {
			gsGrid.getEntryMap().get(entry).setEditable(false);
			gsGrid.getEntryMap().get(entry).setPrefWidth(100);
		});
	}
}