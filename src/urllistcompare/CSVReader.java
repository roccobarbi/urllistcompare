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
 * 
 * This class is tasked with reading the contents of a CSV file into a URLList.
 * <p>
 * The class supports a default constructor, but the suggested way to create a CSVReader is
 * to use a CSVReaderBuilder
 * <p>
 * It can receive the format, otherwise it will prompt the user to receive it.
 * It can be set with the presence of headers, thousand and decimal separators, value separator,
 * with the column numbers for the page impressions and the url. Otherwise, it prompts the user
 * for those values.
 * <p>
 * In case this class is used with a GUI, those values should be set in the constructor or with accessor
 * functions before the class start reading the file.
 * 
 * @see CSVReaderBuilder
 * 
 * @author Rocco Barbini (roccobarbi@gmail.com)
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
	// the position at the destination
	private int position;

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
		position = -1; // default: invalid position
	}
	
	private CSVReader(CSVReaderBuilder builder) {
		this.headers = builder.headers;
		this.isTSep = builder.isTSep;
		this.tSep = builder.tSep;
		this.dSep = builder.dSep;
		this.vSep = builder.vSep;
		this.urlI = builder.urlI;
		this.impI = builder.impI;
		this.set = setFile(builder.source);
		this.format = builder.format;
		this.destination = builder.destination;
		this.position = builder.position;
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
	 * 
	 * @param position the index for the output of this reader within the destination URLList
	 */
	public void setPosition(int position) {
		this.position = position;
	}
	
	/**
	 * 
	 * @param headers true if there is a header line
	 */
	public void setHeaders(boolean headers) {
		this.headers = headers;
	}
	
	/**
	 * 
	 * @param isTSep true if there is a thousand separator
	 */
	public void setIsTSep(boolean isTSep) {
		this.isTSep = isTSep;
	}
	
	/**
	 * 
	 * @param tSep the thousand separator
	 */
	public void setTSep(char tSep) {
		this.tSep = tSep;
	}
	
	/**
	 * 
	 * @param dSep the decimal separator
	 */
	public void setDSep(char dSep) {
		this.dSep = dSep;
	}
	
	/**
	 * 
	 * @param vSep the value separator
	 */
	public void setVSep(char vSep) {
		this.vSep = vSep;
	}
	
	/**
	 * Read the source file's contents into the destination URLList
	 * @return true if successful, false otherwise
	 */
	public boolean read(){
		boolean output = false;
		Scanner inputStream;
		int impressions = 0;
		int columns = 0; // To perform a consistency check based on the first line
		String [] line = null; // To temporarily save the line as a String array
		String row; // To store the row as it is received from the file 
		String impString;
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
						// Count the number of columns that are to be expected
						columns = readCSVLine(row, vSep).length;
					}
					// If there are no headers, parse the first line
					// All other lines are parsed
					if(k > 0 || !headers){
						try{
							line = readCSVLine(row, vSep);
						} catch (Exception e) {
							throw new Exception("Consistency error within the file " + source + " at line " + k + ": " + e.getMessage());
						}
						if(line.length != columns)
							throw new Exception ("Wrong number of columns at line " + k);
						page = line[urlI];
						impString = line[impI];
						try{
							impressions = Parser.parseInt(impString, isTSep ? tSep : 0, dSep);
						} catch (Exception e) {
							throw new Exception("Error parsing the impressions at line " + k + ": " + e.getMessage());
						}
						element = new URLElement(page, format, impressions);
						destination.add(element, position);
					}
					k++;
				}
			} catch(EOFException e){
				System.out.println("File " + source + " letto correttamente!");
			} catch (IOException e) {
				System.out.println("Errore nella lettura da " + source);
			} catch (Exception e) {
				// Close the input stream
				inputStream.close();
				// Stop all engines, but in a way that can be catched
				throw new RuntimeException("Unrecoverable error while reading " + source + ": " + e.getMessage());
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
		StringBuilder tempCol = new StringBuilder();
		ArrayList <String> output = new ArrayList <String> ();
		boolean dQuote = false; // Flag: a doublequote has been opened.
		char [] input = line.toCharArray();
		for(int c = 0; c < input.length; c++){
			if(dQuote){
				// If there is an open double quote
				if(input[c] == '"'){
					// If another doublequote is encountered
					if (input.length == c || input[c+ 1] == vSep) {
						// If it's the last character in the input or the next one is a vSep
						// Unflag dQuote
						dQuote = false;
						// If it is not the last character in the input, move one additional character forward
						if(input.length > c)
							c++;
						// Add tempCol to the output
						output.add(tempCol.toString());
						// Reset tempCol
						tempCol.delete(0, tempCol.length());
					} else if(input[c+ 1] == '"'){
						// If the next character is a doublequote read one of them and add 1 to c
						tempCol.append('"');
						c++;
					} else {
						// Otherwise, there's something wrong with the file
						throw new Exception ("Unescaped doublequote at character " + c + " of a doublequoted sequence!");
					}
				} else if(input.length == c + 1){
					// If this is the last character in the line and it is not a doublequote
					// Then something is wrong with the file
					throw new Exception ("Double quoted sequence not closed at the end of the line!");
				} else {
					// Otherwise just read the character and move on
					tempCol.append(input[c]);
				}
			} else {
				// If there is not an open double quote
				if (input[c] == vSep){
					// If the current char is a vSep
					// Otherwise add tempCol to the output
					output.add(tempCol.toString());
					if(input.length == c + 1){
						// If this was the last character in the line
						// Add an empty string to the output
						output.add("");
					} else if (input[c + 1] == '"'){
						// If the new column starts with a double quote
						// Flag dQuote and jump one character farther
						dQuote = true;
						c++;
					}
					// Reset tempCol
					tempCol.delete(0, tempCol.length());
				} else {
					// if it's a first character and a double quote
					if (c == 0 && input[c] == '"') {
						// flag dQuote
						dQuote = true;
					} else {
						// In any other case, just read the character
						tempCol.append(input[c]);
						if(input.length == c + 1){
							// If this was the last character in the line
							// Add tempCol to the output
							output.add(tempCol.toString());
						}
					}
				}
			}
		}
		return output.toArray(new String[0]);
	}
	
	/**
	 * Implementation with the builder pattern.
	 * 
	 * The CSVReaderBuilder must always be called with a source File as the argument. It is
	 * also strongly advised that the other methods are called to properly set all values
	 * before the build method is ultimately called to return a proper CSVReader.
	 * 
	 * Example:
	 * 
	 * CSVReader reader = new CSVReader
	 *			.CSVReaderBuilder(new File("test001.txt"))
	 *			.headers(true)
	 *			.urlI(0)
	 *			.impI(1)
	 *			.vSep(';')
	 *			.dSep('.')
	 *			.isTSep(true)
	 *			.tSep(',')
	 *			.format(URLFormat.URLNORM)
	 *			.build();
	 * 
	 * @author rocco barbini (roccobarbi@gmail.com)
	 *
	 */
	public static class CSVReaderBuilder {
		private boolean headers;
		private boolean isTSep;
		private char tSep;
		private char dSep;
		private char vSep;
		private int urlI;
		private int impI;
		private URLFormat format;
		private final File source;
		private int position;
		private URLList destination;
		
		public CSVReaderBuilder (File source) {
			this.source = source;
			this.headers = true; // default
			this.isTSep = true; // default
			this.tSep = 0; // default
			this.dSep = 0; // default
			this.vSep = 0; // default
			this.urlI = -1; //default
			this.impI = -1; // default
			this.format = null; // default
			this.destination = null; // default
			this.position = -1;
		}
		
		public CSVReaderBuilder headers(boolean headers) {
			this.headers = headers;
			return this;
		}
		
		public CSVReaderBuilder isTSep(boolean isTSep) {
			this.isTSep = isTSep;
			return this;
		}
		
		public CSVReaderBuilder tSep(char tSep) {
			this.tSep = tSep;
			return this;
		}
		
		public CSVReaderBuilder dSep(char dSep) {
			this.dSep = dSep;
			return this;
		}
		
		public CSVReaderBuilder vSep(char vSep) {
			this.vSep = vSep;
			return this;
		}
		
		public CSVReaderBuilder urlI(int urlI) {
			this.urlI = urlI;
			return this;
		}
		
		public CSVReaderBuilder impI(int impI) {
			this.impI = impI;
			return this;
		}
		
		public CSVReaderBuilder format(URLFormat format) {
			this.format = format;
			return this;
		}
		
		public CSVReaderBuilder destination(URLList destination) {
			this.destination = destination;
			return this;
		}
		
		public CSVReaderBuilder position(int position) {
			this.position = position;
			return this;
		}
		
		public CSVReader build() {
			return new CSVReader(this);
		}
	}

}
