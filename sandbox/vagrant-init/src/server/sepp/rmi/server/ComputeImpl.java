package sepp.rmi.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import sepp.jcl.shared.Compute;
import sepp.jcl.shared.Kvs;
import sepp.jcl.shared.Task;

public class ComputeImpl implements Compute {
	
	private KvsImpl kvs;
	
	public ComputeImpl() {
		super();
	}
	
	public Compute genStub() {
		Compute stub = null;
		try {
			stub = (Compute) UnicastRemoteObject.exportObject(this, 0);
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		return stub;
	}
	
	public Kvs genKvsStub() {
		kvs = new KvsImpl();
		Kvs kvsStub = null;
		try {
			kvsStub = (Kvs) UnicastRemoteObject.exportObject(kvs, 0);
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		return kvsStub;
	}
	
	@Override
	public <A> A executeTask(Task<A> t) throws RemoteException {
		return t.execute();
	}
}
