package dbinterface;

import java.sql.Date;

import org.bson.codecs.pojo.annotations.BsonProperty;

public class Kauf {

	@BsonProperty("kundenNummer")
	String kundenNummer;
	@BsonProperty("artikelNummer")
	String artikelNummer;
	@BsonProperty("kaufdatum")
	Date kaufdatum;
	@BsonProperty("kaufPreis")
	double kaufPreis;
	@BsonProperty("menge")
	int menge;

	public Kauf() {

	}

	public Kauf(@BsonProperty("kundenNummer") String kundenNummer, @BsonProperty("artikelNummer") String artikelNummer,
			@BsonProperty("kaufdatum") Date kaufdatum, @BsonProperty("kaufPreis") double kaufPreis,
			@BsonProperty("menge") int menge) {
		super();
		this.kundenNummer = kundenNummer;
		this.artikelNummer = artikelNummer;
		this.kaufdatum = kaufdatum;
		this.kaufPreis = kaufPreis;
		this.menge = menge;
	}

	public String getKundenNummer() {
		return kundenNummer;
	}

	public void setKundenNummer(String kundenNummerr) {
		this.kundenNummer = kundenNummerr;
	}

	public String getArtikelNummer() {
		return artikelNummer;
	}

	public void setArtikelNummer(String artikelummeNr) {
		this.artikelNummer = artikelummeNr;
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
		return "\nTransaktion fuer den Artikel mit der Artikelnummer: "+artikelNummer+" vom Kunden mit der Kundennummer: "+kundenNummer+"\nAnzahl der gekauften Artikel: "+menge+"\nGesamtpreis: "+kaufPreis;
		
	}

}
