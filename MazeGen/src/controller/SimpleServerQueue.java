package controller;

/**
 * ServerQueue.java
 * 
 * @author			Devin Barry
 * @date			25.10.2012
 * @lastModified	28.10.2012
 * 
 * A very simply wait / notify queue implementation used to
 * pass elements of type E from different socket threads between
 * themselves. This class allows consumer threads to sleep while
 * waiting for producer threads to put an item into the queue.
 * 
 * This class has a queue length of 1. The producer will block
 * when one item is in the queue. The consumer will block when
 * the queue is empty.
 */
public class SimpleServerQueue<E> {

	E e; //The single item "queue"
	boolean valueSet;
	
	public SimpleServerQueue() {
		valueSet = false;
	}

	/**
	 * This get methods get an object from the Queue
	 * 
	 * Here we synchronize over this ServerQueue object
	 * and therefore can call wait and notify
	 * 
	 * @return
	 */
	public synchronized E get() {
		if (!valueSet) {
			try {
				wait();
			} catch (InterruptedException ie) {
				System.out.println("InterruptedException caught");
			}
		}
		//System.out.println("Got: " + e);
		valueSet = false;
		notify();
		return e;
	}

	/**
	 * This put method puts an object into the Queue.
	 * 
	 * Here we synchronize over this ServerQueue object
	 * and therefore can call wait and notify.
	 * 
	 * @param e
	 */
	public synchronized void put(E e) {
		if (valueSet) {
			try {
				wait();
			} catch (InterruptedException ie) {
				System.out.println("InterruptedException caught");
			}
		}
		this.e = e;
		valueSet = true;
		//System.out.println("Put: " + e);
		notify();
	}

}
