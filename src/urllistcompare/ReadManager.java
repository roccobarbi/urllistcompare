/**
 * 
 */
package urllistcompare;

import java.io.File;
import java.io.IOException;
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
 * It provides a standard input interface for the "main" classes that are meant to be executed via a command line interface.
 * 
 * It is possible to use a ReadManager both with the dest already set (if it was passed via a command line argument to the
 * caller). The class does not assume that the argument was right and checks it by itself.
 * 
 * It works mainly through the static userInput method, which returns a CSVReader object which is only missing its destination
 * list (which can be created by the caller once it has all necessary formats).
 *
 */
public class ReadManager {

	private static final String DEFAULT_PROMPT = ">:"; // TODO: make it possible to provide a custom prompt by the caller
	private static final String VSEPARATORS = ".,;:_/|\"'"; // Used to validate non-escaped values for vSep
	
	/**
	 * Default constructor: it does nothing.
	 */
	public ReadManager() {
		// Static method only: do nothing
	}
	
	public static CSVReader userInput(String sourceName){
		CSVReader output = null;
		String prompt = DEFAULT_PROMPT; // TODO: make it possible to provide a custom prompt by the caller
		Scanner keyboard = new Scanner(System.in);
		URLFormat format = null;
		String fileName = "";
		char dSep = 0, tSep = 0, vSep = 0; // Separators
		int[] indexes = {-1, -1}; // urlI, impI
		boolean isTSep = false, headers = false;
		// Check the validity of the argument
		File theFile = new File(sourceName);
		if(!theFile.exists() || !theFile.canRead()){
			System.out.println("File: " + sourceName + " doesn't exist or can't be read.");
			theFile = null; // Use it only if it works
		} else {
			fileName = sourceName; // To improve compatibility later
		}
		// If the source file is not defined, keep asking 
		while(theFile == null){
			fileName = promptFileName(prompt, keyboard);
			theFile = new File(fileName);
			if(!theFile.exists() || !theFile.canRead()){
				System.out.println("File: " + fileName + " doesn't exist or can't be read.");
				theFile = null; // Use it only if it works
			}
		}
		// Get the format
		format = promptFormat(prompt, fileName);
		// Get the decimal separator
		dSep = promptDSep(prompt, fileName, keyboard);
		// Get the thousand separator, if needed
		if(promptIsTSep(prompt, fileName, keyboard)){
			isTSep = true;
			tSep = promptTSep(prompt, fileName, keyboard);
		} else {
			isTSep = false;
			tSep = 0;
		}
		// Get the value separator
		vSep = promptVSep(prompt, fileName, keyboard);
		// Get the indexes
		indexes = promptIndexes(prompt, fileName, keyboard, vSep);
		// Get the headers
		headers = promptHeaders(prompt, fileName, keyboard);
		// Create the CSVReader
		output = new CSVReader(headers, indexes[0], indexes[1], vSep, dSep, isTSep, tSep, theFile, format);
		return output;
	}
	
	/**
	 * 
	 * @param sourceName the filename for the source file
	 * @param extVSep the value separator, or 0 if not set
	 * @param extDSep the decimal separator, or 0 if not set
	 * @param extIsTSep boolean: true if there is a thousand separator, false means that there is none
	 * @param extTSep the external separator, or it must be prompted along with isTSep if 0
	 * @param headerSet boolean: true if extHeader is a valid input
	 * @param extHeader boolean: true if the file has a header
	 * @return
	 */
	public static CSVReader userInput(String sourceName, char extVSep, char extDSep, boolean extIsTSep, char extTSep, boolean headerSet, boolean extHeader){
		CSVReader output = null;
		String prompt = DEFAULT_PROMPT; // TODO: make it possible to provide a custom prompt by the caller
		Scanner keyboard = new Scanner(System.in);
		URLFormat format = null;
		String fileName = "";
		char dSep = 0, tSep = 0, vSep = 0; // Separators
		int[] indexes = {-1, -1}; // urlI, impI
		boolean isTSep = false, headers = false;
		// Check the validity of the argument
		File theFile = new File(sourceName);
		if(!theFile.exists() || !theFile.canRead()){
			System.out.println("File: " + sourceName + " doesn't exist or can't be read.");
			theFile = null; // Use it only if it works
		} else {
			fileName = sourceName; // To improve compatibility later
		}
		// If the source file is not defined, keep asking 
		while(theFile == null){
			fileName = promptFileName(prompt, keyboard);
			theFile = new File(fileName);
			if(!theFile.exists() || !theFile.canRead()){
				System.out.println("File: " + fileName + " doesn't exist or can't be read.");
				theFile = null; // Use it only if it works
			}
		}
		// Get the decimal separator
		if(extDSep == 0){
			dSep = promptDSep(prompt, fileName, keyboard);
		} else {
			dSep = extDSep;
		}
		// Get the thousand separator, if needed
		if(extTSep == 0 && promptIsTSep(prompt, fileName, keyboard)){
			isTSep = true;
			tSep = promptTSep(prompt, fileName, keyboard);
		} else {
			isTSep = extIsTSep;
			tSep = isTSep ? extTSep : 0;
		}
		// Get the value separator
		if(extVSep == 0){
			vSep = promptVSep(prompt, fileName, keyboard);
		} else {
			vSep = extVSep;
		}
		// Get the indexes
		indexes = promptIndexes(prompt, fileName, keyboard, vSep);
		// Get the headers
		if(headerSet){
			headers = extHeader;
		} else{
			headers = promptHeaders(prompt, fileName, keyboard);
		}
		// Get the format
		format = promptFormat(prompt, fileName);
		// Create the CSVReader
		output = new CSVReader(headers, indexes[0], indexes[1], vSep, dSep, isTSep, tSep, theFile, format);
		return output;
	}
	
