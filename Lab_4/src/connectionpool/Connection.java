package connectionpool;

import java.util.concurrent.Callable;
import java.io.IOException;
import java.net.*;

public class Connection implements Callable<Integer> {
	
	private URL url;
	private HttpURLConnection conn;
	
	public Connection(String urlString) throws IOException, MalformedURLException {
		// Create the connection
		this.url = new URL (urlString);
		this.conn = (HttpURLConnection) this.url.openConnection();
		this.conn.setConnectTimeout(5000);
	}

	@Override
	public Integer call() throws IOException, SocketTimeoutException {
		return this.conn.getResponseCode();
	}

}
