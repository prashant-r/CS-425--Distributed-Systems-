package chandylamp;

import java.util.Scanner;
import java.util.Random;
import java.util.ArrayList;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



/**
 *
 * @author Prashant
 */

public class Main implements Runnable {
        private static final int MAX_PRO_NUM = 10;
        private static final int MAX_SNA_NUM = 15;
        private static final int PORT = 4000;
        private static final int SEED = 4000;
        private int process_num;
	private Collector c;
        private Process[] process;
	//private Process  p1;
	//private Process  p2;
	//private Process  p3;
	//private Process  p4;

	public Main(int pro_num, int snap_num) {
		//p1 = new Process(1,  300, 4004);
		//p2 = new Process(2,  750, 4001);
		//p3 = new Process(3,  150, 4002);
		//p4 = new Process(4, 1050, 4003);
                process_num = pro_num;
                Random generator = new Random(SEED);
		c = new Collector(PORT, pro_num, snap_num);
                process = new Process[pro_num];
                for(int i = 0; i < pro_num; i++){
                    process[i] = new Process(i+1, generator.nextInt(10)*100+200, generator.nextInt(10)+10, PORT+i+1, pro_num);
                    //process[i] = new Process(i+1, 1000, PORT+i+1);
                    process[i].registerCollector(c);
                }
                for(int i = 0; i < pro_num; i++){
                    for(int index = 1; index < pro_num; index++){
                        process[i].addNeighbour(process[(i + index)%pro_num]);
                    }
                }
                
                
                for(int i = 0; i < pro_num; i++){
                    new Thread(process[i]).start();
                }
		//p1.registerCollector(c);
		//p2.registerCollector(c);
		//p3.registerCollector(c);
		//p4.registerCollector(c);

		//p1.addNeighbour(p2);
		//p1.addNeighbour(p3);
		//p2.addNeighbour(p1);
		//p2.addNeighbour(p4);
		//p3.addNeighbour(p1);
		//p3.addNeighbour(p4);
		//p4.addNeighbour(p2);
		//p4.addNeighbour(p3);

		//new Thread(p1).start();
		//new Thread(p2).start();
		//new Thread(p3).start();
		//new Thread(p4).start();
		new Thread(c).start();				

		new Thread(this).start();
	}

	public static void main(String[] args) {
                String pro_num = "";
                String snap_num = "";
                boolean flag = false;
                System.out.println("");
                System.out.println("Please type the number of processes you want:");
                Scanner scanIn = new Scanner(System.in);
                while(!flag){
                    pro_num = scanIn.nextLine();
                    int index = 0;
                    flag = true;
                    for(;index < pro_num.length();index++){
                        if(pro_num.charAt(index) < '0' || pro_num.charAt(index) > '9'){
                            flag = false;
                            System.out.println("Number is invalid.\n");
                        }
                    }
                    if(flag == true && Integer.parseInt(pro_num) > MAX_PRO_NUM){
                        flag = false;
                        System.out.println("Number is too big.\n");
                    }
                    if(flag == true && Integer.parseInt(pro_num) < 2){
                        flag = false;
                        System.out.println("Number is too small.\n");
                    }
                }
                flag = false;
                System.out.println("Please type the number of snapshots you want:");
                scanIn = new Scanner(System.in);
                while(!flag){
                    snap_num = scanIn.nextLine();
                    int index = 0;
                    flag = true;
                    for(;index < snap_num.length();index++){
                        if(snap_num.charAt(index) < '0' || snap_num.charAt(index) > '9'){
                            flag = false;
                            System.out.println("Number is invalid.\n");
                        }
                    }
                    if(flag == true && Integer.parseInt(snap_num) > MAX_SNA_NUM){
                        flag = false;
                        System.out.println("Number is too big.\n");
                    }
                    if(flag == true && Integer.parseInt(snap_num) < 1){
                        flag = false;
                        System.out.println("Number is too small.\n");
                    }
                }
                scanIn.close();
		new Main(Integer.parseInt(pro_num), Integer.parseInt(snap_num));
	}
	@Override
	public void run() {
		//Test scenario
		try {
			//p1.saveCurrentState();
			//p2.transferMoney(170, p1);
                        Random generator = new Random(SEED);
                        while(true){
                            if(c.restart_flag == 1){
                                int start_process = generator.nextInt(process_num);
                                process[start_process].saveCurrentState();
                                c.restart_flag = 0;
                            }
                            //while(c.restart_flag == -1){}
                            int money_transfer_process = generator.nextInt(process_num);
                            while(process[money_transfer_process].getBalance() <= 2 || process[money_transfer_process].getWidget() <= 10){money_transfer_process = generator.nextInt(process_num);}
                            //System.out.println("here");
                            int money_transfer_process_destination;
                            if(process_num > 2){money_transfer_process_destination = (money_transfer_process+generator.nextInt(process_num-2)+1)%process_num;}
                            else{money_transfer_process_destination = 1-money_transfer_process;}
                            int money_transfer = 0;
                            int widget_transfer = 0;
                            if((Math.abs(generator.nextInt()) & 0x1) == 0){
                                while(money_transfer == 0){money_transfer = generator.nextInt((int)process[money_transfer_process].getBalance());}//System.out.println("there");
                            }
                            else{
                                while(widget_transfer == 0){widget_transfer = generator.nextInt((int)process[money_transfer_process].getWidget());}//System.out.println("here");
                            }
                            process[money_transfer_process].transferMoney(money_transfer, widget_transfer, process[money_transfer_process_destination]);
                            Thread.sleep(100);
                            //money_transfer_process = generator.nextInt(process_num);
                            //money_transfer = Math.abs(generator.nextInt());
                            //while(money_transfer == 0 || money_transfer > process[money_transfer_process].getBalance()){money_transfer = Math.abs(generator.nextInt());}
                            //process[money_transfer_process].transferMoney(money_transfer, process[generator.nextInt(process_num)]);
                        }
			//p4.transferMoney(250, p2);
			//p4.transferMoney(50,  p3);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        }
}