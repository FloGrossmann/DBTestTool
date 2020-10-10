package dbinterface;

import org.bson.codecs.pojo.annotations.BsonId;

public class Kunde {

	@BsonId
	String kundenNummer; //PS
	String email;
	String telefonNummer;
	String vorname;
	String nachname;
	Adresse adresse;

	public Kunde(String kundennummer, String email, String telefonnummer, String vorname, String nachname,
			Adresse adresse) {
		super();
		this.kundenNummer = kundennummer;
		this.email = email;
		this.telefonNummer = telefonnummer;
		this.vorname = vorname;
		this.nachname = nachname;
		this.adresse = adresse;
	}

	public Kunde() {

	}

	public String getKundenNummer() {
		return kundenNummer;
	}

	public void setKundenNummer(String kundennummer) {
		this.kundenNummer = kundennummer;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelefonNummer() {
		return telefonNummer;
	}

	public void setTelefonNummer(String telefonnummer) {
		this.telefonNummer = telefonnummer;
	}

	public String getVorname() {
		return vorname;
	}

	public void setVorname(String vorname) {
		this.vorname = vorname;
	}

	public String getNachname() {
		return nachname;
	}

	public void setNachname(String nachname) {
		this.nachname = nachname;
	}

	public Adresse getAdresse() {
		return adresse;
	}

	public void setAdresse(Adresse adresse) {
		this.adresse = adresse;
	}

}
