package urllistcompare.unittests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({
	ArraySortTest.class,
	URLFormatTest.class,
	URLElementTest.class,
	URLNormTest.class,
	URLListTest.class,
	ParserTest.class,
	ConfigParserTest.class
})

public class MainTestSuite {

}
