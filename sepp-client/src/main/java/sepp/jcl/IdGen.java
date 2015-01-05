package sepp.jcl;

// wraps a method that generates ids incrementally
public class IdGen {
	
	private static int id = 0;
	
	// constructors and factory methods of objects to be invoked remotely on sepp server request an object id through this method.
	// the id is then passed to sepp server along with the object
	public synchronized static int getId() {
		return id++;
	}
}
