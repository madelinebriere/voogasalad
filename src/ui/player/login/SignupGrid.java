package ui.player.login;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.scene.text.Text;

public class SignupGrid extends DataEntryGrid{

	private Map<Text, String> entryMap;
	private ResourceBundle loginResource;
	private final String textField = "TextField";
	private final String passwordField = "PasswordField";
	
	@Override
	public Map<Text, String> getEntryMap() {
		return entryMap;
	}
	
	public SignupGrid(ResourceBundle resource){
		super(resource);
		loginResource = resource;
		entryMap = new LinkedHashMap<>();
		addValues();
		addToGrid(entryMap);
	}
	
	public void addValues(){
		entryMap.put(new Text(loginResource.getString("username")), textField);
		entryMap.put(new Text(loginResource.getString("password")), passwordField);
		entryMap.put(new Text(loginResource.getString("reenter")), passwordField);
		entryMap.put(new Text(loginResource.getString("email")), textField);
		entryMap.put(new Text(loginResource.getString("reemail")), textField);
	}
	
}