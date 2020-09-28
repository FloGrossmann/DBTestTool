package dbinterface;

import java.util.Date;

public class Kauf {

	String kundenNr;
	String artikelNr;
	Date kaufdatum;
	double kaufPreis;
	int menge;

	public Kauf() {

	}

	public Kauf(String kundenNr, String artikelNr, Date kaufdatum, double kaufPreis, int menge) {
		super();
		this.kundenNr = kundenNr;
		this.artikelNr = artikelNr;
		this.kaufdatum = kaufdatum;
		this.kaufPreis = kaufPreis;
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

}
