package dbinterface;

import org.bson.codecs.pojo.annotations.BsonProperty;

public class Artikel {

	@BsonProperty("artikelNummer")
	String artikelNummer; // PS
	@BsonProperty("artikelName")
	String artikelName;
	@BsonProperty("einzelPreis")
	double einzelPreis;
	@BsonProperty("waehrung")
	String waehrung;
	@BsonProperty("beschreibung")
	String beschreibung;

	public Artikel() {

	}

	public Artikel(@BsonProperty("artikelNummer") String artikelNummer, @BsonProperty("artikelName") String artikelName,
			@BsonProperty("einzelPreis") Double einzelPreis, @BsonProperty("waehrung") String waehrung,
			@BsonProperty("beschreibung") String beschreibung) {
		super();
		this.artikelNummer = artikelNummer;
		this.artikelName = artikelName;
		this.einzelPreis = einzelPreis;
		this.waehrung = waehrung;
		this.beschreibung = beschreibung;
	}

	public String getArtikelNummer() {
		return artikelNummer;
	}

	public void setArtikelNummer(String artikelNummer) {
		this.artikelNummer = artikelNummer;
	}

	public String getArtikelName() {
		return artikelName;
	}

	public void setArtikelName(String artikelName) {
		this.artikelName = artikelName;
	}

	public double getEinzelPreis() {
		return einzelPreis;
	}

	public void setEinzelPreis(double einzelPreis) {
		this.einzelPreis = einzelPreis;
	}

	public String getWaehrung() {
		return waehrung;
	}

	public void setWaehrung(String waehrung) {
		this.waehrung = waehrung;
	}

	public String getBeschreibung() {
		return beschreibung;
	}

	public void setBeschreibung(String beschreibung) {
		this.beschreibung = beschreibung;
	}

}
