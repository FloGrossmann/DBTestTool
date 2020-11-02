package measure;

public enum MethodType {

	ADD_KUNDE("Create Kunde"),
	ADD_ARTIKEL("Create Artikel"),
	ADD_BEWERTUNG("Create Bewertung"),
	ADD_KAUF("Create Kauf"),
	GET_KUNDE_BYKUNDENNR("Get Kunde by Kundennummer"),
	GET_KUNDE_BYEMAIL("Get Kunde by Email"),
	GET_KUNDEN_BYPLZ("Get Kunden by PLZ"),
	GET_KUNDEN_BYNACHNAME("Get Kunden by Nachname"),
	GET_DISTINCTORTE("Get Dsitinct Orte"),
	GET_ARTIKEL_BYARTIKELNUMMER("Get Artikel by Artikelnummer"),
	GET_ARTIKEL_BYARTIKELNAME("Get Artikel by Artikelname"),
	GET_ARTIKEL_WHICHCOSTMORETHAN("Get Artikel which cost more than"),
	GET_BEWERTUNG_BYKUNDENNRANDARTIKELNR("Get Bewertung by KundenNummer and ArtikelNummer"),
	GET_BEWERTUNGEN_BYKUNDENNR("Get Bewertungen by KundenNummer"),
	GET_BEWERTUNGEN_BYARTIKELNR("Get Bewertungen by ArtikelNummer"),
	GET_EINKAEUFE_FORKUNDEN("Get Einkäufe by Kundennummer"),
	GET_EINKAUEFE_FORARTIKEL("Get Einläufe by ArtikelNummer"),
	UPDATE_KUNDE_COMPLETE("Update Kunde - all fields (replace)"),
	UPDATE_KUNDE_NACHNAME("Update Kunde nachname"),
	UPDATE_ARTIKEL_COMPLETE("Update Artikel - all fields (replace)"),
	UPDATE_BEWERTUNG_COMPLETE("Update Bewertung - all fields (replace)"),
	UPDATE_BEWERTUNG_TEXT("Update Bewertung bewertungstext"),
	DELETE_KUNDE_BYKUNDENNR("Delete Kunde by KundenNr"),
	DELETE_ARTIKEL_BYARTIKELNR("Delete Artikel by ArtikelNr"),
	DELETE_BEWERTUNG_BYARTIKELNRANDKUNDENNR("Delete Bewertung by ArtikelNummer and KundenNummer"),
	DELETE_KAUF_BYARTIKELNRANDKUNDENNR("Delete Kauf by ArtikelNummer and KundenNummer"),
	DELETE_ADRESSE_BYKUNDENNR("Delete Adresse by KundenNummer");
	
	String text;

	private MethodType(String s) {
		this.text = s;
	}
}
