package gameengine.grid.exceptions;

public class InvalidIDException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public InvalidIDException(String exceptionMessage){
		super(exceptionMessage);
	}

}
