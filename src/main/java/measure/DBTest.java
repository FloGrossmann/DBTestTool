package measure;

import java.util.LinkedList;

import dbinterface.Artikel;
import dbinterface.Bewertung;
import dbinterface.DBInterface;
import dbinterface.Kauf;
import dbinterface.Kunde;
import main.Main;
import mariadb.MariaDBTest;
import measure.util.CompPrimaryKey;
import monogdb.MongoDBTest;
import ui.ProgressIndicatorPanel;

public class DBTest {

	public MongoDBTest mongoDBTest;
	public MariaDBTest mariaDBTest;
	public LinkedList<AccessTime> mongoDBTestList = new LinkedList<AccessTime>();
	public static final int TESTEVERY = Main.TESTEVERY;
	public static final int REPETITIONS = Main.REPETITIONS;
	public static final int MAXIMUM = Main.MAXIMUM;
	private ProgressIndicatorPanel progress;
	
	public void startTest(String mongoDBConnectionString, String mariaDBConnectionString,
			String mariaDBUsername, String mariaDBPassword, ProgressIndicatorPanel progress) throws Exception {
		this.progress = progress;
		
		progress.setText("Setting up MongoDB...");
		mongoDBTest = new MongoDBTest();
		mongoDBTest.connect(mongoDBConnectionString);
		mongoDBTest.setupDB();
		progress.setText("Setting up MariaDB...");
		mariaDBTest = new MariaDBTest(mariaDBConnectionString, mariaDBUsername, mariaDBPassword);
		mariaDBTest.connect();
		mariaDBTest.setupDB();
		
		progress.setDBUnderTestText("Testing MariaDB");
		databaseTest(mariaDBTest, "MariaDB");
		progress.setDBUnderTestText("Cleanup");
		MockService.clearIdLists();
		progress.setDBUnderTestText("Testing MongoDB");
		databaseTest(mongoDBTest, "MongoDB");
		progress.setDBUnderTestText("Finished");
		progress.setText("Finished");
	}
	
