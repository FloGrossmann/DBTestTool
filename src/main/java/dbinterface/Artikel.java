package dbinterface;

import org.bson.codecs.pojo.annotations.BsonId;

public class Artikel {

	@BsonId
	String artikelNummer; //PS
	String artikelName;
	double einzelPreis;
	String waehrung;
	String beschreibung;

	public Artikel() {

	}

	public Artikel(String artikelNummer, String artikelName, Double einzelPreis, String waehrung,
			String beschreibung) {
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
	
	@Override
	public String toString() {
		return "Artikel \nArtikelnummer: "+artikelNummer+"\nArtikelname: "+artikelName+"\nBeschreibung: "+beschreibung+"\nEinzelpreis: "+einzelPreis+"\nWährung: "+waehrung;
		
	}

}
