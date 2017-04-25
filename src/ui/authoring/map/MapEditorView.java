package ui.authoring.map;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;

import gamedata.MapLayersData;
import gamedata.PathData;
import gamedata.map.LayerData;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import ui.Preferences;
import ui.authoring.PopupSize;
import ui.authoring.delegates.LayerViewDelegate;
import ui.authoring.delegates.PopViewDelegate;
import ui.authoring.map.layer.Layer;
import ui.authoring.map.layer.LayerPopupDelegate;
import ui.authoring.map.layer.LayerPopupView;
import ui.authoring.map.layer.PolygonLayerView;
import ui.authoring.map.layer.PathLayerView;
import ui.general.CustomColors;
import ui.general.ImageViewPane;
import ui.general.UIHelper;


/**
 * 
 * Creates and stores the PathData object
 * 
 * @author TNK
 *
 */
public class MapEditorView extends StackPane implements LayerViewDelegate, LayerPopupDelegate{

	private final String DEFAULT_BACKGROUND_PATH = "default_map_background_0.jpg";
	private static final Color[] LAYER_COLORS = {
			CustomColors.AMBER,
			CustomColors.BLUE_500,
			CustomColors.GREEN,
			CustomColors.INDIGO
	};

	private ImageViewPane myBackgroundView;
	private PathLayerView myPathLayer;
	private MapLayersData myMapData;
	private List<Layer> myLayers = new ArrayList<>();
	private HBox myLayerPicker;
	private PopViewDelegate myPopDelegate;

	private Pane myLayerPopup;

	public MapEditorView(PathData pathData , MapLayersData mapData, PopViewDelegate popDelegate) { 
		super();
		myBackgroundView = new ImageViewPane(new ImageView(new Image(DEFAULT_BACKGROUND_PATH)));
		myPathLayer = new PathLayerView(pathData, myBackgroundView.getImageInsets());
		myMapData = mapData;
		myPopDelegate = popDelegate;
		setupViews();
		setupMouseEvents();
		this.widthProperty().addListener(e -> sizeDidChange());
	}

