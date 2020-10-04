package mariadb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import dbinterface.Adresse;
import dbinterface.Artikel;
import dbinterface.Bewertung;
import dbinterface.DBInterface;
import dbinterface.Kauf;
import dbinterface.Kunde;
import ui.MenuFrame;

public class MariaDBTest implements DBInterface {

	String connectionString;
	String username;
	String password;
	Connection mariaDbConnection;
	Statement statement;
	String databaseName = "Webshop";

	public MariaDBTest(String connectionString, String username, String password) {
		this.connectionString = connectionString;
		this.username = username;
		this.password = password;
	}

	public MariaDBTest() {

	}

	public void connect() {
		System.out.println("Test connection with connectionString: " + connectionString);
		try {
			mariaDbConnection = DriverManager.getConnection(
					"jdbc:mariadb://" + connectionString + "/DB?user=" + username + "&password=" + password);
//			mariaDbConnection=DriverManager.getConnection("jdbc:mariadb://localhost:3000/DB?user=admin&password=admin");
			System.out.println("Verbindung erfolgt...");
			statement = mariaDbConnection.createStatement();
			if (setupDB())
				System.out.println("Statements ausgeführt");
		} catch (SQLException e) {
			System.out.println("Verbindung fehlgeschlagen");
		} finally {
			try {
				if (statement != null)
					mariaDbConnection.close();
			} catch (SQLException se) {
			}
			try {
				if (mariaDbConnection != null)
					mariaDbConnection.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
	}

	public boolean setupDB() {
		try {
			statement.execute("CREATE DATABASE IF NOT EXISTS " + databaseName);
			statement.execute("CREATE TABLE IF NOT EXISTS " + databaseName
					+ ".Adresse (PLZ int, Strasse varchar(255), Hausnummer varchar(255), Ortschaft varchar(255), FOREIGN KEY (Kundennummer) REFERENCES Kunde(Kundennummer), primary key (Kundennummer))");
			statement.execute("CREATE TABLE IF NOT EXISTS " + databaseName
					+ ".Kunde (Kundennummer varchar(255), Vorname varchar(255), Nachname varchar(255), Email varchar(255), Telefonnummer int, primary key (Kundennummer))");
			statement.execute("CREATE TABLE IF NOT EXISTS " + databaseName
					+ ".Artikel (Artikelnummer varchar(255),Artikelname varchar(255), Einzelpreis double(20,2), Waehrung varchar(255), Beschreibung varchar(255), primary key (Artikelnummer))");
			statement.execute("CREATE TABLE IF NOT EXISTS " + databaseName
					+ ".Bewertung (Sterne int, Bewertung varchar(255),  FOREIGN KEY (Kundennummer) REFERENCES Kunde(Kundennummer),  FOREIGN KEY (Artikelnummer) REFERENCES Artikel(Artikelnummer), CONSTRAINT Bewertungsnummer PRIMARY KEY (Kundennummer, Artikelnummer))");
			statement.execute("CREATE TABLE IF NOT EXISTS " + databaseName
					+ ".Kauf (Kaufdatum date, Kaufpreis double(20,2), Menge int, FOREIGN KEY (Kundennummer) REFERENCES Kunde(Kundennummer),  FOREIGN KEY (Artikelnummer) REFERENCES Artikel(Artikelnummer), CONSTRAINT Kaufnummer PRIMARY KEY (Kundennummer, Artikelnummer))");
			System.out.println("Tabellen erstellt");
			return true;
		} catch (SQLException e) {
			System.out.println("Setup fehlgeschlagen");
			return false;
		}

	}

	public boolean cleanData() {
		try {
			statement.execute("DROP DATABASE IF EXISTS "+databaseName);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public Kunde addKunde(Kunde kunde) {
		try {
			Adresse adresse=kunde.getAdresse();
			statement.execute("INSERT INTO "+ databaseName+".Kunde (Kundennummer, Vorname, Nachname, Email, Telefonnummer) VALUES("+kunde.getKundenNummer()+", "+kunde.getVorname()+", "+kunde.getNachname()+", "+kunde.getEmail()+", "+kunde.getTelefonNummer()+")");
			statement.execute("INSERT INTO "+ databaseName+".Adresse (PLZ, Strasse, Hausnummer, Ortschaft, Kundennummer) VALUES("+adresse.getPlz()+", "+adresse.getStrasse()+", "+adresse.getHausnummer()+", "+adresse.getOrtschaft()+", "+kunde.getKundenNummer()+")");
			return kunde;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public Artikel addArtikel(Artikel artikel) {
		try {
			statement.execute("INSERT INTO "+ databaseName+".Artikel (Artikelnummer, Artikelname, Einzelpreis, Waehrung, Beschreibung) VALUES("+artikel.getArtikelNummer()+", "+artikel.getArtikelName()+", "+artikel.getEinzelPreis()+", "+artikel.getWaehrung()+", "+artikel.getBeschreibung()+")");
			return artikel;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public Bewertung addBewertung(Bewertung bewertung) {
		try {
			statement.execute("INSERT INTO "+ databaseName+".Bewertung (Sterne, Bewertung, Kundennummer, Artikelnummer) VALUES("+bewertung.getSterne()+", "+bewertung.getBewertung()+", "+bewertung.getKundenNummer()+", "+bewertung.getArtikelNummer()+")");
			return bewertung;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public Kauf addKauf(Kauf kauf) {
		try {
			statement.execute("INSERT INTO "+ databaseName+".Kauf (Kaufdatum, Kaufpreis, Menge, Kundennummer, Artikelnummer) VALUES("+kauf.getKaufdatum()+", "+kauf.getKaufPreis()+", "+kauf.getMenge()+", "+kauf.getKundenNr()+", "+kauf.getArtikelNr()+")");
			return kauf;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public Kunde getKundeByKundenNr(String kundenNr) {
		// TODO Auto-generated method stub
		return null;
	}

	public Kunde getKundeByEmail(String email) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Kunde> getKundenByPlz(String plz) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Kunde> getKundenByNachName(String nachName) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<String> getDistinctOrte() {
		// TODO Auto-generated method stub
		return null;
	}

	public Artikel getArtikelByArtikelNummer(String artikelNummer) {
		// TODO Auto-generated method stub
		return null;
	}

	public Artikel getArtikelByArtikelName(String artikelName) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Artikel> getArtikelWhichCostMoreThan(Double price) {
		// TODO Auto-generated method stub
		return null;
	}

	public Bewertung getBewertungByKundenNrAndArtikelNr(String artikelNummer, String kundenNummer) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Bewertung> getBewertungenByAnzahlSterne(int sterne) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Bewertung> getBewertungenByKundenNr(String kundenNummer) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Bewertung> getBewertungenByArtikelNr(String artikelNummer) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Kauf> getEinkaeufeForKunde(String kundenNummer) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Kauf> getVerkauefeForArtikel(String artikelNummer) {
		// TODO Auto-generated method stub
		return null;
	}

	public Kunde updateKunde(Kunde kunde) {
		// TODO Auto-generated method stub
		return null;
	}

	public Artikel updateArtikel(Artikel artikel) {
		// TODO Auto-generated method stub
		return null;
	}

	public Bewertung updateBewertung(Bewertung bewertung) {
		// TODO Auto-generated method stub
		return null;
	}

	public Kunde deleteKundeByKundenNr(String kundenNr) {
		// TODO Auto-generated method stub
		return null;
	}

	public Artikel deleteArtikelbyArtikelNr(String artikelNr) {
		// TODO Auto-generated method stub
		return null;
	}

	public Bewertung deleteBewertungByArtikelNrAndKundenNr(String artikelNr, String bewertungNr) {
		// TODO Auto-generated method stub
		return null;
	}

}