	public void databaseTest(DBInterface underTest, String databaseName) {
		
		//CREATE
		LinkedList<AccessTime> create_Kunde = new LinkedList<AccessTime>();
		LinkedList<AccessTime> create_Artikel = new LinkedList<AccessTime>();
		LinkedList<AccessTime> create_Kauf = new LinkedList<AccessTime>();
		LinkedList<AccessTime> create_Bewertung = new LinkedList<AccessTime>();
		
		//DELETE
		LinkedList<AccessTime> delete_Kunde = new LinkedList<AccessTime>();
		LinkedList<AccessTime> delete_Artikel = new LinkedList<AccessTime>();
		LinkedList<AccessTime> delete_Kauf = new LinkedList<AccessTime>();
		LinkedList<AccessTime> delete_Bewertung = new LinkedList<AccessTime>();
		
		//READ
		LinkedList<AccessTime> read_Kunde_ByKndNr = new LinkedList<AccessTime>();
		LinkedList<AccessTime> read_Kunde_ByEmail = new LinkedList<AccessTime>();
		LinkedList<AccessTime> read_Kunde_ByPLZ = new LinkedList<AccessTime>();
		LinkedList<AccessTime> read_Kunde_Distinct_Orte = new LinkedList<AccessTime>();
		LinkedList<AccessTime> read_Artikel_ByArtNr = new LinkedList<AccessTime>();
		LinkedList<AccessTime> read_Artikel_ByArtName = new LinkedList<AccessTime>();
		LinkedList<AccessTime> read_Artikel_WhichCostMoreThan = new LinkedList<AccessTime>();
		LinkedList<AccessTime> read_Kauf_ForKundeNr = new LinkedList<AccessTime>();
		LinkedList<AccessTime> read_Kauf_ForArtikelNr = new LinkedList<AccessTime>();
		LinkedList<AccessTime> read_Bewertung_ByANr_KNr = new LinkedList<AccessTime>();
		LinkedList<AccessTime> read_Bewertung_BySterne = new LinkedList<AccessTime>();
		
		//UPDATE
		LinkedList<AccessTime> update_Kunde_Complete = new LinkedList<AccessTime>();
		LinkedList<AccessTime> update_Kunde_Nachname = new LinkedList<AccessTime>();
		LinkedList<AccessTime> update_Artikel_Complete = new LinkedList<AccessTime>();
		LinkedList<AccessTime> update_Bewertung_Complete = new LinkedList<AccessTime>();
		LinkedList<AccessTime> update_Bewertung_Text = new LinkedList<AccessTime>();
		
		int size = 0;
		
		while (size < MAXIMUM) {
			progress.setText("Adding next " + TESTEVERY + "(*4 objects) entries..");
			do {
				//Add more entries
				// Kunde
				underTest.addKunde(MockService.genRandomInsertKunde());
				// Artikel
				underTest.addArtikel(MockService.genRandomInsertArtikel());
				// Kauf
				underTest.addKauf(MockService.genRandomInsertKauf(false));
				// Bewertung
				underTest.addBewertung(MockService.genRandomInsertBewertung(false));
				size++;
				progress.setAddingProgress((size % TESTEVERY) +1);
			} while (size % TESTEVERY != 0 || size == 0);
			progress.resetAddingBarProgress();
			
			// We have reached the 5k, we want to test now
			LinkedList<Long> testListCreate_Kunde = new LinkedList<Long>();
			LinkedList<Long> testListDelete_Kunde = new LinkedList<Long>();
			LinkedList<Long> testListRead_KundeByKndNr = new LinkedList<Long>();
			LinkedList<Long> testListRead_KundeByEmail = new LinkedList<Long>();
			LinkedList<Long> testListRead_Kunden_byPLZ = new LinkedList<Long>();
			LinkedList<Long> testList_GetDistinctOrte = new LinkedList<Long>();
			LinkedList<Long> testListUpdate_Kunde_Complete = new LinkedList<Long>();
			LinkedList<Long> testListUpdate_Kunde_Nachname = new LinkedList<Long>();
			
			LinkedList<Long> testListCreate_Artikel = new LinkedList<Long>();
			LinkedList<Long> testListDelete_Artikel = new LinkedList<Long>();
			LinkedList<Long> testListRead_ArtikelByArtikelNr = new LinkedList<Long>();
			LinkedList<Long> testListRead_ArtikelByArtikelName = new LinkedList<Long>();
			LinkedList<Long> testListRead_ArtikelWhichCostMoreThan = new LinkedList<Long>();
			LinkedList<Long> testListUpdate_Artikel_Complete = new LinkedList<Long>();
			
			LinkedList<Long> testListCreate_Kauf = new LinkedList<Long>();
			LinkedList<Long> testListDelete_Kauf = new LinkedList<Long>();
			LinkedList<Long> testListRead_Kauf_ForKundeNr = new LinkedList<Long>();
			LinkedList<Long> testListRead_Kauf_ForArtikelNr = new LinkedList<Long>();
			
			LinkedList<Long> testListCreate_Bewertung = new LinkedList<Long>();
			LinkedList<Long> testListDelete_Bewertung = new LinkedList<Long>();
			LinkedList<Long> testListRead_BewertungByANr_KNr = new LinkedList<Long>();
			LinkedList<Long> testListRead_BewertungBySterne = new LinkedList<Long>();
			LinkedList<Long> testListUpdate_Bewertung_Complete = new LinkedList<Long>();
			LinkedList<Long> testListUpdate_Bewertung_Text = new LinkedList<Long>();
			
			for (int i = 0; i < REPETITIONS; i++) {
				progress.setText("Repetition testing.. " + (i+1) + "/" + REPETITIONS);

				// Kunde
				testListCreate_Kunde.add(testAddKunde(underTest));
				testListDelete_Kunde.add(testDeleteKunde(underTest));
				testListRead_KundeByKndNr.add(testReadKunde_byKundenNummer(underTest));
				testListRead_KundeByEmail.add(testReadKunde_byEmail(underTest));
				testListRead_Kunden_byPLZ.add(testReadKunden_byPLZ(underTest));
				testList_GetDistinctOrte.add(testGetDistinctOrte(underTest));
				testListUpdate_Kunde_Complete.add(testUpdateKunde_Complete(underTest));
				testListUpdate_Kunde_Nachname.add(testUpdateKunde_Nachname(underTest));
				
				// Artikel
				testListCreate_Artikel.add(testAddArtikel(underTest));
				testListDelete_Artikel.add(testDeleteArtikel(underTest));
				testListRead_ArtikelByArtikelNr.add(testReadArtikel_byArtikelNummer(underTest));
				testListRead_ArtikelByArtikelName.add(testReadArtikel_byArtikelName(underTest));
				testListRead_ArtikelWhichCostMoreThan.add(testReadArtikel_whichCostMoreThan(underTest));
				testListUpdate_Artikel_Complete.add(testUpdateArtikel_Complete(underTest));
				
				// Kauf
				testListCreate_Kauf.add(testAddKauf(underTest));
				testListDelete_Kauf.add(testDeleteKauf(underTest));
				testListRead_Kauf_ForKundeNr.add(testReadEinkaeufeForKunde(underTest));
				testListRead_Kauf_ForArtikelNr.add(testReadVerkaeufeForArtikel(underTest));
				
				// Bewertung
				testListCreate_Bewertung.add(testAddBewertung(underTest));
				testListDelete_Bewertung.add(testDeleteBewertung(underTest));
				testListRead_BewertungByANr_KNr.add(testReadBewertungByKundenNrAndArtikelNr(underTest));
				testListRead_BewertungBySterne.add(testReadBewertungByAnzahlSterne(underTest));
				testListUpdate_Bewertung_Complete.add(testUpdateBewertung_Complete(underTest));
				testListUpdate_Bewertung_Text.add(testUpdateBewertung_Text(underTest));
				
				progress.setTestRepititionProgress(i+2);
			}
			progress.resetTestRepititionBarProgress();
			// Calculate & Store the results
			// Create
			create_Kunde.add(new AccessTime(CRUDoperation.INSERT, ObjectCategory.KUNDE, MethodType.ADD_KUNDE, new Messreihe(testListCreate_Kunde), size));
			create_Artikel.add(new AccessTime(CRUDoperation.INSERT, ObjectCategory.ARTIKEL, MethodType.ADD_ARTIKEL, new Messreihe(testListCreate_Artikel), size));
			create_Bewertung.add(new AccessTime(CRUDoperation.INSERT, ObjectCategory.BEWERTUNG, MethodType.ADD_BEWERTUNG, new Messreihe(testListCreate_Bewertung), size));
			create_Kauf.add(new AccessTime(CRUDoperation.INSERT, ObjectCategory.KAUF, MethodType.ADD_KAUF, new Messreihe(testListCreate_Kauf), size));
			
			//Delete
			delete_Kunde.add(new AccessTime(CRUDoperation.DELETE, ObjectCategory.KUNDE, MethodType.DELETE_KUNDE_BYKUNDENNR, new Messreihe(testListDelete_Kunde), size));
			delete_Artikel.add(new AccessTime(CRUDoperation.DELETE, ObjectCategory.ARTIKEL, MethodType.DELETE_ARTIKEL_BYARTIKELNR, new Messreihe(testListDelete_Artikel), size));
			delete_Bewertung.add(new AccessTime(CRUDoperation.DELETE, ObjectCategory.BEWERTUNG, MethodType.DELETE_BEWERTUNG_BYARTIKELNRANDKUNDENNR, new Messreihe(testListDelete_Bewertung), size));
			delete_Kauf.add(new AccessTime(CRUDoperation.DELETE, ObjectCategory.KAUF, MethodType.DELETE_KAUF_BYARTIKELNRANDKUNDENNR, new Messreihe(testListDelete_Kauf), size));
			
			// Read
			read_Kunde_ByKndNr.add(new AccessTime(CRUDoperation.SELECT, ObjectCategory.KUNDE, MethodType.GET_KUNDE_BYKUNDENNR, new Messreihe(testListRead_KundeByKndNr), size));
			read_Kunde_ByEmail.add(new AccessTime(CRUDoperation.SELECT, ObjectCategory.KUNDE, MethodType.GET_KUNDE_BYEMAIL, new Messreihe(testListRead_KundeByEmail), size));
			read_Kunde_ByPLZ.add(new AccessTime(CRUDoperation.SELECT, ObjectCategory.KUNDE, MethodType.GET_KUNDEN_BYPLZ, new Messreihe(testListRead_Kunden_byPLZ), size));
			read_Kunde_Distinct_Orte.add(new AccessTime(CRUDoperation.SELECT, ObjectCategory.KUNDE, MethodType.GET_DISTINCTORTE, new Messreihe(testList_GetDistinctOrte), size));
			read_Artikel_ByArtNr.add(new AccessTime(CRUDoperation.SELECT, ObjectCategory.ARTIKEL, MethodType.GET_ARTIKEL_BYARTIKELNUMMER, new Messreihe(testListRead_ArtikelByArtikelNr), size));
			read_Artikel_ByArtName.add(new AccessTime(CRUDoperation.SELECT, ObjectCategory.ARTIKEL, MethodType.GET_ARTIKEL_BYARTIKELNAME, new Messreihe(testListRead_ArtikelByArtikelName), size));
			read_Artikel_WhichCostMoreThan.add(new AccessTime(CRUDoperation.SELECT, ObjectCategory.ARTIKEL, MethodType.GET_ARTIKEL_WHICHCOSTMORETHAN, new Messreihe(testListRead_ArtikelWhichCostMoreThan), size));
			read_Kauf_ForKundeNr.add(new AccessTime(CRUDoperation.SELECT, ObjectCategory.KAUF, MethodType.GET_EINKAEUFE_FORKUNDEN, new Messreihe(testListRead_Kauf_ForKundeNr), size));
			read_Kauf_ForArtikelNr.add(new AccessTime(CRUDoperation.SELECT, ObjectCategory.KAUF, MethodType.GET_EINKAUEFE_FORARTIKEL, new Messreihe(testListRead_Kauf_ForArtikelNr), size));
			read_Bewertung_ByANr_KNr.add(new AccessTime(CRUDoperation.SELECT, ObjectCategory.BEWERTUNG, MethodType.GET_BEWERTUNG_BYKUNDENNRANDARTIKELNR, new Messreihe(testListRead_BewertungByANr_KNr), size));
			read_Bewertung_BySterne.add(new AccessTime(CRUDoperation.SELECT, ObjectCategory.BEWERTUNG, MethodType.GET_KUNDE_BYKUNDENNR, new Messreihe(testListRead_BewertungBySterne), size));
			
			//Update
			update_Kunde_Complete.add(new AccessTime(CRUDoperation.UPDATE, ObjectCategory.KUNDE, MethodType.UPDATE_KUNDE_COMPLETE, new Messreihe(testListUpdate_Kunde_Complete), size));
			update_Kunde_Nachname.add(new AccessTime(CRUDoperation.UPDATE, ObjectCategory.KUNDE, MethodType.UPDATE_KUNDE_NACHNAME, new Messreihe(testListUpdate_Kunde_Nachname), size));
			update_Artikel_Complete.add(new AccessTime(CRUDoperation.UPDATE, ObjectCategory.ARTIKEL, MethodType.UPDATE_ARTIKEL_COMPLETE, new Messreihe(testListUpdate_Artikel_Complete), size));
			update_Bewertung_Complete.add(new AccessTime(CRUDoperation.UPDATE, ObjectCategory.BEWERTUNG, MethodType.UPDATE_BEWERTUNG_COMPLETE, new Messreihe(testListUpdate_Bewertung_Complete), size));
			update_Bewertung_Text.add(new AccessTime(CRUDoperation.UPDATE, ObjectCategory.BEWERTUNG, MethodType.UPDATE_BEWERTUNG_TEXT, new Messreihe(testListUpdate_Bewertung_Text), size));
			
			progress.setGlobalProgress(size / DBTest.TESTEVERY);
		}
		
		// Write to csv
		try {
			CsvBeanWriter.writeCsvFromAccessTimeExample(create_Kunde, databaseName, databaseName + "_Create_Kunde");
			CsvBeanWriter.writeCsvFromAccessTimeExample(create_Artikel, databaseName, databaseName + "_Create_Artikel");
			CsvBeanWriter.writeCsvFromAccessTimeExample(create_Bewertung, databaseName, databaseName + "_Create_Bewertung");
			CsvBeanWriter.writeCsvFromAccessTimeExample(create_Kauf, databaseName, databaseName + "_Create_Kauf");
			
			CsvBeanWriter.writeCsvFromAccessTimeExample(delete_Kunde, databaseName, databaseName + "_Delete_Kunde");
			CsvBeanWriter.writeCsvFromAccessTimeExample(delete_Artikel, databaseName, databaseName + "_Delete_Artikel");
			CsvBeanWriter.writeCsvFromAccessTimeExample(delete_Bewertung, databaseName, databaseName + "_Delete_Bewertung");
			CsvBeanWriter.writeCsvFromAccessTimeExample(delete_Kauf, databaseName, databaseName + "_Delete_Kauf");
			
			CsvBeanWriter.writeCsvFromAccessTimeExample(read_Kunde_ByKndNr, databaseName, databaseName + "_Read_Kunde_ByKndNr");
			CsvBeanWriter.writeCsvFromAccessTimeExample(read_Kunde_ByEmail, databaseName, databaseName + "_Read_Kunde_ByEmail");
			CsvBeanWriter.writeCsvFromAccessTimeExample(read_Kunde_ByPLZ, databaseName, databaseName + "_Read_Kunde_ByPLZ");
			CsvBeanWriter.writeCsvFromAccessTimeExample(read_Kunde_Distinct_Orte, databaseName, databaseName + "_Read_Kunde_Distinct_Orte");
			CsvBeanWriter.writeCsvFromAccessTimeExample(read_Artikel_ByArtNr, databaseName, databaseName + "_Read_Artikel_ByArtNr");
			CsvBeanWriter.writeCsvFromAccessTimeExample(read_Artikel_ByArtName, databaseName, databaseName + "_Read_Artikel_ByArtName");
			CsvBeanWriter.writeCsvFromAccessTimeExample(read_Artikel_WhichCostMoreThan, databaseName, databaseName + "_Read_Artikel_WhichCostMoreThan");
			CsvBeanWriter.writeCsvFromAccessTimeExample(read_Kauf_ForKundeNr, databaseName, databaseName + "_Read_Kauf_ForKundeNr");
			CsvBeanWriter.writeCsvFromAccessTimeExample(read_Kauf_ForArtikelNr, databaseName, databaseName + "_Read_Kauf_ForArtikelNr");
			CsvBeanWriter.writeCsvFromAccessTimeExample(read_Bewertung_ByANr_KNr, databaseName, databaseName + "_Read_Bewertung_ByANr_KNr");
			CsvBeanWriter.writeCsvFromAccessTimeExample(read_Bewertung_BySterne, databaseName, databaseName + "_Read_Bewertung_BySterne");
			
			CsvBeanWriter.writeCsvFromAccessTimeExample(update_Kunde_Complete, databaseName, databaseName + "_Update_Kunde_Complete");
			CsvBeanWriter.writeCsvFromAccessTimeExample(update_Kunde_Nachname, databaseName, databaseName + "_Update_Kunde_Nachname");
			CsvBeanWriter.writeCsvFromAccessTimeExample(update_Artikel_Complete, databaseName, databaseName + "_Update_Artikel_Complete");
			CsvBeanWriter.writeCsvFromAccessTimeExample(update_Bewertung_Complete, databaseName, databaseName + "_Update_Bewertung_Complete");
			CsvBeanWriter.writeCsvFromAccessTimeExample(update_Bewertung_Text, databaseName, databaseName + "_Update_Bewertung_Text");
			progress.resetGlobalProgress();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private long testAddKunde(DBInterface underTest) {
		long timeTook_CREATE;
		do {				
			Kunde testKunde = MockService.genRandomInsertKunde();
			timeTook_CREATE = underTest.addKunde(testKunde);
			underTest.deleteKundeByKundenNr(testKunde.getKundenNummer());
			MockService.removeKundenNr();
		} while (timeTook_CREATE == 0);
		return timeTook_CREATE;
	}
	
	private long testDeleteKunde(DBInterface underTest) {
		long timeTook_DELETE;
		do {
			Kunde testKunde = MockService.genRandomInsertKunde();
			underTest.addKunde(testKunde);
			timeTook_DELETE = underTest.deleteKundeByKundenNr(testKunde.getKundenNummer());
			MockService.removeKundenNr();
		} while (timeTook_DELETE == 0);
		return timeTook_DELETE;
	}
	
	private long testAddArtikel(DBInterface underTest) {
		long timeTook_CREATE = 0;
		do {				
			Artikel testArtikel = MockService.genRandomInsertArtikel();
			timeTook_CREATE = underTest.addArtikel(testArtikel);
			underTest.deleteArtikelbyArtikelNr(testArtikel.getArtikelNummer());
			MockService.removeArtikelNr();
		} while (timeTook_CREATE == 0);
		return timeTook_CREATE;
	}
	
	private long testDeleteArtikel(DBInterface underTest) {
		long timeTook_DELETE;
		do {
			Artikel testArtikel = MockService.genRandomInsertArtikel();
			underTest.addArtikel(testArtikel);
			timeTook_DELETE = underTest.deleteArtikelbyArtikelNr(testArtikel.getArtikelNummer());
			MockService.removeArtikelNr();
		} while (timeTook_DELETE == 0);
		return timeTook_DELETE;
	}
	
	private long testAddKauf(DBInterface underTest) {
		long timeTook_CREATE = 0;
		do {
			Kauf testKauf = MockService.genRandomInsertKauf(true);
			timeTook_CREATE = underTest.addKauf(testKauf);
			underTest.deleteKaufByArtikelNrAndKundenNr(testKauf.getArtikelNummer(), testKauf.getKundenNummer());
		} while (timeTook_CREATE == 0);
		return timeTook_CREATE;
	}
	
	private long testDeleteKauf(DBInterface underTest) {
		long timeTook_DELETE;
		do {
			Kauf testKauf = MockService.genRandomInsertKauf(true);
			underTest.addKauf(testKauf);
			timeTook_DELETE = underTest.deleteKaufByArtikelNrAndKundenNr(testKauf.getArtikelNummer(), testKauf.getKundenNummer());
		} while (timeTook_DELETE == 0);
		return timeTook_DELETE;
	}
	
	private long testAddBewertung(DBInterface underTest) {
		long timeTook_CREATE = 0;
		do {				
			Bewertung testBewertung = MockService.genRandomInsertBewertung(true);
			timeTook_CREATE = underTest.addBewertung(testBewertung);
			underTest.deleteBewertungByArtikelNrAndKundenNr(testBewertung.getArtikelNummer(), testBewertung.getKundenNummer());
		} while (timeTook_CREATE == 0);
		return timeTook_CREATE;
	}
	
	private long testDeleteBewertung(DBInterface underTest) {
		long timeTook_DELETE;
		do {
			Bewertung testBewertung = MockService.genRandomInsertBewertung(true);
			underTest.addBewertung(testBewertung);
			timeTook_DELETE = underTest.deleteBewertungByArtikelNrAndKundenNr(testBewertung.getArtikelNummer(), testBewertung.getKundenNummer());
		} while (timeTook_DELETE == 0);
		return timeTook_DELETE;
	}
	
	private long testReadKunde_byKundenNummer(DBInterface underTest) {
		long timeTook_READ;
		String kundenNr = MockService.getRandomInsertedKundenNummer();
		do {
			timeTook_READ = underTest.getKundeByKundenNr(kundenNr);
		} while (timeTook_READ == 0);
		return timeTook_READ;
	}
	
	private long testReadKunde_byEmail(DBInterface underTest) {
		long timeTook_READ;
		String email = MockService.getRandomInsertedEmail();
		do {
			timeTook_READ = underTest.getKundeByEmail(email);
		} while (timeTook_READ == 0);
		return timeTook_READ;
	}
	
	private long testReadKunden_byPLZ(DBInterface underTest) {
		long timeTook_READ;
		String plz = MockService.getRandomPLZ();
		do {
			timeTook_READ = underTest.getKundenByPlz(plz);
		} while (timeTook_READ == 0);
		return timeTook_READ;
	}
	
	private long testGetDistinctOrte(DBInterface underTest) {
		long timeTook_READ;
		do {
			timeTook_READ = underTest.getDistinctOrte();
		} while (timeTook_READ == 0);
		return timeTook_READ;
	}
	
	private long testReadArtikel_byArtikelNummer(DBInterface underTest) {
		long timeTook_READ;
		String artikelNr = MockService.getRandomInsertedArtikelNummer();
		do {
			timeTook_READ = underTest.getArtikelByArtikelNummer(artikelNr);
		} while (timeTook_READ == 0);
		return timeTook_READ;
	}

	private long testReadArtikel_byArtikelName(DBInterface underTest) {
		long timeTook_READ;
		String artikelName = MockService.getRandomInsertedArtikelName();
		do {
			timeTook_READ = underTest.getArtikelByArtikelName(artikelName);
		} while (timeTook_READ == 0);
		return timeTook_READ;
	}
	
	private long testReadArtikel_whichCostMoreThan(DBInterface underTest) {
		long timeTook_READ;
		do {
			timeTook_READ = underTest.getArtikelWhichCostMoreThan(500d); //500 should be half
		} while (timeTook_READ == 0);
		return timeTook_READ;
	}
	
	private long testReadBewertungByKundenNrAndArtikelNr(DBInterface underTest) {
		long timeTook_READ;
		CompPrimaryKey ps = MockService.getRandombewertungPS();
		do {
			timeTook_READ = underTest.getBewertungByKundenNrAndArtikelNr(ps.getArtikelNummer(), ps.getKundenNummer());
		} while (timeTook_READ == 0);
		return timeTook_READ;
	}
	
	private long testReadBewertungByAnzahlSterne(DBInterface underTest) {
		long timeTook_READ;
		do {
			timeTook_READ = underTest.getBewertungenByAnzahlSterne(7); //The stars are rotated from 1 - 9, there should be an even amount of stars in the DB
		} while (timeTook_READ == 0);
		return timeTook_READ;
	}
	
	private long testReadEinkaeufeForKunde(DBInterface underTest) {
		long timeTook_READ;
		String kundenNr = MockService.getRandomInsertedKundenNummer();
		do {
			timeTook_READ = underTest.getEinkaeufeForKunde(kundenNr);
		} while (timeTook_READ == 0);
		return timeTook_READ;
	}
	
	private long testReadVerkaeufeForArtikel(DBInterface underTest) {
		long timeTook_READ;
		String artikelNr = MockService.getRandomInsertedArtikelNummer();
		do {
			timeTook_READ = underTest.getVerkauefeForArtikel(artikelNr);
		} while (timeTook_READ == 0);
		return timeTook_READ;
	}
	
	private long testUpdateKunde_Complete(DBInterface underTest) {
		long timeTook_UPDATE;
		do {				
			String testKndNr = MockService.getRandomInsertedKundenNummer();
			Kunde testKunde = MockService.randomizeFields(underTest.getKundeByKundenNr_kunde(testKndNr));
			timeTook_UPDATE = underTest.updateKunde(testKunde);
		} while (timeTook_UPDATE == 0);
		return timeTook_UPDATE;
	}
	
	private long testUpdateKunde_Nachname(DBInterface underTest) {
		long timeTook_UPDATE;
		do {				
			String kundenNummer = MockService.getRandomInsertedKundenNummer();
			timeTook_UPDATE = underTest.updateKundenNachname(kundenNummer, MockService.rndString.generate(7));
		} while (timeTook_UPDATE == 0);
		return timeTook_UPDATE;
	}
	
	private long testUpdateArtikel_Complete(DBInterface underTest) {
		long timeTook_UPDATE;
		String testArtikelNr = MockService.getRandomInsertedArtikelNummer();
		Artikel testArtikel = MockService.randomizeFields(underTest.getArtikelByArtikelNummer_artikel(testArtikelNr));
		do {				
			timeTook_UPDATE = underTest.updateArtikel(testArtikel);
		} while (timeTook_UPDATE == 0);
		return timeTook_UPDATE;
	}
	
	private long testUpdateBewertung_Complete(DBInterface underTest) {
		long timeTook_UPDATE;
		CompPrimaryKey ps = MockService.getRandombewertungPS();
		Bewertung testBewertung = MockService.randomizeFields(underTest.getBewertungByKundenNrAndArtikelNr_bewertung(ps.getArtikelNummer(), ps.getKundenNummer()));
		do {				
			timeTook_UPDATE = underTest.updateBewertung(testBewertung);
		} while (timeTook_UPDATE == 0);
		return timeTook_UPDATE;
	}
	
	private long testUpdateBewertung_Text(DBInterface underTest) {
		long timeTook_UPDATE;
		CompPrimaryKey ps = MockService.getRandombewertungPS();
		do {				
			timeTook_UPDATE = underTest.updateBewertungsText(ps.getKundenNummer(), ps.getArtikelNummer(), MockService.rndString.generate(600));
		} while (timeTook_UPDATE == 0);
		return timeTook_UPDATE;
	}
}
