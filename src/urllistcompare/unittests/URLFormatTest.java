package urllistcompare.unittests;

import static org.junit.Assert.*;

import org.junit.Test;

import urllistcompare.URLFormat;

public class URLFormatTest {

	@Test
	public void testInputFormat() {
		URLFormat format001 = URLFormat.inputFormat();
		boolean checkValue = false;
		for(URLFormat format : URLFormat.values()){
			if(format.equals(format001)) checkValue = true;
		}
		System.out.println("You chose: " + format001);
		assertTrue("The format " + format001 + " could not be found in the enumeration.", checkValue);
	}

	@Test
	public void testNormalisePath() {
		// Soft normalisation tests
		String url000 = "www_domain_com.path1.path2.file_ext";
		URLFormat format000 = URLFormat.WTKDEF;
		url000 = format000.normalisePath(url000);
		assertTrue("The path normalised by WTKDEF is incorrect: " + url000, url000.equals("/path1/path2/file.ext"));
		String url001 = "http://www.domain.com/path1/path2/file.ext";
		URLFormat format001 = URLFormat.URLNORM;
		url001 = format001.normalisePath(url001);
		assertTrue("The path normalised by URLNORM is incorrect: " + url001, url001.equals("/path1/path2/file.ext"));
		String url002 = "www.domain.com/path1/path2/file.ext";
		URLFormat format002 = URLFormat.NOPROTNORM;
		url002 = format002.normalisePath(url002);
		assertTrue("The path normalised by NOPROTNORM is incorrect: " + url002, url002.equals("/path1/path2/file.ext"));
		String url003 = "http://www.doMAin.com/PATH1/path2/file.ext?param1=value1&param2=value2#fragment";
		URLFormat format003 = URLFormat.FULLURL;
		url003 = format003.normalisePath(url003);
		assertTrue("The path normalised by FULLURL is incorrect: " + url003, url003.equals("/path1/path2/file.ext"));
		String url004 = "/PATH1/path2/file.ext?param1=value1&param2=value2#fragment";
		URLFormat format004 = URLFormat.GOOG;
		url004 = format004.normalisePath(url004);
		assertTrue("The path normalised by WTKDEF is incorrect: " + url004, url004.equals("/path1/path2/file.ext"));
		// Hard normalisation tests
		url000 = "www_domain_com.path1.path2.file_ext";
		format000 = URLFormat.WTKDEF;
		url000 = format000.normalisePath(url000, true);
		assertTrue("The path normalised by WTKDEF is incorrect: " + url000, url000.equals("/path1/path2/file"));
		url001 = "http://www.domain.com/path1/path2/file.ext";
		format001 = URLFormat.URLNORM;
		url001 = format001.normalisePath(url001, true);
		assertTrue("The path normalised by URLNORM is incorrect: " + url001, url001.equals("/path1/path2/file"));
		url002 = "www.domain.com/path1/path2/file.ext";
		format002 = URLFormat.NOPROTNORM;
		url002 = format002.normalisePath(url002, true);
		assertTrue("The path normalised by NOPROTNORM is incorrect: " + url002, url002.equals("/path1/path2/file"));
		url003 = "http://www.doMAin.com/PATH1/path2/file.ext?param1=value1&param2=value2#fragment";
		format003 = URLFormat.FULLURL;
		url003 = format003.normalisePath(url003, true);
		assertTrue("The path normalised by FULLURL is incorrect: " + url003, url003.equals("/path1/path2/file"));
		url004 = "/PATH1/path2/file.ext?param1=value1&param2=value2#fragment";
		format004 = URLFormat.GOOG;
		url004 = format004.normalisePath(url004, true);
		assertTrue("The path normalised by WTKDEF is incorrect: " + url004, url004.equals("/path1/path2/file"));
	}

}
