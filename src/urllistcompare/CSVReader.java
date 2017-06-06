/**
 * 
 */
package urllistcompare;

import java.io.BufferedReader;
import java.io.File;
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
	// File to be read
	private File source;
	// URL Format
	private URLFormat format;

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
		source = null;
		this.format = null;
		set = false;
	}
	
	/**
	 * Almost full constructor that can be used when isTSep = false, no value needs to be specified.
	 * @param headers
	 * @param urlI
	 * @param impI
	 * @param vSep
	 * @param dSep
	 * @param isTSep
	 */
	public CSVReader(boolean headers, int urlI, int impI, char vSep, char dSep, boolean isTSep) {
		this.headers = headers;
		this.isTSep = isTSep;
		this.tSep = 0; // default: indicates that the value is missing
		this.dSep = dSep;
		this.vSep = vSep;
		this.urlI = urlI;
		this.impI = impI;
		source = null;
		this.format = null;
		set = false;
	}
	
	/**
	 * Almost full constructor that can be used when isTSep = true.
	 * @param headers
	 * @param urlI
	 * @param impI
	 * @param vSep
	 * @param dSep
	 * @param isTSep
	 * @param tSep
	 */
	public CSVReader(boolean headers, int urlI, int impI, char vSep, char dSep, boolean isTSep, char tSep) {
		this.headers = headers;
		this.isTSep = isTSep;
		this.tSep = tSep;
		this.dSep = dSep;
		this.vSep = vSep;
		this.urlI = urlI;
		this.impI = impI;
		source = null;
		this.format = null;
		set = false;
	}
	
	/**
	 * Almost full constructor that can be used when isTSep = false, no value needs to be specified.
	 * @param headers
	 * @param urlI
	 * @param impI
	 * @param vSep
	 * @param dSep
	 * @param isTSep
	 * @param filename
	 */
	public CSVReader(boolean headers, int urlI, int impI, char vSep, char dSep, boolean isTSep, String filename) {
		this.headers = headers;
		this.isTSep = isTSep;
		this.tSep = 0; // default: indicates that the value is missing
		this.dSep = dSep;
		this.vSep = vSep;
		this.urlI = urlI;
		this.impI = impI;
		this.format = null;
		set = setFile(filename);
	}
	
	/**
	 * Almost full constructor that can be used when isTSep = true.
	 * @param headers
	 * @param urlI
	 * @param impI
	 * @param vSep
	 * @param dSep
	 * @param isTSep
	 * @param tSep
	 * @param filename
	 */
	public CSVReader(boolean headers, int urlI, int impI, char vSep, char dSep, boolean isTSep, char tSep, String filename) {
		this.headers = headers;
		this.isTSep = isTSep;
		this.tSep = tSep;
		this.dSep = dSep;
		this.vSep = vSep;
		this.urlI = urlI;
		this.impI = impI;
		this.format = null;
		set = setFile(filename);
	}
	
	/**
	 * Almost full constructor that can be used when isTSep = false, no value needs to be specified.
	 * @param headers
	 * @param urlI
	 * @param impI
	 * @param vSep
	 * @param dSep
	 * @param isTSep
	 * @param filename
	 */
	public CSVReader(boolean headers, int urlI, int impI, char vSep, char dSep, boolean isTSep, File file) {
		this.headers = headers;
		this.isTSep = isTSep;
		this.tSep = 0; // default: indicates that the value is missing
		this.dSep = dSep;
		this.vSep = vSep;
		this.urlI = urlI;
		this.impI = impI;
		this.format = null;
		set = setFile(file);
	}
	
	/**
	 * Almost full constructor that can be used when isTSep = true.
	 * @param headers
	 * @param urlI
	 * @param impI
	 * @param vSep
	 * @param dSep
	 * @param isTSep
	 * @param tSep
	 * @param filename
	 */
	public CSVReader(boolean headers, int urlI, int impI, char vSep, char dSep, boolean isTSep, char tSep, File file) {
		this.headers = headers;
		this.isTSep = isTSep;
		this.tSep = tSep;
		this.dSep = dSep;
		this.vSep = vSep;
		this.urlI = urlI;
		this.impI = impI;
		this.format = null;
		set = setFile(file);
	}
	
	/**
	 * Full constructor that can be used when isTSep = false, no value needs to be specified.
	 * @param headers
	 * @param urlI
	 * @param impI
	 * @param vSep
	 * @param dSep
	 * @param isTSep
	 * @param filename
	 * @param format
	 */
	public CSVReader(boolean headers, int urlI, int impI, char vSep, char dSep, boolean isTSep, String filename, URLFormat format) {
		this.headers = headers;
		this.isTSep = isTSep;
		this.tSep = 0; // default: indicates that the value is missing
		this.dSep = dSep;
		this.vSep = vSep;
		this.urlI = urlI;
		this.impI = impI;
		this.format = format;
		set = setFile(filename);
	}
	
	/**
	 * Full constructor that can be used when isTSep = true.
	 * @param headers
	 * @param urlI
	 * @param impI
	 * @param vSep
	 * @param dSep
	 * @param isTSep
	 * @param tSep
	 * @param filename
	 * @param format
	 */
	public CSVReader(boolean headers, int urlI, int impI, char vSep, char dSep, boolean isTSep, char tSep, String filename, URLFormat format) {
		this.headers = headers;
		this.isTSep = isTSep;
		this.tSep = tSep;
		this.dSep = dSep;
		this.vSep = vSep;
		this.urlI = urlI;
		this.impI = impI;
		this.format = format;
		set = setFile(filename);
	}
	
	/**
	 * Full constructor that can be used when isTSep = false, no value needs to be specified.
	 * @param headers
	 * @param urlI
	 * @param impI
	 * @param vSep
	 * @param dSep
	 * @param isTSep
	 * @param filename
	 * @param format
	 */
	public CSVReader(boolean headers, int urlI, int impI, char vSep, char dSep, boolean isTSep, File file, URLFormat format) {
		this.headers = headers;
		this.isTSep = isTSep;
		this.tSep = 0; // default: indicates that the value is missing
		this.dSep = dSep;
		this.vSep = vSep;
		this.urlI = urlI;
		this.impI = impI;
		this.format = format;
		set = setFile(file);
	}
	
	/**
	 * Full constructor that can be used when isTSep = true.
	 * @param headers
	 * @param urlI
	 * @param impI
	 * @param vSep
	 * @param dSep
	 * @param isTSep
	 * @param tSep
	 * @param filename
	 * @param format
	 */
	public CSVReader(boolean headers, int urlI, int impI, char vSep, char dSep, boolean isTSep, char tSep, File file, URLFormat format) {
		this.headers = headers;
		this.isTSep = isTSep;
		this.tSep = tSep;
		this.dSep = dSep;
		this.vSep = vSep;
		this.urlI = urlI;
		this.impI = impI;
		this.format = format;
		set = setFile(file);
	}
	
	
	
	/**
	 * 
	 * @return true if the CSVReader instance is set up and can be used, false otherwise
	 */
	public boolean isSet(){
		return set;
	}
	
	/*
	 * Checks and updates the set status of the CSVReader instance.
	 * It returns true if the CSVReader instance is set up and can be used, false otherwise
	 */
	private boolean checkSet(){
		set = ((!isTSep || tSep != 0) && dSep != 0 && vSep != 0) && urlI > -1 && impI > -1 && urlI != impI;
		set = set && source != null && source.exists() && source.canRead();
		return set;
	}
	
	/**
	 * 
	 * @param filename the name of the file that needs to be read
	 * @return true if the file exists and can be read
	 */
	public boolean setFile(String filename){
		source = new File(filename);
		return source.exists() && source.canRead();
	}
	
	/**
	 * 
	 * @param filename the name of the file that needs to be read
	 * @param path the path where the file can be found
	 * @return true if the file exists and can be read
	 */
	public boolean setFile(String path, String filename){
		source = new File(path, filename);
		return source.exists() && source.canRead();
	}
	
	/**
	 * 
	 * @param file the file that needs to be read
	 * @param path the path where the file can be found
	 * @return true if the file exists and can be read
	 */
	public boolean setFile(File file){
		source = file;
		return source.exists() && source.canRead();
	}

}