	/**
	 * In case a filename is not provided, this version of userInput prompts the user for the initial file name.
	 * 
	 * @return a CSVReader object for a valid file
	 */
	public static CSVReader userInput(){
		String prompt = DEFAULT_PROMPT; // TODO: make it possible to provide a custom prompt by the caller
		Scanner keyboard = new Scanner(System.in);
		return userInput(promptFileName(prompt, keyboard));
	}
	
	// Utility method to prompt the user for a file name and check that it's not empty
	private static String promptFileName(String prompt, Scanner source){
		boolean keepAsking = true;
		String input = "";
		while(keepAsking){
			System.out.println("Please enter file name.");
			System.out.print(prompt + " ");
			input = source.nextLine();
			if(input.length() == 0){
				System.out.println("\nERROR: the file name cannot be an empty string.\n");
			} else {
				keepAsking = false;
			}
		}
		return input;
	}
	
	// Utility method that prompts the user for the format
	private static URLFormat promptFormat(String prompt, String fileName){
		System.out.println("\nPlease enter format for the file " + fileName);
		return URLFormat.inputFormat(prompt + " ");
		// TODO: this limitation must be applied by classes using the ReadManager
		// if(formats[0] == formats[1]){
		// 	System.out.println("The current version of this program only confronts URL lists of different formats.");
		// 	System.out.println("Aborting execution.");
		// 	System.exit(0);
		// }
	}
	
	// Utility method that prompts the user for the decimal separator
	private static char promptDSep(String prompt, String fileName, Scanner source){
		char dSep = 0;
		String input = "";
		boolean keepAsking = true;
		// Loop until the user provides a good decimal separator
		while(keepAsking){ // Prepped for better input validation
			System.out.println("Please enter the decimal separator for the file " + fileName);
			System.out.println(prompt + " ");
			input = source.nextLine();
			if(input.length() == 0){
				System.out.println("\nERROR: the input cannot be empty.\n");
			} else {
				dSep = input.charAt(0);
				keepAsking = false; // Prepped for better input validation
			}
		}
		return dSep;
	}
	
	// Utility method that checks if the user needs a thousand separator
	private static boolean promptIsTSep(String prompt, String fileName, Scanner source){
		String input = "";
		boolean output = false, keepAsking = true;
		while(keepAsking){
			System.out.println("Is there a thousand separator for the file " + fileName + "? [y|n]");
			System.out.println(prompt + " ");
			input = source.nextLine();
			if(input.length() == 0){
				System.out.println("\nERROR: the input cannot be empty.\n");
			} else {
				if(input.toLowerCase().charAt(0) == 'y'){
					output = true;
				}
				keepAsking = false;
			}
		}
		return output;
	}
	
	// Utility method that prompts the user for the thousand separator
	private static char promptTSep(String prompt, String fileName, Scanner source){
		char tSep = 0;
		String input = "";
		boolean keepAsking = true;
		// Loop until the user provides a good thousand separator
		while(keepAsking){ // Prepped for better input validation
			System.out.println("Please enter the thousand separator for the file " + fileName);
			System.out.println(prompt + " ");
			input = source.nextLine();
			if(input.length() == 0){
				System.out.println("\nERROR: the input cannot be empty.\n");
			} else {
				tSep = input.charAt(0);
				keepAsking = false; // Prepped for better input validation
			}
		}
		return tSep;
	}
	
	// Utility method that prompts the user for the value separator
	private static char promptVSep(String prompt, String fileName, Scanner source){
		char vSep = 0;
		String input = "";
		boolean keepAsking = true;
		// Loop until the user provides a good value separator
		while(keepAsking){
			System.out.println("Please enter the value separator for the file " + fileName);
			System.out.println(prompt + " ");
			input = source.nextLine();
			if(input.length() == 0){
				System.out.println("\nERROR: the input cannot be empty.\n");
			} else {
				if(VSEPARATORS.indexOf(input.charAt(0)) != -1){
					vSep = input.charAt(0);
					keepAsking = false;
				} else if(input.length() > 1 && input.charAt(0) == '\\'){
					switch(input.charAt(1)){ // Prepped to add more separators
					case 't':
						vSep = '\t';
						keepAsking = false;
						break;
					default:
						printVSepInstructions();	
					}
				} else {
					printVSepInstructions();
				}
			}
		}
		return vSep;
	}
	
