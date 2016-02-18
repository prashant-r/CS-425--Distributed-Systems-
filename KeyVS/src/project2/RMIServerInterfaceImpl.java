package project2;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

@SuppressWarnings("serial")
public class RMIServerInterfaceImpl extends UnicastRemoteObject implements RMIServerInterface{
	final static Logger log = Logger.getLogger(RMIServerInterfaceImpl.class);
	final static String PATTERN = "%d [%p|%c|%C{1}] %m%n";

	private static Map<String, String> hash ;
	protected RMIServerInterfaceImpl() throws RemoteException {
		super();
		initializeServer();
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
		fa.setFile("log/project2/_rmi_server.log");
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
		hash = new HashMap<String,String>();
	}
	public String PUT(String clientId, String key,String value)
	{	
		log.info("Server received [PUT " + key +"|"+value.trim() + "] from client " + clientId);
		// atomic function put, synchronize on hash
		String response = "";
		synchronized (hash) {
			// this would overwrite the values of the key
			hash.put(key,value);
			response = "200 OK";
		}
		return response;
	}
	public String GET(String clientId, String key)
	{
		// atomic function get, synchronize on hash
		// if the key is found only then get it
		
		// Ideally Get need not be threadsafe but since it invokes containsKey
		// and then invokes get there could be an error where the key could be deleted
		// between these two operations and that could be a terrible concurrency failure.
		log.info("Server received [GET " + key + "] from client " + clientId);
		String response = "";
		synchronized (hash) {
			
			if(hash.containsKey(key))
				response = hash.get(key);
			else
			{
				response = "No key "+ key + " matches db ";
				log.error(response);
			}	
		}
		return response;
	}
	public String DELETE(String clientId, String key)
	{
		// synchronize delete function on hash
		log.info("Server received [DELETE " + key + "] from client " + clientId);
		String response = "";
		synchronized (hash) {
			if(hash.containsKey(key))
			{
				hash.remove(key);
				response = "Key - " +  key + " has been removed ";
			}
			else
			{
				response = "No such key - " + key +  " exists";
				log.error(response);
			}
		}
		return response;
	}
}
