package sepp.jcl;

import java.lang.reflect.InvocationTargetException;

// initiates rmi connection with sepp server
// dynamically loads the user app
public class SeppContainer {
	
	public SeppContainer(String yourApp, ClassLoader classLoader) {
		// init singleton compute
		SingletonCompute.init();
		
		Class<?> clazz = null;
	    try {
	    	clazz = classLoader.loadClass(yourApp);
	    } catch (ClassNotFoundException e) {
	        e.printStackTrace();
	    }
	    
		// invokes user's main method
		try {
			clazz.getMethod("main", String[].class).invoke(null, (Object)new String[0]);
//			Class.forName(yourApp).getMethod("main", String[].class).invoke(null, (Object)new String[0]);
		}
//		catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		}
		catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		catch (SecurityException e) {
			e.printStackTrace();
		}
	}
}
