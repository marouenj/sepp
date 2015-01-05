package sepp.jcl.shared;

import java.io.Serializable;

public class Wrapper implements Serializable {
	
	private static final long serialVersionUID = 109703689479597939L;
	
	private int key;
	
	public Wrapper(int key) {
		this.key = key;
	}
	
	public int getKey() {
		return key;
	}
}
