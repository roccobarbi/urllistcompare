/**
 * 
 */
package urllistcompare;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import urllistcompare.exceptions.InvalidURLListException;
//import urllistcompare.exceptions.InvalidURLNormException;
import urllistcompare.exceptions.InvalidURLNormException;

/**
 * Implements an hashmap of URLNorm elements that can be processed to extract,
 * for example, any missing elements or elements in which the numbers differ too
 * much between the formats.
 * 
 * @author Rocco Barbini (roccobarbi@gmail.com)
 *
 */
public class URLList implements Serializable {

	private static final long serialVersionUID = 3866668960472537773L;
	// Default threshold for absolute differences (if less than 10, ignore)
	public static final int DEF_ABS_THRESHOLD = 10;
	// Default threshold for percent differences (if less than 0.01, ignore)
	public static final double DEF_PER_THRESHOLD = 0.01;

	private HashMap<String, URLNorm> url;
	private URLFormat[] format;
	private boolean active;
	private boolean noExtension; // Read-only after the URLList has been
									// constructed

	public URLList() {
		url = new HashMap<String, URLNorm>(1500, (float) 0.95);
		format = new URLFormat[2];
		active = false;
		noExtension = false; // Default behaviour to ensure consistency with
								// legacy code
	}

	@Deprecated
	public URLList(URLFormat format01, URLFormat format02) {
		this(format01, format02, false); // Default behaviour to ensure
											// consistency with legacy code:
											// noExtension is false
	}

	public URLList(URLFormat format01, URLFormat format02,
			boolean noExtension) {
		this();
		format[0] = format01;
		format[1] = format02;
		active = true;
		this.noExtension = noExtension;
	}

	/**
	 * 
	 * @param index
	 *            the index from which the format should be read
	 * @return the format at the specified index
	 */
	public URLFormat
		getFormat(int index) {
		if (index < 0 || index >= format.length)
			throw new IndexOutOfBoundsException();
		return format[index];
	}

	/**
	 * Sets the format at a specific index, if null. Otherwise it doesn't change
	 * it.
	 * 
	 * @param index
	 *            the index to set
	 * @param format
	 *            the format to set
	 * @return true if the format was set, false otherwise
	 */
	public boolean
		setFormat(int index, URLFormat format) {
		boolean output = false;
		if (index < 0 || index >= this.format.length)
			throw new IndexOutOfBoundsException();
		if (this.format[index] == null) {
			this.format[index] = format;
			output = true;
		}
		// Activates the list if both formats are set
		// Implemented in a scalable way, in case more than 2 formats are ever
		// managed at the same time
		active = true;
		for (URLFormat f : this.format) {
			if (f == null)
				active = false;
		}
		return output;
	}

	/**
	 * 
	 * @return the keys to the HashMap that contains the URLNorm instances
	 */
	public Set<String>
		keySet() {
		return url.keySet();
	}

	/**
	 * 
	 * @param key
	 *            the normalised url that needs to be found
	 * @return a deep copy of the URLNorm, or null if not present
	 */
	public URLNorm
		getUrlNorm(String key) {
		URLNorm output = null;
		if (url.keySet().contains(key)) {
			output = new URLNorm(url.get(key));
		}
		return output;
	}

	/**
	 * 
	 * @return true if the URLList is completely configured and can be used
	 */
	public boolean
		isActive() {
		return active;
	}

	/**
	 * Adds an element to the hashmap of URLNorm instances, creating the URLNorm
	 * instance if needed.
	 * 
	 * Legacy version that infers the position from the format of the element. 
	 * 
	 * @param element
	 *            the URLElement to add
	 * @return true if successful, false otherwise (e.g. duplicated element)
	 */
	@Deprecated
	public boolean
		add(URLElement element) {
		boolean output = false;
		int pos = -1; // Default: invalid value
		if (!isActive())
			throw new InvalidURLListException("URLList not active!");
		if (element.getFormat() != format[0]
				&& element.getFormat() != format[1])
			throw new RuntimeException(
					"Tried to add a URLElement in the wrong format to a URLList instance!");
		if (format[0] == format[1])
			throw new InvalidURLNormException("Tried to add an element without specifying the position and both formats are equal.");
		if (format[0] == element.getFormat()) {
			pos = 0;
		} else if (format[0] == element.getFormat()) {
			pos = 1;
		} else {
			throw new RuntimeException("Tried to add a URLElement in the wrong format to a URLList instance!");
		}
		if (url.containsKey(element.normalise())) {
			output = url.get(element.normalise()).add(element, pos);
			;
		} else {
			URLNorm n = new URLNorm(format[0], format[1], noExtension);
			output = n.add(element, pos);
			url.put(n.getUrl(), n);
		}
		return output;
	}
	
