package sepp.jcl.shared;

import java.rmi.Remote;
import java.rmi.RemoteException;

// a key-value store (kvs) for managing objects remotely
// server manages a Kvs impl
// client obtains a stub through rmi
public interface Kvs extends Remote {
	
	void add(int key, Object val) throws RemoteException;

	Object get(int key) throws RemoteException;
}
