package urllistcompare.unittests;

import static org.junit.Assert.*;

import org.junit.Test;

import urllistcompare.CSVReader;
import urllistcompare.URLFormat;
import urllistcompare.URLList;

public class CSVReaderTest {

	@Test
	public void testRead() {
		URLList list = new URLList(URLFormat.URLNORM, URLFormat.GOOG, false);
		CSVReader reader = new CSVReader(true, 0, 1, ';', ',', true, '.', "test001.txt", URLFormat.URLNORM);
		reader.setDestination(list);
		try{
			reader.read();
		} catch (Exception e){
			fail("Exception where none should have been thrown: " + e.getMessage());
		}
		reader = new CSVReader(true, 0, 1, ';', ',', true, '.', "test002.txt", URLFormat.URLNORM);
		list = new URLList(URLFormat.URLNORM, URLFormat.GOOG, false);
		reader.setDestination(list);
		try{
			reader.read();
		} catch (Exception e){
			fail("Exception where none should have been thrown: " + e.getMessage());
		}
		reader = new CSVReader(true, 0, 1, ';', ',', true, '.', "test003.txt", URLFormat.URLNORM);
		list = new URLList(URLFormat.URLNORM, URLFormat.GOOG, false);
		reader.setDestination(list);
		try{
			reader.read();
			fail("No Exception with file that had the wrong number of columns!");
		} catch (Exception e){
			System.out.println(e.getMessage());
		}
		reader = new CSVReader(true, 0, 1, ';', ',', true, '.', "test004.txt", URLFormat.URLNORM);
		list = new URLList(URLFormat.URLNORM, URLFormat.GOOG, false);
		reader.setDestination(list);
		try{
			reader.read();
			fail("No Exception with file that had the wrong number of columns!");
		} catch (Exception e){
			System.out.println(e.getMessage());
		}
		reader = new CSVReader(true, 0, 1, ';', ',', true, '.', "test005.txt", URLFormat.URLNORM);
		list = new URLList(URLFormat.URLNORM, URLFormat.GOOG, false);
		reader.setDestination(list);
		try{
			reader.read();
		} catch (Exception e){
			fail("Exception where none should have been thrown: " + e.getMessage());
		}
		reader = new CSVReader(true, 0, 1, ';', ',', true, '.', "test006.txt", URLFormat.URLNORM);
		list = new URLList(URLFormat.URLNORM, URLFormat.GOOG, false);
		reader.setDestination(list);
		try{
			reader.read();
			fail("No Exception with file that had an unclosed double quoted string!");
		} catch (Exception e){
			System.out.println(e.getMessage());
		}
		reader = new CSVReader(true, 0, 1, ';', ',', true, '.', "test007.txt", URLFormat.URLNORM);
		list = new URLList(URLFormat.URLNORM, URLFormat.GOOG, false);
		reader.setDestination(list);
		try{
			reader.read();
			fail("No Exception with file that had an unescaped double quote inside a double quoted string!");
		} catch (Exception e){
			System.out.println(e.getMessage());
		}
	}

}
