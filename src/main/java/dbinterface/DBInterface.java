package dbinterface;

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

	long addKunde(Kunde kunde);

	long addArtikel(Artikel artikel);

	long addBewertung(Bewertung bewertung);

	long addKauf(Kauf kauf);

	// READ

	long getKundeByKundenNr(String kundenNr);

	long getKundeByEmail(String email);

	long getKundenByPlz(String plz);

	long getDistinctOrte();

	long getArtikelByArtikelNummer(String artikelNummer);

	long getArtikelByArtikelName(String artikelName);

	long getArtikelWhichCostMoreThan(Double price);

	long getBewertungByKundenNrAndArtikelNr(String artikelNummer, String kundenNummer);

	long getBewertungenByAnzahlSterne(int sterne);

	// Not used - Depends on randomness
	long getBewertungenByKundenNr(String kundenNummer);
	// Not used - Depends on randomness
	long getBewertungenByArtikelNr(String artikelNummer);
	// Depends on randomness
	long getEinkaeufeForKunde(String kundenNummer);
	// Depends on randomness
	long getVerkauefeForArtikel(String artikelNummer);

	// UPDATE

	long updateKunde(Kunde kunde);
	
	long updateKundenNachname(String kundenNr, String nachName);

	long updateArtikel(Artikel artikel);
	
	long updateBewertung(Bewertung bewertung);
	
	long updateBewertungsText(String kundenNummer, String artikelNummer, String newText);

	// DELETE

	long deleteKundeByKundenNr(String kundenNr);

	long deleteArtikelbyArtikelNr(String artikelNr);

	long deleteBewertungByArtikelNrAndKundenNr(String artikelNr, String kundennummer);
	
	long deleteKaufByArtikelNrAndKundenNr(String artikelNr, String kundennummer);
	
	long deleteAdresseByKundenNr(String kundennummer);

}
