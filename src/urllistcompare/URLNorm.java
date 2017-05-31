/**
 * 
 */
package urllistcompare;

import urllistcompare.util.*;
import urllistcompare.exceptions.*;

/**
 * @author Rocco Barbini
 * @email roccobarbi@gmail.com
 * 
 * A single normalised URL, which includes 2 linked lists of URLElements (for the formats that are being
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
	
	private LinkedList<URLElement> element[];
	private int impressions[];
	private URLFormat format[];

	public URLNorm() {
		element = new LinkedList[2];
		impressions = new int[2];
		format = new URLFormat[2];
	}
	
	public URLNorm(URLFormat format0, URLFormat format1) {
		element = new LinkedList[2];
		impressions = new int[2];
		format = new URLFormat[2];
		format[0] = format0;
		format[1] = format1;
	}
	
	/**
	 * Adds a URLElement instance to the relevant LinkeList and adds its page impressions count to the relevant array.
	 * Checks that both the URLElement and the current URLNorm are properly set.
	 * 
	 * @param u	the URLElement that needs to be added
	 * @throws InvalidURLNormException
	 */
	public void add(URLElement u) throws InvalidURLNormException {
		if(format[0] == null || format[1] == null) throw new InvalidURLNormException("Tried to add an element withoud defining both formats.");
		try{
			if(u.getFormat() == format[0]){
				element[0].add(u);
				impressions[0] += u.getImpressions();
			} else if (u.getFormat() == format[1]){
				element[1].add(u);
				impressions[1] += u.getImpressions();
			} else {
				System.err.println("Invalid format: tried to add to a URLNorm instance a URLElement instance with the wrong URLFormat.");
			}
		} catch (InvalidUrlException e) {
			System.err.println("InvalidUrlException: tried to add an empty URLElement instance to a URLNorm instance.");
		}
	}
	
	// TODO: add functionality

}
