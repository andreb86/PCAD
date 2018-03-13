package asyncfunc;

public class AsynchronousFunction implements Runnable {
	
	private String ID;
	private int counter = 0;
	
	public AsynchronousFunction(String id) {
		this.ID = id;
	}

	@Override
	public void run() {
		synchronized(this) {
			while (this.counter < 100)
				this.counter++;
			System.out.printf("Thread %s: Done!!!\n", this.ID);
			notifyAll();
		}
	}
	
	public int get() {
		return this.counter;
	}

}
