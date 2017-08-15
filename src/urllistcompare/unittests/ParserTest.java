package urllistcompare.unittests;

import static org.junit.Assert.*;

import org.junit.Test;

import urllistcompare.util.Parser;

public class ParserTest {

	@Test
	public void testParseInt() {
		String value1 = "1,234,567", value2 = "1,234,567.1234", value3 = "1.4999", value4 = "1.5000";
		int val01 = 1234567, val02 = 1234567, val03 = 1, val04 = 2;
		String errorDigits1 = "a123098.34";
		String errorDigits2 = "12309c8.34";
		String errorDigits3 = "123098.3r4";
		try{
			assertTrue("Error parsing " + value1, Parser.parseInt(value1, ',', '.') == val01);
			assertTrue("Error parsing " + value2, Parser.parseInt(value2, ',', '.') == val02);
			assertTrue("Error parsing " + value3, Parser.parseInt(value3, ',', '.') == val03);
			assertTrue("Error parsing " + value4, Parser.parseInt(value4, ',', '.') == val04);
		} catch (Exception e) {
			fail("Unexpected exception " + e.getMessage());
		}
		try{
			assertFalse(value2 + " parsed with multiple decimal separators", Parser.parseInt(value2, '.', ',') == val02);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		try{
			assertFalse(value2 + " parsed with multiple decimal separators", Parser.parseInt(value2, '.', ',') == val02);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		try{
			Parser.parseInt(errorDigits1, ',', '.');
			fail(errorDigits1 + " parsed with multiple decimal separators");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		try{
			Parser.parseInt(errorDigits2, ',', '.');
			fail(errorDigits2 + " parsed with multiple decimal separators");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		try{
			Parser.parseInt(errorDigits3, ',', '.');
			fail(errorDigits3 + " parsed with multiple decimal separators");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}
