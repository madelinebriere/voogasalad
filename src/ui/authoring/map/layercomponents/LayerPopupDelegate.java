package ui.authoring.map.layercomponents;

/**
 * 
 * @author TNK
 *
 */
public interface LayerPopupDelegate {
	public abstract void layerPopupDidPressConfirm(String nameInput);
	public abstract void layerPopupDidPressCancel();
}
