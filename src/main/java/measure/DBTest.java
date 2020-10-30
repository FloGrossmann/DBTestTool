package measure;

import java.util.LinkedList;

import mariadb.MariaDBTest;
import monogdb.MongoDBTest;

public class DBTest {

	public static final int TESTSIZE = 500000;
	
	public MongoDBTest mongoDBTest;
	public MariaDBTest mariaDBTest;
	public LinkedList<AccessTime> mongoDBTestList = new LinkedList<AccessTime>();
	
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
		int oer = 0;
		for (int i = 0; i < TESTSIZE; i++) {
			
			// Kunde
			long timeTook = mongoDBTest.addKunde(MockService.getRandomKunde());
			if (timeTook == 0l) {
				System.err.println("0!");
				oer++;
			}
			mongoDBTestList.add(new AccessTime(CRUDoperation.INSERT, ObjectCategory.KUNDE, timeTook, MethodType.ADD_KUNDE));
			// Artikel
			timeTook = mongoDBTest.addArtikel(MockService.getRandomArtikel());
			mongoDBTestList.add(new AccessTime(CRUDoperation.INSERT, ObjectCategory.ARTIKEl, timeTook, MethodType.ADD_ARTIKEL));
			// Kauf
			timeTook = mongoDBTest.addKauf(MockService.getRandomKauf());
			mongoDBTestList.add(new AccessTime(CRUDoperation.INSERT, ObjectCategory.KAUF, timeTook, MethodType.ADD_KAUF));
			// Bewertung
			timeTook = mongoDBTest.addBewertung(MockService.getRandomBewertung());
			mongoDBTestList.add(new AccessTime(CRUDoperation.INSERT, ObjectCategory.BEWERTUNG, timeTook, MethodType.ADD_BEWERTUNG));
		}
		System.out.println(oer);
		try {
			CsvBeanWriter.writeCsvFromAccessTimeExample(mongoDBTestList, "mongoDB_CREATE");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
