package chandylamp;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choo.se Tools | Templates
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

        private final int pro_num;
	private final int id;
	private final int listenPort;
        private double widget;
	private double balance;
	private ArrayList<Process> neighbors;
	private ArrayList<Collector> collectors;
        private int timestamp1;
        private int[] timestamp2;
	private RecordedState rs;
	public Process(int id, double balance, double widget, int listenPort, int pro_num) {
		this.id = id;
		this.balance = balance;
                this.widget = widget;
		this.listenPort = listenPort;
                this.pro_num = pro_num;
                this.timestamp2 = new int[pro_num];
                this.timestamp1 = 0;
                for(int i = 0; i < pro_num; i++)
                {this.timestamp2[i] = 0;}
		neighbors = new ArrayList<Process>();
		collectors = new ArrayList<Collector>();
		rs = new RecordedState(collectors, id);
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
		rs.saveState(balance, widget, channels);

		for (Process n : neighbors) {
			sendMarker(n);
		}

	}

	public void transferMoney(double amount, double widgets, Process receiver) {
		if (!neighbors.contains(receiver))
			return;

		// put your implementation here

		try {
                        timestamp1++;
                        timestamp2[id-1]++;
			MoneyMessage msg = new MoneyMessage(amount, widgets, id, receiver.getID(), timestamp1, timestamp2);
			Socket server = new Socket(receiver.getAddress(),
					receiver.getPort());

			ObjectOutputStream out = new ObjectOutputStream(
					server.getOutputStream());

			out.writeObject(msg);
			out.flush();

			balance = balance - amount;
                        widget = widget - widgets;
			//System.out.println("Process " + id + " send " + amount
			//		+ "$ to process " + receiver.getID());
			//System.out.println("New balance of process " + id + " is "
			//		+ balance + "$");

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

        @Override
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
                                                if(msg.gettimestamp1() >= timestamp1+1){timestamp1 = msg.gettimestamp1()+1;}
                                                else{timestamp1++;}
                                                timestamp2[id-1]++;
                                                for(int i = 0; i<pro_num; i++){
                                                    if(i!=id-1 && timestamp2[i]<msg.gettimestamp2()[i]){timestamp2[i]++; }
                                                }
                                                balance = balance + msg.getAmount();
                                                widget = widget + msg.getWidget();
                                                if (rs.isActive() && rs.isRecording(msg.getSender())) {
                                                    rs.addMessage(msg);
                                                }
                                                //System.out.println("Process "+id+" receives msg from "+msg.getSender()+" "+msg.timestamp2[0]+""+msg.timestamp2[1]+""+msg.timestamp2[2]+""+msg.timestamp2[3]);

						//System.out.println(msg.getAmount() + " sender "
						//		+ msg.getSender() + " --> " + id);

						in.close();
						client.close();
					} else if (obj instanceof MarkerMessage) {

						in.close();
						client.close();

						MarkerMessage msg = (MarkerMessage) obj;
						//System.out.println(" rv MARKER " + msg.getSender()
						// 		+ "  -->  " + id);

						if (rs.isActive()) {
							if (rs.isRecording(msg.getSender())) {
								rs.stopRecord(msg.getSender(), timestamp1, timestamp2);
							}

						} else {
							rs.reset();
							Set<Integer> channels = new HashSet<Integer>();

							for (Process n : neighbors) {
								if (n.getID() != msg.getSender()) {
									channels.add(n.getID());
								}

							}
							rs.saveState(balance, widget, channels);

							for (Process n : neighbors) {
								sendMarker(n);
							}
                                                        if(pro_num == 2){rs.stopRecord(msg.getSender(), timestamp1, timestamp2);}
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
        
        public double getWidget(){
                return widget;
        }
        
	public void sendMarker(Process process) {

		try {
			//System.out.println(" sent MARKER from " + id + " --> "
			//		+ process.getID());
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