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
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("serial")
public class RecordedState implements Serializable {

	// put your implementation here

	private double balance;

	private ArrayList<Collector> collectors;
	private ArrayList<MoneyMessage> msgs;
	private Set<Integer> activeChannels;

	public RecordedState(ArrayList<Collector> collectors) {
		msgs = new ArrayList<MoneyMessage>();
		this.collectors = new ArrayList<Collector>();
		this.collectors = collectors;
		activeChannels = new HashSet<Integer>();

	}

	public void stopRecord(int id) {
		activeChannels.remove(id);

		if (activeChannels.size() == 0) {

			for (Collector c : collectors) {
				sendState(c);
			}
		}

	}

	public void saveState(double balance, Set<Integer> neighbors) {
		this.balance = balance;

		activeChannels.addAll(neighbors);

	}

	public boolean isActive() {
		return (activeChannels.size() != 0);
	}

	public boolean isRecording(int id) {
		return activeChannels.contains(id);
	}

	public void addMessage(MoneyMessage msg) {
		msgs.add(msg);
	}

	public void reset() {
		msgs.clear();
		activeChannels.clear();
		balance = 0;
	}

	private void sendState(Collector collector) {
		try {
			Socket socket = new Socket(collector.getAddress(),
					collector.getPort());

			ObjectOutputStream out = new ObjectOutputStream(
					socket.getOutputStream());
			out.writeObject(balance);
			out.writeObject(msgs);
			out.flush();
			out.close();
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}