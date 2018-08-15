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
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Comparator;
import java.util.Scanner;

//import org.omg.CosNaming.IstringHelper;

import java.util.GregorianCalendar;

//import urllistcompare.unittests.URLFormatTest;
//import urllistcompare.util.ArraySort;

/**
 * This class provides a command line interface that allows users to compare two
 * lists of URLs and check if which ones are different (excluding any missing
 * ones, which are covered by CheckMissing).
 * 
 * @author Rocco Barbini (roccobarbi@gmail.com)
 *
 */
public class CheckBasicDifferences {

	// TODO: improve output with totals per format and sample urls per format

	private static final int CARDINALITY = 2;
	private static final String VSEPARATORS = ".,;:_/|\"'"; // Used to validate
															// non-escaped
															// values for vSep

	private static final String versionText = "CheckMissing (urllistcompare) v0.1.2";

	private static File theFile[] = new File[CARDINALITY];
	private static String fileNames[] = new String[CARDINALITY];
	private static URLList list = null;
	private static CSVReader[] reader = new CSVReader[CARDINALITY];
	private static URLNorm[] differences = null; // It will be provided later by
													// the URLList methods.
	private static long impressions[] = new long[CARDINALITY];

	// Data from the command line interface
	private static String outputFileName = null;
	private static String binOutputFileName = null;
	private static char oSep = '\t'; // Default
	private static char[] vSep = new char[CARDINALITY];
	private static char[] dSep = new char[CARDINALITY];
	private static char[] tSep = new char[CARDINALITY];
	private static boolean[] header = new boolean[CARDINALITY];
	// Flag: is the header value actually set?
	private static boolean[] headerSet = new boolean[CARDINALITY];

	// Flags from the command line interface
	private static boolean noExtension = false;
	@SuppressWarnings("unused")
	private static boolean useGui = false;
	@SuppressWarnings("unused")
	private static boolean verbose = false;
	@SuppressWarnings("unused")
	private static boolean silent = false;

	// Execution mode
	private static mode execMode = null; // Execution mode

	private static final String[] HELPTEXT = { "", "CheckBasicDifferences", "",
			"This program compares two lists of URLs in different formats.",
			"The URLs are normalised to a common format before being compared.",
			"The output is a list of normalised URLs with the differences between",
			"the values stored in the two formats provided.", "", "Usage:", "",
			"CheckBasicDifferences",
			"\tThe program will prompt the user to enter two text files",
			"\twith the lists of URLs that need to be compared.", "",
			"CheckBasicDifferences -f textFile1 -f textFile2",
			"CheckBasicDifferences --file textFile1 --file textFile2",
			"\tThe lists of URLs in the two text files will be compared.", "",
			"CheckBasicDifferences -b [binary file name]",
			"CheckBasicDifferences --binary [binary file name]",
			"\tThe program will load the .ulst binary file provided by",
			"\tthe user and will use its contents.", "",
			"CheckBasicDifferences --version",
			"\tThe program prints the current version.", "",
			"Optional parameters after a file:", "\t --vSep [value separator]",
			"\t --dSep [decimal separator]", "\t --tSep [thousand separator]",
			"\t--header_t if the file has a header line",
			"\t--header_f if the file does not have a header line", "",
			"Other optional parameters", "\t -o [output file name]",
			"\t --output [output file name]", "\t -oSep [separator for output]",
			"\t --binOutput [binary output file name]",
			"\t--noExtension to remove the extension for a harder normalisation",
			"\t-e to remove the extension for a harder normalisation",
			"\t--gui to use a gui when prompted for the settings",
			"\t-g to use a gui when prompted for the settings", "",
			"Report bugs through: <https://github.com/roccobarbi/urllistcompare/issues>",
			"pkg home page: <https://github.com/roccobarbi/urllistcompare>",
			"" };

	private static final String TXT_EXPLANATION_HEADER_001 = " elements are different, excluding all missing elements and those elements whose difference is not significant enough.";

