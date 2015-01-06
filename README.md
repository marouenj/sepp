sepp
====

sepp is an attempt to implement a Java Class Library (JCL) with RPC functionalities so engrained into the methods themselves that invoking an object/method remotely is as seamless as invoking it from a local library.

In other words, the ultimate goal is to simplify the transition from local-method invocations to remote-method invocations by taking code that already runs on top of the standard JCL and run it on top of the sepp JCL.

Not much that has to be changed for the user; Just one line of code where the 'Main' class is injected to the sepp container. The latter takes care of the rest.

# Sample code
Consider the following piece of code. As it reads integers from a file, one at a time, it adds them to a list. When the loading is done, the list is sorted then saved back to a file.

	package launch;
	
	//  imports omitted
	
	public class Main {
		
		// attributes omitted
		
		public static void main(String[] args) {
			List<Integer> lst = FileIO.loadIntoList(INPUT);
			Collections.sort(lst);
			FileIO.saveToFile(lst, OUTPUT);
		}
	}

Utility methods that do the loading/saving:

	public class FileIO {
	
		public static List<Integer> loadIntoList(String fn) {
			try {
				...
				List<Integer> lst = new ArrayList<>();
				try {
					while (true) {
						lst.add(in.readInt());
					}
				} catch (EOFException e) {}
				...
			}
			...
		}
		
		public static void saveToFile(List<Integer> lst, String fn) {
			try {
				...
				for (int i = 0; i < lst.size(); i++)
					out.writeInt(lst.get(i));
				...
			}
			...
		}
	}

# Standard architecture

	## # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
	#                                                            #
	#       launch.Main (class with the static main method)      #
	#       - main entry to your app                             #
	#                                                            #
	# # # # # # # # # # # # # # # # # # # # # # # # # # # # # # ##
	#                                                            #
	#                            JVM                             #
	#                                                            #
	#       # # # # # # # # # # # # # # # # # # # # # # # # # # ##
	#       #                      #             #               #
	#       # standard JCL         # third-party # user library  #
	#       # (boot classpath,     # libraries   # (the rest of  #
	#       # extension classpath, #             # your app)     #
	#       # ...)                 #             #               #
	#       #                      #             #               #
	# # # # # # # # # # # # # # # # # # # # # # # # # # # # # # ##

# Taking the sample code remotely
If you want to do the heavy work of sorting remotely, there's a lot of options out there. Java RMI, Thrift are ways to do it. But then, you have to rewrite your code and change it significantly. Wouldn't it be better if your same code is ready for deployment on a different environment? This is where sepp comes in handy.

# sepp architecture

	# # # # # # # # # # # # # # #               ## # # # # # # # # # # # # # #               ## # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
	#                           #               #                            #               #                                                            #
	#       sepp server         # ============> #        rmi registry        # <============ #          sepp client                                       #
	#      (compute engine)     #               #                            #               #         (wraps your launch.Main class to relieve you       #
	#                           #               # # # # # # # # # # # # # # ##               #          of container init, RMI connection init, ...)      #
	#                           #                                                            #                                                            #
	# # # # # # # # # # # # # # #                                                            ## # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
	#                           #                                                            #                                                            #
	#            JVM            # <========================================================> #                            JVM                             #
	#                           #                                                            #                                                            #
	#       # # # # # # # # # # #                                                            #       # # # # # # # # # # # # # # # # # # # # # # # # # # ##
	#       #     #             #                                                            #       #                      #             #               #
	#       # JCL # sepp server #                                                            #       # sepp JCL             # third-party # user library  #
	#       #     #   library   #                                                            #       # (boot classpath,     # libraries   # (the rest of  #
	#       #     #             #                                                            #       # extension classpath, #             # your app)     #
	# # # # # # # # # # # # # # #                                                            #       # ...)                 #             #               #
	                                                                                         #       #                      #             #               #
	                                                                                         # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # ##                  

This is how it works briefly;
In the standard JCL, 'boolean ArrayList.add(E e)' adds an element to a locally-managed data structure (array). 'void Collections.sort(List<T> list)' sorts a locally-managed list.
In the sepp JCL, 'boolean ArrayList.add(E e)' requests the server to invoke the add method on an remotely-managed (at the server itself), already-instantiated object. 'void Collections.sort(List<T> list)' requests the server to sort a list (either sent along the request, or being already stored at the server).
The server should run on the same standard JCL you use to run you app the usual way.

# sepp server
sepp server is a customisation of the Compute engine in (https://docs.oracle.com/javase/tutorial/rmi/server.html). The Compute engine executes a task specified by the client then returns the result. In the original design, the task is explicitely defined. In sepp, one generic task is specified. Called Wildcard, all it does is sending the method signature, the argument types and values and the object to invoke.

# sepp client

# Extent
Implementing a JCL is not an easy undertaking. In fact it's very, very hard. GNU classpath is one example. An interesting open source project though it may be, it still lags behind in compliance compared to Oracle's implementation (it's currently 'mostly' 1.5 compliant).

sepp implementation requires tremendous work too, but it is easier in the sense that its implementation requires almost no business logic to code. It's mostly boilerplate code that fills the methods to be thrown to the remote side. Refection can help semi-automate the implementation process.

Besides, not every bit has to be changed. It's pointless (and not feasible in some cases) to invoke a Socket or a Swing object remotely.

What would be interesting to implement however is the Java Collections Framework. Taking it seamlessly on the remote side may fit some apps' needs that heavily rely on collections/maps manipulation and sharing...

# What's missing
This implementation is intentionally kept simple, as it is a PoC only. But besides the work of writing the JCL, problems such as concurrency and SPoF have to be addressed:
- Currently, when a client shuts down, the remote objects persist at the server. One way to avoid this is to introduce Zookeeper's ephemeral nodes to the architecture.
