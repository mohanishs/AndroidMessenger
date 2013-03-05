/*
 * Communication.java
 * 
 * Version: 1.0
 * @author Mohanish Sawant
 * @author Amit Shroff
 * @author Sandesh Pardeshi
 * 
 */

package com.androidmessenger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * 
 * The Communication sets the operation field of the tuple to read write or add
 * and converts the object into XML format
 * 
 * 
 */

public class Communication {

	Socket socket;
	BufferedReader in;
	PrintWriter out;

	/** Constructor to construct the global variables. */
	public Communication(Socket socket) {
		this.socket = socket;
		try {
			in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** To get the connection message. */
	public MessageFormat getConnectionMessage() {
		MessageFormat message = new MessageFormat();
		return message;
	}

	/** To set the message to be written on the tuple space */
	public void writeMessage(MessageFormat writeRequest) {
		writeRequest.setOperation("write");
		sendtoServer(writeRequest);
	}
	/** To add a friend */
	public void addFriend(MessageFormat addFriend) {
		addFriend.setOperation("add");
		sendtoServer(addFriend);
	}

	/** To set the message to be taken from the tuple space */
	public void takeMessage(MessageFormat takeRequest) {
		takeRequest.setOperation("take");
		sendtoServer(takeRequest);
	}

	/**
	 * In sendtoServer method the tuple object is converted from object format to XML format then
	 * to a single string format which is  sent to the server
	 */
	public void sendtoServer(MessageFormat request) {
		XStream xstream = new XStream(new DomDriver());
		xstream.alias("Message", MessageFormat.class);

		String message = xstream.toXML(request);

		String finalmessage = "";
		String[] string = message.split("\n");
		for (int i = 0; i < string.length; i++) {
			finalmessage = finalmessage + string[i];
		}

		out.println(finalmessage);
	}

}
