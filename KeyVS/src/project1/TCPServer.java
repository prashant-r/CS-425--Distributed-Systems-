package project1;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

public class TCPServer {

	private static HashMap<String,String> hash;
	final static Logger log = Logger.getLogger(TCPServer.class);
	final static String PATTERN = "%d [%p|%c|%C{1}] %m%n";
	/*
	 * Configuration code for the log4j logger
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

		FileAppender fa = new FileAppender();
		fa.setName("FileLogger");
		fa.setFile("log/project1/_tcp_server.log");
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
		runServer(args);	
	}
	
	/* logic for the server*/
	public static void runServer(String args[])
	{
		//hash serves as the dictionary for our key value pairs
		hash = new HashMap<String, String>();
		ServerSocket serverSocket;
		try{
			if(args.length != 1)
			{
				log.fatal("Usage:- portnumber");
				System.exit(-1);
			}
			//Set up the server on the port specified by the argument and let it accept connections endlessly
			serverSocket = new ServerSocket(Integer.parseInt(args[0]));
			while(true)
			{
				String response = "";
				try{
					Socket socket = serverSocket.accept(); 		
					String receivedString = (new BufferedReader(new InputStreamReader(socket.getInputStream()))).readLine();
					log.info("Received from client - "+ socket.getRemoteSocketAddress() + " : " + receivedString);
					DataOutputStream toClient = new DataOutputStream(socket.getOutputStream());
					String [] recString = receivedString.split(" ");
					// If the string received is ASCII and it contains a command and key at least, then continue with logic
					// else deem the received string as a bad request.
					if((validateString(receivedString) && recString.length>=2))
					{
						String command = recString[0].toUpperCase();
						String key = recString[1].trim();
						String values = "";
						for(int i = 2; i< recString.length; i++)
							values = values + recString[i] + " ";
						values = values.trim();
						switch(command.trim()){
						case "GET":
							// if the key is found only then get it
							if(hash.containsKey(key))
								response = hash.get(key);
							else
							{
								response = "No key "+ key + " matches db ";
								log.error("No key "+ key + " matches db ");
							}
							break;
						case "PUT":
							// this would overwrite the values of the key
							hash.put(key,values);
							response = "200 OK";
							break;
						case "DELETE":
							if(hash.containsKey(key))
							{
								hash.remove(key);
								response = "Key - " +  key + " has been removed ";
							}
							else
								response = "No such key exists";
							break;
						default:
							response = "Invalid command " + command + " was sent";
							log.error(response);
							break;
						}

					}
					else{
						response = "400 BAD REQUEST";
					}
					toClient.writeBytes(response);
					log.info("Replied to client " + socket.getRemoteSocketAddress() +" " + response);
					socket.close();
				}

				catch(Exception e)
				{
					log.error("Socket loop error occured with " + e.getMessage());
				}
			}
		}
		catch(Exception e)
		{	
			log.error("Server connection open on port " + args[0] + " exited with " + e.getMessage());
		}

	}
	// Takes a string and validates that its all ASCII
	public static boolean validateString(String s)
	{
		return s.matches("\\A\\p{ASCII}*\\z");
	}

}
