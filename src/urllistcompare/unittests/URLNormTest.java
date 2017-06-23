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
			fail("Did not throw an exception when one was needed!");
		} catch(InvalidURLNormException e){
			assertTrue("The URL is not null!", e.getMessage() == "url not defined!");
		}
		try{
			url001.getFormats();
			fail("Did not throw an exception when one was needed!");
		} catch(InvalidURLNormException e){
			assertTrue("The URL is not null!", e.getMessage() == "The first format is null!");
		}
		try{
			url001.getImpressions();
			fail("Did not throw an exception when one was needed!");
		} catch(InvalidURLNormException e){
			assertTrue("The URL is not null!", e.getMessage() == "The first format is null!");
		}
	}

	public void testURLNormURLFormatURLFormat() {
		URLNorm url001 = new URLNorm(URLFormat.WTKDEF, URLFormat.URLNORM);
		URLFormat formats[] = new URLFormat[2];
		int[] impressions;
		try{
			url001.getUrl();
			fail("Did not throw an exception when one was needed!");
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
		try{
			impressions = url001.getImpressions();
			assertTrue("The first format has the wrong impressions!", impressions[0] == 0);
			assertTrue("The second format has the wrong impressions!", impressions[1] == 0);
		} catch(InvalidURLNormException e){
			fail("At least one format is null!");
		}
	}

	public void testURLNormURLElementURLFormat() {
		URLElement element001 = new URLElement("www_domain_com.path1.path2.file_ext", URLFormat.WTKDEF, 1200);
		URLNorm url001 = new URLNorm(element001, URLFormat.URLNORM);
		URLFormat formats[] = new URLFormat[2];
		String theUrl;
		int[] impressions;
		try{
			theUrl = url001.getUrl();
			assertTrue("The URL is not correct!", theUrl.equals("/path1/path2/file.ext"));
		} catch(InvalidURLNormException e){
			fail("The URL is null!");
		}
		try{
			formats = url001.getFormats();
			assertTrue("The first format is wrong!", formats[0] == URLFormat.WTKDEF);
			assertTrue("The second format is wrong!", formats[1] == URLFormat.URLNORM);
		} catch(InvalidURLNormException e){
			fail("At least one format is null!");
		}
		try{
			impressions = url001.getImpressions();
			assertTrue("The first format has the wrong impressions!", impressions[0] == 1200);
			assertTrue("The second format has the wrong impressions!", impressions[1] == 0);
		} catch(InvalidURLNormException e){
			fail("At least one format is null!");
		}
	}

	public void testURLNormURLFormatURLFormatURLElement() {
		URLElement element001 = new URLElement("www_domain_com.path1.path2.file_ext", URLFormat.WTKDEF, 1200);
		URLNorm url001 = new URLNorm(URLFormat.WTKDEF, URLFormat.URLNORM, element001);
		URLFormat formats[] = new URLFormat[2];
		String theUrl;
		int[] impressions;
		try{
			theUrl = url001.getUrl();
			assertTrue("The URL is not correct!", theUrl.equals("/path1/path2/file.ext"));
		} catch(InvalidURLNormException e){
			fail("The URL is null!");
		}
		try{
			formats = url001.getFormats();
			assertTrue("The first format is wrong!", formats[0] == URLFormat.WTKDEF);
			assertTrue("The second format is wrong!", formats[1] == URLFormat.URLNORM);
		} catch(InvalidURLNormException e){
			fail("At least one format is null!");
		}
		try{
			impressions = url001.getImpressions();
			assertTrue("The first format has the wrong impressions!", impressions[0] == 1200);
			assertTrue("The second format has the wrong impressions!", impressions[1] == 0);
		} catch(InvalidURLNormException e){
			fail("At least one format is null!");
		}
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
