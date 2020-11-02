package measure;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.text.CharacterPredicates;
import org.apache.commons.text.RandomStringGenerator;

import dbinterface.Adresse;
import dbinterface.Artikel;
import dbinterface.Bewertung;
import dbinterface.Kauf;
import dbinterface.Kunde;
import measure.util.CompPrimaryKey;

public class MockService {

	public static LinkedList<String> kundenNummern = new LinkedList<String>();
	public static LinkedList<String> emails = new LinkedList<String>();
	public static final String[] plzs = { "76131", "76133", "76135", "76137", "76139", "76149", "76185", "76187",
			"76189", "76199", "76227", "76228", "76229", "10115", "10117", "10119", "10176", "10178", "10179", "10243",
			"10245", "10247", "10249", "10315", "10317", "10318", "10319", "10365", "10367", "10369", "10405", "10407",
			"10409", "10435", "10437", "10439", "10551", "10553", "10555", "10557", "10559", "10585", "10587", "10589",
			"10623", "10625", "10627", "10629", "10707", "10709", "10711", "10713", "10715", "10717", "10719", "10777",
			"10779", "10781", "10783", "10785", "10787", "10789", "10823", "10825", "10827", "10829", "10961", "10963",
			"10965", "10967", "10969", "10997", "10999", "12043", "12045", "12047", "12049" };

	public static LinkedList<String> artikelNummern = new LinkedList<String>();
	public static LinkedList<String> artikelNamen = new LinkedList<String>();
	public static int currentStar = 1;
	
	public static LinkedList<CompPrimaryKey> bewertungPS = new LinkedList<CompPrimaryKey>();
	// To keep track of duplicate keys
	public static Set<CompPrimaryKey> kaufPS_SET = new HashSet<CompPrimaryKey>();
	public static Set<CompPrimaryKey> bewertungPS_SET = new HashSet<CompPrimaryKey>();
	
	public static CompPrimaryKey getRandomUniqueCompKey_Kauf() {
		String artikelNr;
		String kundenNr;
		CompPrimaryKey key = null;
		do {
			artikelNr = getRandomInsertedArtikelNummer();
			kundenNr = getRandomInsertedKundenNummer();
			key = new CompPrimaryKey(artikelNr, kundenNr);
		} while (kaufPS_SET.contains(key));
		kaufPS_SET.add(key);
		return key;
	}
	
	public static CompPrimaryKey getRandomUniqueCompKey_Bewertung() {
		String artikelNr;
		String kundenNr;
		CompPrimaryKey key = null;
		do {
			artikelNr = getRandomInsertedArtikelNummer();
			kundenNr = getRandomInsertedKundenNummer();
			key = new CompPrimaryKey(artikelNr, kundenNr);
		} while (bewertungPS_SET.contains(key));
		bewertungPS.add(key);
		bewertungPS_SET.add(key);
		return key;
	}
	
	public static int getNextCurrentStar() {
		currentStar++;
		if (currentStar % 10 == 0) {
			currentStar = 1;
		}
		return currentStar;
	}

	public static String getRandomInsertedArtikelName() {
		return artikelNamen.get(ThreadLocalRandom.current().nextInt(0, artikelNamen.size()));
	}
	
	public static String getRandomInsertedKundenNummer() {
		return kundenNummern.get(ThreadLocalRandom.current().nextInt(0, kundenNummern.size()));
	}

	public static String getRandomInsertedArtikelNummer() {
		return artikelNummern.get(ThreadLocalRandom.current().nextInt(0, artikelNummern.size()));
	}

	public static String getRandomInsertedEmail() {
		return emails.get(ThreadLocalRandom.current().nextInt(0, emails.size()));
	}

	public static String getRandomPLZ() {
		return plzs[ThreadLocalRandom.current().nextInt(0, plzs.length - 1)];
	}

	public static CompPrimaryKey getRandombewertungPS() {
		return bewertungPS.get(ThreadLocalRandom.current().nextInt(0, bewertungPS.size()));
	}

	public static RandomStringGenerator rndString = new RandomStringGenerator.Builder().withinRange('a', 'z')
			.filteredBy(CharacterPredicates.LETTERS, CharacterPredicates.DIGITS).build();

	public static RandomStringGenerator rndInt = new RandomStringGenerator.Builder().withinRange('0', '9')
			.filteredBy(CharacterPredicates.LETTERS, CharacterPredicates.DIGITS).build();

	public static RandomStringGenerator rndStringInt = new RandomStringGenerator.Builder().withinRange('0', 'z')
			.filteredBy(CharacterPredicates.LETTERS, CharacterPredicates.DIGITS).build();

	public static LocalDate getRandomDate() {
		Random random = new Random();
		int minDay = (int) LocalDate.of(1900, 1, 1).toEpochDay();
		int maxDay = (int) LocalDate.of(2015, 1, 1).toEpochDay();
		long randomDay = minDay + random.nextInt(maxDay - minDay);
		return LocalDate.ofEpochDay(randomDay);
	}

