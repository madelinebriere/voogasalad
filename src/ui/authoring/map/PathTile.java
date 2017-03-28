package ui.authoring.map;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public abstract class PathTile extends StackPane{
	
	private ImageView myImageView;
	private String myImagePath;

	public PathTile(String imagePath, double width, double height){
		super();
		myImageView = new ImageView(new Image(imagePath));
		myImageView.setFitWidth(width);
		myImageView.setFitHeight(height);
		myImagePath = imagePath;
		this.getChildren().add(myImageView);
		
	}
	
	public String getImage() {
		return myImagePath;
	}

	public void setImageWithPath(String path) {
		myImagePath = path;
		myImageView.setImage(new Image(path));
	}
	
}
