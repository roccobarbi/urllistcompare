/**
 * 
 */
package urllistcompare;

import urllistcompare.exceptions.InvalidUrlException;

/**
 * @author Rocco Barbini
 * @email roccobarbi@gmail.com
 * 
 * A single URL in a specific format: once created it can't be modified.
 * This is the basic building bloc of the whole program: each url is read into a URLElement, then added
 * to the URLMap based on its normalised path and format.
 * 
 * If the url field is null, it's a signal that the element is broken and MUST NOT be used.
 *
 */
public class URLElement{
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
	public int getImpressions() throws InvalidUrlException {
		if(url == null) throw new InvalidUrlException("The url is null, it can't be normalised!");
		return impressions;
	}
	
	/**
	 * 
	 * @return the URL format
	 * @throws InvalidUrlException
	 */
	public URLFormat getFormat() throws InvalidUrlException {
		if(url == null) throw new InvalidUrlException("The url is null, it can't be normalised!");
		return format;
	}
	
	/**
	 * 
	 * @return the URL (as is, not normalised)
	 * @throws InvalidUrlException
	 */
	public String getUrl() throws InvalidUrlException {
		if(url == null) throw new InvalidUrlException("The url is null, it can't be normalised!");
		return url;
	}
	
	/**
	 * 
	 * @return the normalised path for this URL
	 */
	public String normalise() throws InvalidUrlException {
		if(url == null) throw new InvalidUrlException("The url is null, it can't be normalised!");
		return format.normalisePath(url);
	}
	
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
	
	public String toString(){
		StringBuilder output = new StringBuilder();
		try{
			output.append("URL: ");
			output.append(getUrl());
			output.append(" - Format: ");
			output.append(getFormat());
			output.append(" - Impressions: ");
			output.append(getImpressions());
		} catch (InvalidUrlException e) {
			output = new StringBuilder("Invalid URLElement: ");
			output.append(e.getMessage());
		}
		return output.toString();
	}
}
