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
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Process implements Runnable {

	private int id;
	private double balance;
	private int listenPort;
	private ArrayList<Process> neighbors;
	private ArrayList<Collector> collectors;

	private RecordedState rs;

	public Process(int id, double balance, int listenPort) {
		this.id = id;
		this.balance = balance;
		this.listenPort = listenPort;
		neighbors = new ArrayList<Process>();
		collectors = new ArrayList<Collector>();
		rs = new RecordedState(collectors);
	}

	public void addNeighbour(Process p) {
		if (!neighbors.contains(p))
			neighbors.add(p);
	}

	public void registerCollector(Collector c) {
		if (!collectors.contains(c))
			collectors.add(c);
	}

	public void saveCurrentState() {

		// put your implementation here

		Set<Integer> channels = new HashSet<Integer>();

		for (Process n : neighbors) {

			channels.add(n.getID());

		}

		rs.reset();
		rs.saveState(balance, channels);

		for (Process n : neighbors) {
			sendMarker(n);
		}

	}

	public void transferMoney(double amount, Process receiver) {
		if (!neighbors.contains(receiver))
			return;

		// put your implementation here

		try {
			MoneyMessage msg = new MoneyMessage(amount, id);
			Socket server = new Socket(receiver.getAddress(),
					receiver.getPort());

			ObjectOutputStream out = new ObjectOutputStream(
					server.getOutputStream());

			out.writeObject(msg);
			out.flush();

			balance = balance - amount;

			System.out.println("Process " + id + " send " + amount
					+ "$ to process " + receiver.getID());
			System.out.println("New balance of process " + id + " is "
					+ balance + "$");

			out.close();

			server.close();

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void run() {

		ServerSocket server;
		try {
			server = new ServerSocket(listenPort);

			while (true) {
				try {
					Socket client = server.accept();

					Thread.sleep(100); // simulate delay - do not change

					// put your implementation for part a), b) and c) here
					// you may call other methods that you created

					ObjectInputStream in = new ObjectInputStream(
							client.getInputStream());

					Object obj = in.readObject();
					if (obj instanceof MoneyMessage) {
						MoneyMessage msg = (MoneyMessage) obj;

						balance = balance + msg.getAmount();

						if (rs.isActive() && rs.isRecording(msg.getSender())) {
							rs.addMessage(msg);
						}

						System.out.println(msg.getAmount() + " sender "
								+ msg.getSender() + " --> " + id);

						in.close();
						client.close();
					} else if (obj instanceof MarkerMessage) {

						in.close();
						client.close();

						MarkerMessage msg = (MarkerMessage) obj;
						System.out.println(" rv MARKER " + msg.getSender()
								+ "  -->  " + id);

						if (rs.isActive()) {
							if (rs.isRecording(msg.getSender())) {
								rs.stopRecord(msg.getSender());
							}

						} else {
							rs.reset();
							Set<Integer> channels = new HashSet<Integer>();

							for (Process n : neighbors) {
								if (n.getID() != msg.getSender()) {
									channels.add(n.getID());
								}

							}
							rs.saveState(balance, channels);

							for (Process n : neighbors) {
								sendMarker(n);
							}
						}
					} else {
						// out.close();
						in.close();
						client.close();
						System.out.println("unknown message type");
					}

				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

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

	public int getID() {
		return id;
	}

	public double getBalance() {
		return balance;
	}

	public void sendMarker(Process process) {

		try {

			System.out.println(" sent MARKER from " + id + " --> "
					+ process.getID());
			Socket socket = new Socket(process.getAddress(), process.getPort());
			ObjectOutputStream out = new ObjectOutputStream(
					socket.getOutputStream());

			out.writeObject(new MarkerMessage(id));
			out.flush();
			out.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}