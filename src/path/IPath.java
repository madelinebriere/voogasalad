package path;

import java.util.List;

/**
 * Interface for path object
 * @author Anh
 *
 */
public interface IPath {
	/**
	 * return type default to int for now, change it later
	 * @return
	 */
	int getStart();
	List<Integer> getSuccessor(); // might take in a Actor object as argument 
	int getEnd();
}
