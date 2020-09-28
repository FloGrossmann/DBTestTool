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

import monogdb.MongoDBTest;

public class MenuPanel extends JPanel {

	private static final long serialVersionUID = 448371628021809323L;

	Container c;
	JTextField mongoDBConnectionTextField;
	JLabel mongoDBConnectionLabel;
	JTextArea mongoDBConnectionErrorText;
	JButton mongoDBTestConnectionButton;
	JButton mongoDBClearDBButton;

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
		mongoDBTestConnectionButton.addActionListener(alMongoDBTestConnection);
		add(mongoDBTestConnectionButton);

		mongoDBClearDBButton = new JButton("MongoDB test-DB löschen");
		mongoDBClearDBButton.addActionListener(alMongoDBClear);
		add(mongoDBClearDBButton);
	}

	ActionListener alMongoDBTestConnection=new ActionListener(){

	public void actionPerformed(ActionEvent e){MongoDBTest mongoTest=new MongoDBTest();mongoTest.connect(mongoDBConnectionTextField.getText());}};

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
				mongoDBConnectionErrorText.setText("The connection string is invalid. Connection strings must start with either 'mongodb://' or 'mongodb+srv://");
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