	/**
	 * Adds an element to the hashmap of URLNorm instances, creating the URLNorm
	 * instance if needed.
	 * 
	 * @param element the URLElement to add
	 * @param pos the position of the element (if it's format 1 or 2)
	 * @return true if successful, false otherwise (e.g. duplicated element)
	 */
	public boolean
		add(URLElement element, int pos) {
		boolean output = false;
		if (!isActive())
			throw new InvalidURLListException("URLList not active!");
		if (pos < 0 || pos >= format.length)
			throw new RuntimeException(
					"Tried to add a URLElement to a URLList instance in an invalid position: " + pos + "!");
		if (element.getFormat() != format[pos])
			throw new RuntimeException(
					"Tried to add a URLElement in the wrong format to a URLList instance!");
		if (url.containsKey(element.normalise())) {
			output = url.get(element.normalise()).add(element, pos);
		} else {
			URLNorm n = new URLNorm(format[0], format[1], noExtension);
			output = n.add(element, pos);
			url.put(n.getUrl(), n);
		}
		return output;
	}

	/**
	 * 
	 * @param index
	 *            the index to check for any missing elements
	 * @return An array of URLElement objects that are missing the format of the
	 *         specified index
	 */
	public URLElement[]
		getMissingElements(int index) {
		if (index < 0 || index >= format.length)
			throw new IndexOutOfBoundsException();
		if (!isActive())
			throw new InvalidURLListException("URLList not active!");
		ArrayList<URLElement> output = new ArrayList<URLElement>(100);
		for (String k : url.keySet()) {
			if (url.get(k).isMissing(index)) {
				for (URLElement e : url.get(k)
						.getUrlElements(Math.abs(index - 1))) {
					output.add(e);
				}
			}
		}
		if (output.isEmpty())
			return new URLElement[0];
		else
			return output.toArray(new URLElement[1]);
	}

	/**
	 * 
	 * @param f
	 *            the format to check for any missing elements
	 * @return An array of URLElement objects that are missing the format
	 *         specified
	 */
	public URLElement[]
		getMissingElements(URLFormat f) {
		int index = 0;
		if (format[0] == null || format[1] == null)
			throw new InvalidURLListException(
					"Tried to extract the missing elements"
							+ " without defining both formats.");
		if (format[0] == f) {
			index = 0;
		} else if (format[1] == f) {
			index = 1;
		} else {
			throw new InvalidURLListException(
					"Tried to check if a format is missing"
							+ " with a format that is not included in this URLNorm instance.");
		}
		return getMissingElements(index);
	}

	/**
	 * 
	 * @param index
	 *            the index to be checked
	 * @return an array of deep copies of those URLNorm objects in which the
	 *         difference between the format with the specified index and the
	 *         other format exceeds the default thresholds
	 */
	public URLNorm[]
		getDifferentURLs(int index) {
		if (index < 0 || index >= format.length)
			throw new IndexOutOfBoundsException();
		if (!isActive())
			throw new InvalidURLListException("URLList not active!");
		ArrayList<URLNorm> output = new ArrayList<URLNorm>(100);
		int tempAbsDiff = 0;
		double tempPerDiff = 0.0;
		URLNorm tempUrl = null;
		for (String k : url.keySet()) {
			tempUrl = url.get(k);
			tempAbsDiff = tempUrl.getDifference(format[index]);
			if (Math.abs(tempAbsDiff) > DEF_ABS_THRESHOLD) {
				tempPerDiff = tempUrl.getDifferencePercent(format[index]);
				if (Math.abs(tempPerDiff) > DEF_PER_THRESHOLD) {
					output.add(new URLNorm(tempUrl)); // Deep copy
				}
			}
		}
		return output.toArray(new URLNorm[0]);
	}
	
	// TODO: add versions in which you specify the format and thresholds

	/**
	 * 
	 * @return a reference to the current URLList if noExtension is false,
	 *         otherwise a new URLList recalculated with the extension (soft
	 *         normalisation)
	 */
	public URLList
		addExtension() {
		if (!noExtension) {
			return this;
		} else {
			URLList output = new URLList(this.getFormat(0), this.getFormat(1),
					false);
			for (String key : this.keySet()) {
				for (int i = 0; i < 2; i++) {
					for (URLElement element : this.getUrlNorm(key)
							.getUrlElements(i)) {
						output.add(element);
					}
				}
			}
			return output;
		}
	}

	/**
	 * 
	 * @return a reference to the current URLList if noExtension is true,
	 *         otherwise a new URLList recalculated without the extension (hard
	 *         normalisation)
	 */
	public URLList
		remExtension() {
		if (noExtension) {
			return this;
		} else {
			URLList output = new URLList(this.getFormat(0), this.getFormat(1),
					true);
			for (String key : this.keySet()) {
				for (int i = 0; i < 2; i++) {
					for (URLElement element : this.getUrlNorm(key)
							.getUrlElements(i)) {
						output.add(element);
					}
				}
			}
			return output;
		}
	}
}
