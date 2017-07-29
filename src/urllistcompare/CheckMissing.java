/**
 * 
 */
package urllistcompare;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
	
	private static final String versionText = "CheckMissing (urllistcompare) v0.1.2";
	
	private static File theFile[] = new File[CARDINALITY];
	private static String sourceNames[] = new String[CARDINALITY];
	private static URLList list = null;
	private static CSVReader[] reader = new CSVReader[CARDINALITY];
	private static ArrayList<URLElement>[] elements = new ArrayList[CARDINALITY];
	private static long impressions[] = new long[CARDINALITY];
	
	private static mode execMode = null; // Execution mode
	
	private static final String[] HELPTEXT = {
			"",
			"CheckMissing",
			"",
			"This program compares two lists of URLs in different formats.",
			"The URLs are normalised to a common format before being compared.",
			"The output is a list of URLs from each list that are missing from the other list.",
			"",
			"Usage:",
			"",
			"CheckMissing",
			"\tThe program will prompt the user to enter two text files",
			"\twith the lists of URLs that need to be compared.",
			"",
			"CheckMissing textFile1 textFile2",
			"\tThe lists of URLs in the two text files will be compared.",
			"",
			"CheckMissing -b binFile.ulst",
			"CheckMissing --binary binFile.ulst",
			"\tThe program will load the .ulst binary file provided by",
			"\tthe user and will use its contents.",
			"",
			"CheckMissing --version",
			"\tThe program prints the current version.",
			"",
			"Report bugs through: <https://github.com/roccobarbi/urllistcompare/issues>",
			"pkg home page: <https://github.com/roccobarbi/urllistcompare>",
			""
		};

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
			sourceNames[i] = null;
			reader[i] = null;
			elements[i] = new ArrayList<URLElement>();
			impressions[i] = 0;
			
		}
		// Check the execution mode
		execMode = mode.checkArguments(args);
		execMode.execute();
	}
	
	// Print an impression count to screen
	private static void printOnScreen(){
		for(int i = 0; i < CARDINALITY; i++){
			System.out.println();
			System.out.println(elements[i].size() + " elements are missing from " + sourceNames[i] + 
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
			outputStream.println("File 1: " + sourceNames[i]);
			outputStream.println("Format: " + list.getFormat(i).getFormatSample());
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
	
	// If the user decides to do so, save a binary file with the current URLList
	private static void SaveBinary(){
		ObjectOutputStream output = null;
		Scanner keyboard = new Scanner(System.in);
		String input = null;
		GregorianCalendar currentTime = new GregorianCalendar();
		String fileName = "URLList-" + currentTime.getTimeInMillis() + ".ulst";
		// Check: do you want to save the binary?
		System.out.println("Do you want to save the current list of URLs for future use? [y|n]");
		input = keyboard.nextLine();
		if(input.length() > 0 && input.toLowerCase().trim().charAt(0) == 'y'){
			try{
				output = new ObjectOutputStream(new FileOutputStream(fileName));
			} catch (FileNotFoundException e) {
				System.out.println("ERROR: could not open the file (FileNotFoundException).");
				e.printStackTrace();
			} catch (IOException e) {
				System.out.println("ERROR: could not open the file (IOException).");
				e.printStackTrace();
			}
			try{
				output.writeObject(list);
				output.close();
			} catch (IOException e) {
				System.out.println("ERROR: could not write to the file " + fileName);
				e.printStackTrace();
			}
			System.out.println(fileName + " successfully written!");
		}
	}
	
	private static enum mode{
		
		HELP(){
			public void execute(){
				for(String line : HELPTEXT){
					System.out.println(line);
				}
			}
		},
		VERSION(){
			public void execute(){
				System.out.println(versionText);
			}
		},
		BINARY(){
			private ObjectInputStream input = null;
			public void setFileName(String name){
				fileNames = new String[1];
				fileNames[0] = name;
			}
			public void execute(){
				try{
					input = new ObjectInputStream(new FileInputStream(fileNames[0]));
				} catch (FileNotFoundException e){
					System.out.println("Error reading " + fileNames[0]);
					System.exit(1);
				} catch (IOException e){
					System.out.println("Error reading " + fileNames[0]);
					System.exit(1);
				}
				try{
					list = (URLList) input.readObject();
				} catch (IOException e){
					System.out.println("Error reading " + fileNames[0]);
					System.exit(1);
				} catch (ClassNotFoundException e){
					System.out.println("Error reading " + fileNames[0] + ": wrong contents or corrupt file!");
					System.exit(1);
				}
				// Assign the source names
				for(int i = 0; i < CARDINALITY; i++){
					sourceNames[i] = list.getFormat(i).name() + ": " + list.getFormat(i).getFormatSample();
				}
				checkMissing();
				save();
			}
		},
		FILES(){
			public void setFileNames(String[] names){
				fileNames = names;
			}
			public void execute(){
				// Check the arguments and create the readers
				for(int i = 0; i < CARDINALITY; i++){ // Prepped for future needs if I ever want to compare more than 2 lists at once
					if(fileNames.length > i){
						theFile[i] = new File(fileNames[i]);
						if(!theFile[i].exists() || !theFile[i].canRead()){
							System.out.println("File: " + fileNames[i] + " doesn't exist or can't be read.");
							reader[i] = ReadManager.userInput();
						} else {
							reader[i] = ReadManager.userInput(fileNames[i]);
						}
					}
					else{
						reader[i] = ReadManager.userInput(); // No file was specified for this position
					}
					sourceNames[i] = reader[i].getName(); // Assign the source names for future use
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
				checkMissing();
				save();
			}
		};
		
		private static String[] fileNames = null;
		
		public void execute(){
			// Override only
		};
		
		public void setFileNames(String[] names){
			// Override only
		};
		
		public void setFileName(String name){
			// Override only
		};
		
		public void checkMissing(){
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
		}
		
		public void save(){
			// Create and write the output files
			saveResults();
			// If needed, save a binary file with the URLList
			SaveBinary();
			System.out.println("Execution completed without errors!");
		}
		
		public static mode checkArguments(String[] args){
			mode output = null;
			String[] fileNames = new String[2];
			if(args.length == 0){
				output = mode.FILES;
			} else {
				if(args[0].charAt(0) == '-'){
					switch(args[0].trim()){
					case "--version":
						output = mode.VERSION;
						break;
					case "-b":
					case "--binary":
						if(args.length > 1){
							if(args[1].trim().toLowerCase().endsWith(".ulst")){
								output = mode.BINARY;
								output.setFileName(args[1]);
							} else {
								System.out.println("ERROR: wrong file format!");
								output = mode.HELP;
							}
						} else {
							System.out.println("ERROR: binary file missing!");
							output = mode.HELP;
						}
						break;
					case "-h":
					case "--help":
					default:
						output = mode.HELP;
					}
				} else {
					output = mode.FILES;
					for(int i = 0; i < args.length && i < CARDINALITY; i++){
						fileNames[i] = args[i];
					}
					output.setFileNames(fileNames);
				}
			}
			return output;
		}
		
	}

}
