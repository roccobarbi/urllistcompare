/**
 * 
 */
package urllistcompare;

import java.io.Serializable;
import java.util.HashSet;

import urllistcompare.exceptions.*;
import urllistcompare.util.ArraySort;

/**
 * @author Rocco Barbini (roccobarbi@gmail.com)
 * 
 * A single normalised URL, which includes 2 hashsets of URLElements (for the formats that are being
 * compared).
 * This class must manage at least:
 * - the addition of new URLElement instances, which must be processed to increase the relevant page impressions count;
 * - the decision if one of the formats is missing (0 page impressions);
 * - the computation of relative and absolute differences between the two formats;
 * - the extraction of the list of URLElements for each URL format.
 * 
 * If the url field is null, it's a signal that the element is broken and MUST NOT be used.
 *
 */
public class URLNorm implements Serializable{
	
	private HashSet<URLElement> elements[];
	private int impressions[];
	private URLFormat format[];
	private String url; // Read-only after the URLNorm has been constructed
	private boolean noExtension; // Read-only after the URLNorm has been constructed

	@SuppressWarnings("unchecked")
	public URLNorm() {
		elements = new HashSet[2];
		elements[0] = new HashSet<URLElement>();
		elements[1] = new HashSet<URLElement>();
		impressions = new int[2];
		format = new URLFormat[2];
		noExtension = false; // Default behaviour to ensure consistency with legacy code
	}
	
	@Deprecated
	public URLNorm(URLFormat format01, URLFormat format02) {
		this(format01, format02, false); // Default behaviour to ensure consistency with legacy code: noExtension is false
	}
	
	@SuppressWarnings("unchecked")
	public URLNorm(URLFormat format01, URLFormat format02, boolean noExtension) {
		elements = new HashSet[2];
		elements[0] = new HashSet<URLElement>();
		elements[1] = new HashSet<URLElement>();
		impressions = new int[2];
		format = new URLFormat[2];
		format[0] = format01;
		format[1] = format02;
		this.noExtension = noExtension;
	}
	
	@Deprecated
	public URLNorm(URLElement element01, URLFormat format02) {
		this(element01, format02, false); // Default behaviour to ensure consistency with legacy code: noExtension is false
	}
	
	@SuppressWarnings("unchecked")
	public URLNorm(URLElement element01, URLFormat format02, boolean noExtension) {
		elements = new HashSet[2];
		elements[0] = new HashSet<URLElement>();
		elements[1] = new HashSet<URLElement>();
		impressions = new int[2];
		format = new URLFormat[2];
		format[0] = element01.getFormat();
		format[1] = format02;
		this.noExtension = noExtension;
		add(element01);
		url = element01.normalise(noExtension);
	}
	
	@Deprecated
	public URLNorm(URLFormat format0, URLFormat format1, URLElement first) {
		this(format0, format1, first, false); // Default behaviour to ensure consistency with legacy code: noExtension is false
	}
	
	@SuppressWarnings("unchecked")
	public URLNorm(URLFormat format0, URLFormat format1, URLElement first, boolean noExtension) {
		elements = new HashSet[2];
		elements[0] = new HashSet<URLElement>();
		elements[1] = new HashSet<URLElement>();
		impressions = new int[2];
		format = new URLFormat[2];
		format[0] = format0;
		format[1] = format1;
		this.noExtension = noExtension;
		add(first);
		url = first.normalise();
	}
	
	// Provides a deep copy
	@SuppressWarnings("unchecked")
	public URLNorm(URLNorm original) {
		elements = new HashSet[2];
		elements[0] = new HashSet<URLElement>();
		elements[1] = new HashSet<URLElement>();
		impressions = new int[2];
		format = new URLFormat[2];
		format[0] = original.getFormats()[0];
		format[1] = original.getFormats()[1];
		noExtension = original.getNoExtension(); 
		url = original.getUrl();
		for(URLElement e : original.getUrlElements(0)) add(e);
		for(URLElement e : original.getUrlElements(1)) add(e);
	}
	
	public boolean getNoExtension(){
		return noExtension;
	}
	
	public String getUrl(){
		if(url == null){
			throw new InvalidURLNormException("url not defined!");
		}
		return url;
	}
	
	public URLFormat[] getFormats(){
		if(format[0] == null){
			throw new InvalidURLNormException("The first format is null!");
		}
		if(format[0] == null){
			throw new InvalidURLNormException("The second format is null!");
		}
		return format;
	}
	
	public int[] getImpressions(){
		if(format[0] == null){
			throw new InvalidURLNormException("The first format is null!");
		}
		if(format[0] == null){
			throw new InvalidURLNormException("The second format is null!");
		}
		return impressions;
	}
	
