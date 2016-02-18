package project3;

import java.io.BufferedReader;
import java.io.FileReader;
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
	final static int numReplicas = 5;
	final static int hostPortColumn = 2;
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
		fa.setFile("log/_rmi_client.log");
		fa.setLayout(new PatternLayout("%d %-5p [%c{1}] %m%n"));
		fa.setThreshold(Level.ALL);
		fa.setAppend(true);
		fa.activateOptions();

		//add appender to any Logger (here is root)
		log.addAppender(fa);
		log.setAdditivity(false);
		//repeat with all other desired appenders
	}
	
	public static int getNumReplicas()
	{
		return numReplicas;
	}
	
	public static String[][] readConfigFile()
	{		
		String hostPorts [][] = new String[numReplicas][hostPortColumn];
		try {
			BufferedReader fileReader = new BufferedReader(new FileReader("../configs.txt"));			
			System.out.println("Loading configurations from configs.txt..");
			int c = 0;
			
			while(c++!=numReplicas)
			{
				hostPorts[c-1] = fileReader.readLine().split("\\s+");
				if(hostPorts[c-1][0].isEmpty() || !hostPorts[c-1][1].matches("[0-9]+") || hostPorts[c-1][1].isEmpty())
				{
					System.out.println("You have made incorrect entries for addresses in config file, please investigate.");
					System.exit(-1);
				}
			}
			fileReader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("System exited with error " +e.getMessage());
			System.exit(-1);
		}
		return hostPorts;
	}
	
	public static void main(String args[])
	{
		configureLogger();
		String clientId = "NA";
		// 4 arguments must be passed in.
		String hostPorts [][] = new String[numReplicas][hostPortColumn];
		String command = "";
		String key = "";
		String inkVal[];
		String values = "";		
		int serverNum =0;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		if(args.length == 0)
		{
			System.out.println("Give client an Identifier, any number,string etc");
			try {
				clientId = br.readLine();
				System.out.println("Enter, instruction key value");
				inkVal = br.readLine().split("\\s+");
				command = inkVal[0];
				key = inkVal[1];
				for(int a = 2; a <inkVal.length; a++)
					values +=" " + inkVal[a];
				hostPorts = readConfigFile();
				System.out.println("Which replica to invoke? #1-5");
				serverNum = Integer.parseInt(br.readLine());
				System.out.println("You selected: RMI Server " + hostPorts[serverNum-1][0] + ":"+ hostPorts[serverNum-1][1]);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("Please follow instructions closely on next run");
				System.out.println("Error with message, " + e.getMessage());
				System.exit(-1);
			}
		}
		else if(args.length < 2  )
		{
			log.fatal("Fatal error : instruction key value" );
			System.exit(-1);
		}
		else
		{
			hostPorts = readConfigFile();
			command = args[0];
			key = args[1];
			for(int a = 2; a <args.length; a++)
				values +=" " + args[a];
			serverNum =  Integer.parseInt(System.getProperty("serverChoice"));
		}
		
		try{
			
			String hostname = hostPorts[serverNum-1][0];
			String port = hostPorts[serverNum-1][1];
			// locate the remote object initialize the proxy using the binder
			RMIServerInterface hostImpl = (RMIServerInterface) Naming.lookup("rmi://" + hostname + ":" + port + "/Calls" );
			// call the corresponding methods
			key = key.trim();
			values = values.trim();
			if(System.getProperty("clientId") != null)
				clientId = System.getProperty("clientId");

			switch(command.trim().toUpperCase()){
			case "GET":
				log.info("Client "+ clientId + ":" + hostImpl.GET(clientId,key));
				break;
			case "PUT":
				log.info("Client "+ clientId + ":" +hostImpl.PUT(false,clientId,key,values));
				break;
			case "DELETE":
				log.info("Client "+ clientId + ":" +hostImpl.DELETE(false,clientId,key));
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

