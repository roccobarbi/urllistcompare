/**
 * 
 */
package urllistcompare.exceptions;

/**
 * @author rocco barbini (roccobarbi@gmail.com)
 *
 */
public class ConfigurationFormatException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2580284357704875766L;

	public ConfigurationFormatException() {
		this("Error in the configuration format!");
	}
	
	public ConfigurationFormatException(String message) {
		super(message);
	}
}
