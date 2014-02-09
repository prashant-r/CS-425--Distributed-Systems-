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

	double amount;
	int sender;

	public MoneyMessage(double amount, int sender){
		this.amount = amount;
		this.sender = sender;
	}

	public double getAmount() {
		return amount;
	}

	public int getSender() {
		return sender;
	}
}