	/**
	 * Empty: this class only provides a main argument.
	 */
	public CheckBasicDifferences() {
		// Do nothing
	}

	/**
	 * @param args
	 */
	public static void
		main(String[] args) {
		// Initialisation
		for (int i = 0; i < CARDINALITY; i++) {
			theFile[i] = null;
			fileNames[i] = null;
			reader[i] = null;
			impressions[i] = 0;
			vSep[i] = 0;
			tSep[i] = 0;
			dSep[i] = 0;
			header[i] = false;
			headerSet[i] = false;
		}
		oSep = '\t'; // Default
		// Check the execution mode, set up the sources and run it
		execMode = parseArguments(args);
		execMode.setFileNames(fileNames);
		execMode.execute();
	}

	// Print an impression count to screen
	private static void
		printOnScreen() {
		System.out.println();
		System.out.println(
				differences.length + " elements show different values between "
						+ fileNames[0] + " and " + fileNames[1]);
		System.out.println("Top 5: ");
		for (int k = 0; k < 5 && k < differences.length; k++) {
			System.out.println(differences[k].getUrl() + "\t"
					+ differences[k].getDifference() + "\t"
					+ differences[k].getDifferencePercent());
		}
	}

	// Save the output to appropriate files
	private static void
		saveResults() {
		PrintWriter outputStream = null;
		int [] impressions = null;
		GregorianCalendar currentTime = new GregorianCalendar();
		String fileName = outputFileName == null
				? ("CheckMissing-" + currentTime.getTimeInMillis() + ".txt")
				: outputFileName;
		try {
			outputStream = new PrintWriter(fileName);
		} catch (IOException e) {
			System.out.println(
					"Could not open file " + fileName + ": " + e.getMessage());
			System.out.println("Aborting execution.");
			System.exit(1);
		}
		outputStream
				.println("Format 1: " + list.getFormat(0).getFormatSample());
		outputStream
				.println("Format 2: " + list.getFormat(1).getFormatSample());
		outputStream.print(differences.length);
		outputStream.println(TXT_EXPLANATION_HEADER_001);
		outputStream.println();
		if (differences.length > 0) {
			outputStream.print("url");
			outputStream.print(oSep);
			outputStream.print("impressions fmt 1");
			outputStream.print(oSep);
			outputStream.print("impressions fmt 2");
			outputStream.print(oSep);
			outputStream.print("delta");
			outputStream.print(oSep);
			outputStream.println("delta %");
			for (int k = 0; k < differences.length; k++) {
				impressions = differences[k].getImpressions();
				outputStream.print(differences[k].getUrl());
				outputStream.print(oSep);
				outputStream.print(impressions[0]);
				outputStream.print(oSep);
				outputStream.print(impressions[1]);
				outputStream.print(oSep);
				outputStream.print(differences[k].getDifference());
				outputStream.print(oSep);
				outputStream.println(differences[k].getDifferencePercent());
			}
		}
		outputStream.println();
		outputStream.close();
	}

	// If the user decides to do so, save a binary file with the current URLList
	private static void
		SaveBinary() {
		boolean saveBin = binOutputFileName == null ? false : true;
		ObjectOutputStream output = null;
		@SuppressWarnings("resource")
		Scanner keyboard = new Scanner(System.in);
		String input = null;
		GregorianCalendar currentTime = new GregorianCalendar();
		if (binOutputFileName == null) {
			// Check: do you want to save the binary?
			System.out.println(
					"Do you want to save the current list of URLs for future use? [y|n]");
			input = keyboard.nextLine();
			if (input.length() > 0
					&& input.toLowerCase().trim().charAt(0) == 'y') {
				saveBin = true;
				binOutputFileName = "URLList-" + currentTime.getTimeInMillis()
						+ ".ulst";
			}
		}
		if (saveBin) {
			try {
				output = new ObjectOutputStream(
						new FileOutputStream(binOutputFileName));
			} catch (FileNotFoundException e) {
				System.out.println(
						"ERROR: could not open the file (FileNotFoundException).");
				e.printStackTrace();
			} catch (IOException e) {
				System.out.println(
						"ERROR: could not open the file (IOException).");
				e.printStackTrace();
			}
			try {
				output.writeObject(list);
				output.close();
			} catch (IOException e) {
				System.out.println("ERROR: could not write to the file "
						+ binOutputFileName);
				e.printStackTrace();
			}
			System.out.println(binOutputFileName + " successfully written!");
		}
	}

