/**
 * 
 */
package urllistcompare.unittests;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.Test;

import urllistcompare.ConfigParser;
import urllistcompare.exceptions.ConfigurationFormatException;
import urllistcompare.exceptions.WrongExtensionException;

/**
 * @author rocco
 *
 */
public class ConfigParserTest {

	@Test
	public void getConfigSampleContentsTest() {
		assertNotNull("The config sample content is returned null.", ConfigParser.getConfigSampleContents());
		assertTrue("The confing sample is of length zero.", ConfigParser.getConfigSampleContents().length() > 0);
	}

	@Test
	public void ConfigParserNoargTest() {
		ConfigParser parser = new ConfigParser();
		assertNull("The input is not null.", parser.getInput());
		assertNull("The readers array is not null.", parser.getReaders());
		assertNull("The output file name is not null.", parser.getOutputFileName());
		assertNull("The binary file name is not null.", parser.getBinaryFileName());
		assertFalse("The checkBasicDifferences flag is not false.", parser.isCheckBasicDifferences());
		assertFalse("The checkMissing flag is not false.",parser.isCheckMissing());
		assertFalse("The saveBinary flag is not false.",parser.isSaveBinary());
	}
	
	@Test
	public void ConfigParserArgTest() {
		File config;
		ConfigParser parser;
		try {
			config = new File("urllistcompare/src/unittests/fictionalTestConfig.txt");
			parser = new ConfigParser(config);
			fail("No exception thrown when FileNotFoundException is expected!");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (WrongExtensionException e) {
			fail("Wrong exception: WrongExtensionException when FileNotFoundException is expected!");
			e.printStackTrace();
		} catch (ConfigurationFormatException e) {
			fail("Wrong exception: ConfigurationFormatException when FileNotFoundException is expected!");
			e.printStackTrace();
		}
		try {
			config = new File("urllistcompare/src/unittests/testConfig.txt");
			parser = new ConfigParser(config);
			fail("No exception thrown when WrongExtensionException is expected!");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (WrongExtensionException e) {
			fail("Wrong exception: FileNotFoundException when WrongExtensionException is expected!");
			e.printStackTrace();
		} catch (ConfigurationFormatException e) {
			fail("Wrong exception: ConfigurationFormatException when WrongExtensionException is expected!");
			e.printStackTrace();
		}
		try {
			config = new File("urllistcompare/src/unittests/testConfig-empty.toml");
			parser = new ConfigParser(config);
			fail("No exception thrown when WrongExtensionException is expected!");
		} catch (FileNotFoundException e) {
			fail("Wrong exception: FileNotFoundException when ConfigurationFormatException is expected!");
			e.printStackTrace();
		} catch (WrongExtensionException e) {
			fail("Wrong exception: WrongExtensionException when ConfigurationFormatException is expected!");
			e.printStackTrace();
		} catch (ConfigurationFormatException e) {
			e.printStackTrace();
		}
	}

}