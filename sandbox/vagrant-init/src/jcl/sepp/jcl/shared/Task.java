package sepp.jcl.shared;

// an executable task
// client implements a particular task
// server loads it on-demand through rmi
public interface Task<A> {
	
	A execute();
}
