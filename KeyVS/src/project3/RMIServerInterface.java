package project3;

import java.rmi.Remote;
import java.rmi.RemoteException;
/*
 * Server interface- clients can call the following methods
 */
public interface RMIServerInterface extends Remote{
	public String PUT(boolean go, String clientId, String key,String value) throws RemoteException;
	public String GET(String clientId,String key)throws RemoteException;
	public String DELETE(boolean go, String clientId, String key)throws RemoteException;
	public String ASK(String command, String key, String value) throws RemoteException;
}
