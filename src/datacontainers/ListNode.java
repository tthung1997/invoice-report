package datacontainers;

/**
 * ListNode class provides structure for a specific node of the List with constructor, getter and setter methods
 */
public class ListNode<T> implements Comparable<ListNode<T>> {

	private ListNode<T> next;
	private T item;
	
	public ListNode(T item) {
		this.next = null;
		this.item = item;
	}
	
	public T getItem() {
        return item;
    }

    public ListNode<T> getNext() {
        return next;
    }

    public void setNext(ListNode<T> next) {
        this.next = next;
    }

	@Override
	public int compareTo(ListNode<T> o) {
		return ((Comparable<T>) this.getItem()).compareTo((T) o.getItem());
	}
	
}
