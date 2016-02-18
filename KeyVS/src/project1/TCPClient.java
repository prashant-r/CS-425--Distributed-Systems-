package project1;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

public class TCPClient {
	
	final static Logger log = Logger.getLogger(TCPClient.class);
	final static String PATTERN = "%d [%p|%c|%C{1}] %m%n";
	
	/* Some standard configuration for the apache log4j logger. Can also be written as a log4j.properties file
	 * was written in code to dyamically select the file to output to.
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
		
		// This is for the tcp_client log file
		FileAppender fa = new FileAppender();
		fa.setName("FileLogger");
		fa.setFile("log/project1/_tcp_client.log");
		fa.setLayout(new PatternLayout("%d %-5p [%c{1}] %m%n"));
		fa.setThreshold(Level.ALL);
		fa.setAppend(true);
		fa.activateOptions();

		//add appender to any Logger (here is root)
		log.addAppender(fa);
		//repeat with all other desired appenders
		log.setAdditivity(false);
	}
	
	
	/*
	 * The logic for the TCPClient takes in the arguments from the command line and attempts to connect to server for upto 5 seconds.
	 */
	public static void runClient(String [] args)
	{
		String sendProtocolString = "";
		// 4 arguments must be passed in.
		if(args.length < 4 )
		{
			log.fatal("Fatal error : Usage - hostname port instruction key value" );
			System.exit(-1);
		}
		else if (args.length == 4)
		{   
			// Command is a GET or DELETE(args[2]) and args[3] is the key, command must be applied to.
			sendProtocolString = args[2] + " " + args[3];
		}
		else
		{// Command is a PUT(args[2]) and the other arguments value/s
			sendProtocolString = args[2];
			for(int a = 3; a <args.length; a++)
				sendProtocolString += " "+args[a];
		}
		// Initialize the clientSocket with the hostname and port number available from arguments
		Socket clientSocket;
		log.info(sendProtocolString);
		try{
			clientSocket = new Socket();
			// Setting timeout of 5 seconds
			log.info("Connecting to server" + "....");
			clientSocket.connect(new InetSocketAddress(args[0], Integer.parseInt(args[1])), 5000);
			log.info("Connected to server at "+ clientSocket.getRemoteSocketAddress());
			DataOutputStream toServer = new DataOutputStream(clientSocket.getOutputStream());
			// write the bytes out to server
			toServer.writeBytes(sendProtocolString + "\n");
			String stringFromInputStream = (new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))).readLine();
			if(validateString(stringFromInputStream))
				log.info("Result from server - "+ clientSocket.getRemoteSocketAddress() + " : " + stringFromInputStream);
			else
				log.error("Malformed response from server for request " + sendProtocolString );			
			clientSocket.close();
		} catch (Exception e) {
			// if a timeout 
			log.error("Client connection to " + args[0] +":" + args[1] + " exited with " + e.getMessage());
		}

	}
	public static void main(String[] args) {
		configureLogger(); // At startup configure logger
		runClient(args);
	}
	// Takes a string and validates that it contains all ASCII characters
	public static boolean validateString(String s)
	{
		return s.matches("\\A\\p{ASCII}*\\z");
	}
}
