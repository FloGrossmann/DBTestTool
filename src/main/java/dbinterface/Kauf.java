package dbinterface;

import java.sql.Date;

import org.bson.codecs.pojo.annotations.BsonProperty;

public class Kauf {

	@BsonProperty("kundenNr")
	String kundenNr;
	@BsonProperty("artikelNr")
	String artikelNr;
	@BsonProperty("kaufdatum")
	Date kaufdatum;
	@BsonProperty("kaufPreis")
	double kaufPreis;
	@BsonProperty("menge")
	int menge;

	public Kauf() {

	}

	public Kauf(@BsonProperty("kundenNr") String kundenNr, @BsonProperty("artikelNr") String artikelNr,
			@BsonProperty("kaufdatum") Date kaufdatum, @BsonProperty("kaufPreis") double kaufPreis,
			@BsonProperty("menge") int menge) {
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
	
	@Override
	public String toString() {
		return "\nTransaktion fuer den Artikel mit der Artikelnummer: "+artikelNr+" vom Kunden mit der Kundennummer: "+kundenNr+"\nAnzahl der gekauften Artikel: "+menge+"\nGesamtpreis: "+kaufPreis;
		
	}

}
