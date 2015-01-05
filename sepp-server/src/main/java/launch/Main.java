package launch;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import sepp.jcl.shared.Compute;
import sepp.jcl.shared.Kvs;
import sepp.rmi.server.ComputeImpl;

// for launching the sepp server
// inits compute engine
// inits key-val store for remote object management
// waits for clients to connect, store objects and invoke methods
// this implementation is intentionally kept simple (no tests, no syncing, objects are not ephemeral upon client interruption, ...) as it is a PoC
public class Main {

	private final static String INET_ADDR = "192.168.50.10";
	private final static String REMOTE_COMPUTE_OBJ = "engine001";
	private final static String REMOTE_KEY_VAL_STR = "kvs001";
	
	public static void main(String[] args) {
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}
		try {
			ComputeImpl engine  = new ComputeImpl();
			Compute engineStub = engine.genStub();
			Kvs kvsStub = engine.genKvsStub();
			
			Registry registry = LocateRegistry.getRegistry(INET_ADDR);
			registry.rebind(REMOTE_COMPUTE_OBJ, engineStub);
			registry.rebind(REMOTE_KEY_VAL_STR, kvsStub);
			
			System.out.println("...");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
