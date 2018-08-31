/**
 * 
 */
package urllistcompare;

import java.io.File;
import java.io.FileNotFoundException;

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
		this.input = input;
		readers = null;
		outputFileName = null;
		binaryFileName = null;
		checkMissing = false;
		checkBasicDifferences = false;
		saveBinary = false;
	}
	
	public String getConfigSampleContents() {
		return null;
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
