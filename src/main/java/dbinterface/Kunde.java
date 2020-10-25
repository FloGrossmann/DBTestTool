package dbinterface;

import org.bson.BsonDocument;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.conversions.Bson;

public class Kunde {

	@BsonProperty("kundenNummer")
	String kundenNummer; // PS
	@BsonProperty("email")
	String email;
	@BsonProperty("telefonNummer")
	String telefonNummer;
	@BsonProperty("vorname")
	String vorname;
	@BsonProperty("nachname")
	String nachname;
	@BsonProperty("adresse")
	Adresse adresse;

	@BsonCreator
	public Kunde(@BsonProperty("kundennummer") String kundennummer, @BsonProperty("email") String email,
			@BsonProperty("telefonnummer") String telefonnummer, @BsonProperty("vorname") String vorname, @BsonProperty("nachname") String nachname,
			@BsonProperty("adresse") Adresse adresse) {
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

	@Override
	public String toString() {
		return "\nKunde\nKundennummer: "+kundenNummer+"\nName: "+vorname+" "+nachname+"\nE-Mail Adresse: "+email+"\nTelefonnummer: "+telefonNummer + "\n"+ adresse.toString();
		
	}
}
