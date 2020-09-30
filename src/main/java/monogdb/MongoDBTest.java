package monogdb;

import java.util.LinkedList;
import java.util.List;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.Indexes;
import com.mongodb.connection.ServerSettings;

import static com.mongodb.client.model.Filters.*;

import dbinterface.Artikel;
import dbinterface.Bewertung;
import dbinterface.DBInterface;
import dbinterface.Kauf;
import dbinterface.Kunde;

public class MongoDBTest implements DBInterface {
	
	ConnectionString connString;
	MongoClient mongoClient;
	MongoDatabase testDatabase;
	
	public static final String TESTDATABASENAME = "testDB";
	public static final String COLLECTION_KUNDE = "Kunde";
	public static final String COLLECTION_ARTIKEL = "Artikel";
	public static final String COLLECTION_BEWERTUNG = "Bewertung";
	public static final String COLLECTION_KAUF = "Kauf";
	
	private MongoCollection<Kunde> kundeCollection;
	private MongoCollection<Artikel> artikelCollection;
	private MongoCollection<Bewertung> bewertungCollection;
	private MongoCollection<Kauf> kaufCollection;
	
	public MongoDBTest() {
	}
	
	public boolean connect(String connectionString) throws Exception {
		connString = new ConnectionString(connectionString);
		System.out.println("Test connection with connectionString" + connString.getConnectionString());
		MongoClientSettings settings = MongoClientSettings.builder()
			    .applyConnectionString(connString)
			    .retryWrites(true)
			    .build();
		MongoClient mongoClient = MongoClients.create(settings);
		testDatabase = mongoClient.getDatabase(TESTDATABASENAME);
		MongoIterable<String> databaseNames = mongoClient.listDatabaseNames();
		if (databaseNames != null) {
			for (String dbName : databaseNames) {
				if (dbName.equals(TESTDATABASENAME)) {
					this.cleanData();
				}
			}
			return true;
		} else {
			return false;
		}
	}

	public boolean setupDB() {
		this.cleanData();
		testDatabase.createCollection(COLLECTION_KUNDE);
		testDatabase.createCollection(COLLECTION_ARTIKEL);
		testDatabase.createCollection(COLLECTION_BEWERTUNG);
		testDatabase.createCollection(COLLECTION_KAUF);
		
		kundeCollection = testDatabase.getCollection(COLLECTION_KUNDE, Kunde.class);
		artikelCollection = testDatabase.getCollection(COLLECTION_KUNDE, Artikel.class);
		bewertungCollection = testDatabase.getCollection(COLLECTION_KUNDE, Bewertung.class);
		bewertungCollection.createIndex(Indexes.compoundIndex(Indexes.ascending("kundenNummer"), Indexes.ascending("artikelNummer")));
		kaufCollection = testDatabase.getCollection(COLLECTION_KUNDE, Kauf.class);
		kaufCollection.createIndex(Indexes.compoundIndex(Indexes.ascending("kundenNr"), Indexes.ascending("artikelNr")));
		return true;
	}

	public boolean cleanData() {
		testDatabase.drop();
		return false;
	}

	public Kunde addKunde(Kunde kunde) {
		try {
			if (kundeCollection.insertOne(kunde).wasAcknowledged()) {
				return kunde;
			}
			return null;
		} catch(Exception e) {
			System.err.println(e);
			return null;
		}
	}

	public Artikel addArtikel(Artikel artikel) {
		try {
			if (artikelCollection.insertOne(artikel).wasAcknowledged()) {
				return artikel;
			}
			return null;
		} catch(Exception e) {
			System.err.println(e);
			return null;
		}
	}

	public Bewertung addBewertung(Bewertung bewertung) {
		try {
			if (bewertungCollection.insertOne(bewertung).wasAcknowledged()) {
				return bewertung;
			}
			return null;
		} catch(Exception e) {
			System.err.println(e);
			return null;
		}
	}

	public Kauf addKauf(Kauf kauf) {
		try {
			if (kaufCollection.insertOne(kauf).wasAcknowledged()) {
				return kauf;
			}
			return null;
		} catch(Exception e) {
			System.err.println(e);
			return null;
		}
	}

	public Kunde getKundeByKundenNr(String kundenNr) {
		return kundeCollection.find(eq("kundenNummer", kundenNr)).first();
	}

	public Kunde getKundeByEmail(String email) {
		return kundeCollection.find(eq("email", email)).first();
	}

	public List<Kunde> getKundenByPlz(String plz) {
		LinkedList<Kunde> result = new LinkedList<Kunde>();
		return kundeCollection.find(eq("adresse.plz", plz)).into(result);
	}

	public List<Kunde> getKundenByNachName(String nachName) {
		LinkedList<Kunde> result = new LinkedList<Kunde>();
		return kundeCollection.find(eq("nachname", nachName)).into(result);
	}

	public List<String> getDistinctOrte() {
		LinkedList<String> result = new LinkedList<String>();
		return kundeCollection.distinct("adresse.ortschaft", String.class).into(result);
	}

	public Artikel getArtikelByArtikelNummer(String artikelNummer) {
		return artikelCollection.find(eq("artikelNummer", artikelNummer)).first();
	}

	public Artikel getArtikelByArtikelName(String artikelName) {
		return artikelCollection.find(eq("artikelName", artikelName)).first();
	}

	public List<Artikel> getArtikelWhichCostMoreThan(Double price) {
		LinkedList<Artikel> result = new LinkedList<Artikel>();
		return artikelCollection.find(gt("einzelPreis", price)).into(result);
	}

	public Bewertung getBewertungByKundenNrAndArtikelNr(String artikelNummer, String kundenNummer) {
		return bewertungCollection.find(and(eq("artikelNummer", artikelNummer), eq("kundenNummer", kundenNummer))).first();
	}

	public List<Bewertung> getBewertungenByAnzahlSterne(int sterne) {
		LinkedList<Bewertung> result = new LinkedList<Bewertung>();
		return bewertungCollection.find(eq("sterne", sterne)).into(result);
	}

	public List<Bewertung> getBewertungenByKundenNr(String kundenNummer) {
		LinkedList<Bewertung> result = new LinkedList<Bewertung>();
		return bewertungCollection.find(eq("kundenNummer", kundenNummer)).into(result);
	}

	public List<Bewertung> getBewertungenByArtikelNr(String artikelNummer) {
		LinkedList<Bewertung> result = new LinkedList<Bewertung>();
		return bewertungCollection.find(eq("artikelNummer", artikelNummer)).into(result);
	}

	public List<Kauf> getEinkaeufeForKunde(String kundenNummer) {
		LinkedList<Kauf> result = new LinkedList<Kauf>();
		return kaufCollection.find(eq("kundenNr", kundenNummer)).into(result);
	}

	public List<Kauf> getVerkauefeForArtikel(String artikelNummer) {
		LinkedList<Kauf> result = new LinkedList<Kauf>();
		return kaufCollection.find(eq("artikelNr", artikelNummer)).into(result);
	}

	public Kunde updateKunde(Kunde kunde) {
//		kundeCollection.updateOne(eq("kundenNummer", kunde.getKundenNummer()), kunde);
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
