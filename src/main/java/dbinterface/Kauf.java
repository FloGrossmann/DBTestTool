package dbinterface;

import java.sql.Date;

public class Kauf {

	String kundenNr;
	String artikelNr;
	Date kaufdatum;
	double kaufPreis;
	int menge;

	public Kauf() {

	}

	public Kauf(String kundenNr, String artikelNr, Date kaufdatum, double einzelpreis, int menge) {
		super();
		this.kundenNr = kundenNr;
		this.artikelNr = artikelNr;
		this.kaufdatum = kaufdatum;
		this.kaufPreis = einzelpreis*menge;
		this.menge = menge;
	}

	public String getKundenNr() {
		return kundenNr;
	}

	public void setKundenNr(String kundenNr) {
		this.kundenNr = kundenNr;
	}

	public String getArtikelNr() {
		return artikelNr;
	}

	public void setArtikelNr(String artikelNr) {
		this.artikelNr = artikelNr;
	}

	public Date getKaufdatum() {
		return kaufdatum;
	}

	public void setKaufdatum(Date kaufdatum) {
		this.kaufdatum = kaufdatum;
	}

	public double getKaufPreis() {
		return kaufPreis;
	}

	public void setKaufPreis(double kaufPreis) {
		this.kaufPreis = kaufPreis;
	}

	public int getMenge() {
		return menge;
	}

	public void setMenge(int menge) {
		this.menge = menge;
	}
	
	@Override
	public String toString() {
		return "\nTransaktion für den Artikel mit der Artikelnummer: "+artikelNr+" vom Kunden mit der Kundennummer: "+kundenNr+"\nAnzahl der gekauften Artikel: "+menge+"\nGesamtpreis: "+kaufPreis;
		
	}

}
