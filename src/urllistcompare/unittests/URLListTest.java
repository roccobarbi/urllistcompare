package urllistcompare.unittests;

import static org.junit.Assert.*;

import org.junit.Test;
import urllistcompare.*;
import urllistcompare.exceptions.InvalidURLListException;

public class URLListTest {

	@Test
	public void testURLList() {
		URLList list001 = new URLList();
		assertTrue("Wrong class!", list001 instanceof URLList);
		assertFalse("Active when it should not be!", list001.isActive());
	}

	@Test
	public void testURLListURLFormatURLFormat() {
		URLList list001 = new URLList(URLFormat.WTKDEF, URLFormat.URLNORM);
		assertTrue("Wrong class!", list001 instanceof URLList);
		assertTrue("Not active when it should be!", list001.isActive());
	}

	@Test
	public void testGetFormat() {
		URLList list001 = new URLList(URLFormat.WTKDEF, URLFormat.URLNORM);
		assertTrue("Wrong first format", list001.getFormat(0) == URLFormat.WTKDEF);
		assertTrue("Wrong second format", list001.getFormat(1) == URLFormat.URLNORM);
		try{
			list001.getFormat(-1);
			fail("Did not raise an exception with the wrong index!");
		} catch (IndexOutOfBoundsException e) {
			// Passed
		}
		try{
			list001.getFormat(2);
			fail("Did not raise an exception with the wrong index!");
		} catch (IndexOutOfBoundsException e) {
			// Passed
		}
	}

	@Test
	public void testSetFormat() {
		URLList list001 = new URLList();
		assertTrue("Did not return true after setting the format!", list001.setFormat(0, URLFormat.WTKDEF));
		assertTrue("Did not return true after setting the format!", list001.setFormat(1, URLFormat.URLNORM));
		assertTrue("Wrong first format", list001.getFormat(0) == URLFormat.WTKDEF);
		assertTrue("Wrong second format", list001.getFormat(1) == URLFormat.URLNORM);
		assertTrue("Not active when it should be!", list001.isActive());
		try{
			list001.setFormat(-1, URLFormat.FULLURL);
			fail("Did not raise an exception with the wrong index!");
		} catch (IndexOutOfBoundsException e) {
			// Passed
		}
		try{
			list001.setFormat(2, URLFormat.FULLURL);
			fail("Did not raise an exception with the wrong index!");
		} catch (IndexOutOfBoundsException e) {
			// Passed
		}
		assertFalse("Did not return false after not setting the format!", list001.setFormat(1, URLFormat.FULLURL));
		assertTrue("Wrong first format, it should not have changed!", list001.getFormat(0) == URLFormat.WTKDEF);
	}

	@Test
	public void testIsActive() {
		URLList list001 = new URLList();
		assertFalse("Active when it should not be!", list001.isActive());
		list001.setFormat(0, URLFormat.WTKDEF);
		list001.setFormat(1, URLFormat.URLNORM);
		assertTrue("Not active when it should be!", list001.isActive());
		list001 = new URLList(URLFormat.WTKDEF, URLFormat.URLNORM);
		assertTrue("Not active when it should be!", list001.isActive());
	}

	@Test
	public void testAdd() {
		URLList list001 = new URLList();
		URLElement element001 = new URLElement("www_domain_com.path1.path2.file_ext", URLFormat.WTKDEF, 1200);
		URLElement element002 = new URLElement("www.domain.com/path1/path2/file2.ext", URLFormat.URLNORM, 1200);
		URLElement element003 = new URLElement("http://www.domain.com/path1/path2/file.ext", URLFormat.FULLURL, 1200);
		try{
			list001.add(element001);
			fail("Did not raise an exception while the list was not active!");
		} catch (InvalidURLListException e) {
			// Passed
		}
		list001.setFormat(0, URLFormat.WTKDEF);
		list001.setFormat(1, URLFormat.URLNORM);
		assertTrue("Element not added when it should have been!", list001.add(element001));
		assertTrue("Element not added when it should have been!", list001.add(element002));
		try{
			assertTrue("Element not added when it should have been!", list001.add(element003));
			fail("Did not raise an exception when the format was wrong!");
		} catch (RuntimeException e) {
			assertTrue("Wrong error message!", e.getMessage().equals("Tried to add a URLElement in the wrong format to a URLList instance!"));
		}
	}

	@Test
	public void testGetMissingElementsInt() {
		URLList list001 = new URLList();
		URLElement output001[];
		URLElement output002[];
		list001.setFormat(0, URLFormat.WTKDEF);
		list001.setFormat(1, URLFormat.URLNORM);
		URLElement element001 = new URLElement("www_domain_com.path1.path2.file_ext", URLFormat.WTKDEF, 1200);
		URLElement element002 = new URLElement("www.domain.com/path1/path2/file2.ext", URLFormat.URLNORM, 1200);
		URLElement element003 = new URLElement("www.domain.com/path1/path2/file.ext", URLFormat.URLNORM, 1200);
		URLElement element004 = new URLElement("www.domain.com/path1/path2/file2.ext", URLFormat.URLNORM, 200);
		list001.add(element001);
		list001.add(element002);
		list001.add(element003);
		list001.add(element004);
		URLNorm norm001;
		for(String e : list001.keySet()){
			System.out.println("KEY: " + e);
			norm001 = list001.getUrlNorm(e);
			System.out.println(norm001);
		}
		try{
			list001.getMissingElements(-1);
			fail("Did not throw an exception with wrong index: -1.");
		} catch(IndexOutOfBoundsException e) {
			// passed
		}
		try{
			list001.getMissingElements(2);
			fail("Did not throw an exception with wrong index: 2.");
		} catch(IndexOutOfBoundsException e) {
			// passed
		}
		output001 = list001.getMissingElements(0);
		output002 = list001.getMissingElements(1);
		for(URLElement e : output001){
			System.out.println(e);
		}
		assertTrue("Wrong length for output001: " + output001.length, output001.length == 2);
		assertTrue("Wrong first element in output001", output001[0].equals(element002));
		assertTrue("Wrong second element in output001", output001[1].equals(element004));
		assertTrue("Wrong length for output002: " + output002.length, output002.length == 0);
	}

	@Test
	public void testGetMissingElementsURLFormat() {
		fail("Not yet implemented");
	}

}