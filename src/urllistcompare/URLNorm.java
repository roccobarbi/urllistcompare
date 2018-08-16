/**
 * 
 */
package urllistcompare;

import java.io.Serializable;
import java.util.HashSet;

import urllistcompare.exceptions.*;
import urllistcompare.util.ArraySort;

/**
 * 
 * A single normalised URL, which includes 2 hashsets of URLElements (for the
 * lists that are being compared, which can be of the same format if needed).
 * <p>
 * This class manages:
 * <ul>
 * <li>the addition of new URLElement instances, which are processed to
 * increase the relevant page impressions count;
 * <li>the decision if one of the formats is missing (0 page impressions);
 * <li>the computation of relative and absolute differences between the two
 * formats;
 * <li>the extraction of the list of URLElements for each URL format.
 * </ul>
 * If the url field is null, it's a signal that the element is broken and MUST
 * NOT be used.
 * 
 * @author Rocco Barbini (roccobarbi@gmail.com)
 *
 */
public class URLNorm implements Serializable {
	
	private static final long serialVersionUID = 7469036981953949805L;
	private HashSet<URLElement> elements[];
	private int impressions[];
	private URLFormat format[];
	private String url; // Read-only after the URLNorm has been constructed
	private boolean noExtension; // Read-only after the URLNorm has been
									// constructed

	@SuppressWarnings("unchecked")
	public URLNorm() {
		elements = new HashSet[2];
		elements[0] = new HashSet<URLElement>();
		elements[1] = new HashSet<URLElement>();
		impressions = new int[2];
		format = new URLFormat[2];
		noExtension = false; // Default behaviour to ensure consistency with legacy code
	}

	@SuppressWarnings("unchecked")
	public URLNorm(URLFormat format01, URLFormat format02,
			boolean noExtension) {
		elements = new HashSet[2];
		elements[0] = new HashSet<URLElement>();
		elements[1] = new HashSet<URLElement>();
		impressions = new int[2];
		format = new URLFormat[2];
		format[0] = format01;
		format[1] = format02;
		this.noExtension = noExtension;
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
		for (URLElement e : original.getUrlElements(0))
			add(e, 0);
		for (URLElement e : original.getUrlElements(1))
			add(e, 1);
	}
	
	// Check: are both formats set?
	private boolean isFormatSet() {
		return format[0] != null && format[1] != null;
	}
	
	public boolean
		getNoExtension() {
		return noExtension;
	}

	public String
		getUrl() {
		if (url == null) {
			throw new InvalidURLNormException("url not defined!");
		}
		return url;
	}

	public URLFormat[]
		getFormats() {
		if (!isFormatSet()) {
			throw new InvalidURLNormException("The URL formats are not set!");
		}
		return format;
	}

	public int[]
		getImpressions() {
		if (!isFormatSet()) {
			throw new InvalidURLNormException("The URL formats are not set!");
		}
		return impressions;
	}

	/**
	 * Checks the absolute difference between the page impressions of format 0
	 * and 1. The result is [0] - [1]
	 * 
	 * @param f
	 *            the format to check
	 * @return the difference of f relative to the other format
	 * @throws InvalidURLNormException
	 *             if at least one of the formats has not been set correctly or
	 *             the wrong URLFormat is passed as an argument
	 */
	public int
		getDifference() {
		if (!isFormatSet()) {
			throw new InvalidURLNormException(
					"Tried to check the difference between the formats without defining both formats.");
		}
		return impressions[0] - impressions[1];
	}
	
	/**
	 * Checks the absolute difference between the page impressions of the
	 * specified format and those of the other format. If there are formats A
	 * and B, and format A is passed as argument, the result is A - B.
	 * 
	 * @param index the index of the format to check
	 * @return the difference of f relative to the other format
	 * @throws InvalidURLNormException
	 *             if at least one of the formats has not been set correctly or
	 *             the wrong URLFormat is passed as an argument
	 */
	public int
		getDifference(int index) {
		if (!isFormatSet()) {
			throw new InvalidURLNormException(
					"Tried to check the difference between the formats without defining both formats.");
		}
		if (index < 0 || index > 1) {
			throw new IndexOutOfBoundsException();
		}
		return impressions[index] - impressions[1 - index];
	}

