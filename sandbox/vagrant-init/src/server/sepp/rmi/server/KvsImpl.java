package sepp.rmi.server;

import java.util.HashMap;

import sepp.jcl.shared.Kvs;

public class KvsImpl implements Kvs {
	
	HashMap<Integer, Object> kvs;
	
	public KvsImpl() {
		kvs = new HashMap<>();
	}
	
	public void add(int key, Object val) {
		kvs.put(key, val);
	}

	public Object get(int key) {
		return kvs.get(key);
	}
}
