/*
 * TupleSpaceSessionThread.java
 * 
 * Version: 1.0
 * @author Mohanish Sawant
 * @author Amit Shroff
 * @author Sandesh Pardeshi
 * 
 */

import java.util.ArrayList;

/**
 * 
 * The Tuple Space class creates and maintains a tuple space on the server. It
 * also performs the basic Read, Write & Take operations on the tuple space.
 * 
 * 
 */

public class TupleSpace {

	ArrayList<Object[]> tuples;
	private static TupleSpace tupleSpace;

	/** constructor to instantiate the tuple space (tuples) */
	private TupleSpace() {
		tuples = new ArrayList<Object[]>();
	}

	/** Method to get the tupleSpace object. */
	public static TupleSpace getTupleSpace() {
		if (tupleSpace == null) {
			tupleSpace = new TupleSpace();
		}
		return tupleSpace;
	}

	/** Method to write the tuple on the tuple space. */
	public void write(Object[] Tuple) {
		tuples.add(Tuple);
		System.out.println("" + Tuple[0] + " has written a message ("
				+ Tuple[2] + ") for " + Tuple[1] + ": On The Tuple Space");

	}

	/**
	 * Method to display the message if a friend has been added to a clients
	 * friends list
	 */
	public void add(Object[] Tuple) {

		System.out.println(Tuple[0] + " has added " + Tuple[2]
				+ " in the friends list");
	}

	/** Method to take the tuple from the tuple space. */
	public Object[] take(Object[] TupleTemplate) {
		if (tuples.size() > 0) {
			Object[] Tuple = null;
			int index = tempalteMatch(TupleTemplate);
			if (index > -1) {
				Tuple = tuples.get(index);
				tuples.remove(index);
				System.out.println("" + TupleTemplate[1]
						+ " has taken the message (" + Tuple[2]
						+ ") From tuple space ,Message Sent by "
						+ TupleTemplate[0]);

				return Tuple;
			}
		}
		return null;
	}

	/** Method to match the tuples with the tuple's template. */
	public int tempalteMatch(Object[] template) {
		boolean found = false;
		int index = -1;

		if (tuples.size() > 0) {
			for (int p = 0; p < tuples.size(); p++) {
				Object[] candidateTuple = tuples.get(p);

				for (int q = 0; q < template.length; q++) {
					if (matchObj(candidateTuple[q], template[q]))
						found = true;
					else {
						found = false;
						break;
					}
				}

				if (found) {
					index = p;
					System.out.println("Tuple Found at Index:" + p);
					break;
				}
			}
		}
		return index;
	}

	/** Method to match two objects */
	public boolean matchObj(Object o1, Object o2) {
		if (o1 == null || o2 == null)
			return true;
		else if (o1.equals(o2))
			return true;
		else
			return false;
	}
}
