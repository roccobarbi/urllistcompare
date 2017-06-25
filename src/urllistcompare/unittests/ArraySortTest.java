package urllistcompare.unittests;

import static org.junit.Assert.*;

import org.junit.Test;

import urllistcompare.util.ArraySort;

public class ArraySortTest {

	@Test
	public void testInsertionSortAsc() {
		Integer test[] = {0, 6, 3, 2, 10, 23, 12, 7};
		Integer test2[] = ArraySort.insertionSortAsc(test);
		Integer test3[] = {0, 2, 3, 6, 7, 10, 12, 23};
		for(int i = 0; i < test2.length; i++){
			assertTrue("Wrong order: " + i + " = " + test2[i] + " instead of " + test3[i], test2[i].equals(test3[i]));
		}
	}

	@Test
	public void testInsertionSortDesc() {
		Integer test[] = {0, 6, 3, 2, 10, 23, 12, 7};
		Integer test2[] = ArraySort.insertionSortDesc(test);
		Integer test3[] = {23, 12, 10, 7, 6, 3, 2, 0};
		for(int i = 0; i < test2.length; i++){
			assertTrue("Wrong order: " + i + " = " + test2[i] + " instead of " + test3[i], test2[i].equals(test3[i]));
		}
	}

}
