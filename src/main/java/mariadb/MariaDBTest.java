package mariadb;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import dbinterface.Adresse;
import dbinterface.Artikel;
import dbinterface.Bewertung;
import dbinterface.DBInterface;
import dbinterface.Kauf;
import dbinterface.Kunde;
import measure.AccessTime;
import measure.CRUDoperation;
import measure.CsvBeanWriter;

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

				long differenz = this.deleteKaufByArtikelNrAndKundenNr(artikel.getArtikelNummer(),
						kunde.getKundenNummer());
				timeMeasures.add(new AccessTime("Kauf by Artikel- und Kundennummer", differenz, CRUDoperation.DELETE));

				differenz = this.deleteBewertungByArtikelNrAndKundenNr(artikel.getArtikelNummer(),
						kunde.getKundenNummer());
				timeMeasures
						.add(new AccessTime("Bewertung by Artikel- und Kundennummer", differenz, CRUDoperation.DELETE));

				differenz = this.deleteAdresseByKundenNr(kunde.getKundenNummer());
				timeMeasures.add(new AccessTime("Adresse by Kundennummer", differenz, CRUDoperation.DELETE));

				differenz = this.deleteKundeByKundenNr(kunde.getKundenNummer());
				timeMeasures.add(new AccessTime("Kunde by Kundennummer", differenz, CRUDoperation.DELETE));

				differenz = this.deleteArtikelbyArtikelNr(artikel.getArtikelNummer());
				timeMeasures.add(new AccessTime("Artikel by Artikelnummer", differenz, CRUDoperation.DELETE));

				// Insert Operations
				System.out.println("Hinzufügen");

				differenz = this.addKunde(kunde);
				timeMeasures.add(new AccessTime("Kunde", differenz, CRUDoperation.INSERT));

				this.addArtikel(artikel);
				timeMeasures.add(new AccessTime("Artikel", differenz, CRUDoperation.INSERT));

				differenz = this.addBewertung(bewertung);
				timeMeasures.add(new AccessTime("Bewertung", differenz, CRUDoperation.INSERT));

				differenz = this.addKauf(kauf);
				timeMeasures.add(new AccessTime("Kauf", differenz, CRUDoperation.INSERT));

				// Select Operations
				System.out.println("Alle Select Operationen");
				// Kunde
				System.out.println("Alle für Kunde: \n\n");
				differenz = this.getKundeByKundenNr(kunde.getKundenNummer());
				timeMeasures.add(new AccessTime("get Kunde by Kundennummer", differenz, CRUDoperation.SELECT));

				differenz = this.getKundenByNachName(kunde.getNachname());
				timeMeasures.add(new AccessTime("get Kunde by Nachname", differenz, CRUDoperation.SELECT));

				differenz = this.getKundenByPlz(kunde.getAdresse().getPlz());
				timeMeasures.add(new AccessTime("get Kunde by PLZ", differenz, CRUDoperation.SELECT));

				differenz = this.getKundeByEmail(kunde.getEmail());
				timeMeasures.add(new AccessTime("get Kunde by Email", differenz, CRUDoperation.SELECT));

				differenz = this.getDistinctOrte();
				timeMeasures.add(new AccessTime("get Distinct Orte", differenz, CRUDoperation.SELECT));

				// Artikel
				System.out.println("Alle für Artikel: \n\n");

				differenz = this.getArtikelByArtikelNummer(artikel.getArtikelNummer());
				timeMeasures.add(new AccessTime("get Artikel by Artikelnummer", differenz, CRUDoperation.SELECT));

				differenz = this.getArtikelByArtikelName(artikel.getArtikelName());
				timeMeasures.add(new AccessTime("get Artikel by Artikelname", differenz, CRUDoperation.SELECT));

				differenz = this.getArtikelWhichCostMoreThan(artikel.getEinzelPreis());
				timeMeasures.add(new AccessTime("get Artikel which price is higher than " + artikel.getEinzelPreis(),
						differenz, CRUDoperation.SELECT));

				// Bewertung
				System.out.println("Alle für Bewertung: \n\n");
				differenz = this.getBewertungByKundenNrAndArtikelNr(artikel.getArtikelNummer(),
						kunde.getKundenNummer());
				timeMeasures.add(new AccessTime("get Bewertung by Artikelnummer und Kundennummer", differenz,
						CRUDoperation.SELECT));

				differenz = this.getBewertungenByArtikelNr(artikel.getArtikelNummer());
				timeMeasures.add(new AccessTime("get Bewertung by Artikelnummer", differenz, CRUDoperation.SELECT));
				differenz = this.getBewertungenByKundenNr(kunde.getKundenNummer());
				timeMeasures.add(new AccessTime("get Bewertung by Kundennummer", differenz, CRUDoperation.SELECT));
				differenz = this.getBewertungenByAnzahlSterne(bewertung.getSterne());
				timeMeasures.add(new AccessTime("get Bewertung by Sterne", differenz, CRUDoperation.SELECT));

				// Kauf
				System.out.println("Alle für Kauf: \n\n");

				differenz = this.getEinkaeufeForKunde(kunde.getKundenNummer());
				timeMeasures.add(new AccessTime("get Kaeufe for Kundennummer", differenz, CRUDoperation.SELECT));
				differenz = this.getVerkauefeForArtikel(artikel.getArtikelNummer());
				timeMeasures.add(new AccessTime("get Verkaeufe for Artikel", differenz, CRUDoperation.SELECT));

				try {
					CsvBeanWriter.writeCsvFromAccessTimeExample(timeMeasures);
				} catch (Exception e) {
					e.printStackTrace();
				}
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

	public long addKunde(Kunde kunde) {

		Adresse adresse = kunde.getAdresse();
		String sqlKunde = "INSERT INTO " + databaseName
				+ ".Kunde (Kundennummer, Vorname, Nachname, Email, Telefonnummer) VALUES(?,?,?,?,?)";
		PreparedStatement prestKunde;
		String sqlAdresse = "INSERT INTO " + databaseName
				+ ".Adresse (PLZ, Strasse, Hausnummer, Ortschaft, Kundennummer) VALUES(?,?,?,?,?)";
		PreparedStatement prestAdresse;
		try {
			prestKunde = mariaDbConnection.prepareStatement(sqlKunde);
			prestAdresse = mariaDbConnection.prepareStatement(sqlAdresse);

			prestKunde.setString(1, kunde.getKundenNummer());
			prestKunde.setString(2, kunde.getVorname());
			prestKunde.setString(3, kunde.getNachname());
			prestKunde.setString(4, kunde.getEmail());
			prestKunde.setString(5, kunde.getTelefonNummer());

			prestAdresse.setString(1, adresse.getPlz());
			prestAdresse.setString(2, adresse.getStrasse());
			prestAdresse.setString(3, adresse.getHausnummer());
			prestAdresse.setString(4, adresse.getOrtschaft());
			prestAdresse.setString(5, kunde.getKundenNummer());

			start = Instant.now();
			prestKunde.executeUpdate();
			prestAdresse.executeUpdate();
			end = Instant.now();
//		try {
//			start = Instant.now();
//			statement.executeUpdate("INSERT INTO " + databaseName
//					+ ".Kunde (Kundennummer, Vorname, Nachname, Email, Telefonnummer) VALUES('"
//					+ kunde.getKundenNummer() + "', '" + kunde.getVorname() + "', '" + kunde.getNachname() + "', '"
//					+ kunde.getEmail() + "', '" + kunde.getTelefonNummer() + "')");
//			statement.executeUpdate("INSERT INTO " + databaseName
//					+ ".Adresse (PLZ, Strasse, Hausnummer, Ortschaft, Kundennummer) VALUES('" + adresse.getPlz()
//					+ "', '" + adresse.getStrasse() + "', '" + adresse.getHausnummer() + "', '" + adresse.getOrtschaft()
//					+ "', '" + kunde.getKundenNummer() + "')");
//			end = Instant.now();
			return Duration.between(start, end).toNanos();
		} catch (SQLException e) {
			System.err.println(e);
			return Long.MAX_VALUE;
		}
	}

	public long addArtikel(Artikel artikel) {

		String sql = "INSERT INTO " + databaseName
				+ ".Artikel (Artikelnummer, Artikelname, Einzelpreis, Waehrung, Beschreibung) VALUES(?,?,?,?,?)";
		PreparedStatement prest;
		try {
			prest = mariaDbConnection.prepareStatement(sql);

			prest.setString(1, artikel.getArtikelNummer());
			prest.setString(2, artikel.getArtikelName());
			prest.setDouble(3, artikel.getEinzelPreis());
			prest.setString(4, artikel.getWaehrung());
			prest.setString(5, artikel.getBeschreibung());

			start = Instant.now();
			prest.executeUpdate();
			end = Instant.now();
//		
//		try {
//			start = Instant.now();
//			statement.executeUpdate("INSERT INTO " + databaseName
//					+ ".Artikel (Artikelnummer, Artikelname, Einzelpreis, Waehrung, Beschreibung) VALUES('"
//					+ artikel.getArtikelNummer() + "', '" + artikel.getArtikelName() + "', " + artikel.getEinzelPreis()
//					+ ", '" + artikel.getWaehrung() + "', '" + artikel.getBeschreibung() + "')");
//			end = Instant.now();
			return Duration.between(start, end).toNanos();
		} catch (SQLException e) {
			System.err.println(e);
			return Long.MAX_VALUE;
		}
	}

	public long addBewertung(Bewertung bewertung) {

		String sql = "INSERT INTO " + databaseName
				+ ".Bewertung (Sterne, Bewertung, Kundennummer, Artikelnummer) VALUES(?,?,?,?)";
		PreparedStatement prest;
		try {
			prest = mariaDbConnection.prepareStatement(sql);

			prest.setInt(1, bewertung.getSterne());
			prest.setString(2, bewertung.getBewertung());
			prest.setString(3, bewertung.getKundenNummer());
			prest.setString(4, bewertung.getArtikelNummer());

			start = Instant.now();
			prest.executeUpdate();
			end = Instant.now();
//		try {
//			start = Instant.now();
//			statement.executeUpdate("INSERT INTO " + databaseName
//					+ ".Bewertung (Sterne, Bewertung, Kundennummer, Artikelnummer) VALUES(" + bewertung.getSterne()
//					+ ", '" + bewertung.getBewertung() + "', '" + bewertung.getKundenNummer() + "', '"
//					+ bewertung.getArtikelNummer() + "')");
//			end = Instant.now();
			return Duration.between(start, end).toNanos();
		} catch (SQLException e) {
			System.err.println(e);
			return Long.MAX_VALUE;
		}
	}

	public long addKauf(Kauf kauf) {

//		Andere Möglichkeit zur Ausführung von Statements

		String sql = "INSERT INTO " + databaseName
				+ ".Kauf (Kaufdatum, Kaufpreis, Menge, Kundennummer, Artikelnummer) VALUES(?,?,?,?,?)";
		PreparedStatement prest;
		try {
			prest = mariaDbConnection.prepareStatement(sql);
			prest.setDate(1, kauf.getKaufdatum());
			prest.setDouble(2, kauf.getKaufPreis());
			prest.setInt(3, kauf.getMenge());
			prest.setString(4, kauf.getKundenNummer());
			prest.setString(5, kauf.getArtikelNummer()); // 80781700 ns für die Methode
			start = Instant.now();
			prest.executeUpdate();
			end = Instant.now();

//			start=Instant.now();
//			statement.executeUpdate("INSERT INTO " + databaseName			//1039883600 für die Methode
//						+ ".Kauf (Kaufdatum, Kaufpreis, Menge, Kundennummer, Artikelnummer) VALUES('" + kauf.getKaufdatum()
//						+ "', " + kauf.getKaufPreis() + ", " + kauf.getMenge() + ", '" + kauf.getKundenNummer() + "', '"
//						+ kauf.getArtikelNummer() + "')");
//			end =Instant.now();
//			System.out.println("Zweite Methode: "+Duration.between(start, end).toNanos()+" ns");

			return Duration.between(start, end).toNanos();
		} catch (SQLException e) {
			System.err.println(e);
			return Long.MAX_VALUE;
		}

	}

	public long getKundeByKundenNr(String kundenNr) {

		String sql = "SELECT * FROM " + databaseName + ".Kunde, " + databaseName + ".Adresse WHERE Kunde.Kundennummer='"
				+ kundenNr + "'";
		try {
			start = Instant.now();
			ResultSet resultSet = statement.executeQuery(sql);
			end = Instant.now();
			return Duration.between(start, end).toNanos();
//			while (resultSet.next()) {
//				kunde.setKundenNummer(resultSet.getString("Kundennummer"));
//				kunde.setEmail(resultSet.getString("Email"));
//				kunde.setTelefonNummer(resultSet.getString("Telefonnummer"));
//				kunde.setVorname(resultSet.getString("Vorname"));
//				kunde.setNachname(resultSet.getString("Nachname"));
//				// wie soll das hier gehen? kommt das in der Antwort einfach so mit?? Wird die
//				// mit geladen weil kundennummer der foreign Kay ist? -> habe es erstmal mit
//				// einem JOIN probiert aber vielleicht geht es ja auch ohne
//			}
//			while (resultSetAdresse.next())
//				kunde.setAdresse(
//						new Adresse(resultSetAdresse.getString("Ortschaft"), resultSetAdresse.getString("Hausnummer"),
//								resultSetAdresse.getString("Strasse"), resultSetAdresse.getString("PLZ")));
//			return kunde;
		} catch (SQLException e) {
			System.err.println(e);
			return Long.MAX_VALUE;
		}
	}

	public long getKundeByEmail(String email) {

		String sql = "SELECT * FROM " + databaseName + ".Kunde, " + databaseName + ".Adresse WHERE Kunde.Email='"
				+ email + "'";
		try {
			start = Instant.now();
			statement.executeQuery(sql);
			end = Instant.now();
			return Duration.between(start, end).toNanos();
//			while (resultSet.next()) {
//				kunde.setKundenNummer(resultSet.getString("Kundennummer"));
//				kunde.setEmail(resultSet.getString("Email"));
//				kunde.setTelefonNummer(resultSet.getString("Telefonnummer"));
//				kunde.setVorname(resultSet.getString("Vorname"));
//				kunde.setNachname(resultSet.getString("Nachname"));
//				// wie soll das hier gehen? kommt das in der Antwort einfach so mit?? Wird die
//				// mit geladen weil kundennummer der foreign Kay ist? -> habe es erstmal mit
//				// einem JOIN probiert aber vielleicht geht es ja auch ohne
//				kunde.setAdresse(new Adresse(resultSet.getString("Ortschaft"), resultSet.getString("Hausnummer"),
//						resultSet.getString("Strasse"), resultSet.getString("PLZ")));
//			}
//			return kunde;
		} catch (SQLException e) {
			System.err.println(e);
			return Long.MAX_VALUE;
		}
	}

	public long getKundenByPlz(String plz) {
		
		String sql="SELECT * FROM " + databaseName + ".Kunde, " + databaseName
				+ ".Adresse WHERE Adresse.PLZ='" + plz + "'";
		try {
			start = Instant.now();
			statement.executeQuery(sql);
			end = Instant.now();
			return Duration.between(start, end).toNanos();
//			while (resultSet.next()) {
//				Kunde kunde = new Kunde();
//				kunde.setKundenNummer(resultSet.getString("Kundennummer"));
//				kunde.setEmail(resultSet.getString("Email"));
//				kunde.setTelefonNummer(resultSet.getString("Telefonnummer"));
//				kunde.setVorname(resultSet.getString("Vorname"));
//				kunde.setNachname(resultSet.getString("Nachname"));
//				// wie soll das hier gehen? kommt das in der Antwort einfach so mit?? Wird die
//				// mit geladen weil kundennummer der foreign Kay ist? -> habe es erstmal mit
//				// einem JOIN probiert aber vielleicht geht es ja auch ohne
//				kunde.setAdresse(new Adresse(resultSet.getString("Ortschaft"), resultSet.getString("Hausnummer"),
//						resultSet.getString("Strasse"), resultSet.getString("PLZ")));
//				kundeList.add(kunde);
//			}
//			return kundeList;
		} catch (SQLException e) {
			System.err.println(e);
			return Long.MAX_VALUE;
		}
	}

	public long getKundenByNachName(String nachName) {
		
		String sql="SELECT * FROM " + databaseName + ".Kunde, " + databaseName
				+ ".Adresse WHERE Kunde.Nachname='" + nachName + "'";
		try {
			start = Instant.now();
			statement.executeQuery(sql);
			end = Instant.now();
			return Duration.between(start, end).toNanos();
//			while (resultSet.next()) {
//				Kunde kunde = new Kunde();
//				kunde.setKundenNummer(resultSet.getString("Kundennummer"));
//				kunde.setEmail(resultSet.getString("Email"));
//				kunde.setTelefonNummer(resultSet.getString("Telefonnummer"));
//				kunde.setVorname(resultSet.getString("Vorname"));
//				kunde.setNachname(resultSet.getString("Nachname"));
//				// wie soll das hier gehen? kommt das in der Antwort einfach so mit?? Wird die
//				// mit geladen weil kundennummer der foreign Kay ist? -> habe es erstmal mit
//				// einem JOIN probiert aber vielleicht geht es ja auch ohne
//				kunde.setAdresse(new Adresse(resultSet.getString("Ortschaft"), resultSet.getString("Hausnummer"),
//						resultSet.getString("Strasse"), resultSet.getString("PLZ")));
//				kundeList.add(kunde);
//			}
//			return kundeList;
		} catch (SQLException e) {
			System.err.println(e);
			return Long.MAX_VALUE;
		}
	}

	public long getDistinctOrte() {
		String sql="SELECT Ortschaft FROM " + databaseName + ".Adresse";
		try {
			start = Instant.now();
			statement.executeQuery(sql);
			end = Instant.now();
			return Duration.between(start, end).toNanos();
//			while (resultSet.next()) {
//				String ort = resultSet.getString("Ortschaft");
//				ortList.add(ort);
//			}
//			return ortList;
		} catch (SQLException e) {
			System.err.println(e);
			return Long.MAX_VALUE;
		}
	}

	public long getArtikelByArtikelNummer(String artikelNummer) {
		String sql="SELECT * FROM " + databaseName + ".Artikel WHERE Artikelnummer='" + artikelNummer + "'";
		try {
			start = Instant.now();
			statement.executeQuery(sql);
			end = Instant.now();
			return Duration.between(start, end).toNanos();
//			while (resultSet.next()) {
//				artikel.setArtikelNummer(resultSet.getString("Artikelnummer"));
//				artikel.setArtikelName(resultSet.getString("Artikelname"));
//				artikel.setEinzelPreis(resultSet.getDouble("Einzelpreis"));
//				artikel.setWaehrung(resultSet.getString("Waehrung"));
//				artikel.setBeschreibung(resultSet.getString("Beschreibung"));
//			}
//			return artikel;
		} catch (SQLException e) {
			System.err.println(e);
			return Long.MAX_VALUE;
		}
	}

	public long getArtikelByArtikelName(String artikelName) {
		String sql="SELECT * FROM " + databaseName + ".Artikel WHERE Artikelname='" + artikelName + "'";
		try {
			start = Instant.now();
			statement
					.executeQuery(sql);
			end = Instant.now();
			return Duration.between(start, end).toNanos();
//			while (resultSet.next()) {
//				artikel.setArtikelNummer(resultSet.getString("Artikelnummer"));
//				artikel.setArtikelName(resultSet.getString("Artikelname"));
//				artikel.setEinzelPreis(resultSet.getDouble("Einzelpreis"));
//				artikel.setWaehrung(resultSet.getString("Waehrung"));
//				artikel.setBeschreibung(resultSet.getString("Beschreibung"));
//			}
//			return artikel;
		} catch (SQLException e) {
			System.err.println(e);
			return Long.MAX_VALUE;
		}
	}

	public long getArtikelWhichCostMoreThan(Double price) {
		List<Artikel> artikelList = new ArrayList<Artikel>();
		String sql="SELECT * FROM " + databaseName + ".Artikel WHERE Einzelpreis>" + price;
		try {
			start = Instant.now();
			ResultSet resultSet = statement
					.executeQuery(sql);
			while (resultSet.next()) {
				Artikel artikel = new Artikel();
				artikel.setArtikelNummer(resultSet.getString("Artikelnummer"));
				artikel.setArtikelName(resultSet.getString("Artikelname"));
				artikel.setEinzelPreis(resultSet.getDouble("Einzelpreis"));
				artikel.setWaehrung(resultSet.getString("Waehrung"));
				artikel.setBeschreibung(resultSet.getString("Beschreibung"));
				artikelList.add(artikel);
			}
			end = Instant.now();
			return Duration.between(start, end).toNanos();
		} catch (SQLException e) {
			System.err.println(e);
			return Long.MAX_VALUE;
		}
	}

	public long getBewertungByKundenNrAndArtikelNr(String artikelNummer, String kundenNummer) {
		String sql="SELECT * FROM " + databaseName + ".Bewertung WHERE Artikelnummer='" + artikelNummer
				+ "' AND Kundennummer='" + kundenNummer + "'";
		try {
			start = Instant.now();
			statement.executeQuery(sql);
			end = Instant.now();
			return Duration.between(start, end).toNanos();
//			while (resultSet.next()) {
//				bewertung.setArtikelNummer(resultSet.getString("Artikelnummer"));
//				bewertung.setKundenNummer(resultSet.getString("Kundennummer"));
//				bewertung.setBewertung(resultSet.getString("Bewertung"));
//				bewertung.setSterne(resultSet.getInt("Sterne"));
//			}
//			return bewertung;
		} catch (SQLException e) {
			System.err.println(e);
			return Long.MAX_VALUE;
		}
	}
	// wir müssen uns noch entscheiden ob wir jetzt die Verarbeitung mitmessen oder nicht
	public long getBewertungenByAnzahlSterne(int sterne) {
		List<Bewertung> bewertungList = new ArrayList<Bewertung>();
		String sql="SELECT * FROM " + databaseName + ".Bewertung WHERE Sterne=" + sterne;
		try {
			start = Instant.now();
			ResultSet resultSet = statement
					.executeQuery();
			while (resultSet.next()) {
				Bewertung bewertung = new Bewertung();
				bewertung.setArtikelNummer(resultSet.getString("Artikelnummer"));
				bewertung.setKundenNummer(resultSet.getString("Kundennummer"));
				bewertung.setBewertung(resultSet.getString("Bewertung"));
				bewertung.setSterne(resultSet.getInt("Sterne"));
				bewertungList.add(bewertung);
			}
			end = Instant.now();
			return Duration.between(start, end).toNanos();
		} catch (SQLException e) {
			System.err.println(e);
			return Long.MAX_VALUE;
		}
	}

	public long getBewertungenByKundenNr(String kundenNummer) {
		List<Bewertung> bewertungList = new ArrayList<Bewertung>();
		String sql="SELECT * FROM " + databaseName + ".Bewertung WHERE Kundennummer='" + kundenNummer + "'";
		try {
			start = Instant.now();
			ResultSet resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				Bewertung bewertung = new Bewertung();
				bewertung.setArtikelNummer(resultSet.getString("Artikelnummer"));
				bewertung.setKundenNummer(resultSet.getString("Kundennummer"));
				bewertung.setBewertung(resultSet.getString("Bewertung"));
				bewertung.setSterne(resultSet.getInt("Sterne"));
				bewertungList.add(bewertung);
			}
			end = Instant.now();
			return Duration.between(start, end).toNanos();
		} catch (SQLException e) {
			System.err.println(e);
			return Long.MAX_VALUE;
		}
	}

	public long getBewertungenByArtikelNr(String artikelNummer) {
		List<Bewertung> bewertungList = new ArrayList<Bewertung>();
		String sql="SELECT * FROM " + databaseName + ".Bewertung WHERE Artikelnummer='" + artikelNummer + "'";
		try {
			start = Instant.now();
			ResultSet resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				Bewertung bewertung = new Bewertung();
				bewertung.setArtikelNummer(resultSet.getString("Artikelnummer"));
				bewertung.setKundenNummer(resultSet.getString("Kundennummer"));
				bewertung.setBewertung(resultSet.getString("Bewertung"));
				bewertung.setSterne(resultSet.getInt("Sterne"));
				bewertungList.add(bewertung);
			}
			end = Instant.now();
			return Duration.between(start, end).toNanos();
		} catch (SQLException e) {
			System.err.println(e);
			return Long.MAX_VALUE;
		}
	}

	public long getEinkaeufeForKunde(String kundenNummer) {
		List<Kauf> kaufList = new ArrayList<Kauf>();
		String sql="SELECT * FROM " + databaseName + ".Kauf WHERE Kundennummer='" + kundenNummer + "'";
		try {
			start = Instant.now();
			ResultSet resultSet = statement
					.executeQuery(sql);
			while (resultSet.next()) {
				Kauf kauf = new Kauf();
				kauf.setArtikelNummer(resultSet.getString("Artikelnummer"));
				kauf.setKundenNummer(resultSet.getString("Kundennummer"));
				kauf.setKaufPreis(resultSet.getDouble("Kaufpreis"));
				kauf.setKaufdatum(resultSet.getDate("Kaufdatum"));
				kauf.setMenge(resultSet.getInt("Menge"));
				kaufList.add(kauf);
			}
			end = Instant.now();
			return Duration.between(start, end).toNanos();
		} catch (SQLException e) {
			System.err.println(e);
			return Long.MAX_VALUE;
		}
	}

	public long getVerkauefeForArtikel(String artikelNummer) {
		List<Kauf> kaufList = new ArrayList<Kauf>();
		String sql="SELECT * FROM " + databaseName + ".Kauf WHERE Artikelnummer='" + artikelNummer + "'";
		try {
			start = Instant.now();
			ResultSet resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				Kauf kauf = new Kauf();
				kauf.setArtikelNummer(resultSet.getString("Artikelnummer"));
				kauf.setKundenNummer(resultSet.getString("Kundennummer"));
				kauf.setKaufPreis(resultSet.getDouble("Kaufpreis"));
				kauf.setKaufdatum(resultSet.getDate("Kaufdatum"));
				kauf.setMenge(resultSet.getInt("Menge"));
				kaufList.add(kauf);
			}
			end = Instant.now();
			return Duration.between(start, end).toNanos();
		} catch (SQLException e) {
			System.err.println(e);
			return Long.MAX_VALUE;
		}
	}

	public long updateKunde(Kunde kunde) {
		try {
			start = Instant.now();
			statement.executeUpdate("UPDATE " + databaseName + ".Kunde SET Vorname='" + kunde.getVorname()
					+ "', Nachname='" + kunde.getNachname() + "', Email='" + kunde.getEmail() + "', Telefonnummer='"
					+ kunde.getTelefonNummer() + "' WHERE Kundennummer='" + kunde.getKundenNummer() + "'");
			Adresse adresse = kunde.getAdresse();
			statement.executeUpdate("UPDATE " + databaseName + ".Adresse SET Strasse='" + adresse.getStrasse()
					+ "', Hausnummer='" + adresse.getHausnummer() + "', Ortschaft='" + adresse.getOrtschaft()
					+ "', PLZ='" + adresse.getPlz() + "' WHERE Kundennummer='" + kunde.getKundenNummer() + "'");
			end = Instant.now();
			return Duration.between(start, end).toNanos();
		} catch (SQLException e) {
			System.err.println(e);
			return Long.MAX_VALUE;
		}
	}

	public long updateArtikel(Artikel artikel) {
		try {
			start = Instant.now();
			statement.executeUpdate("UPDATE " + databaseName + ".Artikel SET Artikelname='" + artikel.getArtikelName()
					+ "', Beschreibung='" + artikel.getBeschreibung() + "', Einzelpreis=" + artikel.getEinzelPreis()
					+ ", Waehrung='" + artikel.getWaehrung() + "' WHERE Artikelnummer='" + artikel.getArtikelNummer()
					+ "'");
			end = Instant.now();
			return Duration.between(start, end).toNanos();
		} catch (SQLException e) {
			System.err.println(e);
			return Long.MAX_VALUE;
		}
	}

	public long updateBewertung(Bewertung bewertung) {
		try {
			start = Instant.now();
			statement.executeUpdate("UPDATE " + databaseName + ".Bewertung SET Bewertung='" + bewertung.getBewertung()
					+ "', Sterne=" + bewertung.getSterne() + " WHERE Artikelnummer='" + bewertung.getArtikelNummer()
					+ "' AND Kundennummer='" + bewertung.getKundenNummer() + "'");
			end = Instant.now();
			return Duration.between(start, end).toNanos();
		} catch (SQLException e) {
			System.err.println(e);
			return Long.MAX_VALUE;
		}
	}

	public long deleteKundeByKundenNr(String kundenNr) {
		try {
			start = Instant.now();
			statement.executeUpdate("DELETE FROM " + databaseName + ".Kunde WHERE Kundennummer='" + kundenNr + "'");
			end = Instant.now();
			return Duration.between(start, end).toNanos();
		} catch (SQLException e) {
			System.err.println(e);
			return Long.MAX_VALUE;
		}
	}

	public long deleteArtikelbyArtikelNr(String artikelNr) {
		try {
			start = Instant.now();
			statement.executeUpdate("DELETE FROM " + databaseName + ".Artikel WHERE Artikelnummer='" + artikelNr + "'");
			end = Instant.now();
			return Duration.between(start, end).toNanos();
		} catch (SQLException e) {
			System.err.println(e);
			return Long.MAX_VALUE;
		}
	}

	public long deleteBewertungByArtikelNrAndKundenNr(String artikelnummer, String kundennummer) {
		try {
			start = Instant.now();
			statement.executeUpdate("DELETE FROM " + databaseName + ".Bewertung WHERE Kundennummer='" + kundennummer
					+ "' AND Artikelnummer='" + artikelnummer + "'");
			end = Instant.now();
			return Duration.between(start, end).toNanos();
		} catch (SQLException e) {
			System.err.println(e);
			return Long.MAX_VALUE;
		}
	}

	public long deleteKaufByArtikelNrAndKundenNr(String artikelNr, String kundennummer) {
		try {
			start = Instant.now();
			statement.executeUpdate("DELETE FROM " + databaseName + ".Kauf WHERE Kundennummer='" + kundennummer
					+ "' AND Artikelnummer='" + artikelNr + "'");
			end = Instant.now();
			return Duration.between(start, end).toNanos();
		} catch (SQLException e) {
			System.err.println(e);
			return Long.MAX_VALUE;
		}

	}

	public long deleteAdresseByKundenNr(String kundennummer) {
		try {
			start = Instant.now();
			statement.executeUpdate(
					"DELETE FROM " + databaseName + ".Adresse WHERE Kundennummer='" + kundennummer + "'");
			end = Instant.now();
			return Duration.between(start, end).toNanos();
		} catch (SQLException e) {
			System.err.println(e);
			return Long.MAX_VALUE;
		}

	}

}