	/**
	 * Parse the command line arguments passed to main, set the main program
	 * mode and define any additional flag or variable that might be needed.
	 * 
	 * There can be only one mode, but more flags and variables can be set from
	 * the command line to reduce the number of instructions that must be added
	 * during execution.
	 * 
	 * @param args
	 *            the command line arguments
	 * @return the main mode for running the program
	 */
	public static mode
		parseArguments(String[] args) {
		int currentFile = -1; // Used to keep track of the input file that is
								// being set up
		mode output = mode.FILES; // Default
		/*
		 * There are 5 types of arguments: A) input files; B) input file
		 * attributes; C) independent arguments; D) output arguments; E) binary
		 * input argument
		 * 
		 * Type A and E cannot coexist.
		 * 
		 * Type A arguments include the following: -f [fileName] --file
		 * [fileName]
		 * 
		 * They must be read by the primary parse function (which includes this
		 * comment) and they can be preceded or followed by any of the other
		 * types. Each time a type A argument is found, it initiates the loop to
		 * parse type B arguments.
		 * 
		 * Type B arguments include the following: --vSep [separator] --tSep_f
		 * --tSep [separator] --dSep_f --dSep [separator] --header_t --header_f
		 * 
		 * They can only follow a type A parameter and refer to its file. They
		 * can be followed by any parameter type. They are read by a separate
		 * method, which accepts as arguments the args[] array, the current
		 * index and the index of the file that is being read. It returns the
		 * index right after the last type B parameter has been read.
		 * 
		 * If found outside this inner loop, they are simply ignored.
		 * 
		 * Type C arguments include the following: -h --help --version -e
		 * --noExtension -g --gui --silent -v --verbose
		 * 
		 * They can be preceded by any parameter type, they can be followed by
		 * type A, C or D parameters.
		 * 
		 * Type D parameters include the following: -o [fileName] --output
		 * [fileName] --binOutput [fileName] --oSep [separator]
		 * 
		 * They can be preceded by any parameter type, they can be followed by
		 * type A, C or D parameters.
		 * 
		 * Type E parameters include the following: -b [optional fileName]
		 * --binary [optional fileName]
		 * 
		 */
		for (int i = 0; i < args.length; i++) {
			if (args[i].charAt(0) == '-') {
				try {
					if (args[i].length() < 2)
						throw new Exception("Illegal value at parameter " + i
								+ ": isolated single dash!");
					if (args[i].charAt(1) == '-') {
						if (args[i].length() < 3)
							throw new Exception("Illegal value at parameter "
									+ i + ": isolated double dash!");
						switch (args[i].substring(2).trim()) {
						// List the long parameters
						// default = error
						case "version":
							output = mode.VERSION;
							i = args.length; // With mode version, nothing else
												// is parsed
							break;
						case "help":
							output = mode.HELP;
							i = args.length; // With mode help, nothing else is
												// parsed
							break;
						case "binary":
							if (currentFile > -1)
								throw new Exception(
										"Binary mode and file mode cannot coexist!");
							if (args.length < i + 2
									|| args[i + 1].startsWith("-"))
								throw new Exception(
										"Input file name not specified after option --binary!");
							++currentFile; // Step to the next file (default was
											// -1, so the first file will be 0).
							fileNames[currentFile] = args[++i].trim(); // ...and
																		// here
																		// it
																		// is!
							if (!fileNames[currentFile].endsWith(".ulst"))
								fileNames[currentFile] = fileNames[currentFile]
										+ ".ulst";
							output = mode.BINARY;
							break;
						case "output":
							if (args.length < i + 2)
								throw new Exception(
										"Output file name not specified after option --output!");
							if (args[i + 1].startsWith("-"))
								throw new Exception(
										"Output file name not specified after option --output!");
							outputFileName = args[++i].trim();
							break;
						case "binOutput":
							if (args.length < i + 2)
								throw new Exception(
										"Output file name not specified after option --binOutput!");
							if (args[i + 1].startsWith("-"))
								throw new Exception(
										"Output file name not specified after option --binOutput!");
							binOutputFileName = args[++i].trim();
							if (!binOutputFileName.endsWith(".ulst"))
								binOutputFileName = binOutputFileName + ".ulst";
							break;
						case "oSep":
							if (args.length < i + 2)
								throw new Exception(
										"Value separator for output not specified after option --oSep!");
							if (args[i + 1].startsWith("-"))
								throw new Exception(
										"Value separator for output not specified after option --oSep!");
							if (VSEPARATORS
									.indexOf(args[i + 1].charAt(0)) != -1) {
								oSep = args[i + 1].charAt(0);
							} else if (args[i + 1].length() > 1
									&& args[i + 1].charAt(0) == '\\') {
								switch (args[i + 1].charAt(1)) { // Prepped to
																	// add more
																	// separators
								case 't':
									oSep = '\t';
									break;
								default:
									oSep = '\t'; // Default
								}
							} else {
								oSep = '\t'; // Default
							}
							break;
						case "gui":
							useGui = true;
							break;
						case "noExtension":
							noExtension = true;
							break;
						case "verbose":
							verbose = true;
							break;
						case "silent":
							silent = true;
							break;
						case "file":
							if (args.length < i + 2)
								throw new Exception(
										"Input file name not specified after option -f!");
							if (args[i + 1].startsWith("-"))
								throw new Exception(
										"Input file name not specified after option -f!");
							++currentFile; // Step to the next file (default was
											// -1, so the first file will be 0).
							if (currentFile > CARDINALITY)
								throw new Exception("Too many input files!");
							fileNames[currentFile] = args[++i].trim(); // ...and
																		// here
																		// it
																		// is!
							break;
						default:
							throw new Exception("Unexpected parameter " + i);
						}
					} else {
						switch (args[i].substring(1).trim()) {
						// List the short parameters that need to be alone
						// default = check each character to manage the
						// parameters that can be put together
						case "h":
							output = mode.HELP;
							i = args.length; // With mode version, nothing else
												// is parsed
							break;
						case "o":
							if (args.length < i + 1)
								throw new Exception(
										"Output file name not specified after option -o!");
							if (args[i + 1].startsWith("-"))
								throw new Exception(
										"Output file name not specified after option -o!");
							outputFileName = args[++i].trim();
							break;
						case "f":
							if (args.length < i + 2)
								throw new Exception(
										"Input file name not specified after option --file!");
							if (args[i + 1].startsWith("-"))
								throw new Exception(
										"Input file name not specified after option --file!");
							++currentFile; // Step to the next file (default was
											// -1, so the first file will be 0).
							if (currentFile > CARDINALITY)
								throw new Exception("Too many input files!");
							fileNames[currentFile] = args[++i].trim(); // ...and
																		// here
																		// it
																		// is!
							i = parseSecondaryArguments(args, i, currentFile);
							break;
						case "b":
							if (currentFile > -1)
								throw new Exception(
										"Binary mode and file mode cannot coexist!");
							if (args.length < i + 2
									|| args[i + 1].startsWith("-"))
								throw new Exception(
										"Input file name not specified after option --b!");
							++currentFile; // Step to the next file (default was
											// -1, so the first file will be 0).
							fileNames[currentFile] = args[++i].trim(); // ...and
																		// here
																		// it
																		// is!
							if (!fileNames[currentFile].endsWith(".ulst"))
								fileNames[currentFile] = fileNames[currentFile]
										+ ".ulst";
							output = mode.BINARY;
							break;
						default:
							// TODO: Manage the parameters that can be grouped
							for (char c : args[i].substring(1).toCharArray()) {
								switch (c) {
								case 'g':
									useGui = true;
									break;
								case 'e':
									noExtension = true;
									break;
								case 'v':
									verbose = true;
									break;
								case 's':
									silent = true;
									break;
								default:
									throw new Exception(
											"Unexpected parameter " + i);
								}
							}
						}
					}
				} catch (Exception e) {
					System.out.println("ERROR!");
					System.out.println(e.getMessage());
					System.exit(1);
				}
			} else {
				// In case defaults are used
			}
		}
		return output;
	}

