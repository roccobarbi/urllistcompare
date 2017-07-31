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
	
	// Data from the command line interface
	private static String outputFileName = null;
	private static String binOutputFileName = null;
	private static char oSep = '\t'; // Default
	
	// Flags from the command line interface
	private static boolean noExtension = false;
	
	// Execution mode
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
		execMode = parseArguments(args);
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
	
	/**
	 * Parse the command line arguments passed to main, set the main program mode and define any additional flag or variable
	 * that might be needed
	 * @param args
	 * @return the main mode for running the program
	 */
	public static mode parseArguments(String[] args){
		mode output = null;
		String[] fileNames = new String[2];
		boolean readingInputFile = false; // Flag that is activated when reading an input file settings
		if(args.length == 0){
			return mode.FILES; // If there are no arguments, the mode is FILES and there are no additional options
		}
		// TODO: loop through the arguments, use appropriate flags to detect the current status or any conflicting options.
		// TODO: save the appropriate data to static class variables, then otput the mode
		// TODO: the mode execution should consider the flags and variables that are set
		/*
		 * Options:
		 * --help
		 * -h
		 * --version
		 * --output
		 * -o
		 * --binOutput
		 * --oSep
		 * --vSep
		 * --tSep
		 * --dSep
		 * --file
		 * -d
		 * -t
		 * -v
		 * -p
		 * -i
		 * -e
		 * --noExtension
		 * --gui
		 * -g
		 * --verbose
		 * --silent
		 */
		for(int i = 0; i < args.length; i++){
			// TODO: based on the active flags
			// read the start of the argument
			// interpret it accordingly
			if(args[i].charAt(0) == '-'){
				try{
					if(args[i].length() < 2) throw new Exception("Illegal value at parameter " + i + ": isolated single dash!");
					if(args[i].charAt(1) == '-'){
						if(args[i].length() < 3) throw new Exception("Illegal value at parameter " + i + ": isolated double dash!");
						switch(args[i].substring(2).trim()){
						// List the long parameters
						// default = error
						case "version":
							output = mode.VERSION;
							i = args.length; // With mode version, nothing else is parsed
							readingInputFile = false; // This command interrupts the options for a previous file that was being read
							break;
						case "help":
							output = mode.HELP;
							i = args.length; // With mode help, nothing else is parsed
							readingInputFile = false; // This command interrupts the options for a previous file that was being read
							break;
						case "output":
							if(args.length < i + 2) throw new Exception("Output file name not specified after option --output!");
							if(args[i + 1].startsWith("-")) throw new Exception("Output file name not specified after option --output!");
							outputFileName = args[++i].trim();
							readingInputFile = false; // This command interrupts the options for a previous file that was being read
							break;
						case "binOutput":
							if(args.length < i + 2) throw new Exception("Output file name not specified after option --binOutput!");
							if(args[i + 1].startsWith("-")) throw new Exception("Output file name not specified after option --binOutput!");
							binOutputFileName = args[++i].trim();
							if(!binOutputFileName.endsWith(".ulst")) binOutputFileName = binOutputFileName + ".ulst" ;
							readingInputFile = false; // This command interrupts the options for a previous file that was being read
							break;
						case "oSep":
							if(args.length < i + 2) throw new Exception("Value separator for output not specified after option --oSep!");
							if(args[i + 1].startsWith("-")) throw new Exception("Value separator for output not specified after option --oSep!");
							if(VSEPARATORS.indexOf(args[i+1].charAt(0)) != -1){
								oSep = args[i+1].charAt(0);
							} else if(args[i+1].length() > 1 && args[i+1].charAt(0) == '\\'){
								switch(args[i+1].charAt(1)){ // Prepped to add more separators
								case 't':
									oSep = '\t';
									break;
								default:
									oSep = '\t'; // Default
								}
							} else {
								oSep = '\t'; // Default
							}
							readingInputFile = false; // This command interrupts the options for a previous file that was being read
							break;
						case "vSep":
							if(!readingInputFile) throw new Exception("Orphan --vSep parameter, must follow ");
							break;
						case "tSep":
							break;
						case "dSep":
							break;
						case "gui":
							break;
						case "noExtension":
							noExtension = true;
							break;
						case "verbose":
							break;
						case "silent":
							break;
						case "file":
							break;
						default:
							throw new Exception("Unexpected parameter " + i);
						}
					} else {
						switch(args[i].substring(1).trim()){
							// List the short parameters that need to be alone
							// default = check each character to manage the parameters that can be put together
						case "h":
							output = mode.HELP;
							readingInputFile = false; // This command interrupts the options for a previous file that was being read
							break;
						case "o":
							if(args.length < i + 1) throw new Exception("Output file name not specified after option -o!");
							if(args[i + 1].startsWith("-")) throw new Exception("Output file name not specified after option -o!");
							outputFileName = args[++i].trim();
							readingInputFile = false; // This command interrupts the options for a previous file that was being read
							break;
						case "f":
							// TODO: Read an input file name
							readingInputFile = true; // File reading options are now acceptable
							break;
						default:
							// TODO: Manage he parameters that can be grouped
							readingInputFile = false; // This command interrupts the options for a previous file that was being read
							throw new Exception("Unexpected parameter " + i);
						}
					}
				} catch(Exception e){
					System.out.println("ERROR!");
					System.out.println(e.getMessage());
					System.out.println(e.getStackTrace());
					System.exit(1);
				}
			} else {
				// Read an input file name
				readingInputFile = true; // File reading options are now acceptable
			}
		}
		// LEGACY CODE
		/*else {
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
		}*/
		return output;
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
		
	}

}
