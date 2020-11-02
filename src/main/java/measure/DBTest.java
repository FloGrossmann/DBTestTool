package measure;

import java.util.LinkedList;

import dbinterface.Artikel;
import dbinterface.Bewertung;
import dbinterface.Kauf;
import dbinterface.Kunde;
import mariadb.MariaDBTest;
import measure.util.CompPrimaryKey;
import monogdb.MongoDBTest;

public class DBTest {

	public MongoDBTest mongoDBTest;
	public MariaDBTest mariaDBTest;
	public LinkedList<AccessTime> mongoDBTestList = new LinkedList<AccessTime>();
	public static final int TESTEVERY = 10000;
	public static final int REPETITIONS = 1000;
	public static final int MAXIMUM = 50000;
	
	public void startTest(String mongoDBConnectionString, String mariaDBConnectionString,
			String mariaDBUsername, String mariaDBPassword) throws Exception {
		mongoDBTest = new MongoDBTest();
		mongoDBTest.connect(mongoDBConnectionString);
		mongoDBTest.setupDB();
		mariaDBTest = new MariaDBTest(mariaDBConnectionString, mariaDBUsername, mariaDBPassword);
		mariaDBTest.connect();
		mariaDBTest.setupDB();
		
		
		
		mongoDBCreateTest();
	}
	
