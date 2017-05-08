package ui.authoring.display;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import gamedata.DisplayData;
import gamedata.GameData;
import gamedata.LineageData;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import gamedata.ActorData;
///This entire file is part of my masterpiece.
//Alex Blumenstock
// This class is a representation of the menu of placeable actors that the player will have access to
//in the Game Player.  The menu observes GameData's Collection of LineageData's and checks
//each for the placeable field.  If this field is true, the addActor() method is called, which
//adds the actor to an ActorData list and maps the ActorData to its image. It then adds each of the
//ImageViews in the valueset of the map to a ListView for display.  The Cellfactory shrinks each image to a manageable display size
// and allows for dynamic rearranging of both the list and the listView merely by dragging.  This graphical reordering
//is very author-friendly,instead of being bound by the order that the actors were instantiated.
//the public setter methods at the bottom allow for dynamic resizing of the menu (They are called in the DisplayView class).
//Each time the menu is opened, it evaluates all actors to see if any have gained or lost their placeable status.
public class DisplayMenu {
	private DisplayData myData;
	private List<ActorData> myActors;
	private Map<ActorData, ImageView> actorMap;
	private ListView<ImageView> displayList;
	private boolean success;
	private GameData myGameData;
	private final ObjectProperty<ListCell<ImageView>> dragSource = new SimpleObjectProperty<>();

	public DisplayMenu(DisplayData data, GameData gameData) {
		setupData(data, gameData);
		initializeListView();
		actorMap = new LinkedHashMap<ActorData, ImageView>();
		getPlaceable(gameData);
		setupCellFactory();
		myActors = new ArrayList<ActorData>(actorMap.keySet());
		data.setActorOrder(myActors);
	}

	private void setupData(DisplayData data, GameData gameData) {
		myData = data;
		myGameData = gameData;
		myGameData.setDisplayData(myData);
	}

	private void initializeListView() {
		displayList = new ListView<ImageView>();
		displayList.setMaxWidth(100);
		displayList.setMaxHeight(400);
	}

	private void getPlaceable(GameData gameData) {
		Collection<LineageData> lineage = gameData.getLineageData();
		for (LineageData lineageData : lineage) {
			ActorData actor = lineageData.getProgenitor();
			if (actor.getType().isPlaceable()) {
				addActor(actor);
			}
		}
	}

	private void setupCellFactory() {
		displayList.setCellFactory(d -> {
			ListCell<ImageView> cell = new ListCell<ImageView>() {
				@Override
				public void updateItem(ImageView item, boolean empty) {
					resizeImage(item);
					super.updateItem(item, empty);
					setGraphic(item);
				}
			};
			cell.setOnDragDetected(event -> {
				onDrag(cell);
			});
			cell.setOnDragOver(event -> {
				dragOver(event);
			});
			cell.setOnDragDropped(event -> {
				switchItems(event, cell);
			});
			cell.setScaleShape(true);
			return cell;
		});
	}

	private void resizeImage(ImageView item) {
		if (item != null) {
			item.fitWidthProperty().bind(displayList.maxWidthProperty().subtract(10));
			item.fitHeightProperty().bind(displayList.maxHeightProperty().divide(10));
		}
	}

	private void dragOver(DragEvent event) {
		Dragboard db = event.getDragboard();
		if (db.hasImage()) {
			event.acceptTransferModes(TransferMode.MOVE);
		}
	}

	private void switchItems(DragEvent event, ListCell<ImageView> cell) {
		Dragboard db = event.getDragboard();
		if (db.hasImage())
			rearrangeOrder(cell, db);
		event.setDropCompleted(success);
	}

	private void rearrangeOrder(ListCell<ImageView> cell, Dragboard db) {
		ObservableList<ImageView> items = displayList.getItems();
		int draggedIdx = listIndexOf(items, db.getImage());
		int thisIdx = items.indexOf(cell.getItem());
		ActorData temo = myActors.get(thisIdx);
		ImageView temp = items.get(thisIdx);
		myActors.set(thisIdx, myActors.get(draggedIdx));
		myActors.set(draggedIdx, temo);
		items.set(thisIdx, new ImageView(db.getImage()));
		items.set(draggedIdx, temp);
		success = true;
	}

	private int listIndexOf(List<ImageView> list, Image image) {
		for (ImageView i : list) {
			Image current = i.getImage();
			if (testEquality(current, image)) {
				return list.indexOf(i);
			}
		}
		return -1;
	}

	private void onDrag(ListCell<ImageView> cell) {
		if (!cell.isEmpty()) {
			Dragboard db = cell.startDragAndDrop(TransferMode.MOVE);
			ClipboardContent cc = new ClipboardContent();
			cc.putImage(cell.getItem().getImage());
			db.setContent(cc);
			dragSource.set(cell);
		}
	}

	private boolean testEquality(Image a, Image b) {
		for (int i = 0; i < a.getWidth(); i++) {
			for (int j = 0; j < a.getHeight(); j++) {
				if (!a.getPixelReader().getColor(i, j).equals(b.getPixelReader().getColor(i, j))) {
					return false;
				}
			}
		}
		return true;
	}

	private void addActor(ActorData actor) {
		actorMap.put(actor, new ImageView(new Image(actor.getImagePath())));
		displayList.getItems().add(actorMap.get(actor));
		myActors.add(actor);
	}

	public void updateDisplayList() {
		addPlaceable();
		removeNonPlaceable();
	}

	private void addPlaceable() {
		for (LineageData lineage : myGameData.getLineageData()) {
			if (lineage.getProgenitor().getType().isPlaceable() && !actorMap.containsKey(lineage.getProgenitor())) {
				addActor(lineage.getProgenitor());
			}
		}
	}

	private void removeNonPlaceable() {
		Collection<ActorData> actors = new HashSet<ActorData>(actorMap.keySet());
		for (ActorData actor : actors) {
			if (!actor.getType().isPlaceable()) {
				removeActor(actor);
			}
		}
	}

	private void removeActor(ActorData actor) {
		displayList.getItems().remove(actorMap.get(actor));
		actorMap.remove(actor);
	}

	public ListView<ImageView> getNode() {
		return displayList;
	}

	public void setWidth(double d) {
		displayList.setMaxWidth(d);
	}

	public void setHeight(double d) {
		displayList.setMaxHeight(d);
	}

}
