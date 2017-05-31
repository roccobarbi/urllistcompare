package urllistcompare.unittests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import urllistcompare.*;
import urllistcompare.util.*;
import org.junit.Test;

public class LinkedListTest {

	@Test
	public void testLinkedList() {
		LinkedList <String> list001 = new LinkedList();
		assertTrue("Length is not 0", list001.getLength() == 0);
	}

	@Test
	public void testLinkedListT() {
		String testString = "testString";
		LinkedList <String> list001 = new LinkedList(testString);
		assertTrue("Length is not 1", list001.getLength() == 1);
		LinkedList <String> list002 = new LinkedList("testString");
		assertTrue("Length is not 1", list002.getLength() == 1);
	}

	@Test
	public void testAdd() {
		String testString = "testString";
		LinkedList <String> list001 = new LinkedList(testString);
		list001.add("test002");
		assertTrue("Length is not 2", list001.getLength() == 2);
		String secondTest = "secondTest";
		list001.add(secondTest);
		assertTrue("Length is not 3", list001.getLength() == 3);
	}

	@Test
	public void testIsInList() {
		String testString = "testString";
		LinkedList <String> list001 = new LinkedList(testString);
		list001.add("test002");
		String secondTest = "secondTest";
		list001.add(secondTest);
		assertTrue("payload not found in list", list001.isInList(testString));
		assertTrue("payload not found in list", list001.isInList(secondTest));
		assertTrue("payload not found in list", list001.isInList("test002"));
	}

	@Test
	public void testToArrayList() {
		String testString = "testString";
		LinkedList <String> list001 = new LinkedList(testString);
		list001.add("test002");
		String secondTest = "secondTest";
		list001.add(secondTest);
		ArrayList <String> testArrayList = list001.toArrayList();
		assertTrue("Size is not 3", testArrayList.size() == 3);
		assertTrue("First element is wrong: " + testArrayList.get(0), testArrayList.get(0).equals(secondTest));
	}

	@Test
	public void testResetCurrent() {
		String testString = "testString";
		LinkedList <String> list001 = new LinkedList(testString);
		list001.add("test002");
		String secondTest = "secondTest";
		list001.add(secondTest);
		assertTrue("first iteration of next, wrong result", list001.hasNext());
		list001.next();
		assertTrue("second iteration of next, wrong result", list001.hasNext());
		list001.resetCurrent();
		assertTrue("first iteration of next, wrong result", list001.hasNext());
		list001.next();
		assertTrue("second iteration of next, wrong result", list001.hasNext());
		list001.next();
		assertFalse("third iteration of next, wrong result", list001.hasNext());
	}

	@Test
	public void testHasNext() {
		String testString = "testString";
		LinkedList <String> list001 = new LinkedList(testString);
		list001.add("test002");
		String secondTest = "secondTest";
		list001.add(secondTest);
		assertTrue("first iteration of next, wrong result", list001.hasNext());
		list001.next();
		assertTrue("second iteration of next, wrong result", list001.hasNext());
		list001.next();
		assertFalse("third iteration of next, wrong result", list001.hasNext());
	}

	@Test
	public void testNext() {
		String testString = "testString";
		LinkedList <String> list001 = new LinkedList(testString);
		list001.add("test002");
		String secondTest = "secondTest";
		list001.add(secondTest);
		assertTrue("first iteration of next, wrong result", list001.next().equals("test002"));
		assertTrue("second iteration of next, wrong result", list001.next().equals(testString));
		try{
			list001.next();
			fail("NoSuchElementException not thrown");
		} catch (NoSuchElementException e){
			// NOP
		}
	}
	
	@Test
	public void testGetCurrent() {
		String testString = "testString";
		LinkedList <String> list001 = new LinkedList(testString);
		list001.add("test002");
		String secondTest = "secondTest";
		list001.add(secondTest);
		assertTrue("second iteration of getCurrent, wrong result", list001.getCurrent().equals(secondTest));
		list001.next();
		assertTrue("first iteration of next, wrong result", list001.getCurrent().equals("test002"));
		list001.next();
		assertTrue("second iteration of next, wrong result", list001.getCurrent().equals(testString));
	}
	
	@Test
	public void testGetFirst() {
		String testString = "testString";
		LinkedList <String> list001 = new LinkedList(testString);
		list001.add("test002");
		String secondTest = "secondTest";
		list001.add(secondTest);
		assertTrue("getFirst, wrong result", list001.getFirst().equals(secondTest));
	}
	
	@Test
	public void testGetLast() {
		String testString = "testString";
		LinkedList <String> list001 = new LinkedList(testString);
		list001.add("test002");
		String secondTest = "secondTest";
		list001.add(secondTest);
		assertTrue("getLast, wrong result", list001.getLast().equals(testString));
	}

}
