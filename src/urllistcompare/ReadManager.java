/**
 * 
 */
package urllistcompare;

import java.io.File;
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
		boolean done = false, keepAsking = true;
		Scanner keyboard = new Scanner(System.in);
		// temp variables to store the values needed to create each reader
		URLFormat tempFormat = null;
		boolean tempHeader = false, tempIsTSep = false;
		char tempTSep = 0, tempDSep = 0, tempVSep = 0;
		int tempUrlI = 0, tempImpI = 0;
		String fileName = "", input = "";
		File file = null;
		// Loop the formats and create the URLList
		for(int i = 0; i < QTY_READERS; i++){
			format[i] = URLFormat.inputFormat(PROMPT);
		}
		dest = new URLList(format[0], format[1]);
		// Loop to create each reader
		for(int i = 0; i < QTY_READERS; i++){
			// fileName
			keepAsking = true;
			while(keepAsking){
				// Prompt the user
				System.out.println("Enter the name of the file number " + i + ",\n" +
						"with format " + format[i].getFormatSample() + "\n" +
						"then press ENTER!"); 
				System.out.print(PROMPT);
				input = keyboard.nextLine();
				if(input.length() > 0){ // Otherwise keep looping
					file = new File(input);
					if(file.exists() && file.canRead()){
						keepAsking = false;
					} else {
						System.out.println("The file does not exist or it can't be read!");
					}
				}
			}
			// headers
			keepAsking = true;
			while(keepAsking){
				// Prompt the user
				System.out.println("Does the file have a header line? [y/n]"); 
				System.out.print(PROMPT);
				input = keyboard.nextLine();
				if(input.length() > 0){ // Otherwise keep looping
					char selector = input.toLowerCase().charAt(0);
					switch(selector){
					case 'y':
						tempHeader = true;
						keepAsking = false;
						break;
					case 'n':
						tempHeader = false;
						keepAsking = false;
						break;
					default:
						System.out.println("Invalid selection, please try again!");
					}
				}
			}
			// thousand separator
			keepAsking = true;
			while(keepAsking){
				// Prompt the user
				System.out.println("Does the file have a thousand separator? [y/n]"); 
				System.out.print(PROMPT);
				input = keyboard.nextLine();
				if(input.length() > 0){ // Otherwise keep looping
					char selector = input.toLowerCase().charAt(0);
					switch(selector){
					case 'y':
						tempIsTSep = true;
						keepAsking = false;
						break;
					case 'n':
						tempIsTSep = false;
						keepAsking = false;
						break;
					default:
						System.out.println("Invalid selection, please try again!");
					}
				}
			}
			// thousand separator
			keepAsking = true;
			while(keepAsking && tempIsTSep){
				// Prompt the user
				System.out.println("Choose a thousand separator:");
				System.out.println("1 : .");
				System.out.println("2 : ,");
				System.out.print(PROMPT);
				input = keyboard.nextLine();
				if(input.length() > 0){ // Otherwise keep looping
					char selector = input.toLowerCase().charAt(0);
					switch(selector){
					case '1':
						tempTSep = '.';
						keepAsking = false;
						break;
					case '2':
						tempTSep = '.';
						keepAsking = false;
						break;
					default:
						System.out.println("Invalid selection, please try again!");
					}
				}
			}
			// decimal separator
			keepAsking = true;
			while(keepAsking){
				// Prompt the user
				System.out.println("Choose a decimal separator:");
				System.out.println("1 : .");
				System.out.println("2 : ,");
				System.out.print(PROMPT);
				input = keyboard.nextLine();
				if(input.length() > 0){ // Otherwise keep looping
					char selector = input.toLowerCase().charAt(0);
					switch(selector){ //TODO: check that it's not the same as the thousand separator
					case '1':
						tempDSep = '.';
						keepAsking = false;
						break;
					case '2':
						tempDSep = '.';
						keepAsking = false;
						break;
					default:
						System.out.println("Invalid selection, please try again!");
					}
				}
			}
			// value separator
			keepAsking = true;
			while(keepAsking){
				// Prompt the user
				System.out.println("Choose a value separator:");
				System.out.println("1 : ;");
				System.out.println("2 : ,");
				System.out.println("3 : \t");
				System.out.print(PROMPT);
				input = keyboard.nextLine();
				if(input.length() > 0){ // Otherwise keep looping
					char selector = input.toLowerCase().charAt(0);
					switch(selector){ //TODO: check that it's not the same as the thousand separator AND use an enumeration
					case '1':
						tempVSep = ';';
						keepAsking = false;
						break;
					case '2':
						tempVSep = '.';
						keepAsking = false;
						break;
					case '3':
						tempVSep = '\t';
						keepAsking = false;
						break;
					default:
						System.out.println("Invalid selection, please try again!");
					}
				}
			}
			// Choose the columns that will be parsed
			System.out.println("Which column is used for the page name/url?");
			System.out.print(PROMPT);
			tempUrlI = keyboard.nextInt();
			keyboard.nextLine();
			System.out.println("Which column is used for the page impressions?");
			System.out.print(PROMPT);
			tempImpI = keyboard.nextInt();
			keyboard.nextLine();
			// Create the CSVReader
			if(tempIsTSep){
				reader[i] = new CSVReader(tempHeader, tempUrlI, tempImpI, tempVSep, tempDSep, tempIsTSep, tempTSep, file, format[i]);
			} else {
				reader[i] = new CSVReader(tempHeader, tempUrlI, tempImpI, tempVSep, tempDSep, tempIsTSep, file, format[i]);
			}
		}
		done = true;
		return done;
	}

}
