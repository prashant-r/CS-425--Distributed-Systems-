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
public class MarkerMessage implements Serializable{
	int sender;

	public MarkerMessage(int sender){
		this.sender = sender;
	}

	public int getSender(){
		return sender;
	}
}