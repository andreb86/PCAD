package registrationserver;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Registration implements Callable<String> {
	
	private ServerSocket listener;
	private Socket registrationSocket;
	private BufferedReader in;
	private PrintWriter out;
	private static ArrayList<String> usersDB = new ArrayList<String>();
	private static ReentrantReadWriteLock registrationLock = new ReentrantReadWriteLock();;
	
	public Registration(int port) throws IOException {
		this.listener = new ServerSocket(port);
		this.registrationSocket = listener.accept();
		this.in = new BufferedReader(new InputStreamReader(registrationSocket.getInputStream()));
		this.out = new PrintWriter(registrationSocket.getOutputStream(), true);
	}

	@Override
	public String call() throws Exception {
		String username = in.readLine();
		Registration.registrationLock.readLock().lock();
		if (usersDB.contains(username)) {
			Registration.registrationLock.readLock().unlock();
			this.out.println("Fail: " + username);
			this.in.close();
			this.out.close();
			this.registrationSocket.close();
			this.listener.close();
			return "Fail: " + username;
		} else {
			Registration.registrationLock.writeLock().lock();
			Registration.usersDB.add(username);
			Registration.registrationLock.writeLock().unlock();
			Registration.registrationLock.readLock().unlock();
			this.out.println("OK: " + username);
			this.in.close();
			this.out.close();
			this.registrationSocket.close();
			this.listener.close();
			return "OK: " + username;
		}
	}
}
