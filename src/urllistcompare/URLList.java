/**
 * 
 */
package urllistcompare;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import urllistcompare.exceptions.InvalidURLListException;
import urllistcompare.exceptions.InvalidURLNormException;

/**
 * @author Rocco Barbini (roccobarbi@gmail.com)
 *
 */
public class URLList implements Serializable{

	private HashMap<String, URLNorm> url;
	private URLFormat[] format;
	private boolean active;
	private boolean noExtension; // Read-only after the URLList has been constructed

	public URLList() {
		url = new HashMap<String, URLNorm>(1500, (float) 0.95);
		format = new URLFormat[2];
		active = false;
		noExtension = false; // Default behaviour to ensure consistency with legacy code
	}
	
	@Deprecated
	public URLList(URLFormat format01, URLFormat format02){
		this(format01, format02, false); // Default behaviour to ensure consistency with legacy code: noExtension is false
	}
	
	public URLList(URLFormat format01, URLFormat format02, boolean noExtension){
		this();
		format[0] = format01;
		format[1] = format02;
		active = true;
		this.noExtension = noExtension; 
	}
	
	/**
	 * 
	 * @param index the index from which the format should be read
	 * @return the format at the specified index
	 */
	public URLFormat getFormat(int index){
		if(index < 0 || index >= format.length) throw new IndexOutOfBoundsException();
		return format[index];
	}
	
	/**
	 * Sets the format at a specific index, if null. Otherwise it doesn't change it.
	 * @param index the index to set
	 * @param format the format to set
	 * @return true if the format was set, false otherwise
	 */
	public boolean setFormat(int index, URLFormat format){
		boolean output = false;
		if(index < 0 || index >= this.format.length) throw new IndexOutOfBoundsException();
		if(this.format[index] == null){
			this.format[index] = format;
			output = true;
		}
		// Activates the list if both formats are set
		// Implemented in a scalable way, in case more than 2 formats are ever managed at the same time
		active = true;
		for(URLFormat f : this.format){
			if(f == null) active = false;
		}
		return output;
	}
	
	/**
	 * 
	 * @return the keys to the HashMap that contains the URLNorm instances
	 */
	public Set<String> keySet(){
		return url.keySet();
	}
	
	/**
	 * 
	 * @param key the normalised url that needs to be found
	 * @return a deep copy of the URLNorm, or null if not present
	 */
	public URLNorm getUrlNorm(String key){
		URLNorm output = null;
		if(url.keySet().contains(key)){
			output = new URLNorm(url.get(key));
		}
		return output;
	}
	
	/**
	 * 
	 * @return true if the URLList is completely configured and can be used
	 */
	public boolean isActive(){
		return active;
	}
	
	/**
	 * Adds an element to the hashmap of URLNorm instances, creating the URLNorm instance if needed.
	 * @param element the URLElement to add
	 * @return true if successful, false otherwise (e.g. duplicated element
	 */
	public boolean add(URLElement element){
		boolean output = false;
		if(!isActive()) throw new InvalidURLListException("URLList not active!");
		if(element.getFormat() != format[0] && element.getFormat() != format[1])
			throw new RuntimeException("Tried to add a URLElement in the wrong format to a URLList instance!");
		if(url.containsKey(element.normalise())){
			output = url.get(element.normalise()).add(element);;
		} else {
			URLNorm n = new URLNorm(format[0], format[1], noExtension);
			output = n.add(element);;
			url.put(n.getUrl(), n);
		}
		return output;
	}

	public URLElement[] getMissingElements(int index){
		if(index < 0 || index >= format.length) throw new IndexOutOfBoundsException();
		if(!isActive()) throw new InvalidURLListException("URLList not active!");
		ArrayList<URLElement> output = new ArrayList<URLElement>(100);
		for(String k : url.keySet()){
			if(url.get(k).isMissing(index)){
				for(URLElement e : url.get(k).getUrlElements(Math.abs(index - 1))){
					output.add(e);
				}
			}
		}
		if(output.isEmpty())
			return new URLElement[0];
		else
			return output.toArray(new URLElement[1]);
	}
	
	public URLElement[] getMissingElements(URLFormat f){
		int index = 0;
		if(format[0] == null || format[1] == null) throw new InvalidURLListException("Tried to extract the missing elements" + 
				" without defining both formats.");
		if(format[0] == f){
			index = 0;
		} else if(format[1] == f){
			index = 1;
		} else {
			throw new InvalidURLListException("Tried to check if a format is missing" + 
					" with a format that is not included in this URLNorm instance.");
		}
		return getMissingElements(index);
	}
	
	/**
	 * 
	 * @return a reference to the current URLList if noExtension is false, otherwise a new URLList recalculated with the extension (soft normalisation)
	 */
	public URLList addExtension(){
		if(!noExtension){
			return this;
		} else {
			URLList output = new URLList(this.getFormat(0), this.getFormat(1), false);
			for(String key : this.keySet()){
				for(int i = 0; i < 2; i++){
					for(URLElement element : this.getUrlNorm(key).getUrlElements(i)){
						output.add(element);
					}
				}
			}
			return output;
		}
	}
	
	/**
	 * 
	 * @return a reference to the current URLList if noExtension is true, otherwise a new URLList recalculated without the extension (hard normalisation)
	 */
	public URLList remExtension(){
		if(noExtension){
			return this;
		} else {
			URLList output = new URLList(this.getFormat(0), this.getFormat(1), true);
			for(String key : this.keySet()){
				for(int i = 0; i < 2; i++){
					for(URLElement element : this.getUrlNorm(key).getUrlElements(i)){
						output.add(element);
					}
				}
			}
			return output;
		}
	}
}
