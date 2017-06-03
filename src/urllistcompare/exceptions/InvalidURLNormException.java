/**
 * 
 */
package urllistcompare.exceptions;

/**
 * @author Rocco Barbini
 * @email roccobarbi@gmail.com
 * 
 * This exception should be thrown when the program is trying to use a URLNorm that has not been properly set
 * (e.g. where at least a format has not been defined).
 *
 */
@SuppressWarnings("serial")
public class InvalidURLNormException extends RuntimeException{

	/**
	 * 
	 */
	public InvalidURLNormException() {
		super("Invalid URLNorm!");
	}

	/**
	 * @param message	The error message
	 */
	public InvalidURLNormException(String message) {
		super(message);
	}

}
