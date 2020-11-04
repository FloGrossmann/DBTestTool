package main;

import ui.MenuFrame;

public class Main {
	
	/** States after how many new database entries the next CRUD-Tests should be perforemd */
	public static final int TESTEVERY = 10000;
	/** How often the CRUD-Tests should be performed for each testpoint - the average will be stored for this testpoint */
	public static final int REPETITIONS = 100;
	/** Until how many entries in the database should be tested */
	public static final int MAXIMUM = 100000;

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		if (TESTEVERY < 100) {
			System.err.println("TESTEVERY must be greater than 100!");
			return;
		}
		System.out.println("TESTEVERY: " + TESTEVERY);
		System.out.println("REPETITIONS: " + REPETITIONS);
		System.out.println("MAXIMUM: " + MAXIMUM);
		System.out.println("This means, documents will be added until " + TESTEVERY + " is reached, then the CRUD Operations will be tested " + REPETITIONS 
				+ " times, storing the average as testpoint. \nIn total this will resume until " + MAXIMUM + " documents are in the database, which means there will be "
				+ (MAXIMUM / TESTEVERY) + " Testpoints recorded.");
		
		new MenuFrame();
	}

}
 