	/**
	 * 
	 * Parses the type B parameters.
	 * 
	 * @param args
	 *            the argument string
	 * @param index
	 *            the current index in the main argument loop (not increased to
	 *            reach the first secondary argument)
	 * @param fileIndex
	 *            the index of the file that is currently being populated
	 * @return the index of the last type B argument, or the same index that it
	 *         received if there are none
	 */
	public static int
		parseSecondaryArguments(String args[], int index, int fileIndex)
				throws Exception {
		boolean keepParsing = true;
		while (keepParsing && ++index < args.length) {
			if (args[index].charAt(0) == '-') {
				if (args[index].length() < 2)
					throw new Exception("Illegal value at parameter " + index
							+ ": isolated single dash!");
				if (args[index].charAt(1) == '-') {
					if (args[index].length() < 3)
						throw new Exception("Illegal value at parameter "
								+ index + ": isolated double dash!");
					// Long parameters
					switch (args[index].substring(2).trim()) {
					case "vSep": // Value separator
						if (args.length < index + 2)
							throw new Exception(
									"Separator not specified after option --vSep!");
						if (args[index + 1].startsWith("-"))
							throw new Exception(
									"Separator not specified after option --vSep!");
						if (VSEPARATORS
								.indexOf(args[index + 1].charAt(0)) != -1) {
							vSep[fileIndex] = args[index + 1].charAt(0);
						} else if (args[index + 1].length() > 1
								&& args[index + 1].charAt(0) == '\\') {
							switch (args[index + 1].charAt(1)) { // Prepped to
																	// add more
																	// separators
							case 't':
								vSep[fileIndex] = '\t';
								break;
							default:
								vSep[fileIndex] = '\t'; // Default
							}
						} else {
							throw new Exception("Unsupported value separator: "
									+ args[index + 1] + "!");
						}
						index++; // To avoid parsing the separator twice
						break;
					case "tSep":
						if (args.length < index + 2)
							throw new Exception(
									"Separator not specified after option --tSep!");
						if (args[index + 1].startsWith("-"))
							throw new Exception(
									"Separator not specified after option --tSep!");
						tSep[fileIndex] = args[index + 1].charAt(0);
						index++; // To avoid parsing the separator twice
						break;
					case "dSep":
						if (args.length < index + 2)
							throw new Exception(
									"Separator not specified after option --dSep!");
						if (args[index + 1].startsWith("-"))
							throw new Exception(
									"Separator not specified after option --dSep!");
						dSep[fileIndex] = args[index + 1].charAt(0);
						index++; // To avoid parsing the separator twice
						break;
					case "header_t":
						headerSet[fileIndex] = true;
						header[fileIndex] = true;
						break;
					case "header_f":
						headerSet[fileIndex] = true;
						header[fileIndex] = false;
						break;
					default:
						keepParsing = false; // First non-type-B argument found
						break;
					}
				} else {
					// No short parameters are used for type B
					keepParsing = false; // First non-type-B argument found
				}
			}
		}
		return --index; // To allow the outer loop to go on as usual
	}

