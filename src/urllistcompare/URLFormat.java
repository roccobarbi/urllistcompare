/**
 * 
 */
package urllistcompare;

/**
 * @author Rocco Barbini
 * @email roccobarbi@gmail.com
 * 
 * An enumeration of the URL formats accepted by the program, it can be easily expanded to accept more formats in the future.
 * Each format includes a sample value, which can be used to prompt the user with a series of examples and input the right format code.
 * Each format also includes a normalisePath() method that accepts a String parameter in the specified format and outputs a String with the
 * path, all in lowercase, without the protocol, domain, query and fragment. E.g. /path1/pathn/file.ext. 
 *
 */
public enum URLFormat {
	
	WTKDEF("www_domain_com.path.path.file_ext"){
		public String normalisePath(String url){
			url = url.replace('.', '/');
			url = url.replace('_', '.');
			if(url.contains("/")){
				url = url.substring(url.indexOf("/"));
			} else {
				url = "/"; // This is the root and it was saved without a trailing slash
			}
			return url;
		}
	},
	URLNORM("http://www.domain.com/path/path/file.ext"){
		public String normalisePath(String url){
			if(url.contains("://")){
				url = url.substring(url.indexOf("://") + 3);
			}
			if(url.contains("/")){
				url = url.substring(url.indexOf("/"));
			} else {
				url = "/"; // This is the root and it was saved without a trailing slash
			}
			return url;
		}
	},
	NOPROTNORM("www.domain.com/path/path/file.ext"){
		public String normalisePath(String url){
			if(url.contains("/")){
				url = url.substring(url.indexOf("/"));
			} else {
				url = "/"; // This is the root and it was saved without a trailing slash
			}
			return url;
		}
	},
	FULLURL("http://www.domain.com/PATH/path/file.ext?query#fragment"){
		public String normalisePath(String url){
			url = url.toLowerCase();
			url = url.split("#")[0];
			url = url.split("?")[0];
			if(url.contains("://")){
				url = url.substring(url.indexOf("://") + 3);
			}
			if(url.contains("/")){
				url = url.substring(url.indexOf("/"));
			} else {
				url = "/"; // This is the root and it was saved without a trailing slash
			}
			return url;
		}
	},
	GOOG("/path/path/file.ext?query#fragment"){
		public String normalisePath(String url){
			url = url.split("#")[0];
			url = url.split("?")[0];
			return url;
		}
	};
	
	private final String formatSample;
	
	private URLFormat(String format){
		formatSample = format;
	}
	
	public String getFormatSample(){
		return formatSample;
	}
	
	/**
	 * It returns the URL Path normalised in the /path1/pathn/file.ext format.
	 * @param url
	 * @return The normalised URL path.
	 */
	public String normalisePath(String url){
		// This method MUST be overridden by each individual value of the enumeration.
		return url;
	}
}
