package registrationserver;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class Main {

	public static void main(String[] args) {
		RegistrationServer regServ = new RegistrationServer();
		while (true) {
			try {
				regServ.register();
			} catch (IOException ioe) {
				regServ.stop();
				System.err.println("IO error detected.\n" + ioe.getMessage());
			} catch (InterruptedException ie) {
				regServ.stop();
				System.err.println("Shutting down.\n" + ie.getMessage());
			} catch (ExecutionException ee) {
				regServ.stop();
				System.err.println("Unable to start pool\n" + ee.getMessage());
			}
		}
	}

}
