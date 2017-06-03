/**
 * 
 */
package urllistcompare.exceptions;

/**
 * @author Rocco Barbini
 * @email roccobarbi@gmail.com
 * 
 * This exception should be thrown when the program is trying to use an invalid URL (e.g. a URLElement with url == null).
 *
 */
@SuppressWarnings("serial")
public class InvalidUrlException extends RuntimeException {

	/**
	 * 
	 */
	public InvalidUrlException() {
		super("Invalid URL!");
	}

	/**
	 * @param message	The error message
	 */
	public InvalidUrlException(String message) {
		super(message);
	}

}
