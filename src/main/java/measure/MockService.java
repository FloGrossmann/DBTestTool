package measure;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.Random;
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

	public static Kunde getRandomKunde() {

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

	public static Artikel getRandomArtikel() {
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

	public static Kauf getRandomKauf() {
		if (artikelNummern.size() == 0 || kundenNummern.size() == 0) {
			throw new RuntimeException("No Kunde or artikel to assign the kauf object to");
		}

		Kauf kauf = new Kauf();
		kauf.setKaufPreis(Integer.valueOf(rndInt.generate(3)));
		kauf.setKaufdatum(java.sql.Date.valueOf(getRandomDate()));
		String artikelNr = getRandomInsertedArtikelNummer();
		kauf.setArtikelNummer(artikelNr);
		String kundenNr = getRandomInsertedKundenNummer();
		kauf.setArtikelNummer(kundenNr);
		kauf.setMenge(ThreadLocalRandom.current().nextInt(0, 200));
		return kauf;
	}

	public static Bewertung getRandomBewertung() {
		if (artikelNummern.size() == 0 || kundenNummern.size() == 0) {
			throw new RuntimeException("No Kunde or artikel to assign the kauf object to");
		}
		Bewertung bewertung = new Bewertung();
		bewertung.setBewertung(rndString.generate(600));
		bewertung.setSterne(ThreadLocalRandom.current().nextInt(1, 11));
		String artikelNr = getRandomInsertedArtikelNummer();
		bewertung.setArtikelNummer(artikelNr);
		String kundenNr = getRandomInsertedKundenNummer();
		bewertung.setKundenNummer(kundenNr);
		bewertungPS.add(new CompPrimaryKey(artikelNr, kundenNr));
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
	}

	public static void clearIdLists() {
		artikelNummern.clear();
		kundenNummern.clear();
	}
}
