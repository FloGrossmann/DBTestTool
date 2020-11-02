package monogdb;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gt;

import java.time.Duration;
import java.time.Instant;
import java.util.LinkedList;

import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.ConnectionString;
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
import measure.MockService;

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

	Instant start;
	Instant end;

	public MongoDBTest() {
	}

	public boolean connect(String connectionString) throws Exception {
		connString = new ConnectionString(connectionString);
		System.out.println("Test connection with connectionString" + connString.getConnectionString());
		MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(connString).retryWrites(true)
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
		bewertungCollection.createIndex(
				Indexes.compoundIndex(Indexes.ascending("kundenNummer"), Indexes.ascending("artikelNummer")));
		kaufCollection = testDatabase.getCollection(COLLECTION_KAUF, Kauf.class);
		kaufCollection.createIndex(
				Indexes.compoundIndex(Indexes.ascending("kundenNummer"), Indexes.ascending("artikelNummer")));
		return true;
	}

	public boolean cleanData() {
		testDatabase.drop();
		return true;
	}

	public long addKunde(Kunde kunde) {
		try {
			start = Instant.now();
			kundeCollection.insertOne(kunde);
			end = Instant.now();
			return Duration.between(start, end).toNanos();
		} catch (Exception e) {
			System.err.println(e);
			return -1l;
		}
	}

	public long addArtikel(Artikel artikel) {
		try {
			start = Instant.now();
			artikelCollection.insertOne(artikel);
			end = Instant.now();
			return Duration.between(start, end).toNanos();
		} catch (Exception e) {
			System.err.println(e);
			return -1l;
		}
	}

	public long addBewertung(Bewertung bewertung) {
		try {
			start = Instant.now();
			bewertungCollection.insertOne(bewertung);
			end = Instant.now();
			return Duration.between(start, end).toNanos();
		} catch (Exception e) {
			System.err.println(e);
			return -1l;
		}
	}

	public long addKauf(Kauf kauf) {
		try {
			start = Instant.now();
			kaufCollection.insertOne(kauf);
			end = Instant.now();
			return Duration.between(start, end).toNanos();
		} catch (Exception e) {
			System.err.println(e);
			return -1l;
		}
	}

	public long getKundeByKundenNr(String kundenNr) {
		start = Instant.now();
		kundeCollection.find(eq("kundenNummer", kundenNr)).first();
		end = Instant.now();
		return Duration.between(start, end).toNanos();
	}

	public long getKundeByEmail(String email) {
		start = Instant.now();
		kundeCollection.find(eq("email", email)).first();
		end = Instant.now();
		return Duration.between(start, end).toNanos();
	}

	public long getKundenByPlz(String plz) {
		LinkedList<Kunde> result = new LinkedList<Kunde>();
		start = Instant.now();
		kundeCollection.find(eq("adresse.plz", plz)).into(result);
		end = Instant.now();
		return Duration.between(start, end).toNanos();
	}

	public long getDistinctOrte() {
		LinkedList<String> result = new LinkedList<String>();
		start = Instant.now();
		kundeCollection.distinct("adresse.ortschaft", String.class).into(result);
		end = Instant.now();
		return Duration.between(start, end).toNanos();
	}

	public long getArtikelByArtikelNummer(String artikelNummer) {
		start = Instant.now();
		artikelCollection.find(eq("artikelNummer", artikelNummer)).first();
		end = Instant.now();
		return Duration.between(start, end).toNanos();
	}

	public long getArtikelByArtikelName(String artikelName) {
		start = Instant.now();
		artikelCollection.find(eq("artikelName", artikelName)).first();
		end = Instant.now();
		return Duration.between(start, end).toNanos();
	}

	public long getArtikelWhichCostMoreThan(Double price) {
		start = Instant.now();
		LinkedList<Artikel> result = new LinkedList<Artikel>();
		artikelCollection.find(gt("einzelPreis", price)).into(result);
		end = Instant.now();
		return Duration.between(start, end).toNanos();
	}

	public long getBewertungByKundenNrAndArtikelNr(String artikelNummer, String kundenNummer) {
		start = Instant.now();
		bewertungCollection.find(and(eq("artikelNummer", artikelNummer), eq("kundenNummer", kundenNummer)))
				.first();
		end = Instant.now();
		return Duration.between(start, end).toNanos();
	}

	public long getBewertungenByAnzahlSterne(int sterne) {
		LinkedList<Bewertung> result = new LinkedList<Bewertung>();
		start = Instant.now();
		bewertungCollection.find(eq("sterne", sterne)).into(result);
		end = Instant.now();
		return Duration.between(start, end).toNanos();
	}

	public long getBewertungenByKundenNr(String kundenNummer) {
		LinkedList<Bewertung> result = new LinkedList<Bewertung>();
		start = Instant.now();
		bewertungCollection.find(eq("kundenNummer", kundenNummer)).into(result);
		end = Instant.now();
		return Duration.between(start, end).toNanos();
	}

	public long getBewertungenByArtikelNr(String artikelNummer) {
		LinkedList<Bewertung> result = new LinkedList<Bewertung>();
		start = Instant.now();
		bewertungCollection.find(eq("artikelNummer", artikelNummer)).into(result);
		end = Instant.now();
		return Duration.between(start, end).toNanos();
	}

	public long getEinkaeufeForKunde(String kundenNummer) {
		LinkedList<Kauf> result = new LinkedList<Kauf>();
		start = Instant.now();
		kaufCollection.find(eq("kundenNummer", kundenNummer)).into(result);
		end = Instant.now();
		return Duration.between(start, end).toNanos();
	}

	public long getVerkauefeForArtikel(String artikelNummer) {
		LinkedList<Kauf> result = new LinkedList<Kauf>();
		start = Instant.now();
		kaufCollection.find(eq("artikelNummer", artikelNummer)).into(result);
		end = Instant.now();
		return Duration.between(start, end).toNanos();
	}

	public long updateKunde(Kunde kunde) {
		start = Instant.now();
		kundeCollection.findOneAndReplace(eq("kundenNummer", kunde.getKundenNummer()), kunde);
		end = Instant.now();
		return Duration.between(start, end).toNanos();
	}

	public long updateArtikel(Artikel artikel) {
		start = Instant.now();
		artikelCollection.findOneAndReplace(eq("artikelNummer", artikel.getArtikelNummer()), artikel);
		end = Instant.now();
		return Duration.between(start, end).toNanos();
	}

	public long updateBewertung(Bewertung bewertung) {
		start = Instant.now();
		bewertungCollection.findOneAndReplace(
				and(eq("artikelNummer", bewertung.getArtikelNummer()), eq("kundenNummer", bewertung.getKundenNummer())),
				bewertung);
		end = Instant.now();
		return Duration.between(start, end).toNanos();
	}
	
	@Override
	public long updateKundenNachname(String kundenNummer, String nachName) {
		start = Instant.now();
		kundeCollection.findOneAndUpdate(eq("kundenNummer", kundenNummer), new BasicDBObject("nachname", nachName));
		end = Instant.now();
		return Duration.between(start, end).toNanos();
	}

	@Override
	public long updateBewertungsText(String kundenNummer, String artikelNummer, String newText) {
		start = Instant.now();
		bewertungCollection.findOneAndUpdate(and(eq("artikelNummer", artikelNummer), eq("kundenNummer", kundenNummer)), new BasicDBObject("bewertung", newText));
		end = Instant.now();
		return Duration.between(start, end).toNanos();
	}

	public long deleteKundeByKundenNr(String kundenNr) {
		start = Instant.now();
		kundeCollection.deleteOne(eq("kundenNummer", kundenNr));
		end = Instant.now();
		return Duration.between(start, end).toNanos();
	}

	public long deleteArtikelbyArtikelNr(String artikelNummer) {
		start = Instant.now();
		artikelCollection.deleteOne(eq("artikelNummer", artikelNummer));
		end = Instant.now();
		return Duration.between(start, end).toNanos();
	}

	public long deleteBewertungByArtikelNrAndKundenNr(String artikelNummer, String kundenNummer) {
		start = Instant.now();
		bewertungCollection.deleteOne(
				and(eq("artikelNummer", artikelNummer), eq("kundenNummer", kundenNummer)));
		end = Instant.now();
		return Duration.between(start, end).toNanos();
	}

	@Override
	public long deleteKaufByArtikelNrAndKundenNr(String artikelNummer, String kundennummer) {
		start = Instant.now();
		kaufCollection
				.deleteOne(and(eq("artikelNummer", artikelNummer), eq("kundenNummer", kundennummer)));
		end = Instant.now();
		return Duration.between(start, end).toNanos();
	}

	@Override
	public long deleteAdresseByKundenNr(String kundennummer) {
		start = Instant.now();
		kundeCollection.updateOne(eq("kundenNummer", kundennummer),
				new BasicDBObject("$unset", new BasicDBObject("adresse", "")));
		end = Instant.now();
		return Duration.between(start, end).toNanos();
	}

}