	/**
	 * Checks the absolute difference between the page impressions of format 0 and 1.
	 * The result is [0] - [1]
	 * 
	 * @param f the format to check
	 * @return the difference of f relative to the other format
	 * @throws InvalidURLNormException	if at least one of the formats has not been set correctly or the wrong URLFormat is passed as an argument
	 */
	public int getDifference(){
		if(format[0] == null || format[1] == null) throw new InvalidURLNormException("Tried to check the difference between the formats" + 
				" without defining both formats.");
		return impressions[0] - impressions[1];
	}
	
	/**
	 * Checks the absolute difference between the page impressions of the specified format and those of the other format.
	 * If there are formats A and B, and format A is passed as argument, the result is A - B.
	 * 
	 * @param f the format to check
	 * @return the difference of f relative to the other format
	 * @throws InvalidURLNormException	if at least one of the formats has not been set correctly or the wrong URLFormat is passed as an argument
	 */
	public int getDifference(URLFormat f){
		int output = 0;
		if(format[0] == null || format[1] == null) throw new InvalidURLNormException("Tried to check the difference between the formats" + 
				" without defining both formats.");
		if(format[0] != f && format[1] != f) throw new InvalidURLNormException("Tried to check if a format is missing" + 
				"with a format that is not included in this URLNorm instance.");
		if(f == format[0]){
			output = impressions[0] - impressions[1];
		} else {
			output = impressions[1] - impressions[0];
		}
		return output;
	}
	
