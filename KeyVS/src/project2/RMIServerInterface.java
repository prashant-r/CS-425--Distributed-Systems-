package project2;

import java.rmi.Remote;
import java.rmi.RemoteException;
/*
 * Server interface- clients can call the following methods
 */
public interface RMIServerInterface extends Remote{
	public String PUT(String clientId, String key,String value) throws RemoteException;
	public String GET(String clientId,String key)throws RemoteException;
	public String DELETE(String clientId, String key)throws RemoteException;
}
