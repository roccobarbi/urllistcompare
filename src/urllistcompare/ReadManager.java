/**
 * 
 */
package urllistcompare;

import java.util.Scanner;

/**
 * @author Rocco Barbini (roccobarbi@gmail.com)
 * 
 * This class is tasked with managing the whole reading process:
 * - it prompts the user with the information needed to set up the whole process;
 * - it creates the URLList;
 * - it creates the readers;
 * - it actually runs the readers. 
 *
 */
public class ReadManager {

	private final int QTY_READERS = 2;
	private final String PROMPT = ">: ";
	private CSVReader[] reader;
	private URLList dest;
	private URLFormat[] format;
	
	/**
	 * Default constructor: it does almost nothing.
	 */
	public ReadManager() {
		reader = null;
		dest = null;
		format = null;
	}
	
	public boolean userInput(){
		boolean done = false;
		Scanner keyboard = new Scanner(System.in);
		// temp variables to store the values needed to create each reader
		URLFormat tempFormat = null;
		boolean tempHeader = false, tempIsTSep = false;
		char tempTSep = 0, tempDSep = 0, tempVSep = 0;
		int tempUrlI = 0, tempImpI = 0;
		String fileName = "";
		// Loop the formats and create the URLList
		for(int i = 0; i < QTY_READERS; i++){
			format[i] = URLFormat.inputFormat(PROMPT);
		}
		dest = new URLList(format[0], format[1]);
		// Loop to create each reader
		return done;
	}

}
