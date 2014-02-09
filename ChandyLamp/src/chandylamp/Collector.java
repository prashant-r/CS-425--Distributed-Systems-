package chandylamp;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



/**
 *
 * @author Prashant
 */

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Collector implements Runnable {
	private int listenPort;
	private int processCount; // for checking if all processes send their states

	// for the output
	double sumStates;
	double sumChannels;

	public Collector(int listenPort, int processCount) {
		this.listenPort = listenPort;
		this.processCount = processCount;

	}

	@Override
	public void run() {
		try {
			ServerSocket server = new ServerSocket(listenPort);
			int counter = 0;
			while (true) {
				Socket client = server.accept();

				// put your implementation here
				ObjectInputStream in = new ObjectInputStream(
						client.getInputStream());

				try {
					double balance = (Double) in.readObject();

					ArrayList<MoneyMessage> msgs = (ArrayList<MoneyMessage>) in
							.readObject();

					sumStates += balance;
					for (MoneyMessage msg : msgs) {
						sumChannels += msg.getAmount();
					}

					counter++;

					if (counter == processCount) {
						counter = 0;
						printToConsole();
					}

				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void printToConsole() {
		System.out.println("------------------------------");
		System.out.println("Total amount of money is: "
				+ (sumStates + sumChannels));
		System.out.println("Total amount of money in states is: " + sumStates);
		System.out.println("Total amount of money in channels is: "
				+ sumChannels);
		System.out.println("------------------------------");
		sumStates = 0;
		sumChannels = 0;
	}

	public String getAddress() {
		try {
			return java.net.InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return "localhost";
		}
	}

	public int getPort() {
		return listenPort;
	}

}