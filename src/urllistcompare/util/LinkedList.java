/**
 * 
 */
package urllistcompare.util;

/**
 * @author Rocco Barbini
 * @email roccobarbi@gmail.com
 * 
 * A generic LinkedList that can be used to implement both the lists of URLElement contained within the
 * URLNorm instances and the lists of URLNorm that make up the URLMap hashmap.
 *
 */
public class LinkedList<T>{
	private ListNode head;

	// TODO
	public LinkedList() {
		this.head = null;
	}
	
	public LinkedList(T payload) {
		this.head = new ListNode(payload, null);
	}
	
	private class ListNode{
		// TODO: internal class that implements the nodes
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
