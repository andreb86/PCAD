package registrationserver;

import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class RegistrationServer {
	/**
	 * Registration Server:
	 * The server is initialised with the number of cores available.
	 * A ServerSocket is created on the port used in the constructor
	 * listening for incoming registrations.
	 * 
	 * */
	private static int numberOfListeners;
    private ServerSocket listener;
    private Socket clientRegistrationSocket;
	private ExecutorService registrationPool;
	
	public RegistrationServer(int port) throws IOException{
		System.out.println("Server started!\n");
		RegistrationServer.numberOfListeners = Runtime.getRuntime().availableProcessors();
		this.registrationPool = Executors.newFixedThreadPool(numberOfListeners);
		this.listener = new ServerSocket(port);
		this.clientRegistrationSocket = this.listener.accept();
	}
	
	public void register() throws InterruptedException, ExecutionException, IOException {
		Future<String> reg = this.registrationPool.submit(new Registration(this.clientRegistrationSocket));
		String out = reg.get();
		System.out.println("Registration incoming from: " + out);
	}
	
	public void stop() throws IOException {

	    clientRegistrationSocket.close();
	    listener.close();

		registrationPool.shutdown();
		System.out.println("Shutting down the registration server!");
	}

}