	/**
	 * 
	 * @return the ratio between the difference (0 - 1) and the impressions (1)
	 */
	public double
		getDifferencePercent() {
		double output = 0.0;
		if (!isFormatSet()) {
			throw new InvalidURLNormException(
					"Tried to check the difference between the formats without defining both formats.");
		}
		if (impressions[1] != 0)
			output = (double) getDifference() / (double) impressions[1];
		else
			output = (double) impressions[0] / 1.0;
		return output;
	}
	
	/**
	 * 
	 * @param f the index of the format to check
	 * @return the ratio between the difference between f and the other format
	 *         and the impressions of the other format
	 */
	public double
		getDifferencePercent(int index) {
		double output = 0.0;
		if (!isFormatSet()) {
			throw new InvalidURLNormException(
					"Tried to check the difference between the formats without defining both formats.");
		}
		if (index < 0 || index > 1) {
			throw new IndexOutOfBoundsException();
		}
		if (impressions[1 - index] != 0) {
			output = (double) getDifference(index) / (double) impressions[1 - index];
		} else {
			output = (double) getDifference(index) / 1.0;
		}
		return output;
	}

	/**
	 * If the format is null, it sets it as indicated by the argument. Otherwise
	 * the format becomes read-only.
	 * 
	 * @param index
	 *            the index of the format array that needs to be set
	 * @param format
	 *            the new format
	 * @return true in case of success
	 */
	public boolean
		setFormat(int index, URLFormat format) {
		if (index < 0 || index > 1) {
			throw new IndexOutOfBoundsException(
					"Tried to set format lower than 0 or greater than 1!");
		}
		if (this.format[index] == null) {
			this.format[index] = format;
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Adds a URLElement instance to the relevant LinkedList and adds its page
	 * impressions count to the relevant array. Checks that both the URLElement
	 * and the current URLNorm are properly set. If the normalised url is still
	 * null, it sets it to the normalised url of the new element.
	 * 
	 * Precondition: both formats must be set
	 * 
	 * @param u the URLElement that needs to be added
	 * @param pos the position of the element (if it's format 1 or 2)
	 * @throws InvalidURLNormException
	 *             if at least one of the formats has not been set correctly or
	 *             if the URL has the wrong format
	 * @throws InvalidURLNormException
	 *             if the URL is not null, but it is different from the
	 *             normalised url from the new element.
	 * @throws RuntimeException
	 *             if the URLElement is in a format that is not included in the
	 *             URLNorm instance
	 * @return true if the operation was successful
	 */
	public boolean
		add(URLElement u, int pos) {
		boolean output = false;
		if (!isFormatSet()) {
			throw new InvalidURLNormException("Tried to add an element without defining both formats.");
		}
		if (pos < 0 || pos >= format.length)
			throw new RuntimeException(
					"Tried to add a URLElement to a URLNorm instance in an invalid position: " + pos + "!");
		if (u.getFormat() != format[pos])
			throw new RuntimeException(
					"Tried to add a URLElement in the wrong format to a URLNorm instance!");
		if (url == null) {
			url = u.normalise(noExtension);
		} else if (!u.normalise(noExtension).equals(url))
			throw new InvalidURLNormException("Wrong URL!");
		if (u.getFormat() == format[pos]) {
			if (elements[pos].add(u)) {
				impressions[pos] += u.getImpressions();
				output = true;
			}
		} else {
			throw new RuntimeException(
					"Tried to add a URLElement in the wrong format to a URLNorm instance!");
		}
		return output;
	}

	/**
	 * Checks if the URL is completely missing (zero page impressions) in at
	 * least one of the formats.
	 * 
	 * @param index
	 *            the index of the format that is been checked
	 * @return true if the format passed as an argument recorded zero
	 *         impressions, false otherwise
	 * @throws InvalidURLNormException
	 *             if at least one of the formats has not been set correctly or
	 *             the wrong URLFormat is passed as an argument
	 */
	public boolean
		isMissing(int index) {
		if (!isFormatSet()) {
			throw new InvalidURLNormException(
					"Tried to check if a format is missing without defining both formats.");
		}
		if (index < 0 || index >= format.length)
			throw new IndexOutOfBoundsException(
					"There are only 2 formats in a URLNorm object: the index can only be 0 or 1!");
		return (impressions[index] == 0);
	}

	/**
	 * 
	 * @param index
	 *            the index of the format for which the elements should be
	 *            exported
	 * @return a sorted (desc) array of URLElements for the specified index
	 */
	public URLElement[]
		getUrlElements(int index) {
		URLElement output[] = null;
		if (index < 0 || index >= format.length)
			throw new IndexOutOfBoundsException(
					"There are only 2 formats in a URLNorm object: the index can only be 0 or 1!");
		if (!isFormatSet()) {
			throw new InvalidURLNormException("Tried to extract the elements without defining both formats.");
		}
		output = elements[index]
				.toArray(new URLElement[elements[index].size()]);
		output = ArraySort.insertionSortDesc(output);
		return output;
	}

	/**
	 * Returns the hashCode of the normalised URL (using the standard
	 * String.hashCode method).
	 * 
	 * @return the hashCode
	 */
	@Override
	public int
		hashCode() {
		if (url == null) {
			throw new InvalidURLNormException(
					"Tried to get the hashCode of a URLNorm instance without a url value!");
		}
		return url.hashCode();
	}

	/**
	 * Outputs a string in a valid csv format (semicolon-separated)
	 * 
	 * @param index
	 *            the format the detail of which should be exported
	 * @return a string with all URLs for the specified index,
	 *         semicolon-separated and with each couple terminated by \n
	 */
	public String
		toCsv(int index) {
		if (index < 0 || index > 1) {
			throw new IndexOutOfBoundsException();
		}
		if (!isFormatSet()) {
			throw new InvalidURLNormException("At least one format is missing!");
		}
		StringBuilder outputB = new StringBuilder();
		for (Object element : this.elements[index].toArray()) {
			outputB.append(((URLElement) element).getUrl() + ";"
					+ ((URLElement) element).getImpressions() + "\n");
		}
		return outputB.toString();
	}

	/**
	 * Outputs a string in a valid csv format (separated by sep)
	 * 
	 * @param index
	 *            the format the detail of which should be exported
	 * @param sep
	 *            the separator for the columns in the csv
	 * @return a string with all URLs for the specified index, separated by sep
	 *         and with each couple terminated by \n
	 */
	public String
		toCsv(int index, char sep) {
		if (index < 0 || index > 1) {
			throw new IndexOutOfBoundsException();
		}
		if (!isFormatSet()) {
			throw new InvalidURLNormException("At least one format is missing!");
		}
		StringBuilder outputB = new StringBuilder();
		for (Object element : this.elements[index].toArray()) {
			outputB.append(((URLElement) element).getUrl() + sep
					+ ((URLElement) element).getImpressions() + "\n");
		}
		return outputB.toString();
	}

	/**
	 * Outputs a string in a valid csv format (semicolon-separated)
	 * 
	 * @param f
	 *            the format the detail of which should be exported
	 * @return a string with all URLs for the specified index,
	 *         semicolon-separated and with each couple terminated by \n
	 */
	public String
		toCsv(URLFormat f) {
		int index = 0;
		if (!isFormatSet()) {
			throw new InvalidURLNormException("At least one format is missing!");
		}
		if (format[0] == f) {
			index = 0;
		} else if (format[1] == f) {
			index = 1;
		} else {
			throw new InvalidURLNormException("Invalid format received!");
		}
		return toCsv(index);
	}

	/**
	 * Outputs a string in a valid csv format (separated by sep)
	 * 
	 * @param f
	 *            the format the detail of which should be exported
	 * @param sep
	 *            the separator for the columns in the csv
	 * @return a string with all URLs for the specified index, separated by sep
	 *         and with each couple terminated by \n
	 */
	public String
		toCsv(URLFormat f, char sep) {
		int index = 0;
		if (!isFormatSet()) {
			throw new InvalidURLNormException("At least one format is missing!");
		}
		if (format[0] == f) {
			index = 0;
		} else if (format[1] == f) {
			index = 1;
		} else {
			throw new InvalidURLNormException("Invalid format received!");
		}
		return toCsv(index, sep);
	}

	/**
	 * 
	 * @return a String describing the URLNorm element
	 */
	@Override
	public String
		toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("URLNORM: " + url + "\n");
		builder.append("format[0]: " + format[0]
				+ (isMissing(0) ? " missing " : " not missing ") + "\n");
		builder.append("format[1]: " + format[1]
				+ (isMissing(1) ? " missing " : " not missing ") + "\n");
		for (int i = 0; i < format.length; i++) {
			builder.append("FORMAT " + i + "\n");
			for (URLElement e : getUrlElements(i)) {
				builder.append("\t" + e);
			}
			builder.append("\n");
		}
		return builder.toString();
	}
}
