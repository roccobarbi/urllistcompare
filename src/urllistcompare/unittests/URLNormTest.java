package urllistcompare.unittests;

import junit.framework.TestCase;
import urllistcompare.*;
import urllistcompare.exceptions.InvalidURLNormException;

public class URLNormTest extends TestCase {

	public void testHashCode() {
		fail("Not yet implemented");
	}

	public void testURLNorm() {
		URLNorm url001 = new URLNorm();
		try{
			url001.getUrl();
		} catch(InvalidURLNormException e){
			assertTrue("The URL is not null!", e.getMessage() == "url not defined!");
		}
		try{
			url001.getFormats();
		} catch(InvalidURLNormException e){
			assertTrue("The URL is not null!", e.getMessage() == "The first format is null!");
		}
	}

	public void testURLNormURLFormatURLFormat() {
		URLNorm url001 = new URLNorm(URLFormat.WTKDEF, URLFormat.URLNORM);
		URLFormat formats[] = new URLFormat[2];
		try{
			url001.getUrl();
		} catch(InvalidURLNormException e){
			assertTrue("The URL is not null!", e.getMessage() == "url not defined!");
		}
		try{
			formats = url001.getFormats();
			assertTrue("The first format is wrong!", formats[0] == URLFormat.WTKDEF);
			assertTrue("The second format is wrong!", formats[1] == URLFormat.URLNORM);
		} catch(InvalidURLNormException e){
			fail("At least one format is null!");
		}
	}

	public void testURLNormURLElementURLFormat() {
		fail("Not yet implemented");
	}

	public void testURLNormURLFormatURLFormatURLElement() {
		fail("Not yet implemented");
	}

	public void testGetUrl() {
		fail("Not yet implemented");
	}

	public void testSetFormat() {
		fail("Not yet implemented");
	}

	public void testAdd() {
		fail("Not yet implemented");
	}

	public void testIsMissingURLFormat() {
		fail("Not yet implemented");
	}

	public void testIsMissingInt() {
		fail("Not yet implemented");
	}

	public void testGetDifference() {
		fail("Not yet implemented");
	}

	public void testGetDifferenceURLFormat() {
		fail("Not yet implemented");
	}

	public void testGetElements() {
		fail("Not yet implemented");
	}

	public void testGetUrlElementsInt() {
		fail("Not yet implemented");
	}

	public void testGetUrlElementsURLFormat() {
		fail("Not yet implemented");
	}

}
