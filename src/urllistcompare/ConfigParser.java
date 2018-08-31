/**
 * 
 */
package urllistcompare;

import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
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
	
	public ConfigParser(File input) throws FileNotFoundException, WrongExtensionException, ConfigurationFormatException {
		Scanner inputStream;
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
			System.err.println("Problema nell'apertura del file " + input.getName());
			throw new FileNotFoundException();
		}
		try{
			while(true){
				if(!inputStream.hasNextLine())
					throw new EOFException("End of file reached!");
			}
		} catch(EOFException e){
			System.out.println("File " + input.getName() + " letto correttamente!");
		} catch (Exception e) {
			// Close the input stream
			inputStream.close();
			// Stop all engines, but in a way that can be catched
			throw new RuntimeException("Unrecoverable error while reading " + input.getName() + ": " + e.getMessage());
		} finally {
			// Close the stream
			inputStream.close();
		}
		readers = null;
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
}
