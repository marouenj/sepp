package sepp.jcl.shared;

import java.rmi.Remote;
import java.rmi.RemoteException;

// for executing a particular task remotely
// server manages a Compute impl
// client obtains a stub through rmi
public interface Compute extends Remote {
	
	<A> A executeTask(Task<A> t) throws RemoteException;
}
