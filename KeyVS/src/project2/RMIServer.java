package project2;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

public class RMIServer {
	final static Logger log = Logger.getLogger(RMIServer.class);
	final static String PATTERN = "%d [%p|%c|%C{1}] %m%n";
	static void configureLogger()
	{
		ConsoleAppender console = new ConsoleAppender(); //create appender
		//configure the appender
		console.setLayout(new PatternLayout(PATTERN)); 
		console.setThreshold(Level.ALL);
		console.activateOptions();
		//add appender to any Logger (here is root)
		log.addAppender(console);

		// This is for the tcp_client log file
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
	// Takes a port number to initialize the server
	public RMIServer(Integer portNumber) throws Exception
	{		
		// create the registry 
		RMIServerInterface rmiMethods = new RMIServerInterfaceImpl(); 
		LocateRegistry.createRegistry(portNumber);
		//bind the method to this name so the client can search for it
		String bindMe = "rmi://localhost:" + portNumber + "/Calls";
		Naming.bind(bindMe, rmiMethods);
		System.out.println("RMIServer started successfully");
	}
	public static void main(String args[])
	{
		configureLogger();
		if(args.length != 1 )
		{
			System.out.println("Enter a port number to start server");
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			args = new String[2];
			try{
				args[0] = br.readLine();}
			catch(Exception e)
			{
				log.fatal("Fatal error : Usage - port#" );
				System.exit(-1);
			}
		}
		Integer portNumber =0;
		try{
			portNumber = Integer.parseInt(args[0]);
		}
		catch(Exception e)
		{
			log.fatal("Fatal error : Usage - port#, error caused due to " + e.getMessage());
			System.exit(-1);
		}
		try{
			new RMIServer(portNumber);
		}
		catch(Exception e)
		{
			log.error("RMI server binding failed with " + e.getMessage());
		}
	}

}
