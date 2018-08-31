/**
 * 
 */
package urllistcompare;

import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import urllistcompare.exceptions.ConfigurationFormatException;
import urllistcompare.exceptions.WrongExtensionException;

/**
 * @author rocco barbini (roccobarbi@gmail.com)
 *
 */
public class ConfigParser {

	private final File input;
	private final CSVReader[] readers;
	private final String outputFileName;
	private final String binaryFileName;
	private final boolean checkMissing;
	private final boolean checkBasicDifferences;
	private final boolean saveBinary;

	public ConfigParser() {
		input = null;
		readers = null;
		outputFileName = null;
		binaryFileName = null;
		checkMissing = false;
		checkBasicDifferences = false;
		saveBinary = false;
	}

	public ConfigParser(File input)
			throws FileNotFoundException, WrongExtensionException, ConfigurationFormatException {
		Scanner inputStream;
		String line;
		mode status = mode.IDLE;
		String instruction = "";
		ArrayList<CSVReader> readers = new ArrayList<CSVReader>(); // temporary output
		if (!input.exists() || !input.canRead()) {
			throw new FileNotFoundException();
		}
		if (!input.getName().endsWith(".toml")) {
			throw new WrongExtensionException();
		}
		// Try reading the file
		this.input = input;
		try {
			inputStream = new Scanner(input);
		} catch (FileNotFoundException e) {
			throw new FileNotFoundException("Problema nell'apertura del file " + input.getName());
		}
		try {
			while (true) {
				if (!inputStream.hasNextLine())
					throw new EOFException("End of file reached!");
				line = inputStream.nextLine();
				// TODO: logic that reads instruction statements as per the toml standard and
				// assigns it to the instruction string.
				status = status.read(instruction);
			}
		} catch (EOFException e) {
			// do nothing, the file has been read
		} catch (Exception e) {
			// Close the input stream
			inputStream.close();
			// Stop all engines, but in a way that can be catched
			throw new RuntimeException("Unrecoverable error while reading " + input.getName() + ": " + e.getMessage());
		} finally {
			// Close the stream
			inputStream.close();
		}
		this.readers = readers.toArray(new CSVReader[0]);
		if (this.readers == null || this.readers.length < 1) {
			// No readers, the configuration must have been wrong or empty
			throw new ConfigurationFormatException("No readers were created!");
		}
		outputFileName = null;
		binaryFileName = null;
		checkMissing = false;
		checkBasicDifferences = false;
		saveBinary = false;
	}

	public static String getConfigSampleContents() {
		StringBuilder output = new StringBuilder();
		output.append("[format]\n");
		output.append("# Available formats:\n");
		output.append("# WTKDEF: www_domain_com.path.path.file_ext\n");
		output.append("# URLNORM: http://www.domain.com/path/path/file.ext\n");
		output.append("# NOPROTNORM: www.domain.com/path/path/file.ext\n");
		output.append("# FULLURL: http://www.domain.com/PATH/path/file.ext?query#fragment\n");
		output.append("# GOOG: /path/path/file.ext?query#fragment\n");
		output.append("# NOPROTFULL: www.domain.com/PATH/path/file.ext?query#fragment\n");
		output.append("f1 = \"URLNORM\"\n");
		output.append("f2 = \"GOOG\"\n");
		output.append("output = \"someoutputfile.csv\"\n");
		output.append("binary = \"somebinary.ulst\"\n");
		output.append("checkMissing = true");
		output.append("checkBasicDifference = true");
		output.append("saveBinary = true");
		output.append("\n");
		output.append("[[input]]\n");
		output.append("file = \"input0.csv\"\n");
		output.append("dSep = \".\"\n");
		output.append("isTsep = true\n");
		output.append("tSep= \",\"\n");
		output.append("urlIndex = \"0\"\n");
		output.append("piIndex = \"1\"\n");
		output.append("headers = \"1\" # any int, the header rows\n");
		output.append("footer = \"2\" # any int, the footer rows\n");
		output.append("index = \"0\"\n");
		output.append("format = \"URLNORM\"\n");
		output.append("\n");
		output.append("[[input]]\n");
		output.append("file = \"input1.csv\"\n");
		output.append("dSep = \".\"\n");
		output.append("isTsep = true\n");
		output.append("tSep= \",\"\n");
		output.append("urlIndex = \"0\"\n");
		output.append("piIndex = \"1\"\n");
		output.append("headers = \"1\" # any int, the header rows\n");
		output.append("footer = \"2\" # any int, the footer rows\n");
		output.append("index = \"1\"\n");
		output.append("format = \"GOOG\"");
		return output.toString();
	}

	/**
	 * @return the input
	 */
	public File getInput() {
		return input;
	}

	/**
	 * @return the readers
	 */
	public CSVReader[] getReaders() {
		return readers;
	}

	/**
	 * @return the outputFileName
	 */
	public String getOutputFileName() {
		return outputFileName;
	}

	/**
	 * @return the binaryFileName
	 */
	public String getBinaryFileName() {
		return binaryFileName;
	}

	/**
	 * @return true if checkMissing mode is toggled on
	 */
	public boolean isCheckMissing() {
		return checkMissing;
	}

	/**
	 * @return true if checkBasicDifferences mode is toggled on
	 */
	public boolean isCheckBasicDifferences() {
		return checkBasicDifferences;
	}

	/**
	 * @return true if saveBinary is toggled on
	 */
	public boolean isSaveBinary() {
		return saveBinary;
	}

	// Defines different behaviours while reading different parts of the config file
	private static enum mode {

		IDLE() {
			public mode read(String instruction) {
				if (instruction.trim().equals("[format]")) {
					return FORMAT;
				} else if (instruction.trim().equals("[[input]]")) {
					return INPUT;
				} else {
					return IDLE;
				}
			}
		},	
		FORMAT() {
			public mode read(String instruction) {
				if (instruction.trim().equals("[[input]]")) {
					return INPUT;
				} else {
					// TODO: actually read the format
					return FORMAT;
				}
			}
		},		
	INPUT() {
			public mode read(String instruction) {
				if (instruction.trim().equals("[format]")) {
					return FORMAT;
				} else {
					// TODO: actually read the format
					return INPUT;
				}
			}
		};

	public mode read(String instruction) {
		// TODO Auto-generated method stub
		return null;
	}
};
}
