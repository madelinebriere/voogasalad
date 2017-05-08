package ui.authoring.display;

import java.util.HashMap;
import java.util.Map;
import gamedata.DisplayData;
import gamedata.GameData;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import ui.Preferences;
import ui.authoring.TexttoPosFactory;
import ui.authoring.delegates.DisplayDelegate;
import ui.authoring.delegates.PopViewDelegate;
import ui.general.CustomColors;
import ui.general.ImageButton;
import ui.general.StackButton;
import ui.general.UIHelper;
import util.Location;
public class DisplayView extends BorderPane {
	private PopViewDelegate popViewDelegate;
	private DisplayDelegate displayDelegate;
	private DisplayData myData;
	private GameData myGameData;
	private VBox myVBox;
	private DisplayMenu myMenu;
	private String menuLocs[]={"Top","Left","Right","Bottom"};
	private Map<String, ComboBox<String>> myPreferences;
	public DisplayView(PopViewDelegate delegate,GameData gameData) {
		this(delegate, gameData.getDisplayData(),gameData);
	}
	public DisplayView(PopViewDelegate delegate, DisplayData data, GameData gameData){
		super();
		popViewDelegate = delegate;
		myData = data;
		myGameData=gameData;
		myPreferences = new HashMap<>();
		setupViews();
	}
	private void getPositionComboBox(String title){
		ComboBox<String> posChoice=new ComboBox<String>();
		for(String s :menuLocs){
		posChoice.getItems().add(s);
		}
		posChoice.valueProperty().addListener((x, y, newValue) -> {
			myData.setLocation(newValue);
			TexttoPosFactory.resetCardinalDirections(this);
			TexttoPosFactory.updateMenuPosition(this, newValue, myMenu);
		});
		myVBox.getChildren().add(makeField(title, posChoice));
		myPreferences.put(title, posChoice);
	}
	public void updateDisplayMenu(){
	myMenu.updateDisplayList();
	}
	private void getWidthPrompt(){
		TextField field = new TextField();
		field.textProperty().addListener(new ChangeListener<String>() {
	        @Override
	        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
	            if (!newValue.matches("\\d*")) {
	                field.setText(newValue.replaceAll("[^\\d]", ""));
	            }
	            try {
	            	int newWidth=Integer.parseInt(newValue);
	            	myData.setWidth(newWidth);
	            	myMenu.setWidth(newWidth);
	            } catch (Exception e) {}
	        }
	    });
		field.setMaxWidth(80);
		myVBox.getChildren().add(makeField("Width", field));
	}
	private void getHeightPrompt(){
		TextField field = new TextField();
		field.textProperty().addListener(new ChangeListener<String>() {
	        @Override
	        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
	            if (!newValue.matches("\\d*")) {
	                field.setText(newValue.replaceAll("[^\\d]", ""));
	            }
	            try {
	            	int newHeight=Integer.parseInt(newValue);
	            	myData.setHeight(newHeight);
	            	myMenu.setHeight(newHeight);
	            } catch (Exception e) {}
	        }
	    });
		field.setMaxWidth(80);
		myVBox.getChildren().add(makeField("Height", field));
	}
	private StackButton makeField(String title, Node entry) {
		StackButton button = new StackButton();
		button.setShadow();
		Label label = new Label(title);
		label.setFont(Preferences.FONT_SMALL_BOLD);
		label.setTextFill(Color.WHITE);
		label.setPrefWidth(136);
		label.setAlignment(Pos.CENTER_LEFT);
		button.setIconNode(label);
		button.setIconInsets(new Insets(8, 8, 8, 8));
		button.setCenterNode(entry);
		button.setCenterInsets(new Insets(8, 8, 8, 200));
		button.setHeight(56);
		return button;
	}
	public DisplayData getData() {	
		return myData;
	}
	private void setupBackButton() {
		ImageButton b = new ImageButton("back_icon.png", new Location(30., 30.));
		AnchorPane.setTopAnchor(b, 4.0);
		AnchorPane.setLeftAnchor(b, 4.0);
		b.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> popViewDelegate.closeView(this));
		this.getChildren().add(b);
	}
	private void setupViews() {
		ScrollPane leftSide = new ScrollPane();
		setupScrollPane(leftSide);
		setupVBox(leftSide);
		setupBackButton();	
	}
	private void setupScrollPane(ScrollPane leftSide) {
		double inset = 12.0;
		leftSide.setStyle("-fx-background-color: #" + UIHelper.colorToHex(CustomColors.BLUE_50) + ";");
		leftSide.setStyle("-fx-background: #" + UIHelper.colorToHex(CustomColors.BLUE_50) + ";");
		leftSide.setBorder(new Border(new BorderStroke[] {new BorderStroke(Color.TRANSPARENT, BorderStrokeStyle.SOLID, new CornerRadii(4), BorderWidths.DEFAULT) }));
		leftSide.setHbarPolicy(ScrollBarPolicy.NEVER);
		leftSide.setVbarPolicy(ScrollBarPolicy.NEVER);
		UIHelper.setDropShadow(leftSide);
		leftSide.prefWidthProperty().bind(this.widthProperty().divide(3.0).subtract(inset * 3 / 2));
		this.setCenter(leftSide);
		 myMenu=new DisplayMenu(myData,myGameData);
		this.setLeft(myMenu.getNode());
	}
	private void setupVBox(ScrollPane pane) {
		myVBox = new VBox();
		myVBox.setAlignment(Pos.CENTER);
		myVBox.prefWidthProperty().bind(pane.widthProperty().add(-2));
		pane.setContent(myVBox);
	getWidthPrompt();
	getHeightPrompt();
	getPositionComboBox("Tower Menu Position");
	}
}