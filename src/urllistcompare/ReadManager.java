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
		File file = null;
		String prompt = DEFAULT_PROMPT; // TODO: make it possible to provide a custom prompt by the caller
		Scanner keyboard = new Scanner(System.in);
		URLFormat format = null;
		String fileName = "";
		char dSep = 0, tSep = 0, vSep = 0; // Separators
		// Check the validity of the argument
		File theFile = new File(sourceName);
		if(!theFile.exists() || !theFile.canRead()){
			System.out.println("File: " + sourceName + " doesn't exist or can't be read.");
			theFile = null; // Use it only if it works
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
			tSep = promptTSep(prompt, fileName, keyboard);
		}
		// TODO: complete rewrite of the function
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
		while(keepAsking){ // Prepped for bette input validation
			System.out.println("Please enter the decimal separator for the file " + fileName);
			System.out.println(prompt + " ");
			input = source.nextLine();
			if(input.length() == 0){
				System.out.println("\nERROR: the file name cannot be an empty string.\n");
			} else {
				dSep = input.charAt(0);
				keepAsking = false; // Prepped for bette input validation
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
				System.out.println("\nERROR: the file name cannot be an empty string.\n");
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
		// Loop until the user provides a good decimal separator
		while(keepAsking){ // Prepped for bette input validation
			System.out.println("Please enter the thousand separator for the file " + fileName);
			System.out.println(prompt + " ");
			input = source.nextLine();
			if(input.length() == 0){
				System.out.println("\nERROR: the file name cannot be an empty string.\n");
			} else {
				tSep = input.charAt(0);
				keepAsking = false; // Prepped for bette input validation
			}
		}
		return tSep;
	}

}