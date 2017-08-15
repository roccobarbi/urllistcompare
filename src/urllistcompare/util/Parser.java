/**
 * 
 */
package urllistcompare.util;

import java.util.regex.Pattern;

/**
 * @author Rocco Barbini (roccobarbi@gmail.com)
 * 
 * Utility class that contains methods to parse values into the program.
 *
 */
public final class Parser {

	public Parser() {
		// Do nothing
	}

	/**
	 * Parses a string to an integer, keeping any thousand or decimal separator into consideration.
	 * Decimals are rounded to the nearest unit.
	 * 
	 * @throws Exception if there is more than one instance of the decimal separator
	 * @throws Exception if any digit is not a number
	 * 
	 * @param input the string that contains the value that needs to be parsed to integer
	 * @param tSep the thousand separator, or 0 if none is used
	 * @param dSep the decimal separator, or 0 if none is used
	 * @return the parsed integer
	 */
	public static int parseInt(String input, char tSep, char dSep) throws Exception{
		StringBuilder noTSepSB = new StringBuilder();
		String noTSep = null, decimal = null;
		int round = 0, output = 0;
		// Remove the thousand separator, if used
		if(tSep > 0){
			for(String s : input.split(Pattern.quote(Character.toString(tSep)))) {
				noTSepSB.append(s);
			}
			noTSep = noTSepSB.toString();
		}
		// If the decimal separator is used...
		if(dSep > 0 && noTSep.contains(Character.toString(dSep))){
			// Check that there is only one
			if(noTSep.indexOf(dSep) != noTSep.lastIndexOf(dSep))
				throw new Exception("More than one decimal separator!");
			// Extract the decimal and check that it's only composed of numbers
			decimal = noTSep.substring(noTSep.indexOf(dSep) + 1);
			for(char c : decimal.toCharArray())
				if(!Character.isDigit(c))
					throw new Exception("At least one digit is not a number");
			// Interpret and round the decimals
			if(Integer.parseInt(decimal.substring(0, 1)) > 4)
				round = 1;
			// Extract the integer part for future use
			noTSep = noTSep.substring(0, noTSep.indexOf(dSep));
		}
		// Check that the string is only composed of numbers
		for(char c : noTSep.toCharArray())
			if(!Character.isDigit(c))
				throw new Exception("At least one digit is not a number");
		// Safely parse to int the integer part
		output = Integer.parseInt(noTSep);
		// Add the rounding bit
		output += round;
		return output;
	}
}
