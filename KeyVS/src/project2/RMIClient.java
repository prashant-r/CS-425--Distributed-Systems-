package project2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.Naming;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

public class RMIClient {
	final static Logger log = Logger.getLogger(RMIClient.class);
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
		fa.setFile("log/project2/_rmi_client.log");
		fa.setLayout(new PatternLayout("%d %-5p [%c{1}] %m%n"));
		fa.setThreshold(Level.ALL);
		fa.setAppend(true);
		fa.activateOptions();

		//add appender to any Logger (here is root)
		log.addAppender(fa);
		log.setAdditivity(false);
		//repeat with all other desired appenders
	}
	public static void main(String args[])
	{
		configureLogger();
		String clientId = "NA";
		// 4 arguments must be passed in.
		if(args.length == 0)
		{
			
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Give client an Identifier, any number,string etc");
			try {
				clientId = br.readLine();
				System.out.println("Now, enter the following to connect to RMI Server : hostname port instruction key value");
				args = br.readLine().split("\\s+");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("Please follow instructions closely on next run");
				System.exit(-1);
			}
			
		}
				if(args.length < 4 )
				{
					log.fatal("Fatal error : Usage - hostname port instruction key value" );
					System.exit(-1);
				}
				String hostname = args[0];
				String port = args[1];
				String command = args[2];
				String key = args[3];
				String values = "";
				for(int a = 4; a <args.length; a++)
					values +=" " + args[a];
				try{
					// locate the remote object initialize the proxy using the binder
					RMIServerInterface hostImpl = (RMIServerInterface) Naming.lookup("rmi://" + hostname + ":" + port + "/Calls" );
					// call the corresponding methods
					
					if(System.getProperty("clientId") != null)
						clientId = System.getProperty("clientId");
						
					switch(command.trim().toUpperCase()){
					case "GET":
						log.info("Client "+ clientId + ":" + hostImpl.GET(clientId,key));
						break;
					case "PUT":
						log.info("Client "+ clientId + ":" +hostImpl.PUT(clientId,key,values));
						break;
					case "DELETE":
						log.info("Client "+ clientId + ":" +hostImpl.DELETE(clientId,key));
						break;
					default:
						String response = "Client "+clientId + ":" +  "Invalid command " + command + " was received";
						log.error(response);
						break;
					}
				}
				catch(Exception e)
				{
					log.error("Error occured while connecting to RMI server with error, " + e.getMessage());
				}
				}
	}

