package ui.authoring;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import XML.xmlmanager.classes.ConcreteDirectoryFileHelper;
import XML.xmlmanager.classes.XStreamSerializer;
import XML.xmlmanager.exceptions.IllegalFileException;
import XML.xmlmanager.exceptions.InvalidRootDirectoryException;
import XML.xmlmanager.interfaces.filemanager.DirectoryFileManager;
import builders.GameDataGenerator;
import gamedata.ActorData;
import gamedata.GameData;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import types.BasicActorType;
import ui.Preferences;
import ui.authoring.delegates.*;
import ui.authoring.level.LevelEditorView;
import ui.authoring.map.MapEditorView;
import ui.general.CustomColors;
import ui.general.ImageButton;
import ui.general.UIHelper;
import util.Location;


public class AuthoringView extends AnchorPane implements PopViewDelegate,MenuDelegate{

	private final double SIDE_PANE_WIDTH = 200;
	private final double SIDE_PANE_WIDTH_MIN = 160;
	private final Color THEME_COLOR = CustomColors.GREEN_200;
	
	private GameData myGameData;
	private BorderPane myBorderPane = new BorderPane();
	private LevelEditorView myLevelView;
	private MapEditorView myMapView;
	private LeftPaneView myLeftPane; //purpose of this pane is to flip animate 
	private MenuView myMenuView;
	private Pane myDimmerView;
	private FadeTransition dimAnimator;


	public AuthoringView() {
		UIHelper.setBackgroundColor(this, Color.WHITE);	
		myGameData = new GameData();
		setupViews();
	}

	private void setupViews() {
		setupTitle();
		setupMapView();
		setupLeftPane();
		setupLevelView();
		
		setupBottomPane();
		setupMargins();
		setupBorderPane();
		setupMenuView();
		setupName();
		setupDimmerView();
	}


	private void setupDimmerView() {
		myDimmerView = new Pane();
		UIHelper.setBackgroundColor(myDimmerView, Color.rgb(0, 0, 0, 0.75));
		AnchorPane.setBottomAnchor(myDimmerView, 0.0);
		AnchorPane.setTopAnchor(myDimmerView, 0.0);
		AnchorPane.setRightAnchor(myDimmerView, 0.0);
		AnchorPane.setLeftAnchor(myDimmerView, 0.0);
		this.getChildren().add(myDimmerView);
		myDimmerView.setPickOnBounds(false);
		dimAnimator = new FadeTransition(Duration.seconds(0.4));
		dimAnimator.setNode(myDimmerView);
		setDim(false, Duration.seconds(1));
		
	}
	
	private void setDim(boolean b, Duration d){
		if(b){
			dimAnimator.setToValue(1.0);
			dimAnimator.setOnFinished(e -> {});
			this.getChildren().add(myDimmerView);
		}
		else{
			dimAnimator.setToValue(0.0);
			dimAnimator.setOnFinished(e -> this.getChildren().remove(myDimmerView));
		}
		dimAnimator.setDuration(d);
		dimAnimator.play();
			
	}

	private void setupBorderPane() {
		AnchorPane.setBottomAnchor(myBorderPane, 0.0);
		AnchorPane.setTopAnchor(myBorderPane, 0.0);
		AnchorPane.setLeftAnchor(myBorderPane, 0.0);
		AnchorPane.setRightAnchor(myBorderPane, 0.0);
		this.getChildren().add(myBorderPane);
		
	}

	private void setupMenuView() {
		
		ImageButton menuButton = new ImageButton("menu_icon.png", new Location(40.0,40.0));
		menuButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> slideMenuIn());
		AnchorPane.setLeftAnchor(menuButton, 4.0);
		AnchorPane.setTopAnchor(menuButton, 12.0);
		UIHelper.setDropShadow(menuButton);
		this.getChildren().add(menuButton);
		
