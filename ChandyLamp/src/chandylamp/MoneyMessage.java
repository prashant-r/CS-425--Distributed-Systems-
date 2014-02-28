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
import java.io.Serializable;

@SuppressWarnings("serial")
public class MoneyMessage implements Serializable{

        double widgetamount;
	double amount;
	int sender;
        int receiver;
        int timestamp1;
        int[] timestamp2;
	public MoneyMessage(double amount, double widgetamount, int sender, int receiver, int timestamp1, int[] timestamp2){
		this.amount = amount;
                this.amount = amount;
                this.widgetamount = widgetamount;
                this.timestamp2 = timestamp2;
		this.sender = sender;
                this.receiver = receiver;
	}

	public double getAmount() {
		return amount;
	}
        
        public double getWidget(){
            return widgetamount;
        }
        
	public int getSender() {
		return sender;
	}
        
        public int getReceiver(){
                return receiver;
        }
        
        public int gettimestamp1(){
            return timestamp1;
        }
        
        public int[] gettimestamp2(){
            return timestamp2;
        }
}
