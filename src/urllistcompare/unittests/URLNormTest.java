package urllistcompare.unittests;

import junit.framework.TestCase;
import urllistcompare.*;
import urllistcompare.exceptions.InvalidURLNormException;

public class URLNormTest extends TestCase {

	public void testHashCode() {
		URLElement element001 = new URLElement("www_domain_com.path1.path2.file_ext", URLFormat.WTKDEF, 1200);
		URLNorm url001 = new URLNorm(URLFormat.WTKDEF, URLFormat.URLNORM, element001);
		assertTrue("Wrong hash code", url001.hashCode() == "/path1/path2/file.ext".hashCode());
	}

	public void testURLNorm() {
		URLNorm url001 = new URLNorm();
		try{
			url001.getUrl();
			fail("Did not throw an exception when one was needed!");
		} catch(InvalidURLNormException e){
			assertTrue("The URL is not null!", e.getMessage().equals("url not defined!"));
		}
		try{
			url001.getFormats();
			fail("Did not throw an exception when one was needed!");
		} catch(InvalidURLNormException e){
			assertTrue("The URL is not null!", e.getMessage().equals("The first format is null!"));
		}
		try{
			url001.getImpressions();
			fail("Did not throw an exception when one was needed!");
		} catch(InvalidURLNormException e){
			assertTrue("The URL is not null!", e.getMessage().equals("The first format is null!"));
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
			assertTrue("The URL is not null!", e.getMessage().equals("url not defined!"));
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
		URLElement element001 = new URLElement("www_domain_com.path1.path2.file_ext", URLFormat.WTKDEF, 1200);
		URLNorm url001 = new URLNorm(URLFormat.WTKDEF, URLFormat.URLNORM);
		String theUrl;
		try{
			url001.getUrl();
			fail("Did not throw an exception when one was needed!");
		} catch(InvalidURLNormException e){
			assertTrue("The URL is not null!", e.getMessage().equals("url not defined!"));
		}
		url001.add(element001);
		try{
			theUrl = url001.getUrl();
			assertTrue("The URL is not correct!", theUrl.equals("/path1/path2/file.ext"));
		} catch(InvalidURLNormException e){
			fail("The URL is null!");
		}
	}

	public void testSetFormat() {
		URLNorm url001 = new URLNorm();
		assertTrue("The format was not set", url001.setFormat(0, URLFormat.WTKDEF));
		assertTrue("The format was not set", url001.setFormat(1, URLFormat.GOOG));
		assertFalse("The format was set twice", url001.setFormat(0, URLFormat.NOPROTNORM));
		assertFalse("The format was set twice", url001.setFormat(0, URLFormat.URLNORM));
	}

	public void testAdd() {
		URLNorm url001 = new URLNorm();
		URLElement element001 = new URLElement("www_domain_com.path1.path2.file_ext", URLFormat.WTKDEF, 1200);
		URLElement element002 = new URLElement("http://www.domain.com/path1/path2/file2.ext", URLFormat.WTKDEF, 1200);
		URLElement element003 = new URLElement("http://www.domain.com/path1/path2/file.ext", URLFormat.FULLURL, 1200);
		try{
			url001.add(element001);
			fail("Did not throw an exception when one was needed!");
		} catch(InvalidURLNormException e){
			assertTrue("Wrong error message: " + e.getMessage(), e.getMessage().equals("Tried to add an element without defining both formats."));
		}
		url001 = new URLNorm(URLFormat.WTKDEF, URLFormat.URLNORM);
		url001.add(element001);
		assertTrue("The element was not added correctly!", url001.getImpressions()[0] == 1200);
		try{
			url001.add(element002);
			fail("Did not throw an exception when one was needed!");
		} catch(InvalidURLNormException e){
			assertTrue("Wrong error message: " + e.getMessage(), e.getMessage().equals("Wrong URL!"));
		}
		try{
			url001.add(element003);
			fail("Did not throw an exception when one was needed!");
		} catch(RuntimeException e){
			assertTrue("Wrong error message: " + e.getMessage(), e.getMessage().equals("Tried to add a URLElement in the wrong format to a URLNorm instance!"));
		}
	}

	public void testIsMissingURLFormat() {
		URLNorm url001 = new URLNorm();
		try{
			url001.isMissing(URLFormat.URLNORM);
			fail("Did not throw an exception when one was needed!");
		} catch(InvalidURLNormException e){
			assertTrue("Wrong error message: " + e.getMessage(), e.getMessage().equals("Tried to check if a format is missing without defining both formats."));
		}
		URLElement element001 = new URLElement("www_domain_com.path1.path2.file_ext", URLFormat.WTKDEF, 1200);
		url001 = new URLNorm(URLFormat.WTKDEF, URLFormat.URLNORM, element001);
		assertTrue("A format that should be missing is not so!", url001.isMissing(URLFormat.URLNORM));
		assertFalse("A format that should not be missing is reported as missing!", url001.isMissing(URLFormat.WTKDEF));
		try{
			url001.isMissing(URLFormat.FULLURL);
			fail("Did not throw an exception when one was needed!");
		} catch(InvalidURLNormException e){
			assertTrue("Wrong error message: " + e.getMessage(), e.getMessage().equals("Tried to check if a format is missing with a format that is not included in this URLNorm instance."));
		}
	}

	public void testIsMissingInt() {
		URLNorm url001 = new URLNorm();
		try{
			url001.isMissing(URLFormat.URLNORM);
			fail("Did not throw an exception when one was needed!");
		} catch(InvalidURLNormException e){
			assertTrue("Wrong error message: " + e.getMessage(), e.getMessage().equals("Tried to check if a format is missing without defining both formats."));
		}
		URLElement element001 = new URLElement("www_domain_com.path1.path2.file_ext", URLFormat.WTKDEF, 1200);
		url001 = new URLNorm(URLFormat.WTKDEF, URLFormat.URLNORM, element001);
		assertTrue("A format that should be missing is not so!", url001.isMissing(1));
		assertFalse("A format that should not be missing is reported as missing!", url001.isMissing(0));
		try{
			url001.isMissing(-1);
			fail("Did not throw an exception when one was needed!");
		} catch(IndexOutOfBoundsException e){
			assertTrue("Wrong error message: " + e.getMessage(), e.getMessage().equals("There are only 2 formats in a URLNorm object: the index can only be 0 or 1!"));
		}
	}

	public void testGetDifference() {
		URLNorm url001 = new URLNorm();
		try{
			url001.getDifference();
			fail("Did not throw an exception when one was needed!");
		} catch(InvalidURLNormException e){
			assertTrue("Wrong error message: " + e.getMessage(), e.getMessage().equals("Tried to check the difference between the formats without defining both formats."));
		}
		URLElement element001 = new URLElement("www_domain_com.path1.path2.file_ext", URLFormat.WTKDEF, 1200);
		url001 = new URLNorm(URLFormat.WTKDEF, URLFormat.URLNORM, element001);
		assertTrue("Wrong difference: " + url001.getDifference(), url001.getDifference() == 1200);
		URLElement element002 = new URLElement("www_domain_com.path1.path2.file_ext", URLFormat.WTKDEF, 100);
		url001.add(element002);
		element002 = new URLElement("http://www.domain.com/path1/path2/file.ext", URLFormat.URLNORM, 1300);
		url001.add(element002);
		assertTrue("Wrong difference: " + url001.getDifference(), url001.getDifference() == 0);
		element002 = new URLElement("http://www.domain.com/path1/path2/file.ext", URLFormat.URLNORM, 300);
		url001.add(element002);
		assertTrue("Wrong difference: " + url001.getDifference(), url001.getDifference() == -300);
	}

	public void testGetDifferenceURLFormat() {
		URLNorm url001 = new URLNorm();
		try{
			url001.getDifference(URLFormat.URLNORM);
			fail("Did not throw an exception when one was needed!");
		} catch(InvalidURLNormException e){
			assertTrue("Wrong error message: " + e.getMessage(), e.getMessage().equals("Tried to check the difference between the formats without defining both formats."));
		}
		URLElement element001 = new URLElement("www_domain_com.path1.path2.file_ext", URLFormat.WTKDEF, 1200);
		url001 = new URLNorm(URLFormat.WTKDEF, URLFormat.URLNORM, element001);
		assertTrue("Wrong difference: " + url001.getDifference(URLFormat.URLNORM), url001.getDifference(URLFormat.URLNORM) == -1200);
		URLElement element002 = new URLElement("www_domain_com.path1.path2.file_ext", URLFormat.WTKDEF, 100);
		url001.add(element002);
		element002 = new URLElement("http://www.domain.com/path1/path2/file.ext", URLFormat.URLNORM, 1300);
		url001.add(element002);
		assertTrue("Wrong difference: " + url001.getDifference(URLFormat.URLNORM), url001.getDifference(URLFormat.URLNORM) == 0);
		element002 = new URLElement("http://www.domain.com/path1/path2/file.ext", URLFormat.URLNORM, 300);
		url001.add(element002);
		assertTrue("Wrong difference: " + url001.getDifference(URLFormat.URLNORM), url001.getDifference(URLFormat.URLNORM) == 300);
	}

	public void testGetElements() {
		fail("Not yet implemented");
	}

	public void testGetUrlElementsInt() {
		URLNorm url001 = new URLNorm();
		try{
			url001.getUrlElements(0);
			fail("Did not throw an exception when one was needed!");
		} catch(InvalidURLNormException e){
			assertTrue("Wrong error message: " + e.getMessage(), e.getMessage().equals("Tried to extract the elements without defining both formats."));
		}
		URLElement element001 = new URLElement("www_domain_com.path1.path2.file_ext", URLFormat.WTKDEF, 1200);
		url001 = new URLNorm(URLFormat.WTKDEF, URLFormat.URLNORM, element001);
		try{
			url001.getUrlElements(-1);
			fail("Did not throw an exception when one was needed!");
		} catch(IndexOutOfBoundsException e){
			assertTrue("Wrong error message: " + e.getMessage(), e.getMessage().equals("There are only 2 formats in a URLNorm object: the index can only be 0 or 1!"));
		}
		try{
			url001.getUrlElements(2);
			fail("Did not throw an exception when one was needed!");
		} catch(IndexOutOfBoundsException e){
			assertTrue("Wrong error message: " + e.getMessage(), e.getMessage().equals("There are only 2 formats in a URLNorm object: the index can only be 0 or 1!"));
		}
		URLElement[] output = url001.getUrlElements(0);
		assertTrue("Wrong length of the output: " + output.length, output.length == 1);
		URLElement element002 = new URLElement("www_domain_com.path1.path2.file_ext", URLFormat.WTKDEF, 100);
		url001.add(element002);
		output = url001.getUrlElements(0);
		assertTrue("Wrong length of the output: " + output.length, output.length == 2);
		assertTrue("The output does not include the right elements, rightly ordered.", output[0].equals(element001));
		assertTrue("The output does not include the right elements, rightly ordered.", output[1].equals(element002));
		URLElement element003 = new URLElement("www_domain_com.path1.path2.file_ext", URLFormat.WTKDEF, 3000);
		url001.add(element002);
		output = url001.getUrlElements(0);
		assertTrue("The output does not include the right elements, rightly ordered. 0 = " + output[0], output[0].equals(element003));
		assertTrue("The output does not include the right elements, rightly ordered. 1 = " + output[1], output[1].equals(element001));
		assertTrue("The output does not include the right elements, rightly ordered. 2 = " + output[2], output[2].equals(element002));
	}

	public void testGetUrlElementsURLFormat() {
		fail("Not yet implemented");
	}

}
