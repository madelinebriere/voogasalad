package gameengine.grid.exceptions;
/**
 * 
 * @author Gideon
 *
 * This exception is thrown when more than one actor shares the same ID
 * as well as when someone tries to access an actor that is currently not on the grid
 */
public class InvalidIDException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public InvalidIDException(String exceptionMessage){
		super(exceptionMessage);
	}

}
