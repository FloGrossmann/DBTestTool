package dbinterface;

import java.util.List;

public interface DBInterface {

	// SETUP

	/**
	 * Setup the collections or tables
	 * 
	 * @return true if successful
	 */
	boolean setupDB();

	// CLEAN

	/**
	 * Remove all created test data
	 * 
	 * @return true if successful
	 */
	boolean cleanData();

	// CREATE

	Kunde addKunde(Kunde kunde);

	Artikel addArtikel(Artikel artikel);

	Bewertung addBewertung(Bewertung bewertung);

	Kauf addKauf(Kauf kauf);

	// READ

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

	List<Kauf> getEinkaeufeForKunde(String kundenNummer);

	List<Kauf> getVerkauefeForArtikel(String artikelNummer);

	// UPDATE

	Kunde updateKunde(Kunde kunde);

	Artikel updateArtikel(Artikel artikel);

	Bewertung updateBewertung(Bewertung bewertung);

	// DELETE

	void deleteKundeByKundenNr(String kundenNr);

	void deleteArtikelbyArtikelNr(String artikelNr);

	void deleteBewertungByArtikelNrAndKundenNr(String artikelNr, String bewertungNr);

}
