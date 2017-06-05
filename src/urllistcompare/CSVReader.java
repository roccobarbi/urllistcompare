/**
 * 
 */
package urllistcompare;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

/**
 * @author Rocco Barbini (roccobarbi@gmail.com)
 * 
 * This class is tasked with reading the contents of a CSV file into a URLList.
 * It can receive the format, otherwise it will prompt the user to receive it.
 * It can be set with the presence of headers, thousand and decimal separators, value separator,
 * with the column numbers for the page impressions and the url. Otherwise, it prompts the user
 * for those values.
 * In case this class is used with a GUI, those values should be set in the constructor or with accessor
 * functions before the class start reading the file. 
 *
 */
public class CSVReader {
	// true if the reader is set
	private boolean set;
	// true if there are column headers
	private boolean headers;
	// separators: 0 if the value is not set
	private boolean isTSep;
	private char tSep, dSep, vSep;
	// index of the url and impressions columns, -1 if the value is not set
	private int urlI, impI;
	// bom values (needed to interpret and remove the bom at the start of the file, if present)
	private enum bom{
		UNICODE(new String(new byte[] {(byte) 0xef, (byte) 0xbb, (byte) 0xbf}), 3);
		public final String bomString;		
		public final int bomLength;
		private bom(String bomString, int bomLength){
			this.bomString = bomString;
			this.bomLength = bomLength;
		}
	}

	/**
	 * Default constructor, the CSVReader instance is not set and can't be used unless all variables are set correctly.
	 */
	public CSVReader() {
		headers = true; // default
		isTSep = true; // default, with tSep = 0 it implies that the value is missing
		tSep = 0; // default: indicates that the value is missing
		dSep = 0; // default: indicates that the value is missing
		vSep = 0; // default: indicates that the value is missing
		urlI = -1; // default: indicates that the value is missing
		impI = -1; // default: indicates that the value is missing
		set = false;
	}
	
	public boolean isSet(){
		return set;
	}
	
	private boolean checkSet(){
		set = ((!isTSep || tSep != 0) && dSep != 0 && vSep != 0) && urlI > -1 && impI > -1 && urlI != impI;
		return set;
	}

}
