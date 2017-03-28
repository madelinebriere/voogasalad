package ui.authoring;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import ui.Preferences;
import ui.authoring.level.LevelEditorView;
import ui.authoring.map.MapEditorView;
import ui.general.CustomColors;
import ui.general.Frame;
import ui.general.UIView;

public class AuthoringView extends UIView {

	private Group myContentView;
	private EnemyEditorView myEnemyView;
	private LevelEditorView myLevelView;
	private MapEditorView myMapView;
	private PathEditorView myPathView;
	private TowerEditorView myTowerView;

	public AuthoringView() {
		this.setBackground(new Background(new BackgroundFill[] { new BackgroundFill(CustomColors.GREEN_100, new CornerRadii(3), Insets.EMPTY)}));

		setupViews();
	}

	private void setupViews() {
		setupTitle();
		setupMapView();
		setupEnemyView();
		setupLevelView();
		setupPathView();
		setupTowerView();
	}

	private void setupTitle() {
		Text title = new Text("Game Authoring Environment");
		title.setFont(Preferences.FONT_BIG);
		title.prefWidth(Preferences.SCREEN_WIDTH);
		title.setTextAlignment(TextAlignment.CENTER);
		title.setLayoutX(0);
		title.setLayoutY(30);
		this.getChildren().add(title);
	}

	private void setupTowerView() {
		myTowerView = new TowerEditorView();
	}

	private void setupPathView() {
		myPathView = new PathEditorView();
	}

	private void setupMapView() {
		//this calculation assumes that height < width
		double inset = 60;
		double height = Math.round(Preferences.SCREEN_HEIGHT - 2*inset);
		double dim = 11;
		height = height - dim%2;
		myMapView = new MapEditorView(dim,dim, 
				new Frame((Preferences.SCREEN_WIDTH - height)/2.0, inset, height, height)
				);

		this.getChildren().add(myMapView);
	}

	private void setupLevelView() {
		myLevelView = new LevelEditorView(
				new Frame(myMapView.getFrame().getMaxX() + 12, myMapView.getFrame().getY(), 
						Preferences.SCREEN_WIDTH - myMapView.getFrame().getMaxX() - 24, myMapView.getFrame().getHeight()));
		this.getChildren().add(myLevelView);
	}

	private void setupEnemyView() {
		myEnemyView = new EnemyEditorView();
		
	}
	
	

}
