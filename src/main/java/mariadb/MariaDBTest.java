package mariadb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import dbinterface.Artikel;
import dbinterface.Bewertung;
import dbinterface.DBInterface;
import dbinterface.Kauf;
import dbinterface.Kunde;

public class MariaDBTest implements DBInterface {

	String connectionString;
	String username;
	String password;
	Connection mariaDbConnection;
	Statement statement;

	public MariaDBTest(String connectionString, String username, String password) {
		this.connectionString=connectionString;
		this.username=username;
		this.password=password;
	}
	
	public void connect() {
		System.out.println("Test connection with connectionString: " + connectionString);
		try {
			mariaDbConnection = DriverManager.getConnection(
					"jdbc:mariadb://" + connectionString + "/DB?user=" + username + "&password=" + password);
			statement = mariaDbConnection.createStatement();
			setupDB();
			getA();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Verbindung fehlgeschlagen");
		} finally {
			try {
				if (statement != null)
					mariaDbConnection.close();
			} catch (SQLException se) {
			} // do nothing
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
			statement.execute("CREATE TABLE a (id int not null primary key, value varchar(20))");
			System.out.println("Tabelle erstellt");
			return true;
		} catch (SQLException e) {
			System.out.println("Setup fehlgeschlagen");
			return false;
		}

	}

	public void getA() {
		try {
			ResultSet resultSet = statement.executeQuery("SELECT * FROM a");
			while(resultSet.next()) {
				int id=resultSet.getInt("id");
				String value=resultSet.getString("value");
				
				System.out.println("ID: "+id);
				System.out.println("Value: "+ value);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public boolean cleanData() {
		// TODO Auto-generated method stub
		return false;
	}

	public Kunde addKunde(Kunde kunde) {
		// TODO Auto-generated method stub
		return null;
	}

	public Artikel addArtikel(Artikel artikel) {
		// TODO Auto-generated method stub
		return null;
	}

	public Bewertung addBewertung(Bewertung bewertung) {
		// TODO Auto-generated method stub
		return null;
	}

	public Kauf addKauf(Kauf kauf) {
		// TODO Auto-generated method stub
		return null;
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
