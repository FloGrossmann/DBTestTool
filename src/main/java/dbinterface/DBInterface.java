package dbinterface;

import java.util.List;

public interface DBInterface {

	Kunde getKundeByKundenNr(String kundenNr);
	
	Kunde getKundeByEmail(String email);
	
	List<Kunde> getKundenByPlz(String plz);
	
	List<Kunde> getKundenByNachName(String nachName);
	
	List<String> getDistinctOrte();
	
	Artikel getArtikelByArtikelNummer(String artikelNummer);
	
	Artikel getArtikelByArtikelName(String artikelName);
	
	List<Artikel> getArtikelWhichCostMoreThan(Double price);
	
	Bewertung getBewertungByKundenNrAndArtikelNr(String artikelNummer, String kundenNummer);
	
	List<Bewertung> getBewertungenByAnzahlSterne(int sterne);
	
	List<Bewertung> getBewertungenByKundenNr(String kundenNummer);
	
	List<Bewertung> getBewertungenByArtikelNr(String artikelNummer);
	
}
