/**
 * 
 */
package urllistcompare.util;

/**
 * @author Rocco Barbini (roccobarbi@gmail.com)
 *
 */
public final class ArraySort {

	public static <T extends Comparable<? super T>> T[] insertionSortAsc(T[] a){
		int i, k;
		T temp;
		if(a.length > 1){
			for(i = 1; i < a.length; i++){
				k = i;
				while(k > 0 && a[k].compareTo(a[k - 1]) < 0){
					temp = a[k];
					a[k] = a[k-1];
					a[k-1] = temp;
					k--;
				}
			}
		}
		return a;
	}
	
	public static <T extends Comparable<? super T>> T[] insertionSortDesc(T[] a){
		int i, k;
		T temp;
		if(a.length > 1){
			for(i = 1; i < a.length; i++){
				k = i;
				while(k > 0 && a[k].compareTo(a[k - 1]) > 0){
					temp = a[k];
					a[k] = a[k-1];
					a[k-1] = temp;
					k--;
				}
			}
		}
		return a;
	}

}
