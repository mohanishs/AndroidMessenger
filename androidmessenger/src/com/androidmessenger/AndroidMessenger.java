/*
 * AndroidMessage.java
 * 
 * Version: 1.0
 * 
 * @author Mohanish Sawant
 * @author Amit Shroff
 * @author Sandesh Pardeshi
 * 
 */

package com.androidmessenger;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * This program connects the Android client to the server. then it manages the
 * screen and the input given message and the message received from the other
 * client.
 * 
 * 
 */

public class AndroidMessenger extends Activity implements OnClickListener {
	// String buffer for outgoing messages
	private StringBuffer buffer = new StringBuffer("");
	// Array Adapter to print data on the screen...
	private ArrayAdapter<String> arrayAdapter;
	// Layout Views
	private ListView conversationWindow;
	private EditText outMsgEditText;
	private Button button, addfriends;
	private Thread receiverThread, reqThread;
	private EditText m_addItemText;
	private ArrayAdapter<CharSequence> m_adapterForSpinner;
	// Socket
	Socket socket = null;
	// HardCodes for current clients name...
	public static String senderName = "Alice";
	// request client will be set by the current client through GUI
	public static String receiverName = "";
	public ArrayAdapter<CharSequence> spinadapter;
	private TextView welcomeMsg;
	// Message Communication...
	private Communication commMessage;
	public static final int MSGTOPRINT = 1;
	public static final String MESSAGE = "message";
	private Spinner spinner;
	private Boolean flag = false;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);
		chatScreenSetup();

		String serverAddress = "192.168.1.149";
		connectServer(serverAddress, 6026);
		commMessage = new Communication(socket);
		addfriends = (Button) findViewById(R.id.add_friends);
		addfriends.setOnClickListener(this);

	}

	/** This method sets up the chat screen from input and output. */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	void chatScreenSetup() {
		// welcome message
		welcomeMsg = (TextView) findViewById(R.id.welcomemsg);
		welcomeMsg.setText("Hello  " + senderName);
		arrayAdapter = new ArrayAdapter<String>(this, R.layout.message);
		m_addItemText = (EditText) findViewById(R.id.addfriendstext);

		spinner = ((Spinner) findViewById(R.id.spinner));

		// array adapter to add friend
		m_adapterForSpinner = new ArrayAdapter(this,
				android.R.layout.simple_spinner_item);
		m_adapterForSpinner
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(m_adapterForSpinner);
		m_adapterForSpinner.add("");
		spinner.setOnItemSelectedListener(new MyOnItemSelectedListener());

		conversationWindow = (ListView) findViewById(R.id.in);
		conversationWindow.setAdapter(arrayAdapter);

		// Initialize the compose field with a listener for the return key
		outMsgEditText = (EditText) findViewById(R.id.edit_text_out);
		outMsgEditText.setOnEditorActionListener(msgListener);

		// Initialize the send button with a listener that for click events
		button = (Button) findViewById(R.id.button_send);
		button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// Send a message using content of the edit text widget
				TextView view = (TextView) findViewById(R.id.edit_text_out);
				String message = view.getText().toString();
				sendMessage(message);
			}
		});

		arrayAdapter.clear();
	}

	public class MyOnItemSelectedListener implements OnItemSelectedListener {

		@Override
		/**Starts the Receiver and the Request Threads on selecting the friends from friend list*/
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub

			String Text = spinner.getSelectedItem().toString();
			receiverName = Text;

			if (flag == true) {

				receiverThread.stop();
				reqThread.stop();
				flag = false;
			}
			flag = true;
			receiverThread = new Thread(new ReceiverThread(socket, handler));
			receiverThread.start();
			reqThread = new Thread(new RequestThread(socket));
			reqThread.start();

		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub

		}
	}

	/**
	 * The action listener for the EditText widget, to listen for the return key
	 */
	private TextView.OnEditorActionListener msgListener = new TextView.OnEditorActionListener() {
		/**
		 * If the action is a key-up event on the return key, send the message
		 */
		public boolean onEditorAction(TextView view, int actionId,
				KeyEvent event) {

			if (event.getAction() == KeyEvent.ACTION_UP) {
				String message = view.getText().toString();
				sendMessage(message);
			}

			return true;
		}
	};

	/**
	 * This method is used to pass on the out-message to the sender method and
	 * print it on the screen and clear the textBox.
	 */
	private void sendMessage(String message) {
		if (message.length() > 0) {
			sender(message);
			arrayAdapter.add(senderName + ": " + message);
			// to clean the window...
			buffer.setLength(0);
			outMsgEditText.setText("");
		}
	}

	/** This method connects the client to the server. */
	void connectServer(String serverAddress, int serverPort) {
		try {
			socket = new Socket(serverAddress, serverPort);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * The sender class sets the sender,receiver and the message field of the
	 * object and sends the object to Communication class for further processing
	 * 
	 */

	void sender(String msg) {
		MessageFormat obj = new MessageFormat();
		obj.setSender(senderName);
		obj.setReceiver(receiverName);
		obj.setMessage(msg);

		commMessage.writeMessage(obj);
	}

	/**
	 * This is a handler to handle the messages coming in from the other client
	 * via the Receiver.
	 */
	private final Handler handler = new Handler() {
		public void handleMessage(Message message) {

			switch (message.what) {
			case MSGTOPRINT:
				String str = message.getData().getString(MESSAGE);
				arrayAdapter.add(str);
				break;
			}
		}
	};

	@Override
	public void onClick(View v) {
		addNewSpinnerItem();
	}

	/** Adds new spinner item to the User Interface */
	private void addNewSpinnerItem() {
		CharSequence addFriendText = m_addItemText.getText();
		String addfriend = m_addItemText.getText().toString();
		MessageFormat obj = new MessageFormat();
		obj.setSender(senderName);
		// obj.setReceiver(requestClient);
		obj.setMessage(addfriend);
		obj.setOperation("add");

		if (addfriend.equals("")) {
			welcomeMsg.setText("Hello  " + senderName + " Insert Friends name");
		} else {
			commMessage.addFriend(obj);
			welcomeMsg.setText("Hello  " + senderName);
			m_adapterForSpinner.add(addFriendText);
			m_addItemText.setText("");
		}
	}

}