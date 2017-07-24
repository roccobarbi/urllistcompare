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
import java.util.GregorianCalendar;

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
	private static final String VSEPARATORS = ".,;:_/|\"'"; // Used to validate non-escaped values for vSep
	
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
		// Check the arguments and create the readers
		for(int i = 0; i < CARDINALITY; i++){ // Prepped for future needs if I ever want to compare more than 2 lists at once
			if(args.length > i){
				theFile[i] = new File(args[i]);
				if(!theFile[i].exists() || !theFile[i].canRead()){
					System.out.println("File: " + args[i] + " doesn't exist or can't be read.");
					reader[i] = ReadManager.userInput();
				} else {
					reader[i] = ReadManager.userInput(args[i]);
				}
			}
			else{
				reader[i] = ReadManager.userInput(); // No file was specified for this position
			}
		}
		// Read the files
		list = new URLList(reader[0].getFormat(), reader[1].getFormat());
		for(int i = 0; i < CARDINALITY; i++){
			reader[i].setDestination(list);
			if(!reader[i].read()) {
				System.out.println("Errore nella lettura del file " + reader[i].getName() + "!");
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
		GregorianCalendar currentTime = new GregorianCalendar();
		String fileName = "CheckMissing-" + currentTime.getTimeInMillis() + ".txt";
		String sep = "\t"; // In the future, this might be an information received from the user 
		try{
			outputStream = new PrintWriter(fileName);
		} catch (IOException e) {
			System.out.println("Could not open file " + fileName + ": " + e.getMessage());
			System.out.println("Aborting execution.");
			System.exit(1);
		}
		for(int i = 0; i < CARDINALITY; i++){
			outputStream.println("File 1: " + reader[i].getName());
			outputStream.println("Format: " + reader[i].getFormat().getFormatSample());
			outputStream.println(elements[i].size() + " elements are missing for a total of " + impressions[i] + " page impressions.");
			outputStream.println();
			if(elements[i].size() > 0){
				outputStream.println("url" + sep + "impressions");
				for(int k = 0; k < elements[i].size(); k++){
					outputStream.println(elements[i].get(k).getUrl() + sep + elements[i].get(k).getImpressions());
				}
			}
			outputStream.println();
			outputStream.println();
		}
		outputStream.close();
	}

}
