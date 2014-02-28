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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Collector implements Runnable {
        public int restart_flag ;
        public int snapshots;
        public int snap_num;
	private int listenPort;
	private int processCount; // for checking if all processes send their states
	// for the output
        ArrayList<MoneyMessage> message;
        double balance[];
        double widgets[];
        int[] timestamp1;
        int[][] timestamp2;
        File logfile;
        BufferedWriter writer;
	//double sumStates;
	//double sumChannels;

	public Collector(int listenPort, int processCount, int snap_num) {
		this.listenPort = listenPort;
		this.processCount = processCount;
                restart_flag = 1;
                balance = new double[processCount];
                widgets = new double[processCount];
                timestamp1 = new int[processCount];
                timestamp2 = new int[processCount][processCount];
                message = new ArrayList<MoneyMessage>();
                snapshots = 0;
                this.snap_num = snap_num;
                this.logfile = new File("log.txt");
                try{
                    writer = new BufferedWriter(new FileWriter(logfile));
                }catch(Exception e){e.printStackTrace();}
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
                                        int pro_id = (int) in.readObject();
					balance[pro_id-1] = (Double) in.readObject();
                                        widgets[pro_id-1] = (Double) in.readObject();
                                        timestamp1[pro_id-1] = (int) in.readObject();
                                        timestamp2[pro_id-1] = (int[]) in.readObject();
					ArrayList<MoneyMessage> msgstemp = (ArrayList<MoneyMessage>) in
							.readObject();
                                        for(int i = 0; i<msgstemp.size(); i++){
                                            message.add(msgstemp.get(i));
                                        }
					//sumStates += balance;
					//for (MoneyMessage msg : message) {
					//	sumChannels += msg.getAmount();
					//}

					counter++;
					if (counter == processCount) {
						counter = 0;
						printToConsole();
                                                restart_flag = 1;
                                            if(snap_num == snapshots) restart_flag = -1;
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
                snapshots++;
                try{
		writer.write("------------------------------");
                System.out.printf("------------------------------\n");
                writer.newLine();
		//System.out.println("Total amount of money is: "
		//		+ (sumStates + sumChannels));
                for(int j = 0; j<processCount; j++){
                    writer.write("snapshot ");writer.write(Integer.toString(snapshots));writer.write(" process ");writer.write(Integer.toString(j+1));writer.write(" logical ");writer.write(Integer.toString(timestamp1[j]));
                    writer.write(" vector ");
                    System.out.printf("snapshot %d process %d logical %d vector ", snapshots, j+1, timestamp1[j]);
                    for(int i = 0; i<processCount; i++){
                        writer.write(Integer.toString(timestamp2[j][i]));writer.write(" ");
                        System.out.printf("%d ",timestamp2[j][i]);
                    }
                    writer.write("Money ");writer.write(Integer.toString((int)balance[j]));writer.write(" $");
                    writer.write(" Widgets ");writer.write(Integer.toString((int)widgets[j]));
                    writer.newLine();
                    System.out.printf("Money %f $ Widgets %f", balance[j], widgets[j]);
                    for(int i = 0; i<message.size(); i++){
                        if(message.get(i).getReceiver() == j+1){
                            System.out.printf("Message from %d to %d with %f $ and %f widgets \n", message.get(i).getSender(), message.get(i).getReceiver(), message.get(i).amount, message.get(i).widgetamount);
                            writer.write("Message from ");writer.write(Integer.toString(message.get(i).getSender()));writer.write(" to ");writer.write(Integer.toString(message.get(i).getReceiver()));writer.write(" with ");writer.write(Integer.toString((int)message.get(i).getAmount()));writer.write(" $ and widgets ");writer.write(Integer.toString((int)message.get(i).getWidget()));writer.newLine();
                        }
                    }
                    System.out.printf("\n");
                    writer.newLine();
                }
                message.clear();
                if(snapshots == snap_num){writer.close();}
                }catch(Exception e){e.printStackTrace();}
		//System.out.println("Total amount of money in states is: " + sumStates);
		//System.out.println("Total amount of money in channels is: "
		//		+ sumChannels);
		//System.out.println("------------------------------");
		//sumStates = 0;
		//sumChannels = 0;
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