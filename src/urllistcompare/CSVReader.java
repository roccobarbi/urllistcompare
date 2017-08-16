/**
 * 
 */
package urllistcompare;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.EOFException;
import java.util.ArrayList;
import java.util.Scanner;

import urllistcompare.util.Parser;

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
	// the destination
	private URLList destination;

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
		destination = null;
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
		destination = null;
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
		destination = null;
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
		destination = null;
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
		destination = null;
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
	public CSVReader(boolean headers, int urlI, int impI, char vSep, char dSep, boolean isTSep, File file) {
		this.headers = headers;
		this.isTSep = isTSep;
		this.tSep = 0; // default: indicates that the value is missing
		this.dSep = dSep;
		this.vSep = vSep;
		this.urlI = urlI;
		this.impI = impI;
		this.format = null;
		destination = null;
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
		destination = null;
		set = false;
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
		destination = null;
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
		destination = null;
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
		destination = null;
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
		destination = null;
		set = setFile(file);
	}
	
	public URLFormat getFormat(){
		return format;
	}
	
	public String getName(){
		return source.getName();
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
		set = set && format != null && destination != null;
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
	 * @return true if the file exists and can be read
	 */
	public boolean setFile(File file){
		source = file;
		return source.exists() && source.canRead();
	}
	
	/**
	 * 
	 * @param destination the destination URLList where the file should be written
	 */
	public void setDestination(URLList destination){
		this.destination = destination;
	}
	
	/**
	 * Read the source file's contents into the destination URLList
	 * @return true if successful, false otherwise
	 */
	public boolean read(){
		boolean output = false;
		Scanner inputStream;
		int impressions = 0;
		String impString;
		StringBuilder impStringB;
		String page;
		URLElement element;
		int k = 0;
		// First check if everything is fine and set the BufferedReader object
		set = checkSet();
		if(isSet()){
			try {
				inputStream = new Scanner(source); 
			} catch (FileNotFoundException e) {
				System.err.println("Problema nell'apertura del file " + source.getName());
				return false;
			}
			try{
				String row;
				while(true){
					if(!inputStream.hasNextLine())
						throw new EOFException("End of file reached!");
					row = inputStream.nextLine();
					if(k == 0){ // First line
						// Find and delete the bom, if present
						for(bom e : bom.values()){
							if(row.startsWith(e.bomString)){
								row = row.substring(e.bomLength);
								break;
							}
						}
					}
					// If there are no headers, parse the first line
					// All other lines are parsed
					if(k > 0 || !headers){
						page = row.split(Character.toString(vSep))[urlI];
						impString = row.split(Character.toString(vSep))[impI];
						try{
							impressions = Parser.parseInt(impString, isTSep ? tSep : 0, dSep);
						} catch (Exception e) {
							System.out.println("Error parsing the impressions at line " + k + ": " + e.getMessage());
							System.exit(1);
						}
						element = new URLElement(page, format, impressions);
						destination.add(element);
					}
					k++;
				}
			} catch(EOFException e){
				System.out.println("File " + source + " letto correttamente!");
			} catch (IOException e) {
				System.out.println("Errore nella lettura da " + source);
			} finally {
				inputStream.close();
			}
			output = true;
		}
		return output;
	}
	
	/**
	 * Parses a line of the input file and returns a String array with each column.
	 * After the line has been parsed, the program should check that the number of columns is coherent with the document.
	 * 
	 * @param line the line from the input file
	 * @param vSep the value separator for the file
	 * @return a String array with the columns
	 */
	private String[] readCSVLine(String line, char vSep) throws Exception{
		StringBuilder tempCol = null;
		ArrayList <String> output = new ArrayList <String> ();
		boolean dQuote = false; // Flag: a doublequote has been opened.
		boolean column = false; // Flag: a column is already being read
		char [] input = line.toCharArray();
		for(int c = 0; c < input.length; c++){
			if(!column){
				// If the character is the first one in a column (the column flag is false)
				if(input[c] == '"'){
					// If it is a double quote, jump to the next and set the dQuote flag to true
					dQuote = true;
					column = true;
					// Reset tempCol
					tempCol = new StringBuilder();
				} else if(c != vSep){
					// If it is a vSep, do nothing. Otherwise, read it and go on.
					// Also, set the column flag to true to mark that a column is being read.
					column = true;
					// Reset tempCol
					tempCol = new StringBuilder();
					// Read the character
					tempCol.append(input[c]);
				}
			} else{
				// If the character is not the first one in a column (the column flag is true)
				if(dQuote && input[c] == '"'){
					// If it is a doublequote and the column is double quoted (the dQuote flag is true)
					if (input.length == c || input[c+ 1] == vSep) {
						// If it's the last character in the input or the next one is a vSep
						// Unflag dQuote and column
						dQuote = false;
						column = false;
						// If it is not the last character in the input, move one additional character forward
						if(input.length > c)
							c++;
						// Add tempCol to the output
						output.add(tempCol.toString());
					} else if(input[c+ 1] == '"'){
						// If the next character is a doublequote read one of them and add 1 to i
						tempCol.append('"');
						c++;
					} else {
						// Otherwise, there's something wrong with the file
						throw new Exception ("Unescaped doublequote at character " + c + " of a doublequoted sequence!");
					}
				} else if (!dQuote && input[c] == vSep){
					// If it is a vSep AND the column is not double quoted (the dQuote flag is false)
					// Unflag column
					column = false;
					// Add tempCol to the output
					output.add(tempCol.toString());
				} else {
					// In any other case, just read the character
					tempCol.append(input[c]);
				}
			}
		}
		return output.toArray(new String[0]);
	}

}
