package measure;

import ui.MenuPanel;

public class MeasureThread implements Runnable {

	String mongoDBConnectionString;
	String mariaDBConnectionString;
	String mariaDBUsername;
	String mariaDBPassword;
	MenuPanel menuPanel;
	private Thread t;
	
	public MeasureThread(String mongoDBConnectionString, String mariaDBConnectionString,
			String mariaDBUsername, String mariaDBPassword, MenuPanel menuPanel) {
		this.mongoDBConnectionString = mongoDBConnectionString;
		this.mariaDBConnectionString = mariaDBConnectionString;
		this.mariaDBPassword = mariaDBPassword;
		this.mariaDBUsername = mariaDBUsername;
		this.menuPanel = menuPanel;
	}
	
	@Override
	public void run() {
		
		DBTest dbTest = new DBTest();
		try {
			dbTest.startTest(mongoDBConnectionString, mariaDBConnectionString, mariaDBUsername, mariaDBPassword, menuPanel.getProgressPanel());
			menuPanel.startTestButton.setEnabled(true);
			menuPanel.startTestButton.setText("Start testing... again ? Are you sure?");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void start () {
	      System.out.println("Starting MeasureThread");
	      if (t == null) {
	         t = new Thread (this, "MeasureThread");
	         t.start ();
	      }
	   }

}
