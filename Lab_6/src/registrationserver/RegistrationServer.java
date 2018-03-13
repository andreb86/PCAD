package registrationserver;

import java.io.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class RegistrationServer {
	
	private static int numberOfListeners;
	private ExecutorService registrationPool;
	
	public RegistrationServer() {
		System.out.println("Server started!\n");
		numberOfListeners = Runtime
				.getRuntime()
				.availableProcessors();
		this.registrationPool = Executors
				.newFixedThreadPool(numberOfListeners);
		
	}
	
	public void register() throws InterruptedException, ExecutionException, IOException {
		Future<String> reg = this.registrationPool.submit(new Registration(9000));
		String out = reg.get();
		System.out.println(out);
	}
	
	public void stop() {
		registrationPool.shutdown();
		System.out.println("Shutting down the registration server!");
	}

}
