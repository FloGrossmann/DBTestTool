package ui;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import dbinterface.Adresse;
import dbinterface.Artikel;
import dbinterface.Bewertung;
import dbinterface.Kauf;
import dbinterface.Kunde;
import monogdb.MongoDBTest;

public class MenuPanel extends JPanel {

	private static final long serialVersionUID = 448371628021809323L;

	Container c;
	JTextField mongoDBConnectionTextField;
	JLabel mongoDBConnectionLabel;
	JTextArea mongoDBConnectionErrorText;
	JButton mongoDBTestConnectionButton;
	JButton mongoDBClearDBButton;
	JButton mongoDBStartTestButton;
	
	MongoDBTest mongoTest;

	public MenuPanel() {
		mongoDBConnectionLabel = new JLabel("mongoDBConnectionString:");
		add(mongoDBConnectionLabel);
		mongoDBConnectionErrorText = new JTextArea();
		mongoDBConnectionErrorText.setEditable(false);
		mongoDBConnectionTextField = new JTextField(50);
		mongoDBConnectionTextField.setText("mongodb://localhost:27017/?readPreference=primary&ssl=false");
		mongoDBConnectionTextField.getDocument().addDocumentListener(alMongoDBConnectionStringUpdate);
		add(mongoDBConnectionTextField);
		add(mongoDBConnectionErrorText);

		mongoDBTestConnectionButton = new JButton("Test MongoDB Connection");
		mongoDBTestConnectionButton.addActionListener(alMongoDBTestConnection);
		add(mongoDBTestConnectionButton);

		mongoDBClearDBButton = new JButton("MongoDB test-DB l�schen");
		mongoDBClearDBButton.addActionListener(alMongoDBClear);
		mongoDBClearDBButton.setVisible(false);
		add(mongoDBClearDBButton);
		
		mongoDBStartTestButton = new JButton("Start MongoDB Test");
		mongoDBStartTestButton.addActionListener(alMongoDBSetup);
		mongoDBStartTestButton.setVisible(false);
		add(mongoDBStartTestButton);
	}

	ActionListener alMongoDBTestConnection = new ActionListener() {

		public void actionPerformed(ActionEvent e) {
			MongoDBTest mongoDBTest = new MongoDBTest();
			try {
				boolean success = mongoDBTest.connect(mongoDBConnectionTextField.getText());
				if (success) {
					mongoTest = mongoDBTest;
					mongoDBConnectionErrorText.setText("Connection successful");
					mongoDBClearDBButton.setVisible(true);
					mongoDBStartTestButton.setVisible(true);
				}
			} catch (Exception ex) {
				System.out.println(ex);
				mongoDBConnectionErrorText.setText(ex.getMessage());
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
			mongoTest.cleanData();
		}
	};
	
	ActionListener alMongoDBSetup = new ActionListener() {

		public void actionPerformed(ActionEvent e) {
			mongoTest.setupDB();
			mongoTest.addArtikel(new Artikel("22", "artikelName", 42.42, "euro", "ein Artikel"));
			mongoTest.addKunde(new Kunde("12", "email@web.de", "07234723", "Vorname", "Nachname", new Adresse("ortschaft", "hausnummer", "Stra�e", "712231")));
			mongoTest.addKauf(new Kauf("12", "22", new Date(), 43.43, 2));
			mongoTest.addBewertung(new Bewertung("12", "22", 5, "war okay"));
		}
	};
}