	private static enum mode {

		HELP() {
			public void
				execute() {
				for (String line : HELPTEXT) {
					System.out.println(line);
				}
			}
		},
		VERSION() {
			public void
				execute() {
				System.out.println(versionText);
			}
		},
		BINARY() {
			private ObjectInputStream input = null;

			public void
				setFileName(String name) {
				fileNames = new String[1];
				fileNames[0] = name;
			}

			public void
				setFileNames(String[] names) {
				fileNames = names;
			}

			public void
				execute() {
				try {
					input = new ObjectInputStream(
							new FileInputStream(fileNames[0]));
				} catch (FileNotFoundException e) {
					System.out.println(
							"Error reading " + fileNames[0] + e.getMessage());
					System.exit(1);
				} catch (IOException e) {
					System.out.println(
							"Error reading " + fileNames[0] + e.getMessage());
					System.exit(1);
				}
				try {
					list = (URLList) input.readObject();
				} catch (IOException e) {
					System.out.println(
							"Error reading " + fileNames[0] + e.getMessage());
					System.exit(1);
				} catch (ClassNotFoundException e) {
					System.out.println("Error reading " + fileNames[0]
							+ ": wrong contents or corrupt file!");
					System.exit(1);
				}
				// Assign the source names
				for (int i = 0; i < CARDINALITY; i++) {
					fileNames[i] = list.getFormat(i).name() + ": "
							+ list.getFormat(i).getFormatSample();
				}
				if (noExtension) {
					list = list.remExtension();
				} else {
					list = list.addExtension();
				}
				checkDifferences();
				save();
			}
		},
		FILES() {
			public void
				setFileNames(String[] names) {
				fileNames = names;
			}

			public void
				execute() {
				// Check the arguments and create the readers
				for (int i = 0; i < CARDINALITY; i++) { // Prepped for future
														// needs if I ever want
														// to compare more than
														// 2 lists at once
					if (fileNames.length > i && fileNames[i] != null) {
						theFile[i] = new File(fileNames[i]);
						if (!theFile[i].exists() || !theFile[i].canRead()) {
							System.out.println("File: " + fileNames[i]
									+ " doesn't exist or can't be read.");
							reader[i] = ReadManager.userInput();
						} else {
							if (vSep[i] != 0 || tSep[i] != 0 || dSep[i] != 0
									|| headerSet[i]) {
								reader[i] = ReadManager.userInput(fileNames[i],
										vSep[i], dSep[i],
										tSep[i] != 0 ? true : false, tSep[i],
										headerSet[i], header[i]);
							} else {
								reader[i] = ReadManager.userInput(fileNames[i]);
							}
						}
					} else {
						reader[i] = ReadManager.userInput(); // No file was
																// specified for
																// this position
					}
					fileNames[i] = reader[i].getName(); // Assign the source
														// names for future use
				}
				// Read the files
				list = new URLList(reader[0].getFormat(), reader[1].getFormat(),
						noExtension);
				for (int i = 0; i < CARDINALITY; i++) {
					reader[i].setDestination(list);
					if (!reader[i].read()) {
						System.out.println("Errore nella lettura del file "
								+ reader[i].getName() + "!");
						System.out.println("Aborting execution");
						System.exit(1);
					}
				}
				checkDifferences();
				save();
			}
		};

		private static String[] fileNames = null;

		public void
			execute() {
			// Override only
		};

		public void
			setFileNames(String[] names) {
			// Override only
		};

		@SuppressWarnings("unused")
		public void
			setFileName(String name) {
			// Override only
		};

		public void
			checkDifferences() {
			// Check differences
			differences = list.getDifferentURLs(0);
			// Print the results on screen
			printOnScreen();
		}

		public void
			save() {
			// Create and write the output files
			saveResults();
			// If needed, save a binary file with the URLList
			SaveBinary();
			System.out.println("Execution completed without errors!");
		}

	}

}
