package ui.authoring;

import java.util.Optional;

import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import ui.Preferences;
import ui.authoring.delegates.PopViewDelegate;
import ui.general.CustomColors;
import ui.general.UIHelper;

public class LeftPaneView extends StackPane{
	private PopViewDelegate myDelegate;
	private VBox myLeftPaneFront; //contains the buttons
	private StackPane myLeftPaneBack; //contains the views for buttons 
	private TowerEditorView myTowerView;
	private EnemyEditorView myEnemyView;
	
	
	public LeftPaneView(PopViewDelegate delegate){
		super();
		myDelegate = delegate;
		setupViews();
	}

	private void setupViews() {
		myLeftPaneFront = new VBox(16);
		myLeftPaneBack = new StackPane();
		myLeftPaneFront.setAlignment(Pos.CENTER);
		StackPane.setMargin(myLeftPaneFront, new Insets(10));
		setupLeftPaneButtons();
		setupTowerView();
		setupEnemyView();
		myLeftPaneBack.setScaleX(0);
		this.getChildren().add(myLeftPaneBack);
		this.getChildren().add(myLeftPaneFront);
	}
	
	private void setupLeftPaneButtons() {
		StackPane enemy = UIHelper.buttonStack(e -> {}, 
				Optional.of(labelForStackButton("Enemy Editor")), 
				Optional.of(imageForStackButton("enemy_icon.png")), 
				Pos.CENTER_RIGHT, true);
		StackPane tower = UIHelper.buttonStack(e -> myDelegate.openView(myTowerView), 
				Optional.of(labelForStackButton("Tower Editor")), 
				Optional.of(imageForStackButton("tower_icon.png")), 
				Pos.CENTER_RIGHT, true);
		StackPane splash = UIHelper.buttonStack(e -> System.out.println(e), 
				Optional.of(labelForStackButton("Splash Editor")), 
				Optional.of(imageForStackButton("splash_icon.png")), 
				Pos.CENTER_RIGHT, true);
		StackPane projectile = UIHelper.buttonStack(e -> System.out.println(e), 
				Optional.of(labelForStackButton("Projectile Editor")), 
				Optional.of(imageForStackButton("projectile_icon.png")), 
				Pos.CENTER_RIGHT, true);
		
		enemy.setPrefHeight(56);
		tower.setPrefHeight(56);
		splash.setPrefHeight(56);
		projectile.setPrefHeight(56);
		
		myLeftPaneFront.getChildren().addAll(tower, enemy, projectile, splash);
	}
	private Label labelForStackButton(String title){
		Label lbl = new Label(title);
		lbl.setTextFill(CustomColors.GREEN_100);
		lbl.setFont(Preferences.FONT_SMALL_BOLD);
		return lbl;
	}
	private ImageView imageForStackButton(String imagePath){
		ImageView iv = new ImageView(new Image(imagePath));
		iv.setFitWidth(24);
		iv.setPreserveRatio(true);
		return iv;
	}

	private void setupTowerView() {
		myTowerView = new TowerEditorView(myDelegate);
		UIHelper.setBackgroundColor(myTowerView, Color.WHITE);
	}

	
	private void setupEnemyView() {
		myEnemyView = new EnemyEditorView();
		UIHelper.setBackgroundColor(myEnemyView, Color.WHITE);
	}
	
	
	
}