	private static void printVSepInstructions(){
		// The choices are represented with a loop to allow for changes to the constant that is used to validate them
		System.out.print("Please choose a valid separator between the following: ");
		for(int k = 0; k < VSEPARATORS.length(); k++){
			System.out.print(" " + VSEPARATORS.charAt(k));
		}
		System.out.println(" \\t");
		System.out.println("\\t will be interpreted as a single tabulation.");
	}
	
	// Utility method that reads the file, shows the first few lines and prompts the user
	// for the indexes of the url and impressions.
	private static int[] promptIndexes(String prompt, String fileName, Scanner source, char vSep){
		int[] output = {-1, -1}; // urlI, impI
		Scanner reader = null;
		String splitHeader[] = null, input = null;
		int columns = 0; // To store the number of columns from the headers and implement a consistency check with the file
		int k = 0;
		boolean keepAsking = true;
		// Show the head of the file
		try{
			System.out.println("Trying to read file: " + fileName);
			reader = new Scanner(new File(fileName));
		} catch (IOException e) {
			System.out.println("Can't read from file " + fileName + ": " + e.getMessage());
			System.out.println("Aborting execution");
			System.exit(1);
		}
		System.out.println("Headers for file " + fileName + ":");
		k = 0;
		while(reader.hasNextLine() && k < 10){
			splitHeader = reader.nextLine().split(Character.toString(vSep)); // TODO: improve for doublequote-enclosed columns
			if(k == 0){
				columns = splitHeader.length;
				if(columns < 2){ // Minor coherence check: there must be at least 2 columns
					System.out.println("ERROR: there are less than 2 columns!");
					System.out.println("Aborting execution.");
					System.exit(0);
				}
			} else {
				// TODO: add a consistency check for the file AFTER doublequote-enclosing is accepted
			}
			for(int j = 0; j < splitHeader.length; j++){
				System.out.print(splitHeader[j] + "\t");
			}
			System.out.println();
			k++;
		}
		reader.close();
		// Loop until the user provides a good column index for the url
		keepAsking = true;
		while(keepAsking){ // Prepped for better input validation
			System.out.println("Enter the index for the column where the URL is stored, between 0 and " + (columns - 1) + ".");
			System.out.println(prompt + " ");
			input = source.nextLine();
			if(input.length() == 0){
				System.out.println("\nERROR: the input cannot be empty.\n");
			} else {
				try{
					if(Integer.parseInt(input) >= 0 && Integer.parseInt(input) < columns){
						output[0] = Integer.parseInt(input);
						keepAsking = false;
					}
				} catch	(Exception e) {
					System.out.println("ERROR: could not parse the index.");
					keepAsking = true;
				}
			}
		}
		// Loop until the user provides a good column index for the page impressions
		keepAsking = true;
		while(keepAsking){ // Prepped for better input validation
			System.out.println("Enter the index for the column where the page impressions are stored, between 0 and " + (columns - 1) + ".");
			System.out.println(prompt + " ");
			input = source.nextLine();
			if(input.length() == 0){
				System.out.println("\nERROR: the input cannot be empty.\n");
			} else {
				try{
					if(Integer.parseInt(input) >= 0 && Integer.parseInt(input) < columns){
						if(Integer.parseInt(input) != output[0]){
							output[1] = Integer.parseInt(input);
							keepAsking = false;
						} else {
							System.out.println("\nERROR: the page impression can't be in the same column as the url.\n");
						}
					}
				} catch	(Exception e) {
					System.out.println("ERROR: could not parse the index.");
					keepAsking = true;
				}
			}
		}
		return output;
	}
	
	// Utility method that prompts the user for the presence of a header line
	// TODO: manage multiple lines that need to be ignored
	private static boolean promptHeaders(String prompt, String fileName, Scanner source){
		boolean output = false;
		String input = "";
		boolean keepAsking = true;
		// Loop until the user provides a good answer
		while(keepAsking){ // Prepped for better input validation
			System.out.println("Is there a header line in the file " + fileName + "? [y|n]");
			System.out.println(prompt + " ");
			input = source.nextLine();
			if(input.length() == 0){
				System.out.println("\nERROR: the input cannot be empty.\n");
			} else {
				if(input.toLowerCase().charAt(0) == 'y'){
					output = true;
				} else {
					output = false;
				}
				keepAsking = false; // Prepped for better input validation
			}
		}
		return output;
	}

}