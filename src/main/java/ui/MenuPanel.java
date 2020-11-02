package ui;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import measure.MeasureThread;
import monogdb.MongoDBTest;

public class MenuPanel extends JPanel {

	private static final long serialVersionUID = 448371628021809323L;

	Container c;
	private JTextField mongoDBConnectionTextField;
	private JTextField mariaDBConnectionTextField;
	private JTextField mariaDBUsernameTextField;
	private JTextField mariaDBPasswordTextField;
	private JLabel mongoDBConnectionLabel;
	private JLabel mariaDBConnectionLabel;
	private JLabel mariaDBUsernameLabel;
	private JLabel mariaDBPasswordLabel;
	private JTextArea errorLabel;
	JButton mongoDBTestConnectionButton;
	JButton mongoDBClearDBButton;
	
	MongoDBTest mongoTest;

	public JButton startTestButton;
	
	ProgressIndicatorPanel progressPanel;
	
	MenuPanel self;
	
	public void createMariaDBPanel() {
		mariaDBConnectionLabel = new JLabel("mariaDBConnectionString:");
		mariaDBConnectionTextField = new JTextField(50);
		mariaDBConnectionTextField.setText("localhost:3306");
		mariaDBUsernameLabel = new JLabel("Benutzername:");
		mariaDBPasswordLabel = new JLabel("Passwort:");
		mariaDBPasswordTextField = new JTextField(50);
		mariaDBPasswordTextField.setText("root");
		mariaDBUsernameTextField = new JTextField(50);
		mariaDBUsernameTextField.setText("root");
		add(mariaDBConnectionLabel);
		add(mariaDBConnectionTextField);
		add(mariaDBPasswordLabel);
		add(mariaDBPasswordTextField);
		add(mariaDBUsernameLabel);
		add(mariaDBUsernameTextField);
	}
	
	public void createMongoDBPanel() {
		mongoDBConnectionLabel = new JLabel("mongoDBConnectionString:");
		add(mongoDBConnectionLabel);
		errorLabel = new JTextArea();
		errorLabel.setEditable(false);
		errorLabel.setVisible(false);
		mongoDBConnectionTextField = new JTextField(50);
		mongoDBConnectionTextField.setText("mongodb://localhost:27017/?readPreference=primary&ssl=false");
		mongoDBConnectionTextField.getDocument().addDocumentListener(alMongoDBConnectionStringUpdate);
		add(mongoDBConnectionTextField);
		add(errorLabel);
		self = this;
	}

	public MenuPanel() {
		setLayout(new GridLayout(11, 2));
		
		createMongoDBPanel();
		createMariaDBPanel();
		
		startTestButton = new JButton("Start Testing");
		startTestButton.addActionListener(alStartTest);
		add(startTestButton);
		
		progressPanel = new ProgressIndicatorPanel();
		add(progressPanel);
	}

	DocumentListener alMongoDBConnectionStringUpdate = new DocumentListener() {

		public void changedUpdate(DocumentEvent arg0) {
			testConnectionString();
		}

		public void insertUpdate(DocumentEvent arg0) {
			testConnectionString();
		}

		public void removeUpdate(DocumentEvent arg0) {
			testConnectionString();
		}

		public void testConnectionString() {
			if (!mongoDBConnectionTextField.getText().startsWith("mongodb://")
					&& !mongoDBConnectionTextField.getText().startsWith("mongodb+srv://")) {
				errorLabel.setVisible(true);
				errorLabel.setText(
						"The connection string is invalid. Connection strings must start with either 'mongodb://' or 'mongodb+srv://");
			} else {
				errorLabel.setVisible(false);
				errorLabel.setText("");
			}
		}
	};

	ActionListener alStartTest = new ActionListener() {

		public void actionPerformed(ActionEvent action) {
			System.out.println("Start Testing");
			try {
				errorLabel.setVisible(false);
				errorLabel.setText("");
				startTestButton.setText("Testing..");
				startTestButton.setEnabled(false);
				update(getGraphics());
				MeasureThread measureThread = new MeasureThread(mongoDBConnectionTextField.getText(), mariaDBConnectionTextField.getText(), mariaDBUsernameTextField.getText(), mariaDBPasswordTextField.getText(), self); 
				measureThread.start();
			} catch (Exception e) {
				e.printStackTrace();
				errorLabel.setVisible(true);
				errorLabel.setText(e.getMessage());
				startTestButton.setText("Start Testing");
				startTestButton.setEnabled(true);
				update(getGraphics());
			}
		}
	};

	public ProgressIndicatorPanel getProgressPanel() {
		return progressPanel;
	}
}
