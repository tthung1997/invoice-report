package datacontainers;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * The List class provides the structure of a linked list used to store invoices
 * and some methods to interact with the list.
 */
public class LinkedList<T> implements Iterable<T> {
	
	private ListNode<T> start;
	private ListNode<T> end;
	private int size;
	
	public LinkedList() {
		start = null;
		end = null;
		size = 0;
	}
	
	public boolean isEmpty() {
		return start == null;
	}
	
	public int getSize() {
		return this.size;
	}
	
    public void clear() {
    	start = null;
    	end = null;
    	size = 0;
    }

    public void addToStart(T t) {
    	ListNode<T> tnode = new ListNode<T>(t);
    	if (this.isEmpty()) {
    		this.start = tnode;
    		this.end = tnode;
    	}
    	else {
    		tnode.setNext(this.start);
    		this.start = tnode;
    	}
    	this.size++;
    }

    public void addToEnd(T t) {
    	ListNode<T> tnode = new ListNode<T>(t);
    	if (this.isEmpty()) {
    		this.start = tnode;
    		this.end = tnode;
    	}
    	else {
    		this.end.setNext(tnode);
    		this.end = tnode;
    	}
    	this.size++;
    }

    public void remove(int position) {
    	if (position < 0 || position >= this.getSize()) {
    		throw new IndexOutOfBoundsException();
    	}
    	if (position == 0) {
    		this.start = this.start.getNext();
    	}
    	else {
    		ListNode<T> cur = this.getListNode(position - 1);
    		cur.setNext(cur.getNext().getNext());
    		if (position == this.getSize() - 1) {
    			this.end = cur;
    		}
    	}
		this.size--;
    }
    
    private ListNode<T> getListNode(int position) {
    	if (position < 0 || position >= this.getSize()) {
    		throw new IndexOutOfBoundsException();
    	}
    	ListNode<T> cur = start;
    	for(int i = 1; i <= position; i++) {
    		cur = cur.getNext();
    	}
    	return cur;
    }
    
    public T getItem(int position) {
    	return this.getListNode(position).getItem();
    }
    
    /**
     * This method adds an invoice into the list while maintaining its order
     * @param invoice
     */
    public void addItem(T item) {
    	ListNode<T> node = new ListNode<T>(item);
    	if (this.size == 0) {
    		start = node;
    		end = node;
    	}
    	else if (node.compareTo(start) > 0) {
    		node.setNext(start);
    		start = node;
    	}
    	else {
    		int position = 0;
    		ListNode<T> cur = start;
    		while (position < this.size - 1 && cur.getNext().compareTo(node) > 0) {
    			++position;
    			cur = cur.getNext();
    		}
    		if (position == this.size - 1) {
    			end.setNext(node);
    			end = node;
    		}
    		else {
    			node.setNext(cur.getNext());
    			cur.setNext(node);
    		}
    	}
    	++size;
    }

	public Iterator<T> iterator() {
		return new ListIterator();
	}

	private class ListIterator implements Iterator<T> {

		ListNode<T> current = start;
		
		@Override
		public boolean hasNext() {
			return current != null;
		}

		@Override
		public T next() {
			if (!hasNext()) 
				throw new NoSuchElementException();
			T item = current.getItem();
			current = current.getNext();
			return item;
		}
		
	}
	
}
