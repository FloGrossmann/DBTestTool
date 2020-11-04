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

	public static int kundenNummer = 1;
	public static LinkedList<String> emails = new LinkedList<String>();
	public static final String[] plzs = { "76131", "76133", "76135", "76137", "76139", "76149", "76185", "76187",
			"76189", "76199", "76227", "76228", "76229", "10115", "10117", "10119", "10176", "10178", "10179", "10243",
			"10245", "10247", "10249", "10315", "10317", "10318", "10319", "10365", "10367", "10369", "10405", "10407",
			"10409", "10435", "10437", "10439", "10551", "10553", "10555", "10557", "10559", "10585", "10587", "10589",
			"10623", "10625", "10627", "10629", "10707", "10709", "10711", "10713", "10715", "10717", "10719", "10777",
			"10779", "10781", "10783", "10785", "10787", "10789", "10823", "10825", "10827", "10829", "10961", "10963",
			"10965", "10967", "10969", "10997", "10999", "12043", "12045", "12047", "12049" };

	public static int artikelNummer = 1;
	public static LinkedList<String> artikelNamen = new LinkedList<String>();
	public static int currentStar = 1;
	
	public static int bewertungPSNr = 1;
	public static int kaufPSNr = 1;
	
	public static CompPrimaryKey getRandomUniqueCompKey_Kauf(boolean testing) {
		if (!testing) {			
			return new CompPrimaryKey(String.valueOf(kaufPSNr), String.valueOf(kaufPSNr));
		} else {
			return new CompPrimaryKey(String.valueOf(kaufPSNr-1), String.valueOf(kaufPSNr));
		}
	}
	
	public static CompPrimaryKey getRandomUniqueCompKey_Bewertung(boolean testing) {
		if (!testing) {			
			return new CompPrimaryKey(String.valueOf(bewertungPSNr), String.valueOf(bewertungPSNr));
		} else {
			return new CompPrimaryKey(String.valueOf(bewertungPSNr-1), String.valueOf(bewertungPSNr));
		}
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
		if (kundenNummer == 1) {
			return String.valueOf(1);
		}
		return String.valueOf(ThreadLocalRandom.current().nextInt(1, kundenNummer-1));
	}

	public static String getRandomInsertedArtikelNummer() {
		if (artikelNummer == 1) {
			return String.valueOf(1);
		}
		return String.valueOf(ThreadLocalRandom.current().nextInt(1, artikelNummer-1));
	}

	public static String getRandomInsertedEmail() {
		return emails.get(ThreadLocalRandom.current().nextInt(0, emails.size()));
	}

	public static String getRandomPLZ() {
		return plzs[ThreadLocalRandom.current().nextInt(0, plzs.length - 1)];
	}

	public static CompPrimaryKey getRandombewertungPS() {
		if (bewertungPSNr == 1) {
			return new CompPrimaryKey("1", "1");
		}
		int bewertungPs = ThreadLocalRandom.current().nextInt(1, artikelNummer);
		return new CompPrimaryKey(String.valueOf(bewertungPs), String.valueOf(bewertungPs));
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
		if (emails.size() < 5000) {			
			emails.add(email);
		}
		MockService.kundenNummer++;
		String kundenNummer = "" + MockService.kundenNummer;
		kunde.setKundenNummer(kundenNummer);
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
		if (artikelNamen.size() < 5000) {			
			artikelNamen.add(artikelName);
		}
		MockService.artikelNummer++;
		String artikelNummer = "" + MockService.artikelNummer;
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

	public static Kauf genRandomInsertKauf(boolean testing) {
		if (artikelNummer == 0 || kundenNummer == 0) {
			throw new RuntimeException("No Kunde or artikel to assign the kauf object to");
		}

		Kauf kauf = new Kauf();
		kauf.setKaufPreis(Integer.valueOf(rndInt.generate(3)));
		kauf.setKaufdatum(java.sql.Date.valueOf(getRandomDate()));
		if (!testing) {
			kaufPSNr++;
		}
		CompPrimaryKey ps = getRandomUniqueCompKey_Kauf(testing);
		kauf.setArtikelNummer(ps.getArtikelNummer());
		kauf.setKundenNummer(ps.getKundenNummer());
		kauf.setMenge(ThreadLocalRandom.current().nextInt(0, 200));
		return kauf;
	}

	public static Bewertung genRandomInsertBewertung(boolean testing) {
		if (artikelNummer == 0 || kundenNummer == 0) {
			throw new RuntimeException("No Kunde or artikel to assign the kauf object to");
		}
		Bewertung bewertung = new Bewertung();
		bewertung.setBewertung(rndString.generate(600));
		bewertung.setSterne(ThreadLocalRandom.current().nextInt(1, 11));
		if (!testing) {
			bewertungPSNr++;
		}
		CompPrimaryKey ps = getRandomUniqueCompKey_Bewertung(testing);
		bewertung.setArtikelNummer(ps.getArtikelNummer());
		bewertung.setKundenNummer(ps.getKundenNummer());
		return bewertung;
	}
	
	public static Bewertung randomizeFields(Bewertung bewertung) {
		bewertung.setBewertung(rndString.generate(600));
		bewertung.setSterne(ThreadLocalRandom.current().nextInt(1, 11));
		return bewertung;
	}

	public static void removeArtikelNr() {
		artikelNummer--;
	}

	public static void removeKundenNr() {
		kundenNummer--;
	}
	
	public static void removeKaufNr() {
		kaufPSNr--;
	}
	
	public static void removeBewertungNr() {
		bewertungPSNr--;
	}

	public static void removeEmail(String email) {
		emails.remove(email);
	}

	public static void clearIdLists() {
		artikelNummer = 0;
		kundenNummer = 0;
		kaufPSNr = 0;
		bewertungPSNr = 0;
		emails.clear();
		artikelNamen.clear();
		currentStar = 1;
	}
}
