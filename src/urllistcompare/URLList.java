/**
 * 
 */
package urllistcompare;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import urllistcompare.exceptions.InvalidURLListException;

/**
 * @author Rocco Barbini (roccobarbi@gmail.com)
 *
 */
public class URLList implements Serializable{

	private HashMap<String, URLNorm> url;
	private URLFormat[] format;
	private boolean active;

	public URLList() {
		url = new HashMap<String, URLNorm>(1500, (float) 0.95);
		format = new URLFormat[2];
		active = false;
	}
	
	public URLList(URLFormat format01, URLFormat format02){
		this();
		format[0] = format01;
		format[1] = format02;
		active = true;
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
		if(url.containsKey(element.normalise())){
			output = url.get(element.normalise()).add(element);;
		} else {
			URLNorm n = new URLNorm(format[0], format[1]);
			output = n.add(element);;
			url.put(n.getUrl(), n);
		}
		return output;
	}

	public URLElement[] getMissingElements(int index){
		if(index < 0 || index >= this.format.length) throw new IndexOutOfBoundsException();
		if(!isActive()) throw new InvalidURLListException("URLList not active!");
		ArrayList<URLElement> output = new ArrayList<URLElement>(100);
		for(String k : url.keySet()){
			if(url.get(k).isMissing(index)){
				for(URLElement e : url.get(k).getUrlElements(index)){
					output.add(e);
				}
			}
		}
		return (URLElement[]) output.toArray();
	}
}
