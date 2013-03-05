/*
 * MessageFormat.java
 * 
 * Version: 1.0
 * @author Mohanish Sawant
 * @author Amit Shroff
 * @author Sandesh Pardeshi
 * 
 */
package com.androidmessenger;
/**
 * The MessageFormat class defines the XML format
 * 
 * 
 */

public class MessageFormat {

	private String sender;
	private String receiver;
	private String operation;
	private String message;

	/** To set the sender */
	public void setSender(String sender) {
		this.sender = sender;
	}

	/** To get the sender */
	public String getSender() {
		return sender;
	}

	/** to set the receiver */
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	/** to get the receiver */
	public String getReceiver() {
		return receiver;
	}

	/** to set the operation */
	public void setOperation(String operation) {
		this.operation = operation;
	}

	/** to get the operation */
	public String getOperation() {
		return operation;
	}

	/** to set the message */
	public void setMessage(String message) {
		this.message = message;
	}

	/** to get the message */
	public String getMessage() {
		return message;
	}
}
