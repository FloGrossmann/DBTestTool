package mariadb;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import com.mongodb.DuplicateKeyException;

import dbinterface.Adresse;
import dbinterface.Artikel;
import dbinterface.Bewertung;
import dbinterface.DBInterface;
import dbinterface.Kauf;
import dbinterface.Kunde;
import measure.CRUDoperation;
import measure.CsvBeanWriter;
import measure.AccessTime;

public class MariaDBTest implements DBInterface {

	static final String JDBC_DRIVER = "org.mariadb.jdbc.Driver";

	String connectionString;
	String username;
	String password;
	Connection mariaDbConnection;
	Statement statement;
	String databaseName = "Webshop";
	int kundenId = 000;
	int artikelId = 0;
	Instant start;
	Instant end;
	List<AccessTime> timeMeasures = new ArrayList<AccessTime>();

	public MariaDBTest(String connectionString, String username, String password) {
		this.connectionString = connectionString;
		this.username = username;
		this.password = new String(password.getBytes(), StandardCharsets.UTF_8);
	}

	public MariaDBTest() {

	}

	public <T> void printOutSelect(List<T> list) {
		Iterator<T> iterator = list.iterator();
		while (iterator.hasNext()) {
			T objekt = iterator.next();
			System.out.println(objekt.toString());
		}
	}

	public <T> void printOutSelect(T objekt) {
		System.out.println(objekt.toString());
	}

