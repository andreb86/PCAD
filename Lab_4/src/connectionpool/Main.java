package connectionpool;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class Main {

	public static void main(String[] args) {
		int i;
		String site;
		int connCode;
		
		// Create a list of web sites to connect to
		ArrayList<String> webSites = new ArrayList<String>();
		webSites.add("https://www.google.it");
		webSites.add("https://www.amazon.it");
		webSites.add("https://it.yahoo.com");
		webSites.add("https://www.aruba.it");
		webSites.add("https://duckduckgo.com");
		webSites.add("https://www.alibaba.com");
		webSites.add("https://www.facebook.com");
		webSites.add("https://www.bogussite.xyz");
		webSites.add("https://www.google.com");
		webSites.add("https://www.questositononesiste.moc");
		
		// Construct the connection pool
		ConnectionPool connector = new ConnectionPool();
		
		// Initialise the random number generator
		Random rand = new Random(webSites.size());
		
		while (true) {
			
			i = rand.nextInt(webSites.size());
			site = webSites.get(i);
			
			try {
				
				connCode = connector.request(site).get();
				System.out.printf("Connection to site %s returned code %d\n", site, connCode);
			} catch (MalformedURLException mue) {
				System.err.println("This is not a URL: " + site);
			} catch (SocketTimeoutException ste) {
				System.err.printf("Connection to site %s timed out");
			} catch (IOException ioe) {
				System.err.println("This cannot be found: " + site);
			} catch (InterruptedException ie) {
				System.err.println("Program terminated: ");
				connector.stop();
			} catch (ExecutionException ee) {
				System.err.printf("Something went wrong with " + site + "\n");
			}
		}
	}

}
