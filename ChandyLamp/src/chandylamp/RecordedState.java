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
        private int pro_id;
	private double balance;
        private double widgets;
	private ArrayList<Collector> collectors;
	private ArrayList<MoneyMessage> msgs;
	private Set<Integer> activeChannels;

	public RecordedState(ArrayList<Collector> collectors, int pro_id) {
		msgs = new ArrayList<MoneyMessage>();
		this.collectors = new ArrayList<Collector>();
		this.collectors = collectors;
		activeChannels = new HashSet<Integer>();
                this.pro_id = pro_id;
	}

	public void stopRecord(int id, int timestamp1, int[] timestamp2) {
		activeChannels.remove(id);

		if (activeChannels.size() == 0) {

			for (Collector c : collectors) {
				sendState(c, timestamp1, timestamp2);
			}
		}

	}

	public void saveState(double balance, double widgets, Set<Integer> neighbors) {
		this.balance = balance;
                this.widgets = widgets;
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

	private void sendState(Collector collector, int timestamp1, int[] timestamp2) {
		try {
			Socket socket = new Socket(collector.getAddress(),
					collector.getPort());

			ObjectOutputStream out = new ObjectOutputStream(
					socket.getOutputStream());
                        out.writeObject(pro_id);
			out.writeObject(balance);
                        out.writeObject(widgets);
                        out.writeObject(timestamp1);
                        out.writeObject(timestamp2);
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