	private void setupMouseEvents() {
		this.myBackgroundView.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> this.getScene().setCursor(Cursor.CROSSHAIR));
		this.myBackgroundView.addEventHandler(MouseEvent.MOUSE_EXITED, e -> this.getScene().setCursor(Cursor.DEFAULT));
		this.addEventHandler(KeyEvent.KEY_RELEASED, e -> {
			if(e.getCode().equals(KeyCode.ENTER)){
				printData();
			}
		});
	}

	private void setupViews() {
		UIHelper.setBackgroundColor(myBackgroundView, CustomColors.GREEN);
		StackPane.setAlignment(myBackgroundView, Pos.TOP_CENTER);
		StackPane.setMargin(myBackgroundView, new Insets(8,8,72,8));
		this.getChildren().add(myBackgroundView);

		setupButtons();
		setupLayerSelector();
		
		addLayer(myPathLayer, "Path");
		
		setupMapData();
	}
	
	/**
	 * adds the already existing layers in the mapdata into this classes children nodes
	 */
	private void setupMapData() {
		for(Entry<String, LayerData> entry :myMapData.getMyLayers().entrySet()){
			addLayer(new PolygonLayerView(entry.getValue()),entry.getKey());
		}
		
	}
	
	/**
	 * This adds a layer that spans the size of the backgroundView.
	 * It will able to switched to by clicking the button on the
	 * HBox on the bottom of the MapEditor
	 * @param layer
	 * @param layerName the name that you can set to w.e you want
	 */
	private void addLayer(Layer layer, String layerName){
		StackPane.setAlignment(layer, Pos.TOP_CENTER);
		StackPane.setMargin(layer, new Insets(8,8,72,8));
		layer.setColor(LAYER_COLORS[myLayers.size()%LAYER_COLORS.length]);
		Label layerNumber = new Label(layerName);
		layerNumber.setFont(Preferences.FONT_MEDIUM_BOLD);
		layerNumber.setTextFill(CustomColors.GREEN_900);
		StackPane.setMargin(layerNumber, new Insets(8));
		StackPane layerIcon = UIHelper.buttonStack(e -> switchToLayer(layer), Optional.of(layerNumber), Optional.ofNullable(null),Pos.CENTER, false);
		HBox.setMargin(layerIcon, new Insets(8));
		UIHelper.setBackgroundColor(layerIcon, CustomColors.GREEN_100);
		myLayerPicker.getChildren().add(myLayerPicker.getChildren().size() - 1, layerIcon);
		layer.setOpacity(0.8);
		this.getChildren().add(layer);
		this.myLayers.add(layer);
		switchToLayer(layer);
	}
	
	/**
	 * Switches off all of the layers that aren't visible
	 * @param layer the layer that should be visible to the user
	 */
	private void switchToLayer(Layer layer){
		
		myLayers.forEach( l -> {
			if(l == layer){
				l.setOpacity(0.75);
				l.activate();
				getChildren().remove(layer);
				getChildren().add(layer);
				
			}else{
				l.setOpacity(0.15);
				l.deactivate();

			}
		});
		
	}

	private void setupLayerSelector() {
		//setup HBox
		myLayerPicker = new HBox();
		//add button that switches path maker
		StackPane.setAlignment(myLayerPicker, Pos.BOTTOM_CENTER);
		StackPane.setMargin(myLayerPicker, new Insets(8,192,8,8));
		myLayerPicker.setMaxHeight(56);
		ScrollPane scroll = new ScrollPane();
		scroll.setContent(myLayerPicker);
		//TODO scrollpane
		UIHelper.setBackgroundColor(myLayerPicker, CustomColors.GREEN);
		//add new layer button
		addNewLayerButton();
		this.getChildren().add(myLayerPicker);
		
	}
	
	private void addNewLayerButton() {
		ImageView img = new ImageView(new Image("add_icon_w.png"));
		img.setFitHeight(32);
		img.setPreserveRatio(true);
		StackPane button = UIHelper.buttonStack(e -> didClickNewLayerButton(), Optional.ofNullable(null), Optional.of(img),
				Pos.CENTER, true);
		UIHelper.setBackgroundColor(button, Color.TRANSPARENT);
		this.myLayerPicker.getChildren().add(button);
	}

	private void didClickNewLayerButton(){
		launchLayerPopup();
	}

	private void launchLayerPopup() {
		this.myLayerPopup = new LayerPopupView(this);
		this.myPopDelegate.openViewWithSize(myLayerPopup, PopupSize.SMALL);
	}

	private void setupButtons() {
		List<StackPane> panes = new ArrayList<>();

		ImageView backImage = makeImageFromString("undo_icon.png");
		StackPane b = UIHelper.buttonStack(
				e -> myLayers.stream().filter(
						layer -> layer.isActive()).findFirst().ifPresent(
								layer -> layer.undo()), 
				Optional.ofNullable(null), 
				Optional.of(backImage),
				Pos.CENTER, true);
		StackPane.setMargin(b, new Insets(12));
		panes.add(b);

		ImageView clearImage = makeImageFromString("clear_icon.png");
		StackPane c = UIHelper.buttonStack(
				e -> myLayers.stream().filter(
						layer -> layer.isActive()).findFirst().ifPresent(
								layer -> layer.clear()), 
				Optional.ofNullable(null),
				Optional.of(clearImage), Pos.CENTER_RIGHT, true);
		StackPane.setMargin(c, new Insets(0, 72, 12, 0));
		panes.add(c);

		ImageView BGImage = makeImageFromString("background_icon.png");
		StackPane d = UIHelper.buttonStack(e -> toggleBackground(e), Optional.ofNullable(null), Optional.of(BGImage),
				Pos.CENTER_RIGHT, true);
		StackPane.setMargin(d, new Insets(0, 132, 12, 0));
		panes.add(d);

		for (StackPane s : panes) {
			s.setMaxSize(40, 40);
			StackPane.setAlignment(s, Pos.BOTTOM_RIGHT);
			UIHelper.setBackgroundColor(s, CustomColors.GREEN_100);
			this.getChildren().add(s);
		}
	}

	private ImageView makeImageFromString(String toMake) {
		ImageView IV = new ImageView(new Image(toMake));
		IV.setFitWidth(32);
		IV.setPreserveRatio(true);
		return IV;
	}

	private void toggleBackground(MouseEvent e) {
		e.consume();
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Select Image File");
		fileChooser.getExtensionFilters().add(new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
		File selectedFile = fileChooser.showOpenDialog(this.getScene().getWindow());
		if (selectedFile != null) {
			myBackgroundView.getImageView().setImage(new Image(selectedFile.getName()));
		}
	}


	
	/**
	 * This method updates the location of the points on the map
	 * This method is needed because the scaling of the points will 
	 * change as the user resizes the map
	 */
	private void sizeDidChange() {
		for(Layer layer :myLayers){
			layer.sizeDidChange(myBackgroundView);
		}
	}

	
	/*
	 * LayerViewDelegate
	 * @see ui.authoring.delegates.LayerViewDelegate#removeLayerView(ui.authoring.map.layer.Layer)
	 */
	@Override
	public void removeLayerView(Layer layerView) {
		// TODO Auto-generated method stub
//		this.getChildren().remove(layerView);
//		this.myLayers.remove(layerView);
//		this.myLayerPicker.getChildren().removeIf(filter);
	}

	/*
	 * LayerPopupDelegate
	 * @see ui.authoring.map.LayerPopupDelegate#layerPopupDidPressConfirm(java.lang.String)
	 */
	@Override
	public void layerPopupDidPressConfirm(String nameInput) {
		LayerData data = new LayerData();
		PolygonLayerView layer = new PolygonLayerView(data);
		this.myMapData.addLayer(nameInput, data);
		this.addLayer(layer, nameInput);
		myPopDelegate.closeView(myLayerPopup);
	}

	@Override
	public void layerPopupDidPressCancel() {
		myPopDelegate.closeView(myLayerPopup);		
	}
	
	
	
	
	private void printData(){
		System.out.println(this.myPathLayer.getMyPathData());
		System.out.println(this.myMapData);
	}

}
