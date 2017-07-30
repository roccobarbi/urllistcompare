/**
 * 
 */
package urllistcompare;

import java.util.Scanner;

/**
 * @author Rocco Barbini
 * @email roccobarbi@gmail.com
 * 
 * An enumeration of the URL formats accepted by the program, it can be easily expanded to accept more formats in the future.
 * Each format includes a sample value, which can be used to prompt the user with a series of examples and input the right format code.
 * Each format also includes a normalisePath() method that accepts a String parameter in the specified format and outputs a String with the
 * path, all in lowercase, without the protocol, domain, query and fragment. E.g. /path1/pathn/file.ext. 
 *
 */
public enum URLFormat {
	
	WTKDEF("www_domain_com.path.path.file_ext"){
		public String softNormalise(String url){
			url = url.replace('.', '/');
			url = url.replace('_', '.');
			if(url.contains("/")){
				url = url.substring(url.indexOf("/"));
			} else {
				url = "/"; // This is the root and it was saved without a trailing slash
			}
			return url.toLowerCase();
		}
	},
	URLNORM("http://www.domain.com/path/path/file.ext"){
		public String softNormalise(String url){
			if(url.contains("://")){
				url = url.substring(url.indexOf("://") + 3);
			}
			if(url.indexOf('/') > -1){
				url = url.substring(url.indexOf("/"));
			} else {
				url = "/"; // This is the root and it was saved without a trailing slash
			}
			return url.toLowerCase();
		}
	},
	NOPROTNORM("www.domain.com/path/path/file.ext"){
		public String softNormalise(String url){
			if(url.contains("/")){
				url = url.substring(url.indexOf("/"));
			} else {
				url = "/"; // This is the root and it was saved without a trailing slash
			}
			return url.toLowerCase();
		}
	},
	FULLURL("http://www.domain.com/PATH/path/file.ext?query#fragment"){
		public String softNormalise(String url){
			url = url.split("#")[0];
			url = url.split("\\?")[0];
			if(url.contains("://")){
				url = url.substring(url.indexOf("://") + 3);
			}
			if(url.contains("/")){
				url = url.substring(url.indexOf("/"));
			} else {
				url = "/"; // This is the root and it was saved without a trailing slash
			}
			return url.toLowerCase();
		}
	},
	GOOG("/path/path/file.ext?query#fragment"){
		public String softNormalise(String url){
			url = url.split("#")[0];
			url = url.split("\\?")[0];
			return url.toLowerCase();
		}
	};
	
	private final String formatSample;
	
	// Static variable that defines the normalisation mode for the URLFormat in the current execution for the program
	// that uses the URLFormat enum.
	private static boolean noExtension = false;
	
	public static void setNoExtension(boolean noExt){
		noExtension = noExt;
	}
	
	private URLFormat(String format){
		formatSample = format;
	}
	
	public String getFormatSample(){
		return formatSample;
	}
	
	/**
	 * It returns the URL Path normalised in the /path1/pathn/file.ext format.
	 * @param url
	 * @return The normalised URL path.
	 */
	public String normalisePath(String url){
		if(noExtension)
			return hardNormalise(url);
		else
			return softNormalise(url);
	}
	
	public String softNormalise(String url){
		// Override only
		// This method represents the normalisation that keeps the extension
		return url;
	}
	
	public String hardNormalise(String url){
		// This method represents the normalisation that eliminates the extension
		// It happens on top of the soft normalisation that is specific to each format
		String output = softNormalise(url);  
		return output.substring(0, output.lastIndexOf('.'));
	}
	
	// Prints a list of all available formats, preceded by their index
	// 0 : first format
	// 1 : second format
	// ...
	private static void printFormatsList(){
		for(URLFormat format : URLFormat.values()){
			System.out.println(format.ordinal() + " : " + format.getFormatSample());
		}
	}
	
	/**
	 * Terminal-based function that prompts the user to choose a format, manages wrong choices
	 * and outputs the chosen format.
	 * @return the chosen URLFormat
	 */
	public static URLFormat inputFormat(){
		// Variables needed to process the input loop
		char selector = 0;
		String input;
		Scanner keyboard = new Scanner(System.in);
		// Variables needed to parse the desired index
		StringBuilder indexString = new StringBuilder();
		int index = 0;
		// Loop until aborted (or returned from inside the loop
		while(selector != 'x'){
			// Prompt the user
			System.out.println("Select the right format from the available ones:");
			printFormatsList();
			System.out.println("x : abort and exit");
			System.out.print(">: ");
			input = keyboard.nextLine().toLowerCase();
			if(input.length() > 0){ // Otherwise keep loopint
				selector = input.charAt(0);
				if(selector != 'x'){ // Otherwise the user is aborting all operation
					// Parse the input number, assume that all digits are valid until a non-digit is found
					int i = 0;
					while(i < input.length() && Character.isDigit(input.charAt(i))){
						indexString.append(input.charAt(i));
						i++;
					}
					if(i > 0){
						// If a number was actually entered by the user, parse it
						index = Integer.parseInt(indexString.toString());
						if(index < URLFormat.values().length){
							// If the number is a valid index, return the corresponding format
							return URLFormat.values()[index];
						}
					}
				}
			}
		}
		// If the loop is ended without returning, it means the user is aborting and exiting
		System.out.println("Aborting as requested...");
		System.exit(0);
		return WTKDEF; // Filler, this position should never be reached
	}
	
	/**
	 * Terminal-based function that prompts the user to choose a format, manages wrong choices
	 * and outputs the chosen format.
	 * @param prompt the prefix to show the user when she's prompted for an input (to ensure a consistent user experience).
	 * @return the chosen URLFormat
	 */
	public static URLFormat inputFormat(String prompt){
		// Variables needed to process the input loop
		char selector = 0;
		String input;
		Scanner keyboard = new Scanner(System.in);
		// Variables needed to parse the desired index
		StringBuilder indexString = new StringBuilder();
		int index = 0;
		// Loop until aborted (or returned from inside the loop
		while(selector != 'x'){
			// Prompt the user
			System.out.println("Select the right format from the available ones:");
			printFormatsList();
			System.out.println("x : abort and exit");
			System.out.print(prompt);
			input = keyboard.nextLine().toLowerCase();
			if(input.length() > 0){ // Otherwise keep loopint
				selector = input.charAt(0);
				if(selector != 'x'){ // Otherwise the user is aborting all operation
					// Parse the input number, assume that all digits are valid until a non-digit is found
					int i = 0;
					while(i < input.length() && Character.isDigit(input.charAt(i))){
						indexString.append(input.charAt(i));
						i++;
					}
					if(i > 0){
						// If a number was actually entered by the user, parse it
						index = Integer.parseInt(indexString.toString());
						if(index < URLFormat.values().length){
							// If the number is a valid index, return the corresponding format
							return URLFormat.values()[index];
						}
					}
				}
			}
		}
		// If the loop is ended without returning, it means the user is aborting and exiting
		System.out.println("Aborting as requested...");
		System.exit(0);
		return WTKDEF; // Filler, this position should never be reached
	}
}
