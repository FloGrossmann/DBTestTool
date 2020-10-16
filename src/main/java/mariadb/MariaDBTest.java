package mariadb;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dbinterface.Adresse;
import dbinterface.Artikel;
import dbinterface.Bewertung;
import dbinterface.DBInterface;
import dbinterface.Kauf;
import dbinterface.Kunde;

public class MariaDBTest implements DBInterface {

	static final String JDBC_DRIVER = "org.mariadb.jdbc.Driver";

	String connectionString;
	String username;
	String password;
	Connection mariaDbConnection;
	Statement statement;
	String databaseName = "Webshop";

	public MariaDBTest(String connectionString, String username, String password) {
		this.connectionString = connectionString;
		this.username = username;
		this.password = new String(password.getBytes(), StandardCharsets.UTF_8);
	}

	public MariaDBTest() {

	}

	public void connect() {
		try {
			System.out.println("Verbindung erfolgt...");
//			mariaDbConnection = DriverManager.getConnection(
//					"jdbc:mariadb://localhost:3000","root","admin");
			mariaDbConnection = DriverManager.getConnection("jdbc:mariadb://" + connectionString, username, password);
			statement = mariaDbConnection.createStatement();
			if (setupDB()) {
				Kunde kunde=new Kunde("001", "kevin.hink.professor", //es darf kein @ Zeichen verwendet werden
						"0122336", "Kevin", "Hink", new Adresse("Falkenberg", "8", "Grenzstraße", "04895"));
				Artikel artikel=new Artikel("1", "Stuhl", 25.95, "Euro", "Ein Stuhl zum Sitzen");
				
				this.addKunde(kunde);
				this.addArtikel(artikel);
				this.addBewertung(new Bewertung(kunde.getKundenNummer(), artikel.getArtikelNummer(), 3, "sehr solide"));
				this.addKauf(new Kauf(kunde.getKundenNummer(), artikel.getArtikelNummer(), "16.10.2020", artikel.getEinzelPreis() , 5));
				System.out.println("Statements ausgeführt");
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			System.out.println("Verbindung fehlgeschlagen");
		} finally {
			try {
				if (statement != null) {
					mariaDbConnection.close();
				}
			} catch (SQLException se) {
			}
			try {
				if (mariaDbConnection != null)
					System.out.println("Verbindung erfolgreich");
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
					+ ".Kunde (Kundennummer varchar(255), Vorname varchar(255), Nachname varchar(255), Email varchar(255), Telefonnummer int, primary key (Kundennummer))");
			statement.execute("CREATE TABLE IF NOT EXISTS " + databaseName
					+ ".Adresse (PLZ varchar(255), Strasse varchar(255), Hausnummer varchar(255), Ortschaft varchar(255), Kundennummer varchar(255), FOREIGN KEY (Kundennummer) REFERENCES Kunde(Kundennummer), primary key (Kundennummer))");
			statement.execute("CREATE TABLE IF NOT EXISTS " + databaseName
					+ ".Artikel (Artikelnummer varchar(255),Artikelname varchar(255), Einzelpreis double(20,2), Waehrung varchar(255), Beschreibung varchar(255), primary key (Artikelnummer))");
			statement.execute("CREATE TABLE IF NOT EXISTS " + databaseName
					+ ".Bewertung (Sterne int, Bewertung varchar(255), Kundennummer varchar(255), Artikelnummer varchar(255), FOREIGN KEY (Kundennummer) REFERENCES Kunde(Kundennummer),  FOREIGN KEY (Artikelnummer) REFERENCES Artikel(Artikelnummer), CONSTRAINT Bewertungsnummer PRIMARY KEY (Kundennummer, Artikelnummer))");
			statement.execute("CREATE TABLE IF NOT EXISTS " + databaseName
					+ ".Kauf (Kaufdatum varchar(255), Kaufpreis double(20,2), Menge int, Kundennummer varchar(255), Artikelnummer varchar(255), FOREIGN KEY (Kundennummer) REFERENCES Kunde(Kundennummer),  FOREIGN KEY (Artikelnummer) REFERENCES Artikel(Artikelnummer), CONSTRAINT Kaufnummer PRIMARY KEY (Kundennummer, Artikelnummer))");
			System.out.println("Tabellen erstellt");
			return true;
		} catch (SQLException e) {
			System.out.println("Setup fehlgeschlagen");
			e.printStackTrace();
			return false;
		}

	}

	public boolean cleanData() {
		try {
			statement.execute("DROP DATABASE IF EXISTS " + databaseName);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public Kunde addKunde(Kunde kunde) {
		try {
			Adresse adresse = kunde.getAdresse();
			statement.executeUpdate("INSERT INTO " + databaseName
					+ ".Kunde (Kundennummer, Vorname, Nachname, Email, Telefonnummer) VALUES('" + kunde.getKundenNummer()
					+ "', '" + kunde.getVorname() + "', '" + kunde.getNachname() + "', '" + kunde.getEmail() + "', '"
					+ kunde.getTelefonNummer() + "')");
			statement.executeUpdate("INSERT INTO " + databaseName
					+ ".Adresse (PLZ, Strasse, Hausnummer, Ortschaft, Kundennummer) VALUES('" + adresse.getPlz() + "', '"
					+ adresse.getStrasse() + "', '" + adresse.getHausnummer() + "', '" + adresse.getOrtschaft() + "', '"
					+ kunde.getKundenNummer() + "')");
			return kunde;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public Artikel addArtikel(Artikel artikel) {
		try {
			statement.executeUpdate("INSERT INTO " + databaseName
					+ ".Artikel (Artikelnummer, Artikelname, Einzelpreis, Waehrung, Beschreibung) VALUES('"
					+ artikel.getArtikelNummer() + "', '" + artikel.getArtikelName() + "', " + artikel.getEinzelPreis()
					+ ", '" + artikel.getWaehrung() + "', '" + artikel.getBeschreibung() + "')");
			return artikel;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Bewertung addBewertung(Bewertung bewertung) {
		try {
			statement.execute("INSERT INTO " + databaseName
					+ ".Bewertung (Sterne, Bewertung, Kundennummer, Artikelnummer) VALUES(" + bewertung.getSterne()
					+ ", '" + bewertung.getBewertung() + "', '" + bewertung.getKundenNummer() + "', '"
					+ bewertung.getArtikelNummer() + "')");
			return bewertung;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Kauf addKauf(Kauf kauf) {
		try {
			statement.execute("INSERT INTO " + databaseName
					+ ".Kauf (Kaufdatum, Kaufpreis, Menge, Kundennummer, Artikelnummer) VALUES(" + kauf.getKaufdatum()
					+ ", " + kauf.getKaufPreis() + ", " + kauf.getMenge() + ", '" + kauf.getKundenNr() + "', '"
					+ kauf.getArtikelNr() + "')");
			return kauf;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Kunde getKundeByKundenNr(String kundenNr) {
		Kunde kunde = new Kunde();
		try {
			ResultSet resultSet = statement.executeQuery("SELECT * FROM " + databaseName + ".Kunde FULL JOIN "
					+ databaseName + ".Adresse WHERE Kundennummer='" + kundenNr + "'");
			while (resultSet.next()) {
				kunde.setKundenNummer(resultSet.getString("Kundennummer"));
				kunde.setEmail(resultSet.getString("Email"));
				kunde.setTelefonNummer(resultSet.getString("Telefonnummer"));
				kunde.setVorname(resultSet.getString("Vorname"));
				kunde.setNachname(resultSet.getString("Nachname"));
				// wie soll das hier gehen? kommt das in der Antwort einfach so mit?? Wird die
				// mit geladen weil kundennummer der foreign Kay ist? -> habe es erstmal mit
				// einem JOIN probiert aber vielleicht geht es ja auch ohne
				kunde.setAdresse(new Adresse(resultSet.getString("Ortschaft"), resultSet.getString("Hausnummer"),
						resultSet.getString("Strasse"), resultSet.getString("PLZ")));
			}
			return kunde;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Kunde getKundeByEmail(String email) {
		Kunde kunde = new Kunde();
		try {
			ResultSet resultSet = statement.executeQuery("SELECT * FROM " + databaseName + ".Kunde FULL JOIN "
					+ databaseName + ".Adresse WHERE Email='" + email + "'");
			while (resultSet.next()) {
				kunde.setKundenNummer(resultSet.getString("Kundennummer"));
				kunde.setEmail(resultSet.getString("Email"));
				kunde.setTelefonNummer(resultSet.getString("Telefonnummer"));
				kunde.setVorname(resultSet.getString("Vorname"));
				kunde.setNachname(resultSet.getString("Nachname"));
				// wie soll das hier gehen? kommt das in der Antwort einfach so mit?? Wird die
				// mit geladen weil kundennummer der foreign Kay ist? -> habe es erstmal mit
				// einem JOIN probiert aber vielleicht geht es ja auch ohne
				kunde.setAdresse(new Adresse(resultSet.getString("Ortschaft"), resultSet.getString("Hausnummer"),
						resultSet.getString("Strasse"), resultSet.getString("PLZ")));
			}
			return kunde;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Kunde> getKundenByPlz(String plz) {
		List<Kunde> kundeList = new ArrayList<Kunde>();
		try {
			ResultSet resultSet = statement.executeQuery("SELECT * FROM " + databaseName + ".Kunde FULL JOIN "
					+ databaseName + ".Adresse WHERE PLZ='" + plz + "'");
			while (resultSet.next()) {
				Kunde kunde = new Kunde();
				kunde.setKundenNummer(resultSet.getString("Kundennummer"));
				kunde.setEmail(resultSet.getString("Email"));
				kunde.setTelefonNummer(resultSet.getString("Telefonnummer"));
				kunde.setVorname(resultSet.getString("Vorname"));
				kunde.setNachname(resultSet.getString("Nachname"));
				// wie soll das hier gehen? kommt das in der Antwort einfach so mit?? Wird die
				// mit geladen weil kundennummer der foreign Kay ist? -> habe es erstmal mit
				// einem JOIN probiert aber vielleicht geht es ja auch ohne
				kunde.setAdresse(new Adresse(resultSet.getString("Ortschaft"), resultSet.getString("Hausnummer"),
						resultSet.getString("Strasse"), resultSet.getString("PLZ")));
				kundeList.add(kunde);
			}
			return kundeList;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Kunde> getKundenByNachName(String nachName) {
		List<Kunde> kundeList = new ArrayList<Kunde>();
		try {
			ResultSet resultSet = statement.executeQuery("SELECT * FROM " + databaseName + ".Kunde FULL JOIN "
					+ databaseName + ".Adresse WHERE Nachname='" + nachName + "'");
			while (resultSet.next()) {
				Kunde kunde = new Kunde();
				kunde.setKundenNummer(resultSet.getString("Kundennummer"));
				kunde.setEmail(resultSet.getString("Email"));
				kunde.setTelefonNummer(resultSet.getString("Telefonnummer"));
				kunde.setVorname(resultSet.getString("Vorname"));
				kunde.setNachname(resultSet.getString("Nachname"));
				// wie soll das hier gehen? kommt das in der Antwort einfach so mit?? Wird die
				// mit geladen weil kundennummer der foreign Kay ist? -> habe es erstmal mit
				// einem JOIN probiert aber vielleicht geht es ja auch ohne
				kunde.setAdresse(new Adresse(resultSet.getString("Ortschaft"), resultSet.getString("Hausnummer"),
						resultSet.getString("Strasse"), resultSet.getString("PLZ")));
				kundeList.add(kunde);
			}
			return kundeList;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<String> getDistinctOrte() {
		List<String> ortList = new ArrayList<String>();
		try {
			ResultSet resultSet = statement.executeQuery("SELECT * FROM " + databaseName + ".Adresse");
			// WHERE Ortschaft='" + ??? + "'");Soll da was anderes gemacht werden?
			while (resultSet.next()) {
				String ort = resultSet.getString("Ortschaft");
				ortList.add(ort);
			}
			return ortList;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Artikel getArtikelByArtikelNummer(String artikelNummer) {
		Artikel artikel = new Artikel();
		try {
			ResultSet resultSet = statement.executeQuery("SELECT * FROM " + databaseName + ".Artikel FULL JOIN "
					+ databaseName + ".Adresse WHERE Artikelnummer='" + artikelNummer + "'");
			while (resultSet.next()) {
				artikel.setArtikelNummer(resultSet.getString("Artikelnummer"));
				artikel.setArtikelName(resultSet.getString("Artikelname"));
				artikel.setEinzelPreis(resultSet.getDouble("Einzelpreis"));
				artikel.setWaehrung(resultSet.getString("Waehrung"));
				artikel.setBeschreibung(resultSet.getString("Beschreibung"));
			}
			return artikel;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Artikel getArtikelByArtikelName(String artikelName) {
		Artikel artikel = new Artikel();
		try {
			ResultSet resultSet = statement.executeQuery("SELECT * FROM " + databaseName + ".Artikel FULL JOIN "
					+ databaseName + ".Adresse WHERE Artikelname='" + artikelName + "'");
			while (resultSet.next()) {
				artikel.setArtikelNummer(resultSet.getString("Artikelnummer"));
				artikel.setArtikelName(resultSet.getString("Artikelname"));
				artikel.setEinzelPreis(resultSet.getDouble("Einzelpreis"));
				artikel.setWaehrung(resultSet.getString("Waehrung"));
				artikel.setBeschreibung(resultSet.getString("Beschreibung"));
			}
			return artikel;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Artikel> getArtikelWhichCostMoreThan(Double price) {
		List<Artikel> artikelList = new ArrayList<Artikel>();
		try {
			ResultSet resultSet = statement
					.executeQuery("SELECT * FROM " + databaseName + ".Artikel WHERE Einzelpreis>" + price);
			while (resultSet.next()) {
				Artikel artikel = new Artikel();
				artikel.setArtikelNummer(resultSet.getString("Artikelnummer"));
				artikel.setArtikelName(resultSet.getString("Artikelname"));
				artikel.setEinzelPreis(resultSet.getDouble("Einzelpreis"));
				artikel.setWaehrung(resultSet.getString("Waehrung"));
				artikel.setBeschreibung(resultSet.getString("Beschreibung"));
			}
			return artikelList;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Bewertung getBewertungByKundenNrAndArtikelNr(String artikelNummer, String kundenNummer) {
		Bewertung bewertung = new Bewertung();
		try {
			ResultSet resultSet = statement.executeQuery("SELECT * FROM " + databaseName
					+ ".Bewertung WHERE Artikelnummer='" + artikelNummer + "', Kundennummer='" + kundenNummer + "'");
			while (resultSet.next()) {
				bewertung.setArtikelNummer(resultSet.getString("Artikelnummer"));
				bewertung.setKundenNummer(resultSet.getString("Kundennummer"));
				bewertung.setBewertung(resultSet.getString("Bewertung"));
				bewertung.setSterne(resultSet.getInt("Sterne"));
			}
			return bewertung;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Bewertung> getBewertungenByAnzahlSterne(int sterne) {
		List<Bewertung> bewertungList = new ArrayList<Bewertung>();
		try {
			ResultSet resultSet = statement
					.executeQuery("SELECT * FROM " + databaseName + ".Bewertung WHERE Sterne=" + sterne);
			while (resultSet.next()) {
				Bewertung bewertung = new Bewertung();
				bewertung.setArtikelNummer(resultSet.getString("Artikelnummer"));
				bewertung.setKundenNummer(resultSet.getString("Kundennummer"));
				bewertung.setBewertung(resultSet.getString("Bewertung"));
				bewertung.setSterne(resultSet.getInt("Sterne"));
				bewertungList.add(bewertung);
			}
			return bewertungList;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Bewertung> getBewertungenByKundenNr(String kundenNummer) {
		List<Bewertung> bewertungList = new ArrayList<Bewertung>();
		try {
			ResultSet resultSet = statement.executeQuery(
					"SELECT * FROM " + databaseName + ".Bewertung WHERE Kundennummer='" + kundenNummer + "'");
			while (resultSet.next()) {
				Bewertung bewertung = new Bewertung();
				bewertung.setArtikelNummer(resultSet.getString("Artikelnummer"));
				bewertung.setKundenNummer(resultSet.getString("Kundennummer"));
				bewertung.setBewertung(resultSet.getString("Bewertung"));
				bewertung.setSterne(resultSet.getInt("Sterne"));
				bewertungList.add(bewertung);
			}
			return bewertungList;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Bewertung> getBewertungenByArtikelNr(String artikelNummer) {
		List<Bewertung> bewertungList = new ArrayList<Bewertung>();
		try {
			ResultSet resultSet = statement.executeQuery(
					"SELECT * FROM " + databaseName + ".Bewertung WHERE Artikelnummer='" + artikelNummer + "'");
			while (resultSet.next()) {
				Bewertung bewertung = new Bewertung();
				bewertung.setArtikelNummer(resultSet.getString("Artikelnummer"));
				bewertung.setKundenNummer(resultSet.getString("Kundennummer"));
				bewertung.setBewertung(resultSet.getString("Bewertung"));
				bewertung.setSterne(resultSet.getInt("Sterne"));
				bewertungList.add(bewertung);
			}
			return bewertungList;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Kauf> getEinkaeufeForKunde(String kundenNummer) {
		List<Kauf> kaufList = new ArrayList<Kauf>();
		try {
			ResultSet resultSet = statement
					.executeQuery("SELECT * FROM " + databaseName + ".Kauf WHERE Kundennummer='" + kundenNummer + "'");
			while (resultSet.next()) {
				Kauf kauf = new Kauf();
				kauf.setArtikelNr(resultSet.getString("Artikelnummer"));
				kauf.setKundenNr(resultSet.getString("Kundennummer"));
				kauf.setKaufPreis(resultSet.getDouble("Kaufpreis"));
				kauf.setKaufdatum(resultSet.getString("Kaufdatum"));
				kauf.setMenge(resultSet.getInt("Menge"));
				kaufList.add(kauf);
			}
			return kaufList;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Kauf> getVerkauefeForArtikel(String artikelNummer) {
		List<Kauf> kaufList = new ArrayList<Kauf>();
		try {
			ResultSet resultSet = statement.executeQuery(
					"SELECT * FROM " + databaseName + ".Kauf WHERE Artikelnummer='" + artikelNummer + "'");
			while (resultSet.next()) {
				Kauf kauf = new Kauf();
				kauf.setArtikelNr(resultSet.getString("Artikelnummer"));
				kauf.setKundenNr(resultSet.getString("Kundennummer"));
				kauf.setKaufPreis(resultSet.getDouble("Kaufpreis"));
				kauf.setKaufdatum(resultSet.getString("Kaufdatum"));
				kauf.setMenge(resultSet.getInt("Menge"));
				kaufList.add(kauf);
			}
			return kaufList;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Kunde updateKunde(Kunde kunde) {
		try {
			statement.execute("UPDATE " + databaseName + ".Kunde SET Vorname=" + kunde.getVorname() + ", Nachname="
					+ kunde.getNachname() + ", Email=" + kunde.getEmail() + ", Telefonnummer="
					+ kunde.getTelefonNummer() + " WHERE Kundennummer=" + kunde.getKundenNummer());
			Adresse adresse = kunde.getAdresse();
			statement.execute("UPDATE " + databaseName + ".Adresse SET Strasse=" + adresse.getStrasse()
					+ ", Hausnummer=" + adresse.getHausnummer() + ", Ortschaft=" + adresse.getOrtschaft() + ", PLZ="
					+ adresse.getPlz() + " WHERE Kundennummer=" + kunde.getKundenNummer());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return kunde;
	}

	public Artikel updateArtikel(Artikel artikel) {
		try {
			statement.execute("UPDATE " + databaseName + ".Artikel SET Artikelname=" + artikel.getArtikelName() + ", Beschreibung="
					+ artikel.getBeschreibung() + ", Einzelpreis=" + artikel.getEinzelPreis() + ", Waehrung=" + artikel.getWaehrung()
					+ " WHERE Artkelnummer=" + artikel.getArtikelNummer());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return artikel;
	}

	public Bewertung updateBewertung(Bewertung bewertung) {
		try {
			statement.execute("UPDATE " + databaseName + ".Bewertung SET Bewertung=" + bewertung.getBewertung() + ", Sterne="
					+ bewertung.getSterne() + " WHERE Artkelnummer=" + bewertung.getArtikelNummer()+" AND Kundennummer="+bewertung.getKundenNummer());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return bewertung;
	}

	public void deleteKundeByKundenNr(String kundenNr) {
		try {
			statement.execute("DELETE FROM "+databaseName+".Kunde WHERE Kundennummer="+kundenNr);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void deleteArtikelbyArtikelNr(String artikelNr) {
		try {
			statement.execute("DELETE FROM "+databaseName+".Artikel WHERE Artikelnummer="+artikelNr);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void deleteBewertungByArtikelNrAndKundenNr(String artikelNr, String bewertungNr) {
		try {
			statement.execute("DELETE FROM "+databaseName+".Bewertung WHERE Kundennummer="+bewertungNr+" Artikelnummer="+artikelNr);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
