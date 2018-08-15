package urllistcompare.unittests;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import urllistcompare.CSVReader;
import urllistcompare.URLFormat;
import urllistcompare.URLList;

public class CSVReaderTest {

	@Test
	public void testRead() {
		URLList list = new URLList(URLFormat.URLNORM, URLFormat.GOOG, false);
		CSVReader reader = new CSVReader
				.CSVReaderBuilder(new File("test001.txt"))
				.headers(true)
				.urlI(0)
				.impI(1)
				.vSep(';')
				.dSep('.')
				.isTSep(true)
				.tSep(',')
				.format(URLFormat.URLNORM)
				.build();
		reader.setDestination(list);
		try{
			reader.read();
		} catch (Exception e){
			fail("Exception where none should have been thrown: " + e.getMessage());
		}
		reader = new CSVReader
				.CSVReaderBuilder(new File("test002.txt"))
				.headers(true)
				.urlI(0)
				.impI(1)
				.vSep(';')
				.dSep(',')
				.isTSep(true)
				.tSep('.')
				.format(URLFormat.URLNORM)
				.build();
		list = new URLList(URLFormat.URLNORM, URLFormat.GOOG, false);
		reader.setDestination(list);
		try{
			reader.read();
		} catch (Exception e){
			fail("Exception where none should have been thrown: " + e.getMessage());
		}
		reader = new CSVReader
				.CSVReaderBuilder(new File("test003.txt"))
				.headers(true)
				.urlI(0)
				.impI(1)
				.vSep(';')
				.dSep(',')
				.isTSep(true)
				.tSep('.')
				.format(URLFormat.URLNORM)
				.build();
		list = new URLList(URLFormat.URLNORM, URLFormat.GOOG, false);
		reader.setDestination(list);
		try{
			reader.read();
			fail("No Exception with file that had the wrong number of columns!");
		} catch (Exception e){
			System.out.println(e.getMessage());
		}
		reader = new CSVReader
				.CSVReaderBuilder(new File("test004.txt"))
				.headers(true)
				.urlI(0)
				.impI(1)
				.vSep(';')
				.dSep(',')
				.isTSep(true)
				.tSep('.')
				.format(URLFormat.URLNORM)
				.build();
		list = new URLList(URLFormat.URLNORM, URLFormat.GOOG, false);
		reader.setDestination(list);
		try{
			reader.read();
			fail("No Exception with file that had the wrong number of columns!");
		} catch (Exception e){
			System.out.println(e.getMessage());
		}
		reader = new CSVReader
				.CSVReaderBuilder(new File("test005.txt"))
				.headers(true)
				.urlI(0)
				.impI(1)
				.vSep(';')
				.dSep(',')
				.isTSep(true)
				.tSep('.')
				.format(URLFormat.URLNORM)
				.build();
		list = new URLList(URLFormat.URLNORM, URLFormat.GOOG, false);
		reader.setDestination(list);
		try{
			reader.read();
		} catch (Exception e){
			fail("Exception where none should have been thrown: " + e.getMessage());
		}
		reader = new CSVReader
				.CSVReaderBuilder(new File("test006.txt"))
				.headers(true)
				.urlI(0)
				.impI(1)
				.vSep(';')
				.dSep(',')
				.isTSep(true)
				.tSep('.')
				.format(URLFormat.URLNORM)
				.build();
		list = new URLList(URLFormat.URLNORM, URLFormat.GOOG, false);
		reader.setDestination(list);
		try{
			reader.read();
			fail("No Exception with file that had an unclosed double quoted string!");
		} catch (Exception e){
			System.out.println(e.getMessage());
		}
		reader = new CSVReader
				.CSVReaderBuilder(new File("test007.txt"))
				.headers(true)
				.urlI(0)
				.impI(1)
				.vSep(';')
				.dSep(',')
				.isTSep(true)
				.tSep('.')
				.format(URLFormat.URLNORM)
				.build();
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
