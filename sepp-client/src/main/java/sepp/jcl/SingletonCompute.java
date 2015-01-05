package sepp.jcl;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import sepp.jcl.shared.Compute;
import sepp.jcl.shared.Task;

public class SingletonCompute {
	
	private final static String REMOTE_INET_ADDR = "192.168.50.10";
	private final static String REMOTE_COMPUTE_OBJ = "engine001";
	
	private static Registry registry;
	private static Compute sc;
	
	// prevents instantiation
	private SingletonCompute() {}
	
	public static void init() {
		if (registry == null) {
			try {
				registry = LocateRegistry.getRegistry(REMOTE_INET_ADDR);
				if (sc == null) {
					try {
						sc = (Compute) registry.lookup(REMOTE_COMPUTE_OBJ);
					} catch (NotBoundException e) {
						e.printStackTrace();
					}
				}
			} catch (RemoteException e) {
				e.printStackTrace();
			}		
		}
	}
	
	// used to execute a task remotely (http://docs.oracle.com/javase/tutorial/rmi/designing.html)
	// sepp.jcl.rmi.client.Wildcard (sepp's generic Task implementation) allows the invocation of methods on remote objects
	public static <A> A executeTask(Task<A> t) throws RemoteException {
		return sc.executeTask(t);
	}
}
