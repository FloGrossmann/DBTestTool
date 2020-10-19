package dbinterface;

public class Bewertung {

	String kundenNummer; //PS
	String artikelNummer; //PS
	int sterne;
	String bewertung;

	public Bewertung() {

	}

	public Bewertung(String kundenNummer, String artikelNummer, int sterne, String bewertung) {
		super();
		this.kundenNummer = kundenNummer;
		this.artikelNummer = artikelNummer;
		this.sterne = sterne;
		this.bewertung = bewertung;
	}

	public String getKundenNummer() {
		return kundenNummer;
	}

	public void setKundenNummer(String kundenNummer) {
		this.kundenNummer = kundenNummer;
	}

	public String getArtikelNummer() {
		return artikelNummer;
	}

	public void setArtikelNummer(String artikelNummer) {
		this.artikelNummer = artikelNummer;
	}

	public int getSterne() {
		return sterne;
	}

	public void setSterne(int sterne) {
		this.sterne = sterne;
	}

	public String getBewertung() {
		return bewertung;
	}

	public void setBewertung(String bewertung) {
		this.bewertung = bewertung;
	}
	
	@Override
	public String toString() {
		return "Bewertung für Artikel mit der Artikelnummer: "+artikelNummer+" vom Kunden mit der Kundennummer: "+kundenNummer+"/nSterne: "+sterne+"/nBewertung: "+bewertung;
		
	}

}
