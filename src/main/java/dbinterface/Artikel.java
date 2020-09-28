package dbinterface;

public class Artikel {

	String artikelNummer; //PS
	String artikelName;
	double einzelPreis;
	String waehrung;
	int menge;
	String beschreibung;

	public Artikel() {

	}

	public Artikel(String artikelNummer, String artikelName, Double einzelPreis, String waehrung, int menge,
			String beschreibung) {
		super();
		this.artikelNummer = artikelNummer;
		this.artikelName = artikelName;
		this.einzelPreis = einzelPreis;
		this.waehrung = waehrung;
		this.menge = menge;
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

	public int getMenge() {
		return menge;
	}

	public void setMenge(int menge) {
		this.menge = menge;
	}

	public String getBeschreibung() {
		return beschreibung;
	}

	public void setBeschreibung(String beschreibung) {
		this.beschreibung = beschreibung;
	}

}
