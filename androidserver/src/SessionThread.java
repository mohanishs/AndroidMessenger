/*
 * SessionThread.java
 * 
 * Version: 1.0
 * @author Mohanish Sawant
 * @author Amit Shroff
 * @author Sandesh Pardeshi
 * 
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

/**
 * The SessionThread class manages the current session with the said client.
 * Gets the requests from the clients and sends it to the server for further processing
 * 
 */

public class SessionThread implements Runnable {
	PrintWriter out = null;
	TupleSpaceServer tupleServer = null;
	BufferedReader in = null;

	/** This constructor is to construct the global variables. */
	public SessionThread(Socket socket, TupleSpaceServer tupleServer) {
		try {
			this.tupleServer = tupleServer;
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** method to run the thread. */
	public void run() {
		try {
			
			String getmessage;
			while ((getmessage = in.readLine()) != null) {
				tupleServer.handleRequest(getmessage, out);
			}
		} catch (SocketException e) {
			System.err.println("Client disconnected!!!");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
