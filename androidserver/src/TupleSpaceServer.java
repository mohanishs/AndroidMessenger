/*
 * TupleSpaceServer.java
 * 
 * Version: 1.0
 * @author Mohanish Sawant
 * @author Amit Shroff
 * @author Sandesh Pardeshi
 * 
 */
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * Server class Runs on the WiFi or the lan network
 * 
 * 
 */

public class TupleSpaceServer {

	private TupleSpace tupleSpace;

	/** Constructor to construct the global variables. */
	public TupleSpaceServer() {
		tupleSpace = TupleSpace.getTupleSpace();
	}

	/**
	 * To process the request received from the client
	 * 
	 **/
	public void handleRequest(String message, PrintWriter out) {
		MessageFormat messageObject;
		messageObject = convertMsgFormat(message);
		String operation = messageObject.getOperation();
		checkOperation(operation, messageObject, message, out);
	}

	/** Checks the Operation write/add/take and processes accordingly **/
	private void checkOperation(String operation, MessageFormat messageObject,
			String message, PrintWriter out) {

		if (operation.equals("write")) {
			Object[] messageTuple = new Object[] { messageObject.getSender(),
					messageObject.getReceiver(), messageObject.getMessage() };
			tupleSpace.write(messageTuple);
		}

		if (operation.equals("add")) {
			Object[] messageTuple = new Object[] { messageObject.getSender(),
					messageObject.getReceiver(), messageObject.getMessage() };
			tupleSpace.add(messageTuple);

		}

		if (operation.equals("take")) {
			Object[] messageTemplate = new Object[] {
					messageObject.getReceiver(), messageObject.getSender(),
					null };
			Object[] messageTuple = tupleSpace.take(messageTemplate);
			if ((messageTuple != null)) {
				messageObject.setMessage((String) messageTuple[2]);
				XStream xstream = new XStream(new DomDriver());
				message = xstream.toXML(messageObject);
				out.println((String) messageTuple[0] + ":"
						+ (String) messageTuple[2]);
			}
		}
	}

	/** Converts the messages from XML to Object **/
	private MessageFormat convertMsgFormat(String message) {

		XStream xstream = new XStream(new DomDriver());
		xstream.alias("Message", MessageFormat.class);
		MessageFormat messageObject = (MessageFormat) xstream.fromXML(message);
		return messageObject;
	}

	/**
	 * The main method to create the server and manage the clients as they
	 * connect in. For every client a session thread is created
	 * 
	 * */
	public static void main(String[] args) {
		try {
			TupleSpaceServer tupleServer = new TupleSpaceServer();

			ServerSocket server = null;
			Socket client = null;
			String serverIpAdd = null;

			int portAdd;
			serverIpAdd = InetAddress.getLocalHost().toString();
			String[] serverAdd = serverIpAdd.split("/");
			serverIpAdd = serverAdd[1];
			portAdd = 6026;
			System.out.println("Server is now online");
			System.out.println("Server IP Address: " + serverIpAdd);
			System.out.println("Port Address: " + portAdd);
			server = new ServerSocket(portAdd);

			while (true) {
				client = server.accept();
				System.out.println("Client Connected Address: "
						+ client.getInetAddress());
				new Thread(new SessionThread(client, tupleServer)).start();
			}
		} catch (IOException e) {
			System.err.println(e);
		}

	}
}