	public void mongoDBCreateTest() {
		
		//CREATE
		LinkedList<AccessTime> mongoDB_Create_Kunde = new LinkedList<AccessTime>();
		LinkedList<AccessTime> mongoDB_Create_Artikel = new LinkedList<AccessTime>();
		LinkedList<AccessTime> mongoDB_Create_Kauf = new LinkedList<AccessTime>();
		LinkedList<AccessTime> mongoDB_Create_Bewertung = new LinkedList<AccessTime>();
		
		//DELETE
		LinkedList<AccessTime> mongoDB_Delete_Kunde = new LinkedList<AccessTime>();
		LinkedList<AccessTime> mongoDB_Delete_Artikel = new LinkedList<AccessTime>();
		LinkedList<AccessTime> mongoDB_Delete_Kauf = new LinkedList<AccessTime>();
		LinkedList<AccessTime> mongoDB_Delete_Bewertung = new LinkedList<AccessTime>();
		
		//READ
		LinkedList<AccessTime> mongoDB_Read_Kunde_ByKndNr = new LinkedList<AccessTime>();
		LinkedList<AccessTime> mongoDB_Read_Kunde_ByEmail = new LinkedList<AccessTime>();
		LinkedList<AccessTime> mongoDB_Read_Kunde_ByPLZ = new LinkedList<AccessTime>();
		LinkedList<AccessTime> mongoDB_Read_Kunde_Distinct_Orte = new LinkedList<AccessTime>();
		LinkedList<AccessTime> mongoDB_Read_Artikel_ByArtNr = new LinkedList<AccessTime>();
		LinkedList<AccessTime> mongoDB_Read_Artikel_ByArtName = new LinkedList<AccessTime>();
		LinkedList<AccessTime> mongoDB_Read_Artikel_WhichCostMoreThan = new LinkedList<AccessTime>();
		LinkedList<AccessTime> mongoDB_Read_Kauf_ForKundeNr = new LinkedList<AccessTime>();
		LinkedList<AccessTime> mongoDB_Read_Kauf_ForArtikelNr = new LinkedList<AccessTime>();
		LinkedList<AccessTime> mongoDB_Read_Bewertung_ByANr_KNr = new LinkedList<AccessTime>();
		LinkedList<AccessTime> mongoDB_Read_Bewertung_BySterne = new LinkedList<AccessTime>();
		
		//UPDATE
		LinkedList<AccessTime> mongoDB_Update_Kunde_Complete = new LinkedList<AccessTime>();
		LinkedList<AccessTime> mongoDB_Update_Kunde_Nachname = new LinkedList<AccessTime>();
		LinkedList<AccessTime> mongoDB_Update_Artikel_Complete = new LinkedList<AccessTime>();
		LinkedList<AccessTime> mongoDB_Update_Bewertung_Complete = new LinkedList<AccessTime>();
		LinkedList<AccessTime> mongoDB_Update_Bewertung_Text = new LinkedList<AccessTime>();
		
		int size = 0;
		
		while (size < MAXIMUM) {
			do {
				//Add more entries
				// Kunde
				mongoDBTest.addKunde(MockService.getRandomKunde());
				// Artikel
				mongoDBTest.addArtikel(MockService.getRandomArtikel());
				// Kauf
				mongoDBTest.addKauf(MockService.getRandomKauf());
				// Bewertung
				mongoDBTest.addBewertung(MockService.getRandomBewertung());
				size++;
			} while (size % TESTEVERY != 0 || size == 0);
			
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

				// Kunde
				testListCreate_Kunde.add(testAddKunde());
				testListDelete_Kunde.add(testDeleteKunde());
				testListRead_KundeByKndNr.add(testReadKunde_byKundenNummer());
				testListRead_KundeByEmail.add(testReadKunde_byEmail());
				testListRead_Kunden_byPLZ.add(testReadKunden_byPLZ());
				testList_GetDistinctOrte.add(testGetDistinctOrte());
				testListUpdate_Kunde_Complete.add(testUpdateKunde_Complete());
				testListUpdate_Kunde_Nachname.add(testUpdateKunde_Nachname());
				
				// Artikel
				testListCreate_Artikel.add(testAddArtikel());
				testListDelete_Artikel.add(testDeleteArtikel());
				testListRead_ArtikelByArtikelNr.add(testReadArtikel_byArtikelNummer());
				testListRead_ArtikelByArtikelName.add(testReadArtikel_byArtikelName());
				testListRead_ArtikelWhichCostMoreThan.add(testReadArtikel_whichCostMoreThan());
				testListUpdate_Artikel_Complete.add(testUpdateArtikel_Complete());
				
				// Kauf
				testListCreate_Kauf.add(testAddKauf());
				testListDelete_Kauf.add(testDeleteKauf());
				testListRead_Kauf_ForKundeNr.add(testReadEinkaeufeForKunde());
				testListRead_Kauf_ForArtikelNr.add(testReadVerkaeufeForArtikel());
				
				// Bewertung
				testListCreate_Bewertung.add(testAddBewertung());
				testListDelete_Bewertung.add(testDeleteBewertung());
				testListRead_BewertungByANr_KNr.add(testReadBewertungByKundenNrAndArtikelNr());
				testListRead_BewertungBySterne.add(testReadBewertungByAnzahlSterne());
				testListUpdate_Bewertung_Complete.add(testUpdateBewertung_Complete());
				testListUpdate_Bewertung_Text.add(testUpdateBewertung_Text());
				System.out.println("Repetition: " + i);
			}
			// Calculate & Store the results
			// Create
			mongoDB_Create_Kunde.add(new AccessTime(CRUDoperation.INSERT, ObjectCategory.KUNDE, MethodType.ADD_KUNDE, new Messreihe(testListCreate_Kunde), size));
			mongoDB_Create_Artikel.add(new AccessTime(CRUDoperation.INSERT, ObjectCategory.ARTIKEL, MethodType.ADD_ARTIKEL, new Messreihe(testListCreate_Artikel), size));
			mongoDB_Create_Bewertung.add(new AccessTime(CRUDoperation.INSERT, ObjectCategory.BEWERTUNG, MethodType.ADD_BEWERTUNG, new Messreihe(testListCreate_Bewertung), size));
			mongoDB_Create_Kauf.add(new AccessTime(CRUDoperation.INSERT, ObjectCategory.KAUF, MethodType.ADD_KAUF, new Messreihe(testListCreate_Kauf), size));
			
			//Delete
			mongoDB_Delete_Kunde.add(new AccessTime(CRUDoperation.DELETE, ObjectCategory.KUNDE, MethodType.DELETE_KUNDE_BYKUNDENNR, new Messreihe(testListDelete_Kunde), size));
			mongoDB_Delete_Artikel.add(new AccessTime(CRUDoperation.DELETE, ObjectCategory.ARTIKEL, MethodType.DELETE_ARTIKEL_BYARTIKELNR, new Messreihe(testListDelete_Artikel), size));
			mongoDB_Delete_Bewertung.add(new AccessTime(CRUDoperation.DELETE, ObjectCategory.BEWERTUNG, MethodType.DELETE_BEWERTUNG_BYARTIKELNRANDKUNDENNR, new Messreihe(testListDelete_Bewertung), size));
			mongoDB_Delete_Kauf.add(new AccessTime(CRUDoperation.DELETE, ObjectCategory.KAUF, MethodType.DELETE_KAUF_BYARTIKELNRANDKUNDENNR, new Messreihe(testListDelete_Kauf), size));
			
			// Read
			mongoDB_Read_Kunde_ByKndNr.add(new AccessTime(CRUDoperation.SELECT, ObjectCategory.KUNDE, MethodType.GET_KUNDE_BYKUNDENNR, new Messreihe(testListRead_KundeByKndNr), size));
			mongoDB_Read_Kunde_ByEmail.add(new AccessTime(CRUDoperation.SELECT, ObjectCategory.KUNDE, MethodType.GET_KUNDE_BYEMAIL, new Messreihe(testListRead_KundeByEmail), size));
			mongoDB_Read_Kunde_ByPLZ.add(new AccessTime(CRUDoperation.SELECT, ObjectCategory.KUNDE, MethodType.GET_KUNDEN_BYPLZ, new Messreihe(testListRead_Kunden_byPLZ), size));
			mongoDB_Read_Kunde_Distinct_Orte.add(new AccessTime(CRUDoperation.SELECT, ObjectCategory.KUNDE, MethodType.GET_DISTINCTORTE, new Messreihe(testList_GetDistinctOrte), size));
			mongoDB_Read_Artikel_ByArtNr.add(new AccessTime(CRUDoperation.SELECT, ObjectCategory.ARTIKEL, MethodType.GET_ARTIKEL_BYARTIKELNUMMER, new Messreihe(testListRead_ArtikelByArtikelNr), size));
			mongoDB_Read_Artikel_ByArtName.add(new AccessTime(CRUDoperation.SELECT, ObjectCategory.ARTIKEL, MethodType.GET_ARTIKEL_BYARTIKELNAME, new Messreihe(testListRead_ArtikelByArtikelName), size));
			mongoDB_Read_Artikel_WhichCostMoreThan.add(new AccessTime(CRUDoperation.SELECT, ObjectCategory.ARTIKEL, MethodType.GET_ARTIKEL_WHICHCOSTMORETHAN, new Messreihe(testListRead_ArtikelWhichCostMoreThan), size));
			mongoDB_Read_Kauf_ForKundeNr.add(new AccessTime(CRUDoperation.SELECT, ObjectCategory.KAUF, MethodType.GET_EINKAEUFE_FORKUNDEN, new Messreihe(testListRead_Kauf_ForKundeNr), size));
			mongoDB_Read_Kauf_ForArtikelNr.add(new AccessTime(CRUDoperation.SELECT, ObjectCategory.KAUF, MethodType.GET_EINKAUEFE_FORARTIKEL, new Messreihe(testListRead_Kauf_ForArtikelNr), size));
			mongoDB_Read_Bewertung_ByANr_KNr.add(new AccessTime(CRUDoperation.SELECT, ObjectCategory.BEWERTUNG, MethodType.GET_BEWERTUNG_BYKUNDENNRANDARTIKELNR, new Messreihe(testListRead_BewertungByANr_KNr), size));
			mongoDB_Read_Bewertung_BySterne.add(new AccessTime(CRUDoperation.SELECT, ObjectCategory.BEWERTUNG, MethodType.GET_KUNDE_BYKUNDENNR, new Messreihe(testListRead_BewertungBySterne), size));
			
			//Update
			mongoDB_Update_Kunde_Complete.add(new AccessTime(CRUDoperation.UPDATE, ObjectCategory.KUNDE, MethodType.UPDATE_KUNDE_COMPLETE, new Messreihe(testListUpdate_Kunde_Complete), size));
			mongoDB_Update_Kunde_Nachname.add(new AccessTime(CRUDoperation.UPDATE, ObjectCategory.KUNDE, MethodType.UPDATE_KUNDE_NACHNAME, new Messreihe(testListUpdate_Kunde_Nachname), size));
			mongoDB_Update_Artikel_Complete.add(new AccessTime(CRUDoperation.UPDATE, ObjectCategory.ARTIKEL, MethodType.UPDATE_ARTIKEL_COMPLETE, new Messreihe(testListUpdate_Artikel_Complete), size));
			mongoDB_Update_Bewertung_Complete.add(new AccessTime(CRUDoperation.UPDATE, ObjectCategory.BEWERTUNG, MethodType.UPDATE_BEWERTUNG_COMPLETE, new Messreihe(testListUpdate_Bewertung_Complete), size));
			mongoDB_Update_Bewertung_Text.add(new AccessTime(CRUDoperation.UPDATE, ObjectCategory.BEWERTUNG, MethodType.UPDATE_BEWERTUNG_TEXT, new Messreihe(testListUpdate_Bewertung_Text), size));
			System.out.println("Round " + size + " -+-+-+-+-+-+-");
		}
		
