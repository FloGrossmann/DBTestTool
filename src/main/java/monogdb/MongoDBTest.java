package monogdb;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gt;

import java.util.LinkedList;
import java.util.List;

import org.bson.BsonDocument;
import org.bson.BsonValue;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.ConnectionString;
import com.mongodb.DBObject;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.Indexes;

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
		
		CodecRegistry defaultCodecRegistry = MongoClientSettings.getDefaultCodecRegistry();
	    CodecRegistry fromProvider = CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build());
	    CodecRegistry pojoCodecRegistry = CodecRegistries.fromRegistries(defaultCodecRegistry, fromProvider);
	    
		testDatabase = mongoClient.getDatabase(TESTDATABASENAME).withCodecRegistry(pojoCodecRegistry);
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
		kundeCollection.createIndex(Indexes.ascending("kundenNummer"));
		artikelCollection = testDatabase.getCollection(COLLECTION_ARTIKEL, Artikel.class);
		kundeCollection.createIndex(Indexes.ascending("artikelNummer"));
		bewertungCollection = testDatabase.getCollection(COLLECTION_BEWERTUNG, Bewertung.class);
		bewertungCollection.createIndex(Indexes.compoundIndex(Indexes.ascending("kundenNummer"), Indexes.ascending("artikelNummer")));
		kaufCollection = testDatabase.getCollection(COLLECTION_KAUF, Kauf.class);
		kaufCollection.createIndex(Indexes.compoundIndex(Indexes.ascending("kundenNummer"), Indexes.ascending("artikelNummer")));
		return true;
	}

	public boolean cleanData() {
		testDatabase.drop();
		return true;
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
		return kaufCollection.find(eq("kundenNummer", kundenNummer)).into(result);
	}

	public List<Kauf> getVerkauefeForArtikel(String artikelNummer) {
		LinkedList<Kauf> result = new LinkedList<Kauf>();
		return kaufCollection.find(eq("artikelNummer", artikelNummer)).into(result);
	}

	public Kunde updateKunde(Kunde kunde) {
		kundeCollection.findOneAndReplace(eq("kundenNummer", kunde.getKundenNummer()), kunde);
		return kunde;
	}

	public Artikel updateArtikel(Artikel artikel) {
		artikelCollection.findOneAndReplace(eq("artikelNummer", artikel.getArtikelNummer()), artikel);
		return artikel;
	}

	public Bewertung updateBewertung(Bewertung bewertung) {
		bewertungCollection.findOneAndReplace(and(eq("artikelNummer", bewertung.getArtikelNummer()), eq("kundenNummer", bewertung.getKundenNummer())), bewertung);
		return bewertung;
	}

	public void deleteKundeByKundenNr(String kundenNr) {
		kundeCollection.deleteOne(eq("kundenNummer", kundenNr));
	}

	public void deleteArtikelbyArtikelNr(String artikelNummer) {
		artikelCollection.deleteOne(eq("artikelNummer", new ObjectId(artikelNummer)));
	}

	public void deleteBewertungByArtikelNrAndKundenNr(String artikelNummer, String kundenNummer) {
		bewertungCollection.deleteOne(and(eq("artikelNummer", new ObjectId(artikelNummer)), eq("kundenNummer", new ObjectId(kundenNummer))));
	}

	@Override
	public void deleteKaufByArtikelNrAndKundenNr(String artikelNummer, String kundennummer) {
		kaufCollection.deleteOne(and(eq("artikelNummer", new ObjectId(artikelNummer)), eq("artikelNummer", new ObjectId())));
	}

	@Override
	public void deleteAdresseByKundenNr(String kundennummer) {
		kundeCollection.updateOne(eq("kundenNummer", kundennummer), new BasicDBObject( "$unset", new BasicDBObject("adresse", "")));
	}
	
	
}
