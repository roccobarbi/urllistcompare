/**
 * 
 */
package urllistcompare;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

import urllistcompare.unittests.URLFormatTest;
import urllistcompare.util.ArraySort;

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
	private static long impressions[] = new long[CARDINALITY];

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
			impressions[i] = 0;
			
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
			if(!reader[i].read()) {
				System.out.println("Errore nella lettura del file " + theFile[i].getName() + "!");
				System.out.println("Aborting execution");
				System.exit(1);
			}
		}
		// Check missing
		for(int i = 0; i < CARDINALITY; i++){
			elements[i] = new ArrayList<>(Arrays.asList(list.getMissingElements(i)));
			elements[i].trimToSize();
			elements[i].sort(new Comparator<URLElement>() {public int compare(URLElement first, URLElement second){return  second.compareTo(first);}});
			for(URLElement e : list.getMissingElements(i)){
				impressions[i] += e.getImpressions();
			}
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
			System.out.println("Inserisci il formato per il file " + theFile[i].getName());
			formats[i] = URLFormat.inputFormat(">: ");
		}
		// TODO: future versions will have to remove this limitation
		// This will require a refactoring of the URLNorm class
		if(formats[0] == formats[1]){
			System.out.println("The current version of this program only confronts URL lists of different formats.");
			System.out.println("Aborting execution.");
			System.exit(0);
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
			System.out.println("Is there a thousand separator for the file " + theFile[i].getName() + "? [y|n]");
			System.out.println(">:");
			input = keyboard.nextLine();
			if(input.charAt(0) == 'y'){
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
	
	// For each file, check the value separator for the csv
	private static char[] promptVSep(){
		char[] vSep = new char[CARDINALITY];
		Scanner keyboard = new Scanner(System.in);
		String input = "";
		for(int i = 0; i < CARDINALITY; i++){
			System.out.println("Please enter the value separator for the file " + theFile[i].getName());
			System.out.println(">:");
			input = keyboard.nextLine();
			vSep[i] = input.charAt(0);
		}
		return vSep;
	}
	
	// For each file, show the first few lines and prompt the user for the index number of the URL and impressions columns
	private static void promptIndexes(){
		Scanner keyboard = new Scanner(System.in);
		Scanner reader = null;
		String splitHeader[] = null, input = null;;
		int k = 0;
		boolean askAgain = true;
		for(int i = 0; i < CARDINALITY; i++){
			try{
				reader = new Scanner(theFile[i]);
			} catch (IOException e) {
				System.out.println("Can't read from file " + theFile[i].getName() + ": " + e.getMessage());
				System.out.println("Aborting execution");
				System.exit(1);
			}
			System.out.println("Headers for file " + theFile[i] + ":");
			k = 0;
			while(reader.hasNextLine() && k < 3){
				splitHeader = reader.nextLine().split(Character.toString(vSep[i]));
				for(int j = 0; j < splitHeader.length; j++){
					System.out.print(splitHeader[j] + "\t");
				}
				System.out.println();
				k++;
			}
			reader.close();
			askAgain = true;
			while(askAgain){
				System.out.println("Enter the index for the column where the URL is stored, between 0 and " + (splitHeader.length - 1) + ".");
				System.out.print(">: ");
				input = keyboard.nextLine();
				if(Integer.parseInt(input) >= 0 && Integer.parseInt(input) < splitHeader.length){
					urlI[i] = Integer.parseInt(input);
					askAgain = false;
				}
			}
			askAgain = true;
			while(askAgain){
				System.out.println("Enter the index for the column where the impressions are stored, between 0 and " + (splitHeader.length - 1) + ".");
				System.out.print(">: ");
				input = keyboard.nextLine();
				if(Integer.parseInt(input) >= 0 && Integer.parseInt(input) < splitHeader.length && Integer.parseInt(input) != urlI[i]){
					impI[i] = Integer.parseInt(input);
					askAgain = false;
				}
			}
		}
	}
	
	// Ask the user for each file if it contains headers
	private static boolean[] promptHeaders(){
		boolean[] headers = new boolean[CARDINALITY];
		Scanner keyboard = new Scanner(System.in);
		String input = "";
		for(int i = 0; i < CARDINALITY; i++){
			System.out.println("Is there a header line in the file " + theFile[i].getName() + "? [y|n]");
			System.out.println(">:");
			input = keyboard.nextLine();
			if(input.charAt(0) == 'y'){
				headers[i] = true;
			} else {
				headers[i] = false;
			}
		}
		return headers;
	}
	
	// Print an impression count to screen
	private static void printOnScreen(){
		for(int i = 0; i < CARDINALITY; i++){
			System.out.println();
			System.out.println(elements[i].size() + " elements are missing from " + theFile[i] + 
					" for a total of " + impressions[i] + " page impressions.");
			System.out.println("Top 5: ");
			for(int k = 0; k < 5 && k < elements[i].size(); k++){
				System.out.println(elements[i].get(k));
			}
		}
	}
	
	// Save the output to appropriate files
	private static void saveResults(){
		PrintWriter outputStream = null;
		// TODO
	}

}
