/**
 * 
 */
package urllistcompare;

import java.util.ArrayList;
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
	
	/**
	 * Adds a URLElement instance to the relevant LinkeList and adds its page impressions count to the relevant array.
	 * Checks that both the URLElement and the current URLNorm are properly set.
	 * 
	 * @param u	the URLElement that needs to be added
	 * @throws InvalidURLNormException if at least one of the formats has not been set correctly or if the URL has the wrong format
	 */
	public void add(URLElement u) {
		if(format[0] == null || format[1] == null) throw new InvalidURLNormException("Tried to add an element" + 
				"without defining both formats.");
		try{
			if(u.getFormat() == format[0]){
				if(!elements[0].add(u)) System.err.println("Tried to add URLElement " + u.getUrl() + " twice!");
				impressions[0] += u.getImpressions();
			} else if (u.getFormat() == format[1]){
				if(!elements[1].add(u)) System.err.println("Tried to add URLElement " + u.getUrl() + " twice!");
				impressions[1] += u.getImpressions();
			} else {
				System.err.println("Invalid format: tried to add to a URLNorm instance a URLElement instance with the wrong URLFormat.");
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
		boolean output = false;
		if(format[0] == null || format[1] == null) throw new InvalidURLNormException("Tried to check if a format is missing" + 
				"without defining both formats.");
		if(format[0] != f && format[1] != f) throw new InvalidURLNormException("Tried to check if a format is missing" + 
				"with a format that is not included in this URLNorm instance.");
		if((f == format[0] && impressions[0] == 0) || (f == format[1] && impressions[1] == 0)){
			output = true;
		}
		return output;
	}
	
	/**
	 * Checks the absolute difference between the page impressions of the specified format and those of the other format.
	 * If there are formats A and B, andad format A is passed as argument, the result is A - B.
	 * 
	 * @param f the format to check
	 * @return the difference of f relative to the other format
	 * @throws InvalidURLNormException	if at least one of the formats has not been set correctly or the wrong URLFormat is passed as an argument
	 */
	public int getDifference(URLFormat f) throws InvalidURLNormException {
		int output = 0;
		if(format[0] == null || format[1] == null) throw new InvalidURLNormException("Tried to check if a format is missing" + 
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
	
	// TODO: needs improvement
	@Override
	public int hashCode(){
		if (url == null){
			return -1;
		}
		return url.hashCode();
	}
	// TODO: add functionality

}