		double width = 300;
		myMenuView = new MenuView(this);
		myMenuView.setLayoutX(-width - 5);
		myMenuView.setPrefWidth(width);
		UIHelper.setBackgroundColor(myMenuView, CustomColors.GREEN);
		UIHelper.setDropShadow(myMenuView);
		AnchorPane.setTopAnchor(myMenuView, 0.0);
		AnchorPane.setBottomAnchor(myMenuView, 0.0);
		this.getChildren().add(myMenuView);

	}
	
	private void setupName() {
		
		TextField toAdd = addField("Untitled_Game");
		toAdd.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
               toAdd.clear();
            }
        });
		
		toAdd.textProperty().addListener((o,oldText,newText) -> 
			updateName(newText));
		AnchorPane.setRightAnchor(toAdd, 10.0);
		AnchorPane.setTopAnchor(toAdd, 12.0);
		UIHelper.setDropShadow(toAdd);
		this.getChildren().add(toAdd);

	}
	
	private void updateName(String newName){
		myGameData.setName(newName);
	}

	public TextField addField(String value){
		StackPane lblWrapper = new StackPane();
		TextField field = new TextField(value);
		field.setPrefWidth(200);
		field.setFont(Preferences.FONT_MEDIUM);
		field.setAlignment(Pos.CENTER);
		field.setBackground(UIHelper.backgroundForColor(THEME_COLOR));
		field.setStyle("-fx-text-fill-color: #FFFFFF");
		field.setStyle("-fx-background-color: #" +UIHelper.colorToHex(THEME_COLOR) + ";");
		lblWrapper.getChildren().add(field);
		return field;
	}

	private void setupMargins(){
		double ins = 10;
		BorderPane.setMargin(myLeftPane, new Insets(ins));
		BorderPane.setMargin(myMapView, new Insets(ins));
		BorderPane.setMargin(myLevelView, new Insets(ins));
		
	}

	private void setupTitle() {
		Label title = new Label("Authoring Environment");
		title.setFont(Preferences.FONT_BIG_BOLD);
		title.setPrefWidth(Preferences.SCREEN_WIDTH);
		title.setTextFill(Color.rgb(0, 0, 0, 0.8));
		title.setAlignment(Pos.CENTER);
		title.setPrefHeight(60);
		UIHelper.setDropShadow(title);
		this.myBorderPane.setTop(title);
	}

	private void setupMapView() {
		//this calculation assumes that height < width
		myMapView = new MapEditorView(myGameData.getMyPaths(), myGameData.getLayers(), this);
		myMapView.setMaxWidth(Preferences.SCREEN_WIDTH - 2*SIDE_PANE_WIDTH_MIN);
		UIHelper.setBackgroundColor(myMapView, THEME_COLOR);
		UIHelper.setDropShadow(myMapView);
		myBorderPane.setCenter(myMapView);
		BorderPane.setAlignment(myMapView, Pos.CENTER);

	}

	private void setupLevelView() {
		//TODO
		myLevelView = new LevelEditorView(this, myGameData);//TODO pass gamedata instead
			
		UIHelper.setBackgroundColor(myLevelView, THEME_COLOR);
		UIHelper.setDropShadow(myLevelView);
		myLevelView.setMinWidth(SIDE_PANE_WIDTH_MIN);
		myLevelView.setPrefWidth(SIDE_PANE_WIDTH);
		
		this.myBorderPane.setRight(myLevelView);
		
	} 
	
	private void setupLeftPane(){
		myLeftPane = new LeftPaneView(this, myGameData);
		myLeftPane.setMinWidth(SIDE_PANE_WIDTH_MIN);
		myLeftPane.setPrefWidth(SIDE_PANE_WIDTH);
		AnchorPane.setBottomAnchor(myLeftPane, 12.0);
		AnchorPane.setTopAnchor(myLeftPane, 12.0);
		AnchorPane.setLeftAnchor(myLeftPane, 12.0);
		UIHelper.setBackgroundColor(myLeftPane,THEME_COLOR);
		UIHelper.setDropShadow(myLeftPane);
		this.myBorderPane.setLeft(myLeftPane);
	}

	
	private void setupBottomPane() {
		Pane pane = new Pane();
		pane.setPrefHeight(60);
		this.myBorderPane.setBottom(pane);
	}
	
	private void slideMenuIn(){
		System.out.println("menu pressed");
		TranslateTransition t = new TranslateTransition(Duration.seconds(0.2));
		t.setNode(myMenuView);
		t.setByX(myMenuView.widthProperty().doubleValue());
		t.play();
	}
	private void slideMenuOut(){
		TranslateTransition t = new TranslateTransition(Duration.seconds(0.2));
		t.setNode(myMenuView);
		t.setToX(0);
		t.play();
	}
	private void openPaneWithAnimation(Pane pane, PopupSize size){
		setDim(true, Duration.seconds(0.4));//dim background

		Insets inset = insetForPopupSize(size);
		System.out.println(inset);
		AnchorPane.setBottomAnchor(pane, inset.getBottom());
		AnchorPane.setTopAnchor(pane, inset.getTop());
		AnchorPane.setLeftAnchor(pane, inset.getLeft());
		AnchorPane.setRightAnchor(pane, inset.getRight());
		pane.setScaleX(0);
		pane.setScaleY(0);
		this.getChildren().add(pane);
		
		Duration dur = Duration.seconds(0.5);
		ScaleTransition st = new ScaleTransition(dur);
		st.setNode(pane);
		st.setToX(1);
		st.setToY(1);
		st.play();
	}
	private Insets insetForPopupSize(PopupSize size) {
		
		switch(size){
		case FULL:
			return new Insets(32.0);
		case MEDIUM:
			return new Insets(128.0);
		case SMALL:
			double vInset = 128.0;
			double popHeight = (this.getHeight() - 2*vInset);
			double hInset = (this.getWidth() - popHeight)/2.0;
			return new Insets(vInset,hInset,vInset,hInset);
		default:
			return new Insets(0.0);
		}
	}

	private void closePaneWithAnimation(Pane pane){
		setDim(false, Duration.seconds(0.4));
		ScaleTransition s = new ScaleTransition(Duration.seconds(0.5));
		s.setNode(pane);
		s.setToX(0);
		s.setToY(0);
		s.play();
		s.setOnFinished(e -> this.getChildren().remove(pane));
	}
	
	
	/**
	 * purpose: to feed the GameData into all the subcomponents of authoring view
	 * @param gameData The object that holds all the 
	 */
	private void loadGameData(GameData data) {
		getChildren().clear();
		setupViews();
	}
	
	
	/**
	 * purpose: to retreive the data objects from various
	 * components and to integrate them into a GameData object.
	 * 
	 * @param gameData
	 */
	private void saveGameData() {
		XStreamSerializer x = new XStreamSerializer();
		String xml = x.getXMLStringFromObject(GameDataGenerator.getComplexSampleGame());
		try {
			DirectoryFileManager h = new ConcreteDirectoryFileHelper("games", "games1");
				h.addStringFileToDirectory(xml, "testfile");
		} catch (IllegalFileException | InvalidRootDirectoryException | IOException e) {
			e.printStackTrace();
		} 
	}


	/*
	 * PopViewDelegate
	 * @see ui.authoring.delegates.PopViewDelegate#openView(javafx.scene.layout.Pane)
	 */
	
	@Override
	public void openViewWithSize(Pane pane, PopupSize size) {
		openPaneWithAnimation(pane, size);		
	}
	
	@Override
	public void openView(Pane pane) {
		openPaneWithAnimation(pane, PopupSize.FULL);		
	}

	@Override
	public void closeView(Pane pane) {
		closePaneWithAnimation(pane);		
	}

	/*
	 * MenuDelegate
	 * @see ui.authoring.delegates.MenuDelegate#didPressBackButton()
	 */
	@Override
	public void didPressBackButton() {
		slideMenuOut();		
	}

	@Override
	public void didPressLoadButton() {
		FileChooser f = new FileChooser();
		
		
		//loadGameData();
		
	}

	private GameData getGameData() {
		return myGameData;
	}

	@Override
	public void didPressSaveButton() {
		saveGameData();
		
	}

	
}
