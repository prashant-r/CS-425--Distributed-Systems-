package optional.Paxos;
import java.rmi.*;

import optional.Utility.UtilityClasses;
import optional.Utility.UtilityClasses.*;
public interface PaxosInterface extends Remote {
	
	public PrepareReply Prepare(PrepareArgs prepareArgs) throws RemoteException, InterruptedException;
	public AcceptReply Accept(AcceptArgs acceptArgs) throws RemoteException, InterruptedException;
	public LearnReply Learn(LearnArgs LearnArgs) throws RemoteException,InterruptedException;	
}
