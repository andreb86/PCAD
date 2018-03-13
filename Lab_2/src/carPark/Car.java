package carPark;
import java.util.Random;

public class Car implements Runnable {
	public boolean isIn;
	public int carID;
	private Random randGen = new Random();
	private int gate;
	
	public Car(int c) {
		super();
		this.carID = c;
	}
	
	public void choseGate () {
		this.gate = randGen.nextInt(parkMonitor.gates);
	}
	
	@Override
	public void run() {
		try {
			choseGate();
			this.isIn = parkMonitor.enter(this.gate, this.carID);
			Thread.sleep(2000);
			this.isIn = parkMonitor.leave(this.carID);
		} catch (InterruptedException e) {
			System.out.println("Exiting the program");
			System.exit(1);
		}
	}
}
