package urllistcompare.unittests;

import static org.junit.Assert.*;

import org.junit.Test;

import urllistcompare.URLElement;
import urllistcompare.URLFormat;
import urllistcompare.exceptions.InvalidUrlException;

public class URLElementTest {

	@Test
	public void testGetImpressions() {
		URLElement element001 = new URLElement("www_domain_com.path1.path2.file_ext", URLFormat.WTKDEF, 1200);
		try{
			assertTrue("Wrong number of impressions",element001.getImpressions() == 1200);
		} catch(InvalidUrlException e) {
			System.out.println(e);
			fail("InvalidUrlException thrown when it should not have been.");
		}
		URLElement element002 = new URLElement();
		try{
			element002.getImpressions();
			fail("InvalidUrlException not thrown when it should have been.");
		} catch(InvalidUrlException e) {
			System.out.println(e);
		}
	}

	@Test
	public void testGetFormat() {
		URLElement element001 = new URLElement("www_domain_com.path1.path2.file_ext", URLFormat.WTKDEF, 1200);
		try{
			assertTrue("Wrong format",element001.getFormat() == URLFormat.WTKDEF);
		} catch(InvalidUrlException e) {
			System.out.println(e);
			fail("InvalidUrlException thrown when it should not have been.");
		}
		URLElement element002 = new URLElement();
		try{
			element002.getFormat();
			fail("InvalidUrlException not thrown when it should have been.");
		} catch(InvalidUrlException e) {
			System.out.println(e);
		}
	}

	@Test
	public void testGetUrl() {
		URLElement element001 = new URLElement("www_domain_com.path1.path2.file_ext", URLFormat.WTKDEF, 1200);
		try{
			assertTrue("Wrong url",element001.getUrl() == "www_domain_com.path1.path2.file_ext");
		} catch(InvalidUrlException e) {
			System.out.println(e);
			fail("InvalidUrlException thrown when it should not have been.");
		}
		URLElement element002 = new URLElement();
		try{
			element002.getUrl();
			fail("InvalidUrlException not thrown when it should have been.");
		} catch(InvalidUrlException e) {
			System.out.println(e);
		}
	}

	@Test
	public void testNormalise() {
		URLElement element001 = new URLElement("www_domain_com.path1.path2.file_ext", URLFormat.WTKDEF, 1200);
		URLElement element002 = new URLElement("www.domain.com/path1/path2/file.ext", URLFormat.NOPROTNORM, 1200);
		try{
			assertTrue("Same path normalised as if it was different",element001.normalise().equals(element002.normalise()));
		}  catch(InvalidUrlException e) {
			System.out.println(e);
			fail("InvalidUrlException thrown when it should not have been.");
		}
	}

	@Test
	public void testEqualsObject() {
		URLElement element001 = new URLElement("www_domain_com.path1.path2.file_ext", URLFormat.WTKDEF, 1200);
		URLElement element002 = new URLElement("www_domain_com.path1.path2.file_ext", URLFormat.WTKDEF, 1200);
		assertTrue("Equal URLElements considered not equal",element001.equals(element002));
		URLElement element003 = new URLElement("www_domain_com.path1.path2.file_ext", URLFormat.WTKDEF, 1201);
		assertFalse("Unequal URLElements considered equal",element001.equals(element003));
		URLElement element004 = new URLElement("www.domain.com/path1/path2/file.ext", URLFormat.NOPROTNORM, 1200);
		assertFalse("Unequal URLElements considered equal",element001.equals(element004));
	}

	@Test
	public void testToString() {
		URLElement element001 = new URLElement("www_domain_com.path1.path2.file_ext", URLFormat.WTKDEF, 1200);
		assertTrue("Same path normalised as if it was different",element001.toString().equals("URL: www_domain_com.path1.path2.file_ext - Format: WTKDEF - Impressions: 1200"));
	}

}