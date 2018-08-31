/**
 * 
 */
package urllistcompare.exceptions;

/**
 * @author rocco barbini (roccobarbi@gmail.com)
 *
 */
public class ConfigurationFormatException extends Exception {

	public ConfigurationFormatException() {
		this("Error in the configuration format!");
	}
	
	public ConfigurationFormatException(String message) {
		super(message);
	}
}
