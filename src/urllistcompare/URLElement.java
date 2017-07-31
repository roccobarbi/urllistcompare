/**
 * 
 */
package urllistcompare;

import java.io.Serializable;

import urllistcompare.exceptions.InvalidUrlException;

/**
 * @author Rocco Barbini (roccobarbi@gmail.com)
 * 
 * A single URL in a specific format: once created it can't be modified.
 * This is the basic building bloc of the whole program: each url is read into a URLElement, then added
 * to the URLMap based on its normalised path and format.
 * 
 * If the url field is null, it's a signal that the element is broken and MUST NOT be used.
 * 
 * If the URLElement is normalised without passing any arguments, soft normalisation is used by default. This behaviour is defined
 * to ensure compatibility with legacy code.
 * If the URLElement is normalised by passing a boolean value as an argument (true = noExtension, false = keep the extension), the
 * appropriate type of normalisation is guaranteed. This is the recommended usage for the normalise method.
 * The same goes for normalHashCode.
 * 
 * Note: this class has a natural ordering that is inconsistent with equals.
 *
 */
public class URLElement implements Comparable<Object>, Serializable{
	private final String url;
	private final URLFormat format;
	private final int impressions;
	
	public URLElement(){
		url = null;
		format = null;
		impressions = 0;
	}
	
	public URLElement(String url, URLFormat format, int impressions){
		this.url = url;
		this.format = format;
		this.impressions = impressions;
	}
	
	/**
	 * 
	 * @return the page impressions
	 * @throws InvalidUrlException
	 */
	public int getImpressions() {
		if(url == null) throw new InvalidUrlException("The url is null, it can't be normalised!");
		return impressions;
	}
	
	/**
	 * 
	 * @return the URL format
	 * @throws InvalidUrlException
	 */
	public URLFormat getFormat() {
		if(url == null) throw new InvalidUrlException("The url is null, it can't be normalised!");
		return format;
	}
	
	/**
	 * 
	 * @return the URL (as is, not normalised)
	 * @throws InvalidUrlException
	 */
	public String getUrl() {
		if(url == null) throw new InvalidUrlException("The url is null, it can't be normalised!");
		return url;
	}
	
	/**
	 * 
	 * @return the soft-normalised path for this URL (it keeps the file extension)
	 * @throws InvalidUrlException
	 */
	public String normalise() {
		if(url == null) throw new InvalidUrlException("The url is null, it can't be normalised!");
		return format.normalisePath(url);
	}
	
	/**
	 * 
	 * @return if the parameter is true, the hard-normalised path for this URL (it removes the file extension), otherwise the soft-normalised version (it keeps the file extension)
	 * @throws InvalidUrlException
	 */
	public String normalise(boolean noExtension) {
		if(url == null) throw new InvalidUrlException("The url is null, it can't be normalised!");
		return format.normalisePath(url, noExtension);
	}
	
	/**
	 * @return true if the contents are equal and not null, false otherwise
	 */
	public boolean equals(Object obj){
		boolean output = false;
		try{
			if(url == null) throw new InvalidUrlException("The url is null, it can't be normalised!");
			if(obj != null && obj instanceof URLElement){
				URLElement other = (URLElement) obj;
				if(other.getUrl() == url &&
						other.getImpressions() == impressions &&
						other.getFormat() == format){
					output = true;
				}
			}
		} catch (InvalidUrlException e) {
			output = false;
		}
		return output;
	}
	
	/**
	 * @return a string representation for the instance
	 */
	public String toString(){
		StringBuilder output = new StringBuilder();
		output.append("URL: ");
		output.append(getUrl());
		output.append(" - Format: ");
		output.append(getFormat());
		output.append(" - Impressions: ");
		output.append(getImpressions());
		return output.toString();
	}
	
	/**
	 * Note: this class has a natural ordering that is inconsistent with equals.
	 * The compareTo method only takes into account the page impressions, whereas equals takes into account all fields.
	 * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object.
	 * @throws ClassCastException	if the object being compared is not of the type URLElement
	 */
	public int compareTo(Object other){
		int output = 0;
		if(other instanceof URLElement){
			URLElement otherUrl = (URLElement) other;
			output = getImpressions() - otherUrl.getImpressions();
		}
		else throw new ClassCastException("Cannot compare URLElement with other classes.");
		return output;
	}
	
	/**
	 * @return the hashcode of the url field
	 * @throws InvalidUrlException	if the url field is empty
	 */
	public int hashCode(){
		if (url == null){
			throw new InvalidUrlException("Tried to get the hashcode of a URLElement with no url!");
		}
		return url.hashCode();
	}
	
	public int normalHashCode(){
		if (url == null){
			throw new InvalidUrlException("Tried to get the hashcode of a URLElement with no url!");
		}
		return format.normalisePath(url).hashCode();
	}
}