		// Write to csv
		try {
			CsvBeanWriter.writeCsvFromAccessTimeExample(mongoDB_Create_Kunde, "mongoDB_Create_Kunde");
			CsvBeanWriter.writeCsvFromAccessTimeExample(mongoDB_Create_Artikel, "mongoDB_Create_Artikel");
			CsvBeanWriter.writeCsvFromAccessTimeExample(mongoDB_Create_Bewertung, "mongoDB_Create_Bewertung");
			CsvBeanWriter.writeCsvFromAccessTimeExample(mongoDB_Create_Kauf, "mongoDB_Create_Kauf");
			
			CsvBeanWriter.writeCsvFromAccessTimeExample(mongoDB_Delete_Kunde, "mongoDB_Delete_Kunde");
			CsvBeanWriter.writeCsvFromAccessTimeExample(mongoDB_Delete_Artikel, "mongoDB_Delete_Artikel");
			CsvBeanWriter.writeCsvFromAccessTimeExample(mongoDB_Delete_Bewertung, "mongoDB_Delete_Bewertung");
			CsvBeanWriter.writeCsvFromAccessTimeExample(mongoDB_Delete_Kauf, "mongoDB_Delete_Kauf");
			
			CsvBeanWriter.writeCsvFromAccessTimeExample(mongoDB_Read_Kunde_ByKndNr, "mongoDB_Read_Kunde_ByKndNr");
			CsvBeanWriter.writeCsvFromAccessTimeExample(mongoDB_Read_Kunde_ByEmail, "mongoDB_Read_Kunde_ByEmail");
			CsvBeanWriter.writeCsvFromAccessTimeExample(mongoDB_Read_Kunde_ByPLZ, "mongoDB_Read_Kunde_ByPLZ");
			CsvBeanWriter.writeCsvFromAccessTimeExample(mongoDB_Read_Kunde_Distinct_Orte, "mongoDB_Read_Kunde_Distinct_Orte");
			CsvBeanWriter.writeCsvFromAccessTimeExample(mongoDB_Read_Artikel_ByArtNr, "mongoDB_Read_Artikel_ByArtNr");
			CsvBeanWriter.writeCsvFromAccessTimeExample(mongoDB_Read_Artikel_ByArtName, "mongoDB_Read_Artikel_ByArtName");
			CsvBeanWriter.writeCsvFromAccessTimeExample(mongoDB_Read_Artikel_WhichCostMoreThan, "mongoDB_Read_Artikel_WhichCostMoreThan");
			CsvBeanWriter.writeCsvFromAccessTimeExample(mongoDB_Read_Kauf_ForKundeNr, "mongoDB_Read_Kauf_ForKundeNr");
			CsvBeanWriter.writeCsvFromAccessTimeExample(mongoDB_Read_Kauf_ForArtikelNr, "mongoDB_Read_Kauf_ForArtikelNr");
			CsvBeanWriter.writeCsvFromAccessTimeExample(mongoDB_Read_Bewertung_ByANr_KNr, "mongoDB_Read_Bewertung_ByANr_KNr");
			CsvBeanWriter.writeCsvFromAccessTimeExample(mongoDB_Read_Bewertung_BySterne, "mongoDB_Read_Bewertung_BySterne");
			
			CsvBeanWriter.writeCsvFromAccessTimeExample(mongoDB_Update_Kunde_Complete, "mongoDB_Update_Kunde_Complete");
			CsvBeanWriter.writeCsvFromAccessTimeExample(mongoDB_Update_Kunde_Nachname, "mongoDB_Update_Kunde_Nachname");
			CsvBeanWriter.writeCsvFromAccessTimeExample(mongoDB_Update_Artikel_Complete, "mongoDB_Update_Artikel_Complete");
			CsvBeanWriter.writeCsvFromAccessTimeExample(mongoDB_Update_Bewertung_Complete, "mongoDB_Update_Bewertung_Complete");
			CsvBeanWriter.writeCsvFromAccessTimeExample(mongoDB_Update_Bewertung_Text, "mongoDB_Update_Bewertung_Text");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private long testAddKunde() {
		long timeTook_CREATE;
		Kunde testKunde = MockService.getRandomKunde();
		do {				
			timeTook_CREATE = mongoDBTest.addKunde(testKunde);
			mongoDBTest.deleteKundeByKundenNr(testKunde.getKundenNummer());
			MockService.removeKundenNr(testKunde.getKundenNummer());
		} while (timeTook_CREATE == 0);
		return timeTook_CREATE;
	}
	
	private long testDeleteKunde() {
		long timeTook_DELETE;
		Kunde testKunde = MockService.getRandomKunde();
		do {
			mongoDBTest.addKunde(testKunde);
			timeTook_DELETE = mongoDBTest.deleteKundeByKundenNr(testKunde.getKundenNummer());
			MockService.removeKundenNr(testKunde.getKundenNummer());
		} while (timeTook_DELETE == 0);
		return timeTook_DELETE;
	}
	
	private long testAddArtikel() {
		Artikel testArtikel = MockService.getRandomArtikel();
		long timeTook_CREATE = 0;
		do {				
			timeTook_CREATE = mongoDBTest.addArtikel(testArtikel);
			mongoDBTest.deleteArtikelbyArtikelNr(testArtikel.getArtikelNummer());
			MockService.removeArtikelNr(testArtikel.getArtikelNummer());
		} while (timeTook_CREATE == 0);
		return timeTook_CREATE;
	}
	
	private long testDeleteArtikel() {
		long timeTook_DELETE;
		Artikel testArtikel = MockService.getRandomArtikel();
		do {
			mongoDBTest.addArtikel(testArtikel);
			timeTook_DELETE = mongoDBTest.deleteArtikelbyArtikelNr(testArtikel.getArtikelNummer());
			MockService.removeKundenNr(testArtikel.getArtikelNummer());
		} while (timeTook_DELETE == 0);
		return timeTook_DELETE;
	}
	
	private long testAddKauf() {
		Kauf testKauf = MockService.getRandomKauf();
		long timeTook_CREATE = 0;
		do {				
			timeTook_CREATE = mongoDBTest.addKauf(testKauf);
			mongoDBTest.deleteKaufByArtikelNrAndKundenNr(testKauf.getArtikelNummer(), testKauf.getKundenNummer());
		} while (timeTook_CREATE == 0);
		return timeTook_CREATE;
	}
	
	private long testDeleteKauf() {
		long timeTook_DELETE;
		Kauf testKauf = MockService.getRandomKauf();
		do {
			mongoDBTest.addKauf(testKauf);
			timeTook_DELETE = mongoDBTest.deleteKaufByArtikelNrAndKundenNr(testKauf.getArtikelNummer(), testKauf.getKundenNummer());
		} while (timeTook_DELETE == 0);
		return timeTook_DELETE;
	}
	
	private long testAddBewertung() {
		Bewertung testBewertung = MockService.getRandomBewertung();
		long timeTook_CREATE = 0;
		do {				
			timeTook_CREATE = mongoDBTest.addBewertung(testBewertung);
			mongoDBTest.deleteBewertungByArtikelNrAndKundenNr(testBewertung.getArtikelNummer(), testBewertung.getKundenNummer());
			MockService.removeBewertungPS(testBewertung.getArtikelNummer(), testBewertung.getKundenNummer());
		} while (timeTook_CREATE == 0);
		return timeTook_CREATE;
	}
	
	private long testDeleteBewertung() {
		long timeTook_DELETE;
		Bewertung testBewertung = MockService.getRandomBewertung();
		do {
			mongoDBTest.addBewertung(testBewertung);
			timeTook_DELETE = mongoDBTest.deleteBewertungByArtikelNrAndKundenNr(testBewertung.getArtikelNummer(), testBewertung.getKundenNummer());
			MockService.removeBewertungPS(testBewertung.getArtikelNummer(), testBewertung.getKundenNummer());
		} while (timeTook_DELETE == 0);
		return timeTook_DELETE;
	}
	
	private long testReadKunde_byKundenNummer() {
		long timeTook_READ;
		String kundenNr = MockService.getRandomInsertedKundenNummer();
		do {
			timeTook_READ = mongoDBTest.getKundeByKundenNr(kundenNr);
		} while (timeTook_READ == 0);
		return timeTook_READ;
	}
	
	private long testReadKunde_byEmail() {
		long timeTook_READ;
		String email = MockService.getRandomInsertedEmail();
		do {
			timeTook_READ = mongoDBTest.getKundeByEmail(email);
		} while (timeTook_READ == 0);
		return timeTook_READ;
	}
	
	private long testReadKunden_byPLZ() {
		long timeTook_READ;
		String plz = MockService.getRandomPLZ();
		do {
			timeTook_READ = mongoDBTest.getKundenByPlz(plz);
		} while (timeTook_READ == 0);
		return timeTook_READ;
	}
	
	private long testGetDistinctOrte() {
		long timeTook_READ;
		do {
			timeTook_READ = mongoDBTest.getDistinctOrte();
		} while (timeTook_READ == 0);
		return timeTook_READ;
	}
	
	private long testReadArtikel_byArtikelNummer() {
		long timeTook_READ;
		String artikelNr = MockService.getRandomInsertedArtikelNummer();
		do {
			timeTook_READ = mongoDBTest.getArtikelByArtikelNummer(artikelNr);
		} while (timeTook_READ == 0);
		return timeTook_READ;
	}

	private long testReadArtikel_byArtikelName() {
		long timeTook_READ;
		String artikelName = MockService.getRandomInsertedArtikelName();
		do {
			timeTook_READ = mongoDBTest.getArtikelByArtikelName(artikelName);
		} while (timeTook_READ == 0);
		return timeTook_READ;
	}
	
	private long testReadArtikel_whichCostMoreThan() {
		long timeTook_READ;
		do {
			timeTook_READ = mongoDBTest.getArtikelWhichCostMoreThan(500d); //500 should be half
		} while (timeTook_READ == 0);
		return timeTook_READ;
	}
	
	private long testReadBewertungByKundenNrAndArtikelNr() {
		long timeTook_READ;
		CompPrimaryKey ps = MockService.getRandombewertungPS();
		do {
			timeTook_READ = mongoDBTest.getBewertungByKundenNrAndArtikelNr(ps.getArtikelNummer(), ps.getKundenNummer());
		} while (timeTook_READ == 0);
		return timeTook_READ;
	}
	
	private long testReadBewertungByAnzahlSterne() {
		long timeTook_READ;
		do {
			timeTook_READ = mongoDBTest.getBewertungenByAnzahlSterne(7); //The stars are rotated from 1 - 9, there should be an even amount of stars in the DB
		} while (timeTook_READ == 0);
		return timeTook_READ;
	}
	
	private long testReadEinkaeufeForKunde() {
		long timeTook_READ;
		String kundenNr = MockService.getRandomInsertedKundenNummer();
		do {
			timeTook_READ = mongoDBTest.getEinkaeufeForKunde(kundenNr);
		} while (timeTook_READ == 0);
		return timeTook_READ;
	}
	
	private long testReadVerkaeufeForArtikel() {
		long timeTook_READ;
		String artikelNr = MockService.getRandomInsertedArtikelNummer();
		do {
			timeTook_READ = mongoDBTest.getVerkauefeForArtikel(artikelNr);
		} while (timeTook_READ == 0);
		return timeTook_READ;
	}
	
	private long testUpdateKunde_Complete() {
		long timeTook_UPDATE;
		Kunde testKunde = MockService.getRandomKunde();
		String kundenNummer = MockService.getRandomInsertedKundenNummer();
		testKunde.setKundenNummer(kundenNummer);
		do {				
			timeTook_UPDATE = mongoDBTest.updateKunde(testKunde);
		} while (timeTook_UPDATE == 0);
		return timeTook_UPDATE;
	}
	
	private long testUpdateKunde_Nachname() {
		long timeTook_UPDATE;
		String kundenNummer = MockService.getRandomInsertedKundenNummer();
		do {				
			timeTook_UPDATE = mongoDBTest.updateKundenNachname(kundenNummer, MockService.rndString.generate(7));
		} while (timeTook_UPDATE == 0);
		return timeTook_UPDATE;
	}
	
	private long testUpdateArtikel_Complete() {
		long timeTook_UPDATE;
		Artikel testArtikel = MockService.getRandomArtikel();
		String artikelNummer = MockService.getRandomInsertedArtikelNummer();
		testArtikel.setArtikelNummer(artikelNummer);
		do {				
			timeTook_UPDATE = mongoDBTest.updateArtikel(testArtikel);
		} while (timeTook_UPDATE == 0);
		return timeTook_UPDATE;
	}
	
	private long testUpdateBewertung_Complete() {
		long timeTook_UPDATE;
		Bewertung testBewertung = MockService.getRandomBewertung();
		CompPrimaryKey ps = MockService.getRandombewertungPS();
		testBewertung.setArtikelNummer(ps.getArtikelNummer());
		testBewertung.setKundenNummer(ps.getKundenNummer());
		do {				
			timeTook_UPDATE = mongoDBTest.updateBewertung(testBewertung);
		} while (timeTook_UPDATE == 0);
		return timeTook_UPDATE;
	}
	
	private long testUpdateBewertung_Text() {
		long timeTook_UPDATE;
		CompPrimaryKey ps = MockService.getRandombewertungPS();
		do {				
			timeTook_UPDATE = mongoDBTest.updateBewertungsText(ps.getKundenNummer(), ps.getArtikelNummer(), MockService.rndString.generate(600));
		} while (timeTook_UPDATE == 0);
		return timeTook_UPDATE;
	}
}
