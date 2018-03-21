package registrationserver;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class Main {

	public static void main(String[] args) throws IOException {
		RegistrationServer regServ = new RegistrationServer(9000);
		try {
			while (true) {
				regServ.register();
			}
		} catch (IOException ioe) {
			System.err.println("IO error detected.\n" + ioe.getMessage());
			regServ.stop();
			System.exit(1);
		} catch (InterruptedException ie) {
			System.err.println("Shutting down.\n" + ie.getMessage());
			regServ.stop();
			System.exit(2);
		} catch (ExecutionException ee) {
			System.err.println("Unable to start pool\n" + ee.getMessage());
			regServ.stop();
			System.exit(3);
		}
	}

}