	public static Kunde genRandomInsertKunde() {

		Kunde kunde = new Kunde();
		String email = UUID.randomUUID().toString();
		kunde.setEmail(email);
		emails.add(email);
		String kundenNummer = UUID.randomUUID().toString();
		kunde.setKundenNummer(kundenNummer);
		kundenNummern.add(kundenNummer);
		kunde.setNachname(rndString.generate(7));
		kunde.setVorname(rndString.generate(7));
		kunde.setTelefonNummer(rndInt.generate(12).toString());

		Adresse adresse = new Adresse();
		adresse.setHausnummer(rndStringInt.generate(3));
		adresse.setPlz(getRandomPLZ());
		adresse.setOrtschaft(adresse.getPlz().startsWith("7") ? "Karlsruhe" : "Berlin");
		adresse.setStrasse(rndString.generate(12));
		kunde.setAdresse(adresse);
		return kunde;
	}
	
	public static Kunde randomizeFields(Kunde kunde) {

		kunde.setNachname(rndString.generate(7));
		kunde.setVorname(rndString.generate(7));
		kunde.setTelefonNummer(rndInt.generate(12).toString());

		Adresse adresse = new Adresse();
		adresse.setHausnummer(rndStringInt.generate(3));
		adresse.setPlz(getRandomPLZ());
		adresse.setOrtschaft(adresse.getPlz().startsWith("7") ? "Karlsruhe" : "Berlin");
		adresse.setStrasse(rndString.generate(12));
		kunde.setAdresse(adresse);
		return kunde;
	}

	public static Artikel genRandomInsertArtikel() {
		Artikel artikel = new Artikel();
		String artikelName = rndString.generate(12);
		artikel.setArtikelName(artikelName);
		artikelNamen.add(artikelName);
		String artikelNummer = UUID.randomUUID().toString();
		artikelNummern.add(artikelNummer);
		artikel.setArtikelNummer(artikelNummer);
		artikel.setBeschreibung(rndString.generate(40));
		artikel.setEinzelPreis(Integer.valueOf(rndInt.generate(3)));
		artikel.setWaehrung(rndString.generate(4));
		return artikel;
	}
	
	public static Artikel randomizeFields(Artikel artikel) {

		artikel.setBeschreibung(rndString.generate(40));
		artikel.setEinzelPreis(Integer.valueOf(rndInt.generate(3)));
		artikel.setWaehrung(rndString.generate(4));
		return artikel;
	}

	public static Kauf genRandomInsertKauf() {
		if (artikelNummern.size() == 0 || kundenNummern.size() == 0) {
			throw new RuntimeException("No Kunde or artikel to assign the kauf object to");
		}

		Kauf kauf = new Kauf();
		kauf.setKaufPreis(Integer.valueOf(rndInt.generate(3)));
		kauf.setKaufdatum(java.sql.Date.valueOf(getRandomDate()));
		CompPrimaryKey ps = getRandomUniqueCompKey_Kauf();
		kauf.setArtikelNummer(ps.getArtikelNummer());
		kauf.setKundenNummer(ps.getKundenNummer());
		kauf.setMenge(ThreadLocalRandom.current().nextInt(0, 200));
		return kauf;
	}

	public static Bewertung genRandomInsertBewertung() {
		if (artikelNummern.size() == 0 || kundenNummern.size() == 0) {
			throw new RuntimeException("No Kunde or artikel to assign the kauf object to");
		}
		Bewertung bewertung = new Bewertung();
		bewertung.setBewertung(rndString.generate(600));
		bewertung.setSterne(ThreadLocalRandom.current().nextInt(1, 11));
		CompPrimaryKey ps = getRandomUniqueCompKey_Bewertung();
		bewertung.setArtikelNummer(ps.getArtikelNummer());
		bewertung.setKundenNummer(ps.getKundenNummer());
		return bewertung;
	}
	
	public static Bewertung randomizeFields(Bewertung bewertung) {
		bewertung.setBewertung(rndString.generate(600));
		bewertung.setSterne(ThreadLocalRandom.current().nextInt(1, 11));
		return bewertung;
	}

	public static void removeArtikelNr(String artikelNr) {
		artikelNummern.remove(artikelNr);
	}

	public static void removeKundenNr(String kundenNr) {
		kundenNummern.remove(kundenNr);
	}

	public static void removeBewertungPS(String artikelNr, String kundenNr) {
		removeBewertungPS(new CompPrimaryKey(artikelNr, kundenNr));
	}

	public static void removeBewertungPS(CompPrimaryKey ps) {
		bewertungPS.remove(ps);
		bewertungPS_SET.remove(ps);
	}
	
	public static void removeKaufPS(String artikelNr, String kundenNr) {
		removeKaufPS(new CompPrimaryKey(artikelNr, kundenNr));
	}

	public static void removeKaufPS(CompPrimaryKey ps) {
		kaufPS_SET.remove(ps);
	}
	
	public static void removeEmail(String email) {
		emails.remove(email);
	}

	public static void clearIdLists() {
		artikelNummern.clear();
		kundenNummern.clear();
		emails.clear();
		artikelNamen.clear();
		bewertungPS.clear();
		currentStar = 1;
		kaufPS_SET.clear();
		bewertungPS_SET.clear();
	}
}
