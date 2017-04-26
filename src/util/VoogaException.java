package util;

/**
 * @author sarahzhou
 *
 */
public class VoogaException extends Exception {
	
	public static final String ILLEGAL_UPDATE = "Illegal update";
	public static final String INVALID_ACTORTYPE = "Invalid actor type";
	public static final String INVALID_LOCATION = "Illegal location";
	public static final String NONEXISTANT_LEVEL = "Level does not exist";
	public static final String INVALID_GAMEDATA = "Invalid GameData file";
	
	private static final long serialVersionUID = 1L;
	
	public VoogaException(String message) {
		super(message);
	}
}
