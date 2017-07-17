/**
 * 
 */
package urllistcompare;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import urllistcompare.unittests.URLFormatTest;

/**
 * @author Rocco Barbini (roccobarbi@gmail.com)
 * 
 * This class provides a command line interface that allows users to compare two lists of URLs and check if
 * some are missing from one file or the other.
 *
 */
public class CheckMissing {
	
	private static final int CARDINALITY = 2;
	
	private static File theFile[] = new File[CARDINALITY];
	private static URLFormat[] format = new URLFormat[CARDINALITY];
	private static boolean[] isTSep = new boolean[CARDINALITY]; // true if the user sets a thousand separator
	private static char[] dSep = new char[CARDINALITY];
	private static char[] tSep = new char[CARDINALITY];
	private static int[] urlI = new int[CARDINALITY];
	private static int[] impI = new int[CARDINALITY];
	public static char[] vSep = new char[CARDINALITY];
	private static boolean[] headers = new boolean[CARDINALITY];
	private static URLList list = null;
	private static CSVReader[] reader = new CSVReader[CARDINALITY];
	private static ArrayList<URLElement>[] elements = new ArrayList[CARDINALITY];

	/**
	 * Empty: this class only provides a main argument.
	 */
	public CheckMissing() {
		// Do nothing
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Initialisation
		for(int i = 0; i < CARDINALITY; i++){
			theFile[i] = null;
			format[i] = null;
			isTSep[i] = false; // true if the user sets a thousand separator
			tSep[i] = 0;
			dSep[i] = 0;
			urlI[i] = -1;
			impI[i] = -1;
			vSep[i] = 0;
			headers[i] = false;
			reader[i] = null;
			elements[i] = new ArrayList<URLElement>();
			
		}
		// Check the arguments
		if(!checkArguments(args)){
			// If the arguments don't include the files OR the files are not valid, prompt the user for the filenames and check them.
			if(!promptFileNames()){
				// Something is wrong here
				System.out.println("ERROR reading the file names: the program will be shut down!");
				System.exit(1);
			}
		}
		// Prompt the user for the formats
		format = promptFormat();
		// Prompt the user to check the decimal separators
		dSep = promptDSep();
		// Prompt the users to check the thousand separators (or lack thereof)
		tSep = promptTSep();
		// For each file, check the value separator for the csv
		vSep = promptVSep();
		// For each file, show the first few lines and prompt the user for the index number of the URL and impressions columns
		promptIndexes();
		// For each file, check the headers
		headers = promptHeaders();
		// Read the files
		list = new URLList(format[0], format[1]);
		for(int i = 0; i < CARDINALITY; i++){
			reader[i] = new CSVReader(headers[i], urlI[i], impI[i], vSep[i], dSep[i], isTSep[i], tSep[i], theFile[i], format[i]);
			reader[i].setDestination(list);
			reader[i].read();
		}
		// Check missing
		for(int i = 0; i < CARDINALITY; i++){
			elements[i] = new ArrayList<>(Arrays.asList(list.getMissingElements(i)));
		}
		// Print an impression count to screen
		printOnScreen();
		// Create and write the output files
		saveResults();
		System.out.println("Execution completed without errors!");
	}
	
	private static boolean checkArguments(String[] args){
		boolean output = false; // True if the arguments contain two files that are readable
		// Check the quality of the arguments and save them to file1 and file2
		if(args.length > 0){
			theFile[0] = new File(args[0]);
			if(!theFile[0].exists() || !theFile[0].canRead()){
				System.out.println("File: " + args[0] + " doesn't exist or can't be read.");
				theFile[0] = null; // Use it only if it works
			}
		}
		if(args.length > 1){
			theFile[1] = new File(args[1]);
			if(!theFile[1].exists() || !theFile[1].canRead()){
				System.out.println("File: " + args[1] + " doesn't exist or can't be read.");
				theFile[1] = null; // Use it only if it works
			}
		}
		if(theFile[0] != null && theFile[1] != null) output = true;
		return output;
	}
	
	private static boolean promptFileNames(){
		boolean output = false; // True if the user provides good filenames
		Scanner keyboard = new Scanner(System.in);
		String input = "";
		while(!output){
			for(int i = 0; i < CARDINALITY; i++){
				if(theFile[i] == null){
					System.out.println("Please enter file " + (i + 1));
					System.out.println("or just hit enter to abort");
					System.out.print(">: ");
					input = keyboard.nextLine();
					if(input.length() == 0){
						System.out.println("Empty line detected, aborting execution.");
						System.exit(0);
					}
					theFile[i] = new File(input);
					if(!theFile[i].exists() || !theFile[i].canRead()){
						System.out.println("File: " + (i + 1) + " doesn't exist or can't be read.");
						theFile[i] = null; // Use it only if it works
					}
				}
			}
			output = true;
			for(int i = 0; i < CARDINALITY; i++){ // are the files good?
				if(theFile[i] == null){ // apparently not
					output = false;
					break;
				}
			}
		}
		return output;
	}
	
	private static URLFormat[] promptFormat(){
		URLFormat[] formats = new URLFormat[CARDINALITY];
		// Loop until the user provides good formats or decides to abort
		for(int i = 0; i < CARDINALITY; i++){
			System.out.println();
			System.out.println("Inserisci il formato per il file " + theFile[0].getName());
			format[i] = URLFormat.inputFormat(">: ");
		}
		return formats;
	}
	
	private static char[] promptDSep(){
		char[] dSep = new char[CARDINALITY];
		Scanner keyboard = new Scanner(System.in);
		String input = "";
		// Loop until the user provides good decimal separators or decides to abort
		for(int i = 0; i < CARDINALITY; i++){
			System.out.println("Please enter the decimal separator for the file " + theFile[i].getName());
			System.out.println(">:");
			input = keyboard.nextLine();
			dSep[i] = input.charAt(0);
		}
		return dSep;
	}
	
	private static char[] promptTSep(){
		char[] tSep = new char[CARDINALITY];
		Scanner keyboard = new Scanner(System.in);
		String input = "";
		// Check if there are thosand separators
		// If so, make the corresponding fields true and loop until the user provides good thousand separators or decides to abort
		for(int i = 0; i < CARDINALITY; i++){
			System.out.println("Is there a thousand separator for the file " + theFile[i].getName() + "? [s|n]");
			System.out.println(">:");
			input = keyboard.nextLine();
			if(input.charAt(0) == 's'){
				isTSep[i] = true;
				System.out.println("What is the thousand separator for the file " + theFile[i].getName() + "?");
				System.out.println(">:");
				input = keyboard.nextLine();
				tSep[i] = input.charAt(0);
			} else {
				isTSep[i] = false;
			}
		}
		return tSep;
	}
	
	private static char[] promptVSep(){
		char[] vSep = new char[CARDINALITY];
		Scanner keyboard = new Scanner(System.in);
		String input = "";
		// For each file, check the value separator for the csv
		for(int i = 0; i < CARDINALITY; i++){
			System.out.println("Please enter the value separator for the file " + theFile[i].getName());
			System.out.println(">:");
			input = keyboard.nextLine();
			vSep[i] = input.charAt(0);
		}
		return vSep;
	}
	
	private static void promptIndexes(){
		// For each file, show the first few lines and prompt the user for the index number of the URL and impressions columns
	}
	
	private static boolean[] promptHeaders(){
		boolean[] headers = new boolean[CARDINALITY];
		// Ask the user for each file if it contains headers
		return headers;
	}
	
	// Print an impression count to screen
	private static void printOnScreen(){
		// TODO
	}
	
	// Save the output to appropriate files
	private static void saveResults(){
		// TODO
	}

}