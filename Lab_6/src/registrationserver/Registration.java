package registrationserver;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Registration implements Callable<String> {
	
	private BufferedReader in;
	private PrintWriter out;
	private static ArrayList<String> usersDB = new ArrayList<String>();
	private static ReentrantReadWriteLock registrationLock = new ReentrantReadWriteLock();;
	
	public Registration(Socket clientSocket) throws IOException {
		this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		this.out = new PrintWriter(clientSocket.getOutputStream(), true);
	}

	@Override
	public String call() throws Exception {
		String username = in.readLine();
		String success;
		Registration.registrationLock.readLock().lock();
		if (usersDB.contains(username)) {
			Registration.registrationLock.readLock().unlock();
			this.out.println("Fail: " + username);
			success = "Fail: " + username;
		} else {
			Registration.registrationLock.readLock().unlock();
			Registration.registrationLock.writeLock().lock();
			Registration.usersDB.add(username);
			Registration.registrationLock.writeLock().unlock();
			this.out.println("OK: " + username);
			success = "OK: " + username;
		}
		
		return success;
		
	}
}
