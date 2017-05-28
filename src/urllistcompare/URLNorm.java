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
	private URLFormat formats[];
	
	// TODO

	public URLNorm() {
		element = new LinkedList[2];
		impressions = new int[2];
		formats = new URLFormat[2];
	}
	
	public URLNorm(URLFormat format0, URLFormat format1) {
		element = new LinkedList[2];
		impressions = new int[2];
		formats = new URLFormat[2];
		formats[0] = format0;
		formats[1] = format1;
	}

}
