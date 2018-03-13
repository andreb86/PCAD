package carPark;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


/**
 * @author abasso
 * This is the parkMonitor Class that contains the shared resources for the car par app.
 */
public class parkMonitor {

	/**
	 * Private variables
	 */
	private static final long serialVersionUID = 1L;
	public static int gates;
	private static ReentrantLock lock;
	private static int available;
	public static Condition notFull;

	/**
	 * Constructor of the parkMonitor.
	 */
	public parkMonitor(int v, int g) {
		super();
		parkMonitor.available = v;
		parkMonitor.gates = g;
		parkMonitor.lock = new ReentrantLock();
		parkMonitor.notFull = parkMonitor.lock.newCondition();
	}
	
	/**
	 * Enter the car park, decrease the number of available space and return true.
	 * Useful to understand if the car is in.
	 */
	public static boolean enter (int gate, int carID) throws InterruptedException {
		
		// Acquire the lock
		parkMonitor.lock.lock();
		
		// If the park is full wait in queue.
		while (parkMonitor.available == 0)
			parkMonitor.notFull.await();
		
		// Decrement the number of available slots.
		try {
			parkMonitor.available--;
			System.out.printf(
					"Car No. %d is entering from gate %d; %d slots available\n",
					carID,
					gate,
					parkMonitor.available);
		} finally {
			parkMonitor.lock.unlock();
		}
		return true;
	}
	
	/**
	 * Leave the car park, increase the number of available spaces and return false.
	 * Useful to understand if the car is out.
	 */
	public static boolean leave (int carID) throws InterruptedException {
		
		// Acquire the lock
		parkMonitor.lock.lock();
		
		try {
			parkMonitor.available++;
			System.out.printf(
					"Car No. %d is leaving; %d slots available\n\n",
					carID,
					parkMonitor.available);
			parkMonitor.notFull.signal();
		} finally {
			parkMonitor.lock.unlock();
		}
		return false;
	}
	
	
}
