package mariadb;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
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

public class MariaDBTest implements DBInterface {

	static final String JDBC_DRIVER = "org.mariadb.jdbc.Driver";

	String connectionString;
	String username;
	String password;
	Connection mariaDbConnection;
	Statement statement;
	String databaseName = "Webshop";
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

	public void connect() throws SQLException {
		System.out.println("Verbindung zu MariaDB erfolgt...");
		mariaDbConnection = DriverManager.getConnection("jdbc:mariadb://" + connectionString, username, password);
		statement = mariaDbConnection.createStatement();
	}

	public boolean setupDB() {
		try {
			cleanData();
			statement.execute("CREATE DATABASE IF NOT EXISTS " + databaseName);
			statement.execute("CREATE TABLE IF NOT EXISTS " + databaseName
					+ ".Kunde (Kundennummer varchar(255), Vorname varchar(255), Nachname varchar(255), Email varchar(255), Telefonnummer varchar(255), primary key (Kundennummer))");
			statement.execute("CREATE TABLE IF NOT EXISTS " + databaseName
					+ ".Ortschaft (PLZ varchar(255), Ortschaft varchar(255), primary key (PLZ))");
			statement.execute("CREATE TABLE IF NOT EXISTS " + databaseName
					+ ".Adresse (PLZ varchar(255), Strasse varchar(255), Hausnummer varchar(255), Kundennummer varchar(255), FOREIGN KEY (PLZ) REFERENCES Ortschaft(PLZ), FOREIGN KEY (Kundennummer) REFERENCES Kunde(Kundennummer), primary key (Kundennummer))");
			statement.execute("CREATE TABLE IF NOT EXISTS " + databaseName
					+ ".Artikel (Artikelnummer varchar(255),Artikelname varchar(255), Einzelpreis double(20,2), Waehrung varchar(255), Beschreibung varchar(255), primary key (Artikelnummer))");
			statement.execute("CREATE TABLE IF NOT EXISTS " + databaseName
					+ ".Bewertung (Sterne int, Bewertung varchar(1050), Kundennummer varchar(255), Artikelnummer varchar(255), FOREIGN KEY (Kundennummer) REFERENCES Kunde(Kundennummer),  FOREIGN KEY (Artikelnummer) REFERENCES Artikel(Artikelnummer), CONSTRAINT Bewertungsnummer PRIMARY KEY (Kundennummer, Artikelnummer))");
			statement.execute("CREATE TABLE IF NOT EXISTS " + databaseName
					+ ".Kauf (Kaufdatum Date, Kaufpreis double(20,2), Menge int, Kundennummer varchar(255), Artikelnummer varchar(255), FOREIGN KEY (Kundennummer) REFERENCES Kunde(Kundennummer),  FOREIGN KEY (Artikelnummer) REFERENCES Artikel(Artikelnummer), CONSTRAINT Kaufnummer PRIMARY KEY (Kundennummer, Artikelnummer))");
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
		String sqlOrtschaft = "INSERT IGNORE INTO " + databaseName + ".Ortschaft (PLZ, Ortschaft) VALUES(?,?)";
		PreparedStatement prestOrtschaft;
		String sqlAdresse = "INSERT INTO " + databaseName
				+ ".Adresse (PLZ, Strasse, Hausnummer, Kundennummer) VALUES(?,?,?,?)";
		PreparedStatement prestAdresse;
		try {
			prestKunde = mariaDbConnection.prepareStatement(sqlKunde);
			prestOrtschaft = mariaDbConnection.prepareStatement(sqlOrtschaft);
			prestAdresse = mariaDbConnection.prepareStatement(sqlAdresse);

			prestKunde.setString(1, kunde.getKundenNummer());
			prestKunde.setString(2, kunde.getVorname());
			prestKunde.setString(3, kunde.getNachname());
			prestKunde.setString(4, kunde.getEmail());
			prestKunde.setString(5, kunde.getTelefonNummer());

			prestOrtschaft.setString(1, adresse.getPlz());
			prestOrtschaft.setString(2, adresse.getOrtschaft());

			prestAdresse.setString(1, adresse.getPlz());
			prestAdresse.setString(2, adresse.getStrasse());
			prestAdresse.setString(3, adresse.getHausnummer());
			prestAdresse.setString(4, kunde.getKundenNummer());

			start = Instant.now();
			prestKunde.executeUpdate();
			prestOrtschaft.executeUpdate();
			prestAdresse.executeUpdate();
			end = Instant.now();
			return Duration.between(start, end).toMillis();
		} catch (SQLException e) {
			System.err.println(e);
			return Long.MAX_VALUE;
		}
	}

	public long addKunden(List<Kunde> kundenList) {

		String sqlKunde = "INSERT INTO " + databaseName
				+ ".Kunde (Kundennummer, Vorname, Nachname, Email, Telefonnummer) VALUES(?,?,?,?,?)";
		PreparedStatement prestKunde;
		String sqlOrtschaft = "INSERT IGNORE INTO " + databaseName + ".Ortschaft (PLZ, Ortschaft) VALUES(?,?)";
		PreparedStatement prestOrtschaft;
		String sqlAdresse = "INSERT INTO " + databaseName
				+ ".Adresse (PLZ, Strasse, Hausnummer, Kundennummer) VALUES(?,?,?,?)";
		PreparedStatement prestAdresse;
		try {
			prestKunde = mariaDbConnection.prepareStatement(sqlKunde);
			prestOrtschaft = mariaDbConnection.prepareStatement(sqlOrtschaft);
			prestAdresse = mariaDbConnection.prepareStatement(sqlAdresse);

			Iterator<Kunde> iterator = kundenList.iterator();

			while (iterator.hasNext()) {
				Kunde kunde = iterator.next();
				Adresse adresse = kunde.getAdresse();

				prestKunde.setString(1, kunde.getKundenNummer());
				prestKunde.setString(2, kunde.getVorname());
				prestKunde.setString(3, kunde.getNachname());
				prestKunde.setString(4, kunde.getEmail());
				prestKunde.setString(5, kunde.getTelefonNummer());
				prestKunde.addBatch();
//				prestKunde.clearParameters(); zum Clearen - weiß nicht in wie weit das nötig sein wird

				prestOrtschaft.setString(1, adresse.getPlz());
				prestOrtschaft.setString(2, adresse.getOrtschaft());
				prestOrtschaft.addBatch();
//				prestOrtschaft.clearParameters();

				prestAdresse.setString(1, adresse.getPlz());
				prestAdresse.setString(2, adresse.getStrasse());
				prestAdresse.setString(3, adresse.getHausnummer());
				prestAdresse.setString(4, kunde.getKundenNummer());
				prestAdresse.addBatch();
//				prestAdresse.clearParameters();
			}
			start = Instant.now();
			prestKunde.executeBatch();
			prestOrtschaft.executeBatch();
			prestAdresse.executeBatch();
			end = Instant.now();
			return Duration.between(start, end).toMillis();
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

			return Duration.between(start, end).toMillis();
		} catch (SQLException e) {
			System.err.println(e);
			return Long.MAX_VALUE;
		}
	}

	public long addArtikelListe(List<Artikel> artikelList) {

		String sql = "INSERT INTO " + databaseName
				+ ".Artikel (Artikelnummer, Artikelname, Einzelpreis, Waehrung, Beschreibung) VALUES(?,?,?,?,?)";
		PreparedStatement prest;
		try {
			prest = mariaDbConnection.prepareStatement(sql);

			Iterator<Artikel> iterator = artikelList.iterator();
			while (iterator.hasNext()) {
				Artikel artikel = iterator.next();

				prest.setString(1, artikel.getArtikelNummer());
				prest.setString(2, artikel.getArtikelName());
				prest.setDouble(3, artikel.getEinzelPreis());
				prest.setString(4, artikel.getWaehrung());
				prest.setString(5, artikel.getBeschreibung());
				prest.addBatch();
//				prest.clearParameters();

			}
			start = Instant.now();
			prest.executeBatch();
			end = Instant.now();

			return Duration.between(start, end).toMillis();
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
			return Duration.between(start, end).toMillis();
		} catch (SQLException e) {
			System.err.println(e);
			return Long.MAX_VALUE;
		}
	}

	public long addBewertungen(List<Bewertung> bewertungList) {

		String sql = "INSERT INTO " + databaseName
				+ ".Bewertung (Sterne, Bewertung, Kundennummer, Artikelnummer) VALUES(?,?,?,?)";
		PreparedStatement prest;
		try {
			prest = mariaDbConnection.prepareStatement(sql);

			Iterator<Bewertung> iterator = bewertungList.iterator();
			while (iterator.hasNext()) {
				Bewertung bewertung = iterator.next();
				prest.setInt(1, bewertung.getSterne());
				prest.setString(2, bewertung.getBewertung());
				prest.setString(3, bewertung.getKundenNummer());
				prest.setString(4, bewertung.getArtikelNummer());
				prest.addBatch();
//				prest.clearParameters();
			}
			start = Instant.now();
			prest.executeBatch();
			end = Instant.now();
			return Duration.between(start, end).toMillis();
		} catch (SQLException e) {
			System.err.println(e);
			return Long.MAX_VALUE;
		}
	}

	public long addKauf(Kauf kauf) {

		String sql = "INSERT INTO " + databaseName
				+ ".Kauf (Kaufdatum, Kaufpreis, Menge, Kundennummer, Artikelnummer) VALUES (?,?,?,?,?)";
		PreparedStatement prest;
		try {
			prest = mariaDbConnection.prepareStatement(sql);
			prest.setDate(1, kauf.getKaufdatum());
			prest.setDouble(2, kauf.getKaufPreis());
			prest.setInt(3, kauf.getMenge());
			prest.setString(4, kauf.getKundenNummer());
			prest.setString(5, kauf.getArtikelNummer());
			start = Instant.now();
			prest.executeUpdate();
			end = Instant.now();

			return Duration.between(start, end).toMillis();
		} catch (SQLException e) {
			System.err.println(e);
			return Long.MAX_VALUE;
		}

	}

	public long addKaeufe(List<Kauf> kaufList) {

		String sql = "INSERT INTO " + databaseName
				+ ".Kauf (Kaufdatum, Kaufpreis, Menge, Kundennummer, Artikelnummer) VALUES (?,?,?,?,?)";
		PreparedStatement prest;
		try {
			prest = mariaDbConnection.prepareStatement(sql);

			Iterator<Kauf> iterator = kaufList.iterator();
			while (iterator.hasNext()) {
				Kauf kauf=iterator.next();
				prest.setDate(1, kauf.getKaufdatum());
				prest.setDouble(2, kauf.getKaufPreis());
				prest.setInt(3, kauf.getMenge());
				prest.setString(4, kauf.getKundenNummer());
				prest.setString(5, kauf.getArtikelNummer());
				prest.addBatch();
//				prest.clearParameters();
			}
			start = Instant.now();
			prest.executeBatch();
			end = Instant.now();

			return Duration.between(start, end).toMillis();
		} catch (SQLException e) {
			System.err.println(e);
			return Long.MAX_VALUE;
		}

	}

	public long getKundeByKundenNr(String kundenNr) {

		String sql = "SELECT kunde.Kundennummer, kunde.vorname, kunde.nachname, kunde.email, kunde.telefonnummer, adresse.plz, ortschaft.ortschaft, adresse.hausnummer, adresse.strasse "
				+ "FROM " + databaseName +".kunde, " + databaseName +".adresse, " + databaseName +".ortschaft WHERE kunde.Kundennummer = '" + kundenNr + "' AND adresse.kundennummer = kunde.kundennummer AND ortschaft.plz = adresse.plz";

		try {
			start = Instant.now();
			statement.executeQuery(sql);
			end = Instant.now();
			return Duration.between(start, end).toMillis();
//			while (resultSet.next()) {
//				kunde.setKundenNummer(resultSet.getString("Kundennummer"));
//				kunde.setEmail(resultSet.getString("Email"));
//				kunde.setTelefonNummer(resultSet.getString("Telefonnummer"));
//				kunde.setVorname(resultSet.getString("Vorname"));
//				kunde.setNachname(resultSet.getString("Nachname"));
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

	@Override
	public Kunde getKundeByKundenNr_kunde(String kundenNr) {
		String sql = "SELECT kunde.Kundennummer, kunde.vorname, kunde.nachname, kunde.email, kunde.telefonnummer, adresse.plz, ortschaft.ortschaft, adresse.hausnummer, adresse.strasse "
				+ "FROM " + databaseName +".kunde, " + databaseName +".adresse, " + databaseName +".ortschaft WHERE kunde.Kundennummer = '" + kundenNr + "' AND adresse.kundennummer = kunde.kundennummer AND ortschaft.plz = adresse.plz";

		try {

			ResultSet resultSet = statement.executeQuery(sql);
			Kunde kunde = new Kunde();

			while (resultSet.next()) {
				kunde.setKundenNummer(resultSet.getString("Kundennummer"));
				kunde.setEmail(resultSet.getString("Email"));
				kunde.setTelefonNummer(resultSet.getString("Telefonnummer"));
				kunde.setVorname(resultSet.getString("Vorname"));
				kunde.setNachname(resultSet.getString("Nachname"));
			}

			while (resultSet.next())
				kunde.setAdresse(new Adresse(resultSet.getString("Ortschaft"), resultSet.getString("Hausnummer"),
						resultSet.getString("Strasse"), resultSet.getString("PLZ")));
			return kunde;
		} catch (SQLException e) {
			System.err.println(e);
			return null;
		}
	}

	public long getKundeByEmail(String email) {

		String sql = "SELECT kunde.Kundennummer, kunde.vorname, kunde.nachname, kunde.email, kunde.telefonnummer, adresse.plz, ortschaft.ortschaft, adresse.hausnummer, adresse.strasse "
				+ "FROM " + databaseName +".kunde, " + databaseName +".adresse, " + databaseName +".ortschaft WHERE kunde.email = '" + email + "' AND adresse.kundennummer = kunde.kundennummer AND ortschaft.plz = adresse.plz";

		try {
			start = Instant.now();
			statement.executeQuery(sql);
			end = Instant.now();
			return Duration.between(start, end).toMillis();
//			while (resultSet.next()) {
//				kunde.setKundenNummer(resultSet.getString("Kundennummer"));
//				kunde.setEmail(resultSet.getString("Email"));
//				kunde.setTelefonNummer(resultSet.getString("Telefonnummer"));
//				kunde.setVorname(resultSet.getString("Vorname"));
//				kunde.setNachname(resultSet.getString("Nachname"));
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

		String sql = "SELECT kunde.Kundennummer, kunde.vorname, kunde.nachname, kunde.email, kunde.telefonnummer, adresse.plz, ortschaft.ortschaft, adresse.hausnummer, adresse.strasse "
				+ "FROM " + databaseName +".kunde, " + databaseName +".adresse, " + databaseName +".ortschaft WHERE adresse.PLZ = '" + plz + "' AND adresse.kundennummer = kunde.kundennummer AND ortschaft.plz = adresse.plz";
		try {
			start = Instant.now();
			statement.executeQuery(sql);
			end = Instant.now();
			return Duration.between(start, end).toMillis();
//			while (resultSet.next()) {
//				Kunde kunde = new Kunde();
//				kunde.setKundenNummer(resultSet.getString("Kundennummer"));
//				kunde.setEmail(resultSet.getString("Email"));
//				kunde.setTelefonNummer(resultSet.getString("Telefonnummer"));
//				kunde.setVorname(resultSet.getString("Vorname"));
//				kunde.setNachname(resultSet.getString("Nachname"));
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
		String sql = "SELECT Ortschaft FROM " + databaseName + ".Ortschaft";
		try {
			start = Instant.now();
			statement.executeQuery(sql);
			end = Instant.now();
			return Duration.between(start, end).toMillis();
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
		String sql = "SELECT * FROM " + databaseName + ".Artikel WHERE Artikelnummer='" + artikelNummer + "'";
		try {
			start = Instant.now();
			statement.executeQuery(sql);
			end = Instant.now();
			return Duration.between(start, end).toMillis();
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

	@Override
	public Artikel getArtikelByArtikelNummer_artikel(String artikelNummer) {
		String sql = "SELECT * FROM " + databaseName + ".Artikel WHERE Artikelnummer='" + artikelNummer + "'";
		try {
			ResultSet resultSet = statement.executeQuery(sql);
			Artikel artikel = new Artikel();
			while (resultSet.next()) {
				artikel.setArtikelNummer(resultSet.getString("Artikelnummer"));
				artikel.setArtikelName(resultSet.getString("Artikelname"));
				artikel.setEinzelPreis(resultSet.getDouble("Einzelpreis"));
				artikel.setWaehrung(resultSet.getString("Waehrung"));
				artikel.setBeschreibung(resultSet.getString("Beschreibung"));
			}
			return artikel;
		} catch (SQLException e) {
			System.err.println(e);
			return null;
		}
	}

	public long getArtikelByArtikelName(String artikelName) {
		String sql = "SELECT * FROM " + databaseName + ".Artikel WHERE Artikelname='" + artikelName + "'";
		try {
			start = Instant.now();
			statement.executeQuery(sql);
			end = Instant.now();
			return Duration.between(start, end).toMillis();
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
		String sql = "SELECT * FROM " + databaseName + ".Artikel WHERE Einzelpreis>" + price;
		try {
			start = Instant.now();
			statement.executeQuery(sql);
			end = Instant.now();
			return Duration.between(start, end).toMillis();
//			while (resultSet.next()) {
//				Artikel artikel = new Artikel();
//				artikel.setArtikelNummer(resultSet.getString("Artikelnummer"));
//				artikel.setArtikelName(resultSet.getString("Artikelname"));
//				artikel.setEinzelPreis(resultSet.getDouble("Einzelpreis"));
//				artikel.setWaehrung(resultSet.getString("Waehrung"));
//				artikel.setBeschreibung(resultSet.getString("Beschreibung"));
//				artikelList.add(artikel);
//			}
		} catch (SQLException e) {
			System.err.println(e);
			return Long.MAX_VALUE;
		}
	}

	public long getBewertungByKundenNrAndArtikelNr(String artikelNummer, String kundenNummer) {
		String sql = "SELECT * FROM " + databaseName + ".Bewertung WHERE Artikelnummer='" + artikelNummer
				+ "' AND Kundennummer='" + kundenNummer + "'";
		try {
			start = Instant.now();
			statement.executeQuery(sql);
			end = Instant.now();
			return Duration.between(start, end).toMillis();
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

	@Override
	public Bewertung getBewertungByKundenNrAndArtikelNr_bewertung(String artikelNummer, String kundenNummer) {
		String sql = "SELECT * FROM " + databaseName + ".Bewertung WHERE Artikelnummer='" + artikelNummer
				+ "' AND Kundennummer='" + kundenNummer + "'";
		try {
			ResultSet resultSet = statement.executeQuery(sql);
			Bewertung bewertung = new Bewertung();
			while (resultSet.next()) {
				bewertung.setArtikelNummer(resultSet.getString("Artikelnummer"));
				bewertung.setKundenNummer(resultSet.getString("Kundennummer"));
				bewertung.setBewertung(resultSet.getString("Bewertung"));
				bewertung.setSterne(resultSet.getInt("Sterne"));
			}
			return bewertung;
		} catch (SQLException e) {
			System.err.println(e);
			return null;
		}
	}

	public long getBewertungenByAnzahlSterne(int sterne) {
		List<Bewertung> bewertungList = new ArrayList<Bewertung>();
		String sql = "SELECT * FROM " + databaseName + ".Bewertung WHERE Sterne=" + sterne;
		try {
			start = Instant.now();
			statement.executeQuery(sql);
			end = Instant.now();
			return Duration.between(start, end).toMillis();
//			while (resultSet.next()) {
//				Bewertung bewertung = new Bewertung();
//				bewertung.setArtikelNummer(resultSet.getString("Artikelnummer"));
//				bewertung.setKundenNummer(resultSet.getString("Kundennummer"));
//				bewertung.setBewertung(resultSet.getString("Bewertung"));
//				bewertung.setSterne(resultSet.getInt("Sterne"));
//				bewertungList.add(bewertung);
//			}
		} catch (SQLException e) {
			System.err.println(e);
			return Long.MAX_VALUE;
		}
	}

	public long getBewertungenByKundenNr(String kundenNummer) {
		List<Bewertung> bewertungList = new ArrayList<Bewertung>();
		String sql = "SELECT * FROM " + databaseName + ".Bewertung WHERE Kundennummer='" + kundenNummer + "'";
		try {
			start = Instant.now();
			statement.executeQuery(sql);
			end = Instant.now();
			return Duration.between(start, end).toMillis();
//			while (resultSet.next()) {
//				Bewertung bewertung = new Bewertung();
//				bewertung.setArtikelNummer(resultSet.getString("Artikelnummer"));
//				bewertung.setKundenNummer(resultSet.getString("Kundennummer"));
//				bewertung.setBewertung(resultSet.getString("Bewertung"));
//				bewertung.setSterne(resultSet.getInt("Sterne"));
//				bewertungList.add(bewertung);
//			}
		} catch (SQLException e) {
			System.err.println(e);
			return Long.MAX_VALUE;
		}
	}

	public long getBewertungenByArtikelNr(String artikelNummer) {
		List<Bewertung> bewertungList = new ArrayList<Bewertung>();
		String sql = "SELECT * FROM " + databaseName + ".Bewertung WHERE Artikelnummer='" + artikelNummer + "'";
		try {
			start = Instant.now();
			statement.executeQuery(sql);
			end = Instant.now();
			return Duration.between(start, end).toMillis();
//			while (resultSet.next()) {
//				Bewertung bewertung = new Bewertung();
//				bewertung.setArtikelNummer(resultSet.getString("Artikelnummer"));
//				bewertung.setKundenNummer(resultSet.getString("Kundennummer"));
//				bewertung.setBewertung(resultSet.getString("Bewertung"));
//				bewertung.setSterne(resultSet.getInt("Sterne"));
//				bewertungList.add(bewertung);
//			}
		} catch (SQLException e) {
			System.err.println(e);
			return Long.MAX_VALUE;
		}
	}

	public long getEinkaeufeForKunde(String kundenNummer) {
		List<Kauf> kaufList = new ArrayList<Kauf>();
		String sql = "SELECT * FROM " + databaseName + ".Kauf WHERE Kundennummer='" + kundenNummer + "'";
		try {
			start = Instant.now();
			statement.executeQuery(sql);
			end = Instant.now();
			return Duration.between(start, end).toMillis();
//			while (resultSet.next()) {
//				Kauf kauf = new Kauf();
//				kauf.setArtikelNummer(resultSet.getString("Artikelnummer"));
//				kauf.setKundenNummer(resultSet.getString("Kundennummer"));
//				kauf.setKaufPreis(resultSet.getDouble("Kaufpreis"));
//				kauf.setKaufdatum(resultSet.getDate("Kaufdatum"));
//				kauf.setMenge(resultSet.getInt("Menge"));
//				kaufList.add(kauf);
//			}
		} catch (SQLException e) {
			System.err.println(e);
			return Long.MAX_VALUE;
		}
	}

	public long getVerkauefeForArtikel(String artikelNummer) {
		List<Kauf> kaufList = new ArrayList<Kauf>();
		String sql = "SELECT * FROM " + databaseName + ".Kauf WHERE Artikelnummer='" + artikelNummer + "'";
		try {
			start = Instant.now();
			statement.executeQuery(sql);
			end = Instant.now();
			return Duration.between(start, end).toMillis();
//			while (resultSet.next()) {
//				Kauf kauf = new Kauf();
//				kauf.setArtikelNummer(resultSet.getString("Artikelnummer"));
//				kauf.setKundenNummer(resultSet.getString("Kundennummer"));
//				kauf.setKaufPreis(resultSet.getDouble("Kaufpreis"));
//				kauf.setKaufdatum(resultSet.getDate("Kaufdatum"));
//				kauf.setMenge(resultSet.getInt("Menge"));
//				kaufList.add(kauf);
//			}
		} catch (SQLException e) {
			System.err.println(e);
			return Long.MAX_VALUE;
		}
	}

	public long updateKunde(Kunde kunde) {
		try {
			Adresse adresse = kunde.getAdresse();
			String sqlOrtschaft = "INSERT IGNORE INTO " + databaseName + ".Ortschaft (PLZ, Ortschaft) VALUES(?,?)";
			PreparedStatement prestOrtschaft = mariaDbConnection.prepareStatement(sqlOrtschaft);
			prestOrtschaft.setString(1, adresse.getPlz());
			prestOrtschaft.setString(2, adresse.getOrtschaft());
			start = Instant.now();
			statement.executeUpdate("UPDATE " + databaseName + ".Kunde SET Vorname='" + kunde.getVorname()
					+ "', Nachname='" + kunde.getNachname() + "', Email='" + kunde.getEmail() + "', Telefonnummer='"
					+ kunde.getTelefonNummer() + "' WHERE Kundennummer='" + kunde.getKundenNummer() + "'");
			prestOrtschaft.execute(); // Maybe we add a Kunde whose Ortschaft is not yet added to the database
			statement.executeUpdate("UPDATE " + databaseName + ".Adresse SET Strasse='" + adresse.getStrasse()
					+ "', Hausnummer='" + adresse.getHausnummer() + "', PLZ='" + adresse.getPlz()
					+ "' WHERE Kundennummer='" + kunde.getKundenNummer() + "'");
			end = Instant.now();
			return Duration.between(start, end).toMillis();
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
			return Duration.between(start, end).toMillis();
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
			return Duration.between(start, end).toMillis();
		} catch (SQLException e) {
			System.err.println(e);
			return Long.MAX_VALUE;
		}
	}

	@Override
	public long updateKundenNachname(String kundenNummer, String nachName) {
		try {
			start = Instant.now();
			statement.executeUpdate("UPDATE " + databaseName + ".Kunde SET Nachname='" + nachName + "'"
					+ " WHERE Kundennummer='" + kundenNummer + "'");
			end = Instant.now();
			return Duration.between(start, end).toMillis();
		} catch (SQLException e) {
			System.err.println(e);
			return Long.MAX_VALUE;
		}
	}

	@Override
	public long updateBewertungsText(String kundenNummer, String artikelNummer, String newText) {
		try {
			start = Instant.now();
			statement.executeUpdate("UPDATE " + databaseName + ".Bewertung SET Bewertung='" + newText + "'"
					+ " WHERE Artikelnummer='" + artikelNummer + "' AND Kundennummer='" + kundenNummer + "'");
			end = Instant.now();
			return Duration.between(start, end).toMillis();
		} catch (SQLException e) {
			System.err.println(e);
			return Long.MAX_VALUE;
		}
	}

	public long deleteKundeByKundenNr(String kundenNr) {
		try {
			start = Instant.now();
			statement.executeUpdate("DELETE FROM " + databaseName + ".Adresse WHERE Kundennummer='" + kundenNr + "'");
			statement.executeUpdate("DELETE FROM " + databaseName + ".Kunde WHERE Kundennummer='" + kundenNr + "'");
			end = Instant.now();
			return Duration.between(start, end).toMillis();
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
			return Duration.between(start, end).toMillis();
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
			return Duration.between(start, end).toMillis();
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
			return Duration.between(start, end).toMillis();
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
			return Duration.between(start, end).toMillis();
		} catch (SQLException e) {
			System.err.println(e);
			return Long.MAX_VALUE;
		}

	}
}
