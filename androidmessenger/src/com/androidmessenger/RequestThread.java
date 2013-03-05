/*
 * RequestThread.java
 * 
 * Version: 1.0
 * 
 * @author Mohanish Sawant
 * @author Amit Shroff
 * @author Sandesh Pardeshi
 * 
 */
package com.androidmessenger;

import java.net.Socket;

/**
 * 
 * The RequestThread class requests the server to check whether there are any
 * tuples meant for this client to be taken from the tuple space.
 * 
 */

public class RequestThread implements Runnable {
	Socket socket;

	/** Constructor to set the global variables. */
	RequestThread(Socket socket) {
		this.socket = socket;
	}

	/**
	 * Method to run the thread. To request the server to check for any tuples
	 * for this client.
	 */
	public void run() {
		Communication commMessage = new Communication(socket);
		MessageFormat msgobj = new MessageFormat();
		msgobj.setSender(AndroidMessenger.senderName);
		msgobj.setReceiver(AndroidMessenger.receiverName);

		while (true) {
			commMessage.takeMessage(msgobj);
		}

	}
}
