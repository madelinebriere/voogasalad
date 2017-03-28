package ui.authoring.level;

import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import ui.general.CustomColors;
import ui.general.Frame;
import ui.general.UIView;

public class LevelEditorView extends UIView{
	
	public LevelEditorView(Frame frame){
		super(frame);
		this.setBackground(new Background(new BackgroundFill[] { new BackgroundFill(CustomColors.GREEN, new CornerRadii(4), Insets.EMPTY)}));

	}

}
