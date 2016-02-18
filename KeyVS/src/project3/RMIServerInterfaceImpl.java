package project3;
import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ConcurrentNavigableMap;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.mapdb.*;

@SuppressWarnings("serial")
public class RMIServerInterfaceImpl extends UnicastRemoteObject implements RMIServerInterface{
	final static Logger log = Logger.getLogger(RMIServerInterfaceImpl.class);
	final static String PATTERN = "%d [%p|%c|%C{1}] %m%n";
	private static PersistentHash hash ;
	private static String[][] hostPorts;
	public int port;
	public String hostname;
	private static final String ACCEPT = "ACCEPT";
	private static final String ACK = "ACK";
	private static final String NACK = "NACK";
	private static final int numReplicas = 5;
	/*
	 * Flavor of hash that persists to disk
	 */
	private static class PersistentHash{

		private static ConcurrentNavigableMap<String,String> treeMap;
		private static DB db;
		public PersistentHash()
		{
			initializeDB();
		}
		public static void initializeDB()
		{
			db = DBMaker.fileDB(new File("testdb"))
					.closeOnJvmShutdown()
					.make();
			treeMap = db.treeMap("map");
		}
		public boolean containsKey(String key)
		{
			return treeMap.containsKey(key);
		}
		public String get(String key)
		{
			return treeMap.get(key);
		}
		public void remove(String key)
		{
			treeMap.remove(key);
			db.commit();
		}
		public void put(String key,String value)
		{
			treeMap.put(key, value);
			db.commit();
		}
	}
	protected RMIServerInterfaceImpl(int portNumber) throws RemoteException {
		super();
		initializeServer();
		port = portNumber;
		try {
			hostname = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			hostname= "INVALID";
			log.error( "At" + hostname + ":" + port + " "+ "Unable to resolve local host name");
		}
	}
	/* Configure the log4j appenders
	 */
	static void configureLogger()
	{
		ConsoleAppender console = new ConsoleAppender(); //create appender
		//configure the appender
		console.setLayout(new PatternLayout(PATTERN)); 
		console.setThreshold(Level.ALL);
		console.activateOptions();
		//add appender to any Logger (here is root)
		log.addAppender(console);
		// This is for the rmi_server log file
		FileAppender fa = new FileAppender();
		fa.setName("FileLogger");
		fa.setFile("log/_rmi_server.log");
		fa.setLayout(new PatternLayout("%d %-5p [%c{1}] %m%n"));
		fa.setThreshold(Level.ALL);
		fa.setAppend(true);
		fa.activateOptions();

		//add appender to any Logger (here is root)
		log.addAppender(fa);
		log.setAdditivity(false);
		//repeat with all other desired appenders
	}
	/*
	 * Setup server when constructor is called
	 */
	protected static void initializeServer()
	{
		configureLogger();
		hash = new PersistentHash();
		hostPorts= RMIClient.readConfigFile();
	}
	/*
	 * (non-Javadoc)
	 * @see project3.RMIServerInterface#PUT(boolean, java.lang.String, java.lang.String, java.lang.String)
	 * Two phase commit for PUT operation. Only if go is true. we would commit to disk.
	 */
	public String PUT(boolean go, String clientId, String key,String value)
	{	
		if(go)
		{	
			log.info("Server at " + hostname + ":" + port + " "+ "received [PUT " + key +"|"+value.trim() + "] from client " + clientId);
			String response = "";
			// this would overwrite the values of the key
			hash.put(key,value);
			response = ACK;
			return response;
		}
		else
			return TwoPCommit("PUT", key, value) ? ACK : NACK;
	}
	/*
	 * Implementation of 2Phase Commit first make request to all other replicas.
	 * If "ACCEPT" is received from all the replicas then proceed to second phase, which is to commit changes.
	 * Used by the PUT and DELETE methods as they are the only ones that make write operations.
	 */
	public boolean TwoPCommit(String command, String key, String value)
	{
		int accept = 0;
		String result = "";
		String host = "";
		int portNumber =0;
		// ASK proxies at other servers in cluster
		for(int a = 0; a<numReplicas; a++)
		{
			result = "";
			try {
				host = InetAddress.getByName(hostPorts[a][0]).getHostAddress();
				portNumber = Integer.parseInt(hostPorts[a][1]);
				RMIServerInterface remoteImpl = (RMIServerInterface) Naming.lookup("rmi://" + host.trim() + ":" + portNumber + "/Calls" );
				long startTime = System.currentTimeMillis(); //Timeout after 10s
				while(result.isEmpty())
				{
					if((System.currentTimeMillis()-startTime)>10000)
					{
						log.error("Unable to ASK " + host + ":" + portNumber + "Timed-out!");
						break;
					}
					result = remoteImpl.ASK(command, key, value);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				log.error("Connection to "+ host + ":" + portNumber + " met error with "+ e.getMessage());
			}
			log.info(result+ " for " + host+ ":"+ portNumber);
			if(result.trim().toUpperCase().equals(ACCEPT))
				++accept;
		}
		/*
		 * If all the replicas have accepted the requested command via the ASK method, then make commit.
		 */
		if(accept == numReplicas)
		{	
			for(int a = 0; a<numReplicas; a++)
			{
				result = "";
				try {
					host = InetAddress.getByName(hostPorts[a][0]).getHostAddress();
					portNumber = Integer.parseInt(hostPorts[a][1]);
					RMIServerInterface remoteImpl = (RMIServerInterface) Naming.lookup("rmi://" + host.trim() + ":" + portNumber + "/Calls" );
					long startTime = System.currentTimeMillis(); //Timeout after 10s
					while(!result.equals(ACK))
					{
						if((System.currentTimeMillis()-startTime)>10000)
						{
							log.error("Unable to commit update to " + host + ":" + portNumber + "Timed-out!");
							break;
						}
						if(command.toUpperCase().equals("PUT"))
							result = (remoteImpl.PUT(true,host+":"+port, key, value));
						else if(command.toUpperCase().equals("DELETE"))
							result = (remoteImpl.DELETE(true, host+":"+port, key));
					}
				}
				catch (Exception e) {
					log.error("Connection to "+ host + ":" + portNumber + " met error with "+ e.getMessage());
				}
			}
			return true;
		}
		return false;
	}

	public String GET(String clientId, String key)
	{
		log.info("Server at " + hostname + ":" + port + " "+ "received [GET " + key + "] from client " + clientId);
		String response = "";

		if(hash.containsKey(key))
			response = hash.get(key);
		else
		{
			response = "No key "+ key + " matches db ";
			log.error(response);
		}	
		return response;
	}
	public String DELETE(boolean go, String clientId, String key)
	{
		// synchronize delete function on hash
		if(go)
		{
			log.info("Server at " + hostname + ":" + port + " "+ "received [DELETE " + key + "] from client " + clientId);
			String response = "";
			if(hash.containsKey(key))
			{
				hash.remove(key);
				response = ACK;
			}
			else
			{
				response = "No such key - " + key +  " exists";
				log.error(response);
			}
			return response;
		}
		else
			return TwoPCommit("DELETE", key, "") ? ACK : NACK;
	}
	/*
	 * (non-Javadoc)
	 * @see project3.RMIServerInterface#ASK(java.lang.String, java.lang.String, java.lang.String)
	 * ASK method checks with the replica that the command being requested with the key and value is legitimate and acceptable to the server.
	 * If the value is not being modified for the key passed in, then reject.
	 * Similarly, if the key doesn't exist in the replica, then ASK would reject for this replica.
	 * Enforces consistency of data in the absence of failures.  
	 */
	@Override
	public String ASK(String command, String key, String value)
	{
		if(hash.containsKey(key))
		{
			String existingVal = hash.get(key);
			if(command.toUpperCase().equals("PUT"))
				if(!existingVal.equals(value))
					return "ACCEPT";
				else
					return "REJECT";
			else
				return "ACCEPT";
		}
		else{
			if(command.toUpperCase().equals("PUT"))
				return "ACCEPT";
			else
				return "REJECT";
		}                        
	}
}
