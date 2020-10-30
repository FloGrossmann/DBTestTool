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

public class MockService {
	
	public static LinkedList<String> kundenNummern = new LinkedList<String>();
	public static LinkedList<String> emails = new LinkedList<String>();
	public static LinkedList<String> artikelNummern = new LinkedList<String>();
	
	public static RandomStringGenerator rndString =
	        new RandomStringGenerator.Builder()
            .withinRange('a', 'z')
            .filteredBy(CharacterPredicates.LETTERS, CharacterPredicates.DIGITS)
            .build();
	
	public static RandomStringGenerator rndInt =
	        new RandomStringGenerator.Builder()
            .withinRange('0', '9')
            .filteredBy(CharacterPredicates.LETTERS, CharacterPredicates.DIGITS)
            .build();
	
	public static RandomStringGenerator rndStringInt =
	        new RandomStringGenerator.Builder()
            .withinRange('0', 'z')
            .filteredBy(CharacterPredicates.LETTERS, CharacterPredicates.DIGITS)
            .build();
	
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
		adresse.setOrtschaft(rndString.generate(7));
		adresse.setPlz(rndInt.generate(7));
		adresse.setStrasse(rndString.generate(12));
		kunde.setAdresse(adresse);
		return kunde;
	}
	
	public static Artikel getRandomArtikel()  {
		Artikel artikel = new Artikel();
		artikel.setArtikelName(rndString.generate(12));
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
		int artikelNrIndex = ThreadLocalRandom.current().nextInt(0, artikelNummern.size());
		kauf.setArtikelNummer(artikelNummern.get(artikelNrIndex));
		int kundeNrIndex = ThreadLocalRandom.current().nextInt(0, kundenNummern.size());
		kauf.setArtikelNummer(kundenNummern.get(kundeNrIndex));
		kauf.setMenge(ThreadLocalRandom.current().nextInt(0, 200));
		return kauf;
	}
	
	public static Bewertung getRandomBewertung() {
		if (artikelNummern.size() == 0 || kundenNummern.size() == 0) {
			throw new RuntimeException("No Kunde or artikel to assign the kauf object to");
		}
		Bewertung bewertung = new Bewertung();
		bewertung.setBewertung(rndString.generate(200));
		bewertung.setSterne(ThreadLocalRandom.current().nextInt(1, 11));
		int artikelNrIndex = ThreadLocalRandom.current().nextInt(0, artikelNummern.size());
		bewertung.setArtikelNummer(artikelNummern.get(artikelNrIndex));
		int kundeNrIndex = ThreadLocalRandom.current().nextInt(0, kundenNummern.size());
		bewertung.setArtikelNummer(kundenNummern.get(kundeNrIndex));
		return bewertung;
	}
	
}
