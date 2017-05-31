/**
 * 
 */
package urllistcompare.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author Rocco Barbini
 * @email roccobarbi@gmail.com
 * 
 * A generic LinkedList that can be used to implement both the lists of URLElement contained within the
 * URLNorm instances and the lists of URLNorm that make up the URLMap hashmap.
 *
 */
public class LinkedList<T> implements Iterator{
	private ListNode head, current;
	private int length;

	public LinkedList() {
		head = null;
		length = 0;
		current = head;
	}
	
	public LinkedList(T payload) {
		head = new ListNode(payload, null);
		length = 1;
		current = head;
	}
	
	/**
	 * 
	 * @return the number of elements in the list
	 */
	public int getLength(){
		return length;
	}
	
	/**
	 * 
	 * @return the payload of the current iterator
	 */
	public T getCurrent(){
		return current.payload;
	}
	
	/**
	 * Adds a new node to the head of the list, updates the length and resets the current iterator.
	 * This works as a stack: the last element to be added is the first to be retrieved.
	 * @param payload
	 */
	public void add(T payload){
		ListNode newNode = new ListNode(payload, head);
		head = newNode;
		length++;
		resetCurrent();
	}
	
	// Returns the ListNode that includes the payload passed as argument, or null.
	private ListNode find(T payload){
		ListNode current = head;
		while(current != null && !current.payload.equals(payload)){
			current = current.next;
		}
		return current;
	}
	
	/**
	 * Checks if a specific payload is in the list
	 * @param payload the element that is being searched for
	 * @return true if found, false if not found
	 */
	public boolean isInList(T payload){
		boolean output = false;
		if(find(payload) != null){
			output = true;
		}
		return output;
	}
	
	/**
	 * Outputs the contents of the list as an ArrayList.
	 * This works as a steck: the order of the arraylist is the inverse of the order in which elements were 
	 * added to the linkedlist (FILO).
	 * @return an ArrayList of the payloads of the list nodes, or null if the list is empty
	 */
	public ArrayList<T> toArrayList(){
		if(head == null) return null; // Faster and safer
		ArrayList<T> output = new ArrayList<T>(length);
		ListNode current = head;
		while(current != null){
			output.add(current.payload);
			current = current.next;
		}
		output.trimToSize();
		return output;
	}
	
	/**
	 * Resets the current iterator to the list head.
	 */
	public void resetCurrent(){
		current = head;
	}

	@Override
	public boolean hasNext() {
		if(current.next == null) return false;
		else return true;
	}

	@Override
	public Object next() {
		if(!hasNext()) throw new NoSuchElementException();
		else current = current.next;
		return current.payload;
	}
	
	// TODO: complete the functionality
	
	// Inner class that represents the list nodes
	private class ListNode{
		T payload;
		ListNode next;
		
		private ListNode(){
			this.payload = null;
			this.next = null;
		}
		
		private ListNode(T payload, ListNode next){
			this.payload = payload;
			this.next = next;
		}
	}

}
