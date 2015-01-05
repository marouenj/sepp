package sepp.jcl.rmi.client;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import sepp.jcl.shared.Kvs;
import sepp.jcl.shared.Task;

// Wildcard is a generic implementation of Task (http://docs.oracle.com/javase/tutorial/rmi/designing.html).
// it's generic in that it doesn't implement a specific task (like this one for computing Pi: http://docs.oracle.com/javase/tutorial/displayCode.html?code=http://docs.oracle.com/javase/tutorial/rmi/examples/client/Pi.java).
// you can regard Wildcard as a task that requests a remote object (on sepp server) to invoke a certain method
public class Wildcard<A> implements Task<A>, Serializable {
	
	private static final long serialVersionUID = 4561387821773794885L;

	private final static String REMOTE_INET_ADDR = "192.168.50.10";
	private final static String REMOTE_KEY_VAL_STR = "kvs001";
	
	private int objId;
	private String clazz;
	private String method;
	Class<?>[] paramTypes;
	
	private Object[] args;
	private int nxtArgIdx;
	
	// when instantiating a Wildcard task, one should provide the unique id of the object to be invoked remotely hereafter,
	// the name of the class from which the object has been instantiated,
	// the method to be invoked,
	// the parameter types
	public Wildcard(int objKey, String clazz, String method, Class<?>[] paramTypes) {
		this.objId = objKey;
		this.clazz = clazz;
		this.method = method;
		this.paramTypes = paramTypes;
		
		this.args = new Object[paramTypes.length];
		this.nxtArgIdx = -1;
	}
	
	// arguments of the remote method are input one by one through this method,
	// input order, as determined by the method signature, should be preserved
	public Wildcard<A> setArg(Object o) {
		if (++nxtArgIdx < args.length)
			args[nxtArgIdx] = o;
		return this;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public A execute() {
		Object rez = null;
		try {
			Registry registry = LocateRegistry.getRegistry(REMOTE_INET_ADDR);
			Kvs kvs = (Kvs) registry.lookup(REMOTE_KEY_VAL_STR);
	    	Object obj;
	    	if (objId == -1) { // static call
	    		obj = null;
	    	} else {
	    		obj = kvs.get(objId);
	    		if (obj == null) { // not exist yet
	    			obj = Class.forName(clazz).getConstructor().newInstance();
	    		}
	    	}
    		System.out.println("invoking method " + method + " for object " + obj + " with params " + args);
	    	rez = Class.forName(clazz).getMethod(method, paramTypes).invoke(obj, args);
	    	System.out.println(obj.toString());
	    	kvs.add(objId, obj);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
	    } catch (NotBoundException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return (A) rez;
	}
}
