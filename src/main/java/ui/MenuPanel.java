package ui;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import mariadb.MariaDBTest;
import monogdb.MongoDBTest;

public class MenuPanel extends JPanel {

	private static final long serialVersionUID = 448371628021809323L;

	Container c;
	JTextField mongoDBConnectionTextField;
	JTextField mariaDBConnectionTextField;
	JTextField mariaDBUsernameTextField;
	JTextField mariaDBPasswordTextField;
	JLabel mongoDBConnectionLabel;
	JLabel mariaDBConnectionLabel;
	JLabel mariaDBUsernameLabel;
	JLabel mariaDBPasswordLabel;
	JTextArea mongoDBConnectionErrorText;
	JButton mongoDBTestConnectionButton;
	JButton mongoDBClearDBButton;
	JButton mariaDBConnectionButton;

	public void createMariaDBPanel() {
		mariaDBConnectionLabel = new JLabel("mariaDBConnectionString:");
		mariaDBConnectionTextField = new JTextField(50);
		mariaDBUsernameLabel = new JLabel("Benutzername:");
		mariaDBPasswordLabel = new JLabel("Passwort:");
		mariaDBPasswordTextField = new JTextField(50);
		mariaDBUsernameTextField = new JTextField(50);
		mariaDBConnectionButton = new JButton("Test MariaDB Connection");
		mariaDBConnectionButton.addActionListener(alDBTestConnection);
		add(mariaDBConnectionLabel);
		add(mariaDBConnectionTextField);
		add(mariaDBPasswordLabel);
		add(mariaDBPasswordTextField);
		add(mariaDBUsernameLabel);
		add(mariaDBUsernameTextField);
		add(mariaDBConnectionButton);
	}
	
	public MenuPanel() {
		mongoDBConnectionLabel = new JLabel("mongoDBConnectionString:");
		add(mongoDBConnectionLabel);
		mongoDBConnectionErrorText = new JTextArea();
		mongoDBConnectionErrorText.setEditable(false);
		mongoDBConnectionTextField = new JTextField(50);
		mongoDBConnectionTextField.getDocument().addDocumentListener(alMongoDBConnectionStringUpdate);
		add(mongoDBConnectionTextField);
		add(mongoDBConnectionErrorText);

		mongoDBTestConnectionButton = new JButton("Test MongoDB Connection");
		mongoDBTestConnectionButton.addActionListener(alDBTestConnection);
		add(mongoDBTestConnectionButton);

		mongoDBClearDBButton = new JButton("MongoDB test-DB löschen");
		mongoDBClearDBButton.addActionListener(alMongoDBClear);
		add(mongoDBClearDBButton);
		
		createMariaDBPanel();
	}

	ActionListener alDBTestConnection = new ActionListener() {

		public void actionPerformed(ActionEvent e) {
			JButton jButton=(JButton) e.getSource();
			if(jButton.getText().contains("Mongo")) {
			MongoDBTest mongoTest = new MongoDBTest();
			mongoTest.connect(mongoDBConnectionTextField.getText());
			}else {
				MariaDBTest mariaDBTest=new MariaDBTest(mariaDBConnectionTextField.getText(), mariaDBUsernameTextField.getText(), mariaDBPasswordTextField.getText());
				mariaDBTest.connect();
			}
		}
	};

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
				mongoDBConnectionErrorText.setText(
						"The connection string is invalid. Connection strings must start with either 'mongodb://' or 'mongodb+srv://");
			} else {
				mongoDBConnectionErrorText.setText("");
			}
		}
	};

	ActionListener alMongoDBClear = new ActionListener() {

		public void actionPerformed(ActionEvent e) {
			System.out.println(mongoDBConnectionTextField.getText());

		}
	};
	
	
}
