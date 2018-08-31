/**
 * 
 */
package urllistcompare.exceptions;

/**
 * @author rocco barbini (roccobarbi@gmail.com)
 *
 */
public class WrongExtensionException extends Exception {
	
	public WrongExtensionException() {
		this("Wrong file extension!");
	}
	
	public WrongExtensionException(String message) {
		super(message);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 3487973145728530959L;

}