	/**
	 * If the format is null, it sets it as indicated by the argument. Otherwise the format becomes read-only.
	 * @param index the index of the format array that needs to be set
	 * @param format the new format
	 * @return true in case of success
	 */
	public boolean setFormat(int index, URLFormat format){
		if(index < 0 || index > 1){
			throw new IndexOutOfBoundsException("Tried to set format lower than 0 or greater than 1!");
		}
		if(this.format[index] == null){
			this.format[index] = format;
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Adds a URLElement instance to the relevant LinkeList and adds its page impressions count to the relevant array.
	 * Checks that both the URLElement and the current URLNorm are properly set.
	 * If the normalised url is still null, it sets it to the normalised url of the new element.
	 * 
	 * Precondition: both formats must be set
	 * 
	 * @param u	the URLElement that needs to be added
	 * @throws InvalidURLNormException if at least one of the formats has not been set correctly or if the URL has the wrong format
	 * @throws InvalidURLNormException if the URL is not null, but it is different from the normalised url from the new element.
	 * @throws RuntimeException if the URLElement is in a format that is not included in the URLNorm instance
	 * @return true if the operation was successful
	 */
	public boolean add(URLElement u) {
		boolean output = false;
		if(format[0] == null || format[1] == null) throw new InvalidURLNormException("Tried to add an element " + 
				"without defining both formats.");
		if(url == null) {
			url = u.normalise();
		} else if(!u.normalise().equals(url)) throw new InvalidURLNormException("Wrong URL!");
		if(u.getFormat() == format[0]){
			if(elements[0].add(u)){
				impressions[0] += u.getImpressions();
				output = true;
			}
		} else if (u.getFormat() == format[1]){
			if(elements[1].add(u)){
				impressions[1] += u.getImpressions();
				output = true;
			}
		} else {
			throw new RuntimeException("Tried to add a URLElement in the wrong format to a URLNorm instance!");
		}
		return output;
	}
	
	/**
	 * Checks if the URL is completely missing (zero page impressions) in at least one of the formats.
	 * 
	 * @param f the URLFormat that is been checked
	 * @return true if the format passed as an argument recorded zero impressions, false otherwise
	 * @throws InvalidURLNormException	if at least one of the formats has not been set correctly or the wrong URLFormat is passed as an argument
	 */
	public boolean isMissing(URLFormat f){
		if(format[0] == null || format[1] == null) throw new InvalidURLNormException("Tried to check if a format is missing" + 
				" without defining both formats.");
		if(format[0] != f && format[1] != f) throw new InvalidURLNormException("Tried to check if a format is missing" + 
				" with a format that is not included in this URLNorm instance.");
		return ((f == format[0] && impressions[0] == 0) || (f == format[1] && impressions[1] == 0));
	}
	
	/**
	 * Checks if the URL is completely missing (zero page impressions) in at least one of the formats.
	 * 
	 * @param index the index of the format that is been checked
	 * @return true if the format passed as an argument recorded zero impressions, false otherwise
	 * @throws InvalidURLNormException	if at least one of the formats has not been set correctly or the wrong URLFormat is passed as an argument
	 */
	public boolean isMissing(int index){
		if(format[0] == null || format[1] == null) throw new InvalidURLNormException("Tried to check if a format is missing" + 
				" without defining both formats.");
		if(index < 0 || index >= format.length) throw new IndexOutOfBoundsException("There are only 2 formats in a URLNorm object: the index can only be 0 or 1!");
		return (impressions[index] == 0);
	}
	
	/**
	 * 
	 * @param index the index of the format for which the elements should be exported
	 * @return a sorted (desc) array of URLElements for the specified index
	 */
	public URLElement[] getUrlElements(int index){
		URLElement output[] = null;
		if(index < 0 || index >= format.length) throw new IndexOutOfBoundsException("There are only 2 formats in a URLNorm object: the index can only be 0 or 1!");
		if(format[0] == null || format[1] == null) throw new InvalidURLNormException("Tried to extract the elements" + 
				" without defining both formats.");
		output = elements[index].toArray(new URLElement[elements[index].size()]);
		output = ArraySort.insertionSortDesc(output);
		return output;
	}
	
	/**
	 * 
	 * @param f the format for which the elements should be exported
	 * @return a sorted (desc) array of URLElements in the specified format
	 */
	public URLElement[] getUrlElements(URLFormat f){
		int index = 0;
		if(format[0] == null || format[1] == null) throw new InvalidURLNormException("Tried to extract the elements" + 
				" without defining both formats.");
		if(format[0] == f){
			index = 0;
		} else if(format[1] == f){
			index = 1;
		} else {
			throw new InvalidURLNormException("Tried to check if a format is missing" + 
					" with a format that is not included in this URLNorm instance.");
		}
		return getUrlElements(index);
	}

	/**
	 * Returns the hashCode of the normalised URL (using the standard String.hashCode method).
	 * @return the hashCode
	 */
	@Override
	public int hashCode(){
		if (url == null){
			throw new InvalidURLNormException("Tried to get the hashCode of a URLNorm instance without a url value!");
		}
		return url.hashCode();
	}
	
	/**
	 * Outputs a string in a valid csv format (semicolon-separated)
	 * @param index the format the detail of which should be exported
	 * @return a string with all URLs for the specified index, semicolon-separated and with each couple terminated by \n
	 */
	public String toCsv(int index){
		if(index < 0 || index > 1){
			throw new IndexOutOfBoundsException();
		}
		if(format[0] == null || format[1] == null){
			throw new InvalidURLNormException("At least one format is missing!"); 
		}
		StringBuilder outputB = new StringBuilder();
		for(Object element : this.elements[index].toArray()){
			outputB.append(((URLElement) element).getUrl() + ";" + ((URLElement) element).getImpressions() + "\n");
		}
		return outputB.toString();
	}
	
	/**
	 * Outputs a string in a valid csv format (separated by sep)
	 * @param index the format the detail of which should be exported
	 * @param sep the separator for the columns in the csv
	 * @return a string with all URLs for the specified index, separated by sep and with each couple terminated by \n
	 */
	public String toCsv(int index, char sep){
		if(index < 0 || index > 1){
			throw new IndexOutOfBoundsException();
		}
		if(format[0] == null || format[1] == null){
			throw new InvalidURLNormException("At least one format is missing!"); 
		}
		StringBuilder outputB = new StringBuilder();
		for(Object element : this.elements[index].toArray()){
			outputB.append(((URLElement) element).getUrl() + sep + ((URLElement) element).getImpressions() + "\n");
		}
		return outputB.toString();
	}
	
	/**
	 * Outputs a string in a valid csv format (semicolon-separated)
	 * @param f the format the detail of which should be exported
	 * @return a string with all URLs for the specified index, semicolon-separated and with each couple terminated by \n
	 */
	public String toCsv(URLFormat f){
		int index = 0;
		if(format[0] == null || format[1] == null) throw new InvalidURLNormException("At least one format is null!");
		if(format[0] == f){
			index = 0;
		} else if(format[1] == f){
			index = 1;
		} else {
			throw new InvalidURLNormException("Invalid format received!");
		}
		return toCsv(index);
	}
	
	/**
	 * Outputs a string in a valid csv format (separated by sep)
	 * @param f the format the detail of which should be exported
	 * @param sep the separator for the columns in the csv
	 * @return a string with all URLs for the specified index, separated by sep and with each couple terminated by \n
	 */
	public String toCsv(URLFormat f, char sep){
		int index = 0;
		if(format[0] == null || format[1] == null) throw new InvalidURLNormException("At least one format is null!");
		if(format[0] == f){
			index = 0;
		} else if(format[1] == f){
			index = 1;
		} else {
			throw new InvalidURLNormException("Invalid format received!");
		}
		return toCsv(index, sep);
	}
	
	/**
	 * @override
	 * @return a String describing the URLNorm element
	 */
	public String toString(){
		StringBuilder builder = new StringBuilder();
		builder.append("URLNORM: " + url + "\n");
		builder.append("format[0]: " + format[0] + (isMissing(0) ? " missing " : " not missing ") + "\n");
		builder.append("format[1]: " + format[1] + (isMissing(1) ? " missing " : " not missing ") + "\n");
		for(int i = 0; i < format.length; i++){
			builder.append("FORMAT " + i + "\n");
			for(URLElement e : getUrlElements(i)){
				builder.append("\t" + e);
			}
			builder.append("\n");
		}
		return builder.toString();
	}
}
