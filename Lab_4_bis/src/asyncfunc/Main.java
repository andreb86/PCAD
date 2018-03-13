package asyncfunc;

import java.util.Random;

public class Main {

	public static void main(String[] args) {
		int l = args.length, i;
		AsynchronousFunction af;
		String ID;
		Random r = new Random();
		Thread t;
		while (true) {
			i = r.nextInt(l);
			ID = args[i];
			af = new AsynchronousFunction(ID);
			t = new Thread(af);
			t.start();
			synchronized(af) {
				try {
					af.wait();
					System.out.printf("Thread %s got %d\n\n", ID, af.get());
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

}
