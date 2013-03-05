/*
 * ReceiverThread.java
 * 
 * Version: 1.0
 * 
 * @author Mohanish Sawant
 * @author Amit Shroff
 * @author Sandesh Pardeshi
 * 
 */
package com.androidmessenger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * The ReceiverThread class  is used to receive the message from the other client via the
 * server.
 * 
 */

public class ReceiverThread implements Runnable {
	private Socket socket;
	private BufferedReader in = null;
	private Handler handler;

	/** Constructor to construct the global variables. */
	public ReceiverThread(Socket server, Handler handler) {
		this.handler = handler;
		this.socket = server;
	}

	/**
	 * method to run the thread and check if there are any messages received
	 * from the server.
	 */
	public void run() {
		try {
			in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			String str;

			while ((str = in.readLine()) != null) {
				Log.e("Receiver", str);
				Log.e("Receiver", Integer.toString(Log.ERROR));
				Message _msg = handler
						.obtainMessage(AndroidMessenger.MSGTOPRINT);
				Bundle bundle = new Bundle();
				bundle.putString(AndroidMessenger.MESSAGE, str);
				_msg.setData(bundle);
				handler.sendMessage(_msg);
			}
		} catch (IOException e) {
			Log.e("Receiver", "IOException");
		}
	}
}
