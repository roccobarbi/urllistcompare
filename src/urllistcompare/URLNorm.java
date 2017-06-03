/**
 * 
 */
package urllistcompare;

import java.util.HashSet;

import urllistcompare.exceptions.*;

/**
 * @author Rocco Barbini
 * @email roccobarbi@gmail.com
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
public class URLNorm {
	
	private HashSet<URLElement> elements[];
	private int impressions[];
	private URLFormat format[];
	private String url;

	@SuppressWarnings("unchecked")
	public URLNorm() {
		elements = new HashSet[2];
		impressions = new int[2];
		format = new URLFormat[2];
	}
	
	@SuppressWarnings("unchecked")
	public URLNorm(URLFormat format01, URLFormat format02) {
		elements = new HashSet[2];
		impressions = new int[2];
		format = new URLFormat[2];
		format[0] = format01;
		format[1] = format02;
	}
	
	@SuppressWarnings("unchecked")
	public URLNorm(URLElement element01, URLFormat format02) {
		elements = new HashSet[2];
		impressions = new int[2];
		format = new URLFormat[2];
		format[0] = element01.getFormat();
		format[1] = format02;
		add(element01);
		url = element01.normalise();
	}
	
	@SuppressWarnings("unchecked")
	public URLNorm(URLFormat format0, URLFormat format1, URLElement first) {
		elements = new HashSet[2];
		impressions = new int[2];
		format = new URLFormat[2];
		format[0] = format0;
		format[1] = format1;
		add(first);
		url = first.normalise();
	}
	
	public String getUrl(){
		if(url == null){
			throw new InvalidURLNormException("url not defined!");
		}
		return url;
	}
	
	/**
	 * 
	 * @param index the index of the format array that needs to be set
	 * @param format the new format
	 * @return true in case of success
	 */
	public boolean setFormat(int index, URLFormat format){
		if(index < 0 || index > 1){
			throw new IndexOutOfBoundsException("Tried to set format lower than 0 or greater than 1!");
		}
		this.format[index] = format;
		return true;
	}
	
	/**
	 * Adds a URLElement instance to the relevant LinkeList and adds its page impressions count to the relevant array.
	 * Checks that both the URLElement and the current URLNorm are properly set.
	 * 
	 * @param u	the URLElement that needs to be added
	 * @throws InvalidURLNormException if at least one of the formats has not been set correctly or if the URL has the wrong format
	 * @throws RuntimeException if the URLElement is in a format that is not included in the URLNorm instance
	 */
	public void add(URLElement u) {
		if(format[0] == null || format[1] == null) throw new InvalidURLNormException("Tried to add an element" + 
				"without defining both formats.");
		try{
			if(u.getFormat() == format[0]){
				if(!elements[0].add(u)){
					System.err.println("Tried to add URLElement " + u.getUrl() + " twice!");
				} else {
					impressions[0] += u.getImpressions();
				}
			} else if (u.getFormat() == format[1]){
				if(!elements[1].add(u)){
					System.err.println("Tried to add URLElement " + u.getUrl() + " twice!");
				} else {
					impressions[1] += u.getImpressions();
				}
			} else {
				throw new RuntimeException("Tried to add a URLElement in the wrong format to a URLNorm instance!");
			}
		} catch (InvalidUrlException e) {
			System.err.println("InvalidUrlException: tried to add an empty URLElement instance to a URLNorm instance.");
		}
	}
	
	/**
	 * Checks if the URL is completely missing (zero page impressions) in at least one of the formats.
	 * 
	 * @param f the URLFormat that is been checked
	 * @return true if the format passed as an argument recorded zero impressions, false otherwise
	 * @throws InvalidURLNormException	if at least one of the formats has not been set correctly or the wrong URLFormat is passed as an argument
	 */
	public boolean isMissing(URLFormat f) throws InvalidURLNormException {
		if(format[0] == null || format[1] == null) throw new InvalidURLNormException("Tried to check if a format is missing" + 
				"without defining both formats.");
		if(format[0] != f && format[1] != f) throw new InvalidURLNormException("Tried to check if a format is missing" + 
				"with a format that is not included in this URLNorm instance.");
		return ((f == format[0] && impressions[0] == 0) || (f == format[1] && impressions[1] == 0));
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
		if(format[0] == null || format[1] == null) throw new InvalidURLNormException("Tried to check if a format is missing" + 
				"without defining both formats.");
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
				"without defining both formats.");
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
	 * Checks the list of URLElements of a specific format.
	 * 
	 * @param f the format for which the list of elements needs to be extracted
	 * @return the list of URLElements of a specific format
	 * @throws InvalidURLNormException	if at least one of the formats has not been set correctly or the wrong URLFormat is passed as an argument
	 */
	public URLElement[] getElements(URLFormat f) throws InvalidURLNormException{
		int index = 0;
		if(format[0] == null || format[1] == null) throw new InvalidURLNormException("Tried to check if a format is missing" + 
				"without defining both formats.");
		if(format[0] == f){
			index = 0;
		} else if(format[1] == f){
			index = 1;
		} else {
			throw new InvalidURLNormException("Tried to check if a format is missing" + 
					"with a format that is not included in this URLNorm instance.");
		}
		URLElement[] output = elements[index].toArray(new URLElement[elements[index].size()]);
		return output;
	}
	
	@Override
	public int hashCode(){
		if (url == null){
			throw new InvalidURLNormException("Tried to get the hashCode of a URLNorm instance without a url value!");
		}
		return url.hashCode();
	}
	
	public String toCsv(int index){
		if(index < 0 || index > 1){
			throw new IndexOutOfBoundsException("Tried to set format lower than 0 or greater than 1!");
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
	// TODO: add functionality

}