	public void connect() {
		try {
			System.out.println("Verbindung erfolgt...");
			mariaDbConnection = DriverManager.getConnection("jdbc:mariadb://" + connectionString, username, password);
			statement = mariaDbConnection.createStatement();
			if (setupDB()) {
				Kunde kunde = new Kunde(++this.kundenId + "", "kevin.hink.professor", // es darf kein @ Zeichen
																						// verwendet werden
						"0122336", "Kevin", "Hink", new Adresse("Falkenberg", "8", "Grenzstraße", "04895"));
				Artikel artikel = new Artikel(++this.artikelId + "", "Stuhl", 25.95, "Euro", "Ein Stuhl zum Sitzen");
				Bewertung bewertung = new Bewertung(kunde.getKundenNummer(), artikel.getArtikelNummer(), 3,
						"sehr solide");
				Kauf kauf = new Kauf(kunde.getKundenNummer(), artikel.getArtikelNummer(),
						new Date(new java.util.Date().getTime()), artikel.getEinzelPreis() * 5, 5);

				// Delete Operations -> müssen in der Reihenfolge ausgeführt werden
				System.out.println("Löschen");

				start = Instant.now();
				this.deleteKaufByArtikelNrAndKundenNr(artikel.getArtikelNummer(), kunde.getKundenNummer());
				end = Instant.now();
				timeMeasures.add(new AccessTime("Kauf by Artikel- und Kundennummer",
						Duration.between(start, end).toMillis(), CRUDoperation.DELETE));// für die richtigen Zeiten
																						// müssen noch die Sysouts aus
																						// den Methoden genommen werden.
				start = Instant.now();
				this.deleteBewertungByArtikelNrAndKundenNr(artikel.getArtikelNummer(), kunde.getKundenNummer());
				end = Instant.now();
				timeMeasures.add(new AccessTime("Bewertung by Artikel- und Kundennummer",
						Duration.between(start, end).toMillis(), CRUDoperation.DELETE));

				start = Instant.now();
				this.deleteAdresseByKundenNr(kunde.getKundenNummer());
				end = Instant.now();
				timeMeasures.add(new AccessTime("Adresse by Kundennummer", Duration.between(start, end).toMillis(),
						CRUDoperation.DELETE));

				start = Instant.now();
				this.deleteKundeByKundenNr(kunde.getKundenNummer());
				end = Instant.now();
				timeMeasures.add(new AccessTime("Kunde by Kundennummer", Duration.between(start, end).toMillis(),
						CRUDoperation.DELETE));

				start = Instant.now();
				this.deleteArtikelbyArtikelNr(artikel.getArtikelNummer());
				end = Instant.now();
				timeMeasures.add(new AccessTime("Artikel by Artikelnummer", Duration.between(start, end).toMillis(),
						CRUDoperation.DELETE));

				// Insert Operations
				System.out.println("Hinzufügen");

				start = Instant.now();
				this.addKunde(kunde);
				end = Instant.now();
				timeMeasures
						.add(new AccessTime("Kunde", Duration.between(start, end).toMillis(), CRUDoperation.INSERT));

				start = Instant.now();
				this.addArtikel(artikel);
				end = Instant.now();
				timeMeasures
						.add(new AccessTime("Artikel", Duration.between(start, end).toMillis(), CRUDoperation.INSERT));

				start = Instant.now();
				this.addBewertung(bewertung);
				end = Instant.now();
				timeMeasures.add(
						new AccessTime("Bewertung", Duration.between(start, end).toMillis(), CRUDoperation.INSERT));

				start = Instant.now();
				this.addKauf(kauf);
				end = Instant.now();
				timeMeasures.add(new AccessTime("Kauf", Duration.between(start, end).toMillis(), CRUDoperation.INSERT));

				try {
					CsvBeanWriter.writeCsvFromAccessTimeExample(timeMeasures);
				} catch (Exception e) {
					e.printStackTrace();
				}

				// Select Operations
				System.out.println("Alle Select Operationen");
				// Kunde
				System.out.println("Alle für Kunde: \n\n");
				printOutSelect(this.getKundeByKundenNr(kunde.getKundenNummer()));
				printOutSelect(this.getKundenByNachName(kunde.getNachname()));
				printOutSelect(this.getKundenByPlz(kunde.getAdresse().getPlz()));
				printOutSelect(this.getKundeByEmail(kunde.getEmail()));

				// printOutSelect(this.getDistinctOrte());//die Methode funktioniert so noch
				// nicht

				// Artikel
				System.out.println("Alle für Artikel: \n\n");
				printOutSelect(this.getArtikelByArtikelNummer(artikel.getArtikelNummer()));
				printOutSelect(this.getArtikelByArtikelName(artikel.getArtikelName()));
				printOutSelect(this.getArtikelWhichCostMoreThan(artikel.getEinzelPreis()));

				// Bewertung
				System.out.println("Alle für Bewertung: \n\n");
				printOutSelect(
						this.getBewertungByKundenNrAndArtikelNr(artikel.getArtikelNummer(), kunde.getKundenNummer()));
				printOutSelect(this.getBewertungenByArtikelNr(artikel.getArtikelNummer()));
				printOutSelect(this.getBewertungenByKundenNr(kunde.getKundenNummer()));
				printOutSelect(this.getBewertungenByAnzahlSterne(bewertung.getSterne()));

				// Kauf
				System.out.println("Alle für Kauf: \n\n");
				printOutSelect(this.getEinkaeufeForKunde(kunde.getKundenNummer()));
				printOutSelect(this.getVerkauefeForArtikel(artikel.getArtikelNummer()));

				// Update Objects
				kunde.setNachname("Schaak");
				kunde.getAdresse().setHausnummer(kunde.getAdresse().getHausnummer() + "a");
				artikel.setArtikelName(artikel.getArtikelName() + " v1.10");
				artikel.setBeschreibung(artikel.getBeschreibung() + " jetzt mit einer neuen Version");
				bewertung.setSterne(bewertung.getSterne() + 1);

				// Update
				this.updateKunde(kunde);
				this.updateArtikel(artikel);
				this.updateBewertung(bewertung);

				// Select of changed objects
				System.out.println("Select für alle geänderten Objekte: \n\n");
				printOutSelect(this.getKundenByNachName(kunde.getNachname()));
				printOutSelect(this.getArtikelByArtikelName(artikel.getArtikelName()));
				printOutSelect(this.getBewertungenByAnzahlSterne(bewertung.getSterne()));

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
					+ ".Kunde (Kundennummer, Vorname, Nachname, Email, Telefonnummer) VALUES('"
					+ kunde.getKundenNummer() + "', '" + kunde.getVorname() + "', '" + kunde.getNachname() + "', '"
					+ kunde.getEmail() + "', '" + kunde.getTelefonNummer() + "')");
			statement.executeUpdate("INSERT INTO " + databaseName
					+ ".Adresse (PLZ, Strasse, Hausnummer, Ortschaft, Kundennummer) VALUES('" + adresse.getPlz()
					+ "', '" + adresse.getStrasse() + "', '" + adresse.getHausnummer() + "', '" + adresse.getOrtschaft()
					+ "', '" + kunde.getKundenNummer() + "')");
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
			statement.executeUpdate("INSERT INTO " + databaseName
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

//		Andere Möglichkeit zur Ausführung von Statements

		String sql = "INSERT INTO " + databaseName
				+ ".Kauf (Kaufdatum, Kaufpreis, Menge, Kundennummer, Artikelnummer) VALUES(?,?,?,?,?)";
		PreparedStatement prest;
		try {
			prest = mariaDbConnection.prepareStatement(sql);
			prest.setDate(1, kauf.getKaufdatum());
			prest.setDouble(2, kauf.getKaufPreis());
			prest.setInt(3, kauf.getMenge());
			prest.setString(4, kauf.getKundenNr());
			prest.setString(5, kauf.getArtikelNr());
			prest.executeUpdate();
			return kauf;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
//	    
//		try {
//			statement.executeUpdate("INSERT INTO " + databaseName
//					+ ".Kauf (Kaufdatum, Kaufpreis, Menge, Kundennummer, Artikelnummer) VALUES('" + kauf.getKaufdatum()
//					+ "', " + kauf.getKaufPreis() + ", " + kauf.getMenge() + ", '" + kauf.getKundenNr() + "', '"
//					+ kauf.getArtikelNr() + "')");
//			return kauf;
//		} catch (SQLException e) {
//			e.printStackTrace();
//			return null;
//		}
	}

	public Kunde getKundeByKundenNr(String kundenNr) {
		Kunde kunde = new Kunde();
		try {
			ResultSet resultSet = statement
					.executeQuery("SELECT * FROM " + databaseName + ".Kunde WHERE Kundennummer='" + kundenNr + "'");
			ResultSet resultSetAdresse = statement
					.executeQuery("SELECT * FROM " + databaseName + ".Adresse WHERE Kundennummer='" + kundenNr + "'");
//					+ "FULL JOIN "+ databaseName + ".Adresse WHERE Kundennummer='" + kundenNr + "'"); Für die Adresse mit hinzu
			while (resultSet.next()) {
				kunde.setKundenNummer(resultSet.getString("Kundennummer"));
				kunde.setEmail(resultSet.getString("Email"));
				kunde.setTelefonNummer(resultSet.getString("Telefonnummer"));
				kunde.setVorname(resultSet.getString("Vorname"));
				kunde.setNachname(resultSet.getString("Nachname"));
				// wie soll das hier gehen? kommt das in der Antwort einfach so mit?? Wird die
				// mit geladen weil kundennummer der foreign Kay ist? -> habe es erstmal mit
				// einem JOIN probiert aber vielleicht geht es ja auch ohne
			}
			while (resultSetAdresse.next())
				kunde.setAdresse(
						new Adresse(resultSetAdresse.getString("Ortschaft"), resultSetAdresse.getString("Hausnummer"),
								resultSetAdresse.getString("Strasse"), resultSetAdresse.getString("PLZ")));
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
					+ ".Bewertung WHERE Artikelnummer='" + artikelNummer + "' AND Kundennummer='" + kundenNummer + "'");
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
				kauf.setKaufdatum(resultSet.getDate("Kaufdatum"));
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
				kauf.setKaufdatum(resultSet.getDate("Kaufdatum"));
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
			statement.executeUpdate("UPDATE " + databaseName + ".Kunde SET Vorname='" + kunde.getVorname()
					+ "', Nachname='" + kunde.getNachname() + "', Email='" + kunde.getEmail() + "', Telefonnummer='"
					+ kunde.getTelefonNummer() + "' WHERE Kundennummer='" + kunde.getKundenNummer() + "'");
			Adresse adresse = kunde.getAdresse();
			statement.executeUpdate("UPDATE " + databaseName + ".Adresse SET Strasse='" + adresse.getStrasse()
					+ "', Hausnummer='" + adresse.getHausnummer() + "', Ortschaft='" + adresse.getOrtschaft()
					+ "', PLZ='" + adresse.getPlz() + "' WHERE Kundennummer='" + kunde.getKundenNummer() + "'");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return kunde;
	}

	public Artikel updateArtikel(Artikel artikel) {
		try {
			statement.executeUpdate("UPDATE " + databaseName + ".Artikel SET Artikelname='" + artikel.getArtikelName()
					+ "', Beschreibung='" + artikel.getBeschreibung() + "', Einzelpreis=" + artikel.getEinzelPreis()
					+ ", Waehrung='" + artikel.getWaehrung() + "' WHERE Artikelnummer='" + artikel.getArtikelNummer()
					+ "'");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return artikel;
	}

	public Bewertung updateBewertung(Bewertung bewertung) {
		try {
			statement.executeUpdate("UPDATE " + databaseName + ".Bewertung SET Bewertung='" + bewertung.getBewertung()
					+ "', Sterne=" + bewertung.getSterne() + " WHERE Artikelnummer='" + bewertung.getArtikelNummer()
					+ "' AND Kundennummer='" + bewertung.getKundenNummer() + "'");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return bewertung;
	}

	public void deleteKundeByKundenNr(String kundenNr) {
		try {
			statement.executeUpdate("DELETE FROM " + databaseName + ".Kunde WHERE Kundennummer='" + kundenNr + "'");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void deleteArtikelbyArtikelNr(String artikelNr) {
		try {
			statement.executeUpdate("DELETE FROM " + databaseName + ".Artikel WHERE Artikelnummer='" + artikelNr + "'");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void deleteBewertungByArtikelNrAndKundenNr(String artikelnummer, String kundennummer) {
		try {
			statement.executeUpdate("DELETE FROM " + databaseName + ".Bewertung WHERE Kundennummer='" + kundennummer
					+ "' AND Artikelnummer='" + artikelnummer + "'");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void deleteKaufByArtikelNrAndKundenNr(String artikelNr, String kundennummer) {
		try {
			statement.executeUpdate("DELETE FROM " + databaseName + ".Kauf WHERE Kundennummer='" + kundennummer
					+ "' AND Artikelnummer='" + artikelNr + "'");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void deleteAdresseByKundenNr(String kundennummer) {
		try {
			statement.executeUpdate(
					"DELETE FROM " + databaseName + ".Adresse WHERE Kundennummer='" + kundennummer + "'");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
