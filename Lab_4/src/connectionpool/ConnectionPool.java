package connectionpool;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutorService;

public class ConnectionPool {
	private static int numOfProcessors;
	private static ExecutorService connectionPool;
	private static ConcurrentHashMap<String, Connection> connectionsHashMap;
	
	public ConnectionPool() {
		numOfProcessors = Runtime.getRuntime().availableProcessors();
		connectionPool = Executors.newFixedThreadPool(numOfProcessors);
		connectionsHashMap = new ConcurrentHashMap<String, Connection>(100);
	}
	
	public Future<Integer> request(String url) throws IOException, MalformedURLException {
		Future<Integer> connCode;
		connectionsHashMap.putIfAbsent(url, new Connection(url));
		connCode = ConnectionPool.connectionPool.submit(connectionsHashMap.get(url));
		return connCode;
	}
	
	public void stop() {
		ConnectionPool.connectionPool.shutdown();
		ConnectionPool.connectionsHashMap.clear();
	